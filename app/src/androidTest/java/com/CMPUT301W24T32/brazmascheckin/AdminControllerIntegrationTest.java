import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.models.Admin;
import com.google.android.gms.tasks.OnFailureListener;
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
public class AdminControllerIntegrationTest {

    private FirebaseFirestore mockDatabase;
    private CollectionReference mockCollectionRef;
    private DocumentReference mockDocumentRef;

    private AdminController adminController;

    @Before
    public void setUp() {
        mockDatabase = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class);
        mockDocumentRef = mock(DocumentReference.class);

        when(mockDatabase.collection(anyString())).thenReturn(mockCollectionRef);
        when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        adminController = new AdminController(mockDatabase);
    }

    /**
     * Test for getting an admin successfully.
     */
    @Test
    public void testGetAdmin_Success() {
        Admin mockAdmin = mock(Admin.class);
        String adminId = "mock_admin_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockCollectionRef.add(any(Admin.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        adminController.addAdmin(mockAdmin, null, null);

        adminController.getAdmin(adminId,
                admin -> assertEquals(mockAdmin, admin),
                e -> fail("Failure callback should not be involved."));

    }

    /**
     * Test for getting a user failure.
     */
    @Test
    public void testGetAdmin_Failure() {
        String adminId = "mock_admin_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);

        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false);

        adminController.getAdmin(adminId,
                admin -> fail("Success callback should not be invoked."),
                e -> targetListenerInvoked.set(true));

        assertTrue(targetListenerInvoked.get());
    }
}
