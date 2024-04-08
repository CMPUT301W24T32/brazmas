package com.CMPUT301W24T32.brazmascheckin;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.annotation.UiThreadTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerIntegrationTest {
    private FirebaseFirestore mockDatabase;
    private CollectionReference mockCollectionRef;

    private DocumentReference mockDocumentRef;
    private UserController userController;

    @Before
    public void setUp() {
        mockDatabase = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class);
        mockDocumentRef = mock(DocumentReference.class);
        when(mockDatabase.collection(anyString())).thenReturn(mockCollectionRef);
        when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);
        userController = new UserController(mockDatabase);
    }

    /**
     * Test for adding a user successfully.
     */
    @Test
    @UiThreadTest
    public void testAddUser_Success() {
        User mockUser = mock(User.class);
        when(mockDocumentRef.getId()).thenReturn("mock_event_id");
        Task mockTask = mock(Task.class);
        when(mockCollectionRef.add(any(User.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        userController.addUser(mockUser, object -> {
            assert object.equals("mock_user_id");
        }, e -> fail());
    }

    /**
     * Test for adding user unsuccesfully
     */
    @Test
    @UiThreadTest
    public void testAddUser_Failure() {
        User mockUser = mock(User.class);
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.getId()).thenReturn("mock_user_id");
        when(mockCollectionRef.add(any(User.class))).thenReturn(mockTask);

        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception());
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false); // need atomic for concurrency
        userController.addUser(mockUser, null, failure -> targetListenerInvoked.set(true));
        assertTrue(targetListenerInvoked.get());
    }


    /**
     * Test for setting a user successfully.
     */
    @Test
    @UiThreadTest
    public void testSetUser_Success() {
        User user = new User("mock_user_Id");
        Task mockTask = mock(Task.class);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        when(mockDocumentRef.set(any(User.class))).thenReturn(mockTask);
        userController.setUser(user, () -> assertTrue(true), e-> fail());
        verify(mockDocumentRef).set(user);
    }

    /**
     * Test for setting a user failure.
     */
    @Test
    @UiThreadTest
    public void testSetUser_Failure() {
        User user = new User("mock_user_Id");
        Task<Void> mockTask = mock(Task.class);

        when(mockDocumentRef.set(any(User.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        userController.setUser(user, () -> fail(), e -> assertTrue(true));

        verify(mockDocumentRef).set(user);
    }

    /**
     * Test for getting a user successfully.
     */
    @Test
    @UiThreadTest
    public void testGetEvent_Success() {
        User mockUser = mock(User.class);
        String userId = "mock_user_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockCollectionRef.add(any(User.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        userController.addUser(mockUser, null, null);

        userController.getUser(userId,
                user -> assertEquals(mockUser, user),
                e -> fail("Failure callback should not be invoked"));
    }

    /**
     * Test for getting a user failure.
     */
    @Test
    @UiThreadTest
    public void testGetUser_Failure() {
        String userId = "mock_user_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);

        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false);

        userController.getUser(userId,
                user -> fail("Success callback should not be invoked"),
                e -> targetListenerInvoked.set(true));

        assertTrue(targetListenerInvoked.get());
    }

    /**
     * Test for deleting a user successfully.
     */
    @Test
    @UiThreadTest
    public void testDeleteUser_Success() {
        String userId = "mock_event_id";
        User user = new User(userId);
        Task<Void> mockTask = mock(Task.class);

        when(mockDocumentRef.delete()).thenReturn(mockTask);

        when(mockTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false);

        userController.deleteUser(user,
                () -> targetListenerInvoked.set(true),
                e -> fail("Failure callback should not be invoked"));

        assertTrue(targetListenerInvoked.get());
    }

    /**
     * Test for deleting a user failure.
     */
    @Test
    @UiThreadTest
    public void testDeleteUser_Failure() {
        String userId = "mock_user_id";
        User user = new User(userId);
        Task<Void> mockTask = mock(Task.class);

        when(mockDocumentRef.delete()).thenReturn(mockTask);

        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false);

        userController.deleteUser(user,
                () -> fail("Success callback should not be invoked"),
                e -> targetListenerInvoked.set(true));

        assertTrue(targetListenerInvoked.get());
    }
}
