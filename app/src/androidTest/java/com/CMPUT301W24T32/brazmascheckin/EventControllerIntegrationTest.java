package com.CMPUT301W24T32.brazmascheckin;

import static org.mockito.Mockito.*;

import androidx.test.core.app.ApplicationProvider;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
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

@RunWith(MockitoJUnitRunner.class)
public class EventControllerIntegrationTest {

//    @Mock
    private FirebaseFirestore mockDatabase;
//    @Mock
    private CollectionReference mockCollectionRef;
//    @Mock
    private DocumentReference mockDocumentRef;

//    @InjectMocks
    private EventController eventController;
    private FirestoreDB mockDB;

    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
        mockDatabase = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class);
        mockDocumentRef = mock(DocumentReference.class);
        mockDB = mock(FirestoreDB.class);
//
//        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

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
}
