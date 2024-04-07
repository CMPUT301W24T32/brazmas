import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;

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

/**
 * This class contains integration tests for {@link AdminController} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminControllerIntegrationTest {

    private FirebaseFirestore mockDatabase;
    private CollectionReference mockCollectionRef;
    private DocumentReference mockDocumentRef;
    private AdminController adminController;

    /**
     * Sets up the necessary mocks and initializes the AdminController instance for testing.
     */
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
     * Test for adding an admin successfully.
     * Verifies that the admin is added successfully to the Firestore database.
     */
    @Test
    public void testAddAdmin_Success() {
        Admin mockAdmin = mock(Admin.class);
        when(mockDocumentRef.getId()).thenReturn("mock_admin_id");
        Task mockTask = mock(Task.class);
        when(mockCollectionRef.add(any(Admin.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        adminController.addAdmin(mockAdmin, object -> {
            assert object.equals("mock_admin_id");
        }, null);
    }

    /**
     * Test for adding an admin failure.
     * Verifies that the failure callback is invoked when adding an admin fails.
     */
    @Test
    public void testAddAdmin_Failure() {
        Admin mockAdmin = mock(Admin.class);
        Task mockTask = mock(Task.class);

        when(mockDocumentRef.getId()).thenReturn("mock_admin_id");
        when(mockCollectionRef.add(any(Admin.class))).thenReturn(mockTask);

        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception());
            return mockTask;
        });

        AtomicBoolean targetListenerInvoked = new AtomicBoolean(false);
        adminController.addAdmin(mockAdmin, null, failure -> targetListenerInvoked.set(true));
        assertTrue(targetListenerInvoked.get());
    }

    /**
     * Test for getting an admin successfully.
     * Verifies that an admin is retrieved successfully from the Firestore database.
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
     * Test for getting an admin failure.
     * Verifies that the failure callback is invoked when getting an admin fails.
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

    /**
     * Test for setting an admin successfully.
     * Verifies that an admin is set successfully in the Firestore database.
     */
    @Test
    public void testSetAdmin_Success() {
        Admin admin = new Admin("mock_admin_Id");
        Task mockTask = mock(Task.class);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        when(mockDocumentRef.set(any(Admin.class))).thenReturn(mockTask);
        adminController.setAdmin(admin, () -> assertTrue(true), e -> fail());
        verify(mockDocumentRef).set(admin);
    }

    /**
     * Test for setting an admin failure.
     * Verifies that the failure callback is invoked when setting an admin fails.
     */
    @Test
    public void testSetAdmin_Failure() {
        Admin admin = new Admin("mock_admin_Id");
        Task<Void> mockTask = mock(Task.class);

        when(mockDocumentRef.set(any(Admin.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        when(mockTask.addOnFailureListener(any())).thenAnswer(invocation -> {
            OnFailureListener listener = invocation.getArgument(0);
            listener.onFailure(new Exception("Mock failure"));
            return mockTask;
        });

        adminController.setAdmin(admin, () -> fail(), e -> assertTrue(true));
        verify(mockDocumentRef).set(admin);
    }
}
