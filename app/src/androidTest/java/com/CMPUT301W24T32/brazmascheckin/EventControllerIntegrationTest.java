package com.CMPUT301W24T32.brazmascheckin;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

import androidx.test.core.app.ApplicationProvider;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerIntegrationTest {


    private FirebaseFirestore mockDatabase;

    private CollectionReference mockCollectionRef;

    private DocumentReference mockDocumentRef;

    private EventController eventController;

    @Before
    public void setUp() {
        mockDatabase = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class);
        mockDocumentRef = mock(DocumentReference.class);

        when(mockDatabase.collection(anyString())).thenReturn(mockCollectionRef);
        when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        eventController = new EventController(mockDatabase);
    }

    @Test
    public void testAddEvent_Success() {
        Event mockEvent = mock(Event.class);
        when(mockDocumentRef.getId()).thenReturn("mock_event_id");
        Task mockTask = mock(Task.class);
        when(mockCollectionRef.add(any(Event.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        eventController.addEvent(mockEvent, object -> {
            assert object.equals("mock_event_id");
        }, null);
    }

    @Test
    public void testAddEvent_Failure() {
        Event mockEvent = mock(Event.class);
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.getId()).thenReturn("mock_event_id");
        when(mockCollectionRef.add(any(Event.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);

        // Configure mockTask behavior for addOnFailureListener to execute the failure listener callback
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception());
            return mockTask;
        });

        // Create a flag to track whether the failure listener is invoked
        AtomicBoolean failureListenerInvoked = new AtomicBoolean(false);

        // Call the method under test
        eventController.addEvent(mockEvent, null, failure -> failureListenerInvoked.set(true));
        assertTrue(failureListenerInvoked.get());
    }

    @Test
    public void testAddEvent_SuccessListener() {
        Event mockEvent = mock(Event.class);
        Task<DocumentReference> mockTask = mock(Task.class);
        when(mockDocumentRef.getId()).thenReturn("mock_event_id");
        when(mockCollectionRef.add(any(Event.class))).thenReturn(mockTask);

        // Configure mockTask behavior for addOnSuccessListener to execute the success listener callback
        when(mockTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            OnSuccessListener<DocumentReference> listener = invocation.getArgument(0);
            DocumentReference documentReference = mock(DocumentReference.class);
            when(documentReference.getId()).thenReturn("mock_event_id");
            listener.onSuccess(documentReference);
            return mockTask;
        });

        // Create a flag to track whether the success listener is invoked
        AtomicBoolean successListenerInvoked = new AtomicBoolean(false);

        // Call the method under test
        eventController.addEvent(mockEvent, id -> {
            // Assert success handling logic here
            assertEquals("mock_event_id", id);
            successListenerInvoked.set(true);
        }, null);

        // Assert that the success listener was invoked
        assertTrue(successListenerInvoked.get());
    }

    @Test
    public void testSetEvent_Success() {
        Event mockEvent = mock(Event.class);
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.set(any(Event.class))).thenReturn(mockTask);

        eventController.setEvent(mockEvent, () -> assertTrue(true), e -> fail());
        verify(mockDocumentRef).set(mockEvent);
    }

    @Test
    public void testSetEvent_Failure() {
        Event mockEvent = mock(Event.class);
        Task<Void> mockTask = mock(Task.class);

        when(mockDocumentRef.set(any(Event.class))).thenReturn(mockTask);

        // Configure mockTask behavior for addOnFailureListener to execute the failure listener callback
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        // Call the method under test
        eventController.setEvent(mockEvent, () -> fail(), e -> assertTrue(true));

        // Verify that the set method was called on the mockDocumentRef
        verify(mockDocumentRef).set(mockEvent);
    }

    @Test
    public void testGetEvent_Success() {
        Event mockEvent = mock(Event.class);
        String eventId = "mock_event_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockCollectionRef.add(any(Event.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        eventController.addEvent(mockEvent, null, null);

        eventController.getEvent(eventId,
                event -> {
                    // Assert that the event is retrieved successfully
                    assertEquals(mockEvent, event);
                },
                e -> {
                    // Handle failure case if needed
                    fail("Failure callback should not be invoked");
                });
    }
}
