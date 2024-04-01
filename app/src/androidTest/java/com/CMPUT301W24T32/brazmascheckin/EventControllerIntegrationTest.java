package com.CMPUT301W24T32.brazmascheckin;

import static org.mockito.Mockito.*;

import androidx.test.core.app.ApplicationProvider;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
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

    @Mock
    private FirebaseFirestore mockDatabase;
    @Mock
    private CollectionReference mockCollectionRef;
    @Mock
    private DocumentReference mockDocumentRef;

    @InjectMocks
    private EventController eventController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//        mockDatabase = mock(FirebaseFirestore.class);
//        mockCollectionRef = mock(CollectionReference.class);
//        mockDocumentRef = mock(DocumentReference.class);
//
//        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        when(mockDatabase.collection("events")).thenReturn(mockCollectionRef);
        when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

//        eventController = new EventController(mockDatabase);
    }

    @Test
    public void testAddEvent_Success() {
        Event mockEvent = mock(Event.class);
        when(mockDocumentRef.getId()).thenReturn("mock_event_id");

        eventController.addEvent(mockEvent, object -> {
            assert object.equals("mock_event_id");
        }, null);
    }
}
