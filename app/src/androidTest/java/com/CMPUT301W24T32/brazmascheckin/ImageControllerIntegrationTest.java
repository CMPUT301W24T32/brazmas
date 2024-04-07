package com.CMPUT301W24T32.brazmascheckin;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.AddSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerIntegrationTest {
    private FirebaseStorage mockStorage;
    private StorageReference mockStorageRef;
    private StorageReference mockImageRef;
    private ImageController imageController;

    @Before
    public void setUp() {
        mockStorage = mock(FirebaseStorage.class);
        mockStorageRef = mock(StorageReference.class);
        mockImageRef = mock(StorageReference.class);

        when(mockStorage.getReference(anyString())).thenReturn(mockStorageRef);
        when(mockStorageRef.child(anyString())).thenReturn(mockImageRef);

        imageController = new ImageController(mockStorage);
    }

    @Test
    public void testUploadImage_Success() {
        UploadTask mockTask = mock(UploadTask.class);
        when(mockImageRef.putFile(any(Uri.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        Uri mockUri = mock(Uri.class);

        imageController.uploadImage(ImageController.EVENT_POSTER, "", mockUri,
                object -> {
                    assert object.equals(mockUri);
                }, e -> fail());

    }

    @Test
    public void testUploadImage_Failure() {
        UploadTask mockTask = mock(UploadTask.class);
        when(mockImageRef.putFile(any(Uri.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        Uri mockUri = mock(Uri.class);

        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        imageController.uploadImage(ImageController.EVENT_POSTER, "", mockUri,
                Uri -> fail(), e -> assertTrue(true));

    }

    @Test
    public void testGetImage_Success() {
        Task mockTask = mock(Task.class);
        when(mockImageRef.getBytes(any(Long.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        imageController.getImage(ImageController.EVENT_POSTER, "",
                bytes -> assertTrue(true), e -> fail());
    }

    @Test
    public void testGetImage_Failure() {
        Task mockTask = mock(Task.class);
        when(mockImageRef.getBytes(any(Long.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        imageController.getImage(ImageController.EVENT_POSTER, "",
                bytes -> fail(), e -> assertTrue(true));
    }

    @Test
    public void testDeleteImage_Success() {
        Task mockTask = mock(Task.class);
        when(mockImageRef.delete()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        imageController.deleteImage(ImageController.EVENT_POSTER, "",
                () -> assertTrue(true), e -> fail());
    }

    @Test
    public void testDeleteImage_Failure() {
        Task mockTask = mock(Task.class);
        when(mockImageRef.delete()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        imageController.deleteImage(ImageController.EVENT_POSTER, "",
                TestCase::fail, e -> assertTrue(true));}


}
