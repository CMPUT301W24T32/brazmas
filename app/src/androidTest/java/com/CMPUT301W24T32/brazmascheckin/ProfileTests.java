package com.CMPUT301W24T32.brazmascheckin;

import static android.app.Activity.RESULT_OK;
import static android.app.PendingIntent.getActivity;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.InjectEventSecurityException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


import static com.CMPUT301W24T32.brazmascheckin.controllers.ImageController.DEFAULT_PROFILE_PICTURE_PATH;
import static org.hamcrest.Matchers.instanceOf;

import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AddEventActivity;
import com.CMPUT301W24T32.brazmascheckin.views.AnnouncementActivity;
import com.CMPUT301W24T32.brazmascheckin.views.EditProfileActivity;
import com.CMPUT301W24T32.brazmascheckin.views.ProfileActivity;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RunWith(AndroidJUnit4.class)
public class ProfileTests {

    @Rule
    public ActivityScenarioRule<ProfileActivity> scenario = new
            ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);

    private UserController userController;
    private EventController eventController;
    private ImageController imageController;
    private ArrayList<Integer> colors;
    private Uri imageUri;
    private Bitmap bitmap;
    private String fileID;
    private User user;


    @Before
    public void setUp() {
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());


        String deviceID = DeviceID.getDeviceID(ApplicationProvider.getApplicationContext());
        user = new User("John", "Doe", new ArrayList<>(), deviceID, new ArrayList<>(),
                false, 0, null,
                null, new ArrayList<>());

        String firstLetter = "J";
        int color = ContextCompat.getColor(ApplicationProvider.getApplicationContext(), R.color.black);
        bitmap = textAsBitmap(firstLetter, 70, color);
        imageUri = getImageUri(ApplicationProvider.getApplicationContext(), bitmap);
        user.setDefaultProfilePicture(uploadFile());


        userController.setUser(user, null, null);
    }

    @After
    public void cleanUp() {
        userController.getUser(user.getID(), object -> user = object, null);

        ArrayList<String> signedUpEvents = user.getSignedUpEvents();
        ArrayList<String> organizedEvents = user.getOrganizedEvents();

        for (String event : signedUpEvents) {
            eventController.deleteEvent(event, null, null);
        }
        for (String event : organizedEvents) {
            eventController.deleteEvent(event, null, null);
        }
        userController.deleteUser(user, null, null);
    }

    @Test
    public void testNameDisplayed() {
        String fullName = user.getFirstName() + " " + user.getLastName();
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText(fullName))));
        Intents.release();
    }

    @Test
    public void editProfileLaunched() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void editProfileDone() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void firstNameChanged() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("Mehar"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("Mehar Doe"))));
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("John"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("John Doe"))));
        Intents.release();

    }

    @Test
    public void lastNameChanged() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Goat"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("John Goat"))));
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Doe"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("John Doe"))));
        Intents.release();

    }

    @Test
    public void fullNameChanged() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("Mehar"));
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Goat"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("Mehar Goat"))));
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("John"));
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Doe"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("John Doe"))));

        Intents.release();
    }


    @Test
    public void removeDefaultCheck() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.remove_profile_picture_btn))
                .perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.profile_picture_edit))
                .check(matches(isDisplayed()))
                .check(matches(withContentDescription("default pfp")));
        Intents.release();
    }

    @Test
    public void addNewPfpTest() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));

        Matcher<Intent> expected = allOf(hasAction(Intent.ACTION_GET_CONTENT));
        Instrumentation.ActivityResult activityResult = createGalleyPickerResult();
        intending(expected).respondWith(activityResult);

        onView(ViewMatchers.withId(R.id.change_profile_picture_btn))
                .perform(ViewActions.click());
        intended(expected);
        Intents.release();
    }

    private Instrumentation.ActivityResult createGalleyPickerResult() {
        Resources resources = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
        imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.drawable.ic_launcher_background) + "/" +
                resources.getResourceTypeName(R.drawable.ic_launcher_background) + "/" +
                resources.getResourceEntryName(R.drawable.ic_launcher_background));

        Intent resultIntent = new Intent();
        resultIntent.setData(imageUri);
        return new Instrumentation.ActivityResult(RESULT_OK,resultIntent);
    }


    @Test
    public void endToEndProfileTest(){
        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("Mehar"));
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Goat"));

        onView(ViewMatchers.withId(R.id.remove_profile_picture_btn))
                .perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.profile_picture_edit))
                .check(matches(isDisplayed()))
                .check(matches(withContentDescription("default pfp")));

        Matcher<Intent> expected = allOf(hasAction(Intent.ACTION_GET_CONTENT));
        Instrumentation.ActivityResult activityResult = createGalleyPickerResult();
        intending(expected).respondWith(activityResult);

        onView(ViewMatchers.withId(R.id.change_profile_picture_btn))
                .perform(ViewActions.click());
        intended(expected);


        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("Mehar Goat"))));
        onView(ViewMatchers.withId(R.id.edit_profile_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.first_name_et)).perform(replaceText("John"));
        onView(ViewMatchers.withId(R.id.last_name_et)).perform(replaceText("Doe"));
        onView(ViewMatchers.withId(R.id.done_btn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.name_tv)).check(matches((withText("John Doe"))));

        Intents.release();
    }
    /**
     * converts text into  a bitmap
     * used from stack overflow from user Ted Hopp
     * @param text text to convert
     * @param textSize the size of the text wanted
     * @param textColor the color wanted for the text
     * @return the Bitmap
     */
    public Bitmap textAsBitmap(String text, float textSize, int textColor) {


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 60); // round
        int height = (int) (baseline + paint.descent() + 40);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int color = 0xFFC8A4D4;
        Canvas canvas = new Canvas(image);
        canvas.drawColor(color);
        canvas.drawText(text, 30, baseline + 20, paint);
        return image;
    }

    /**
     * converts the Bitmap into an image Uri
     * used from stack overflow from user Ajay
     * @param inContext the current context
     * @param inImage the bitmap
     * @return the image uri
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path != null) {
            return Uri.parse(path);
        }
        return null;
    }

    /**
     * Method to upload selected image file
     * @return String of the fileid
     */
    private String uploadFile() {
        fileID = String.valueOf(System.currentTimeMillis());

        if (imageUri != null) {
            imageController.uploadImage(DEFAULT_PROFILE_PICTURE_PATH, fileID, imageUri,
                    null, e -> {
                    });
        } else {
            fileID = null;
        }
        return fileID;
    }


}
