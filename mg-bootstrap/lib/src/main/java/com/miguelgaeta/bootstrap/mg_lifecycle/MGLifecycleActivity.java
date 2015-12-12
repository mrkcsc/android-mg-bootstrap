package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelgaeta.bootstrap.mg_keyboard.MGKeyboard;
import com.miguelgaeta.bootstrap.mg_keyboard.MGKeyboardState;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This is a generic helper activity class for
 * android.  Support convenience methods for
 * setting the content view based on a naming
 * convention and custom activity transitions.
 */
@SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
public class MGLifecycleActivity extends AppCompatActivity {

    @Getter
    // Configuration object for the activity.
    private MGLifecycleActivityConfig config = new MGLifecycleActivityConfig(this);

    @Getter
    // Custom transitions object for the activity.
    private MGLifecycleActivityTransitions transitions = new MGLifecycleActivityTransitions(this);

    @Getter
    // Custom observable that emits activity paused events.
    private final SerializedSubject<Void, Void> paused = new SerializedSubject<>(PublishSubject.create());

    // Menu layout.
    private @Nullable Integer menuResourceId;

    // Menu layout item selected callback.
    private @Nullable Action1<MenuItem> menuResourceIdSelectedAction;

    // Tracks if we are currently going back.
    private static boolean goingBack;

    @Getter(value = AccessLevel.PACKAGE)
    // Allow fragments hook into the back event.
    private final Map<String, Func0<Boolean>> fragmentOnBackPressed = new HashMap<>();

    /**
     * Handles setting the content view
     * via naming conventions.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If saved state this is not the first creation.
        getConfig().setRecreated(savedInstanceState != null);

        // Attempt to set the content view.
        MGLifecycleContentView.setContentView(this);

        // Inject butter-knife views.
        ButterKnife.bind(this);

        // Invoke create or resume.
        onCreateOrResume();

        // Create version has been invoked.
        getConfig().setOnCreateOrResumeInvoked(true);
    }

    /**
     * Helper lifecycle method that runs either on
     * create or resume.  This is helpful for RxJava
     * based observables that die on pause.
     */
    protected void onCreateOrResume() {

    }

    /**
     * Handles running custom transitions.
     */
    @Override
    protected void onPause() {
        super.onPause();

        getPaused().onNext(null);

        transitions.run(goingBack, false);
    }

    /**
     * Handles running custom transitions and
     * resets the going back flag.
     */
    @Override
    protected void onResume() {
        super.onResume();

        getTransitions().run(goingBack, true);

        goingBack = false;

        if (getConfig().isOnCreateOrResumeInvoked()) {
            getConfig().setOnCreateOrResumeInvoked(false);
        } else {

            // On resume version invoked.
            onCreateOrResume();
        }
    }

    /**
     * First check to see if any fragments are attempting to
     * over ride the default back button behavior.  If
     * not run default back behavior and track
     * going back flag.
     */
    @Override
    public void onBackPressed() {

        onBackPressed(true);
    }

    public void onBackPressed(boolean alertFragments) {

        for (Func0<Boolean> fragmentOnBack : getFragmentOnBackPressed().values()) {

            if (fragmentOnBack.call()) {

                return;
            }
        }

        super.onBackPressed();

        goingBack = true;
    }

    /**
     * Intercept base context and pass it through
     * calligraphy library for custom fonts.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (menuResourceId != null) {

            getMenuInflater().inflate(menuResourceId, menu);

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (MGKeyboardState.isOpened()) {
                MGKeyboard.setKeyboardOpen(this, false);
            }

            onBackPressed();
        }

        if (menuResourceIdSelectedAction != null) {
            menuResourceIdSelectedAction.call(item);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds a clear history overload to the
     * start activity call.
     */
    public void startActivity(Intent intent, boolean clearHistory) {
        super.startActivity(intent);

        if (clearHistory) {

            config.setHistoryCleared();
        }
    }

    /**
     * Start activity without any additional
     * intent options and clear history flag.
     */
    public void startActivity(Class activityClass, boolean clearHistory) {

        startActivity(new Intent(this, activityClass), clearHistory);
    }

    /**
     * Start activity without any additional
     * intent options.
     */
    public void startActivity(Class activityClass) {

        startActivity(activityClass, false);
    }

    /**
     * Set option menu layout and a convenience callback for when a menu item is selected.
     */
    protected void setOptionsMenu(@MenuRes int menuResourceId, Action1<MenuItem> menuItemSelectedAction) {

        this.menuResourceId = menuResourceId;
        this.menuResourceIdSelectedAction = menuItemSelectedAction;
    }
}
