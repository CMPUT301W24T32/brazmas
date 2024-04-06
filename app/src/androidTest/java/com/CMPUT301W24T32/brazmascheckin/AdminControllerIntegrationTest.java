import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.models.Admin;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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

        when(mockDatabase.collection(any())).thenReturn(mockCollectionRef);
        when(mockCollectionRef.document(any())).thenReturn(mockDocumentRef);

        adminController = new AdminController(mockDatabase);
    }

    @Test
    public void testGetAdmin_Success() {
        String adminId = "mock_admin_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            adminController.getAdmin(adminId, admin -> {
                assertNotNull(admin);
                assertEquals(adminId, admin.getId());
            }, e -> fail("Failure callback should not be invoked"));
            return mockTask;
        });

        adminController.getAdmin(adminId, admin -> {}, e -> {});

    }

    @Test
    public void testGetAdmin_Failure() {
        String adminId = "mock_admin_id";
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.get()).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            AdminController.GetFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        adminController.getAdmin(adminId, admin -> fail("Success callback should not be invoked"), e -> {});
    }
}
