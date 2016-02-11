package fyber.jehandadk.com.offerconfig;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fyber.jehandadk.com.Constants;
import fyber.jehandadk.com.R;
import fyber.jehandadk.com.base.BaseTest;
import fyber.jehandadk.com.data.models.ApiConfig;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.not;

/**
 * Created by jehandad.kamal on 2/8/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class ConfigActivityTest extends BaseTest {


    private static final String TEST_VALUE = "test";
    private static final int TEST_APP_ID = 100;
    private static final ApiConfig config = new ApiConfig();

    static {
        config.setPub0(TEST_VALUE);
        config.setAppId(TEST_APP_ID);
        config.setApiKey(TEST_VALUE);
        config.setUid(TEST_VALUE);
    }

    @Rule
    public ActivityTestRule<ConfigActivity> activityRule = new ActivityTestRule<>(
            ConfigActivity.class,
            true,     // initialTouchMode
            false);

    public static Matcher<View> hasErrorText(final Matcher<String> stringMatcher) {
        checkNotNull(stringMatcher);
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                if (view.getError() == null) return stringMatcher.matches(view.getError());
                return stringMatcher.matches(view.getError().toString());
            }
        };
    }

    @Test
    public void testEmptyPrefs() {

        Mockito.when(pref.apiConfig()).thenReturn(null);
        activityRule.launchActivity(null);

        onView(withId(R.id.edt_apikey)).check(matches(withText(Constants.DEFAULT_API_KEY)));
        onView(withId(R.id.edt_appid)).check(matches(withText(Constants.DEFAULT_APP_ID)));
        onView(withId(R.id.edt_pub0)).check(matches(withText(Constants.DEFAULT_PUB0)));
        onView(withId(R.id.edt_uid)).check(matches(withText(Constants.DEFAULT_UID)));


    }

    @Test
    public void testValuesWhenPrefsAreSet() {

        Mockito.when(pref.apiConfig()).thenReturn(config);
        activityRule.launchActivity(null);
        onView(withId(R.id.edt_apikey)).check(matches(withText(TEST_VALUE)));
        onView(withId(R.id.edt_appid)).check(matches(withText(TEST_APP_ID + "")));
        onView(withId(R.id.edt_pub0)).check(matches(withText(TEST_VALUE)));
        onView(withId(R.id.edt_uid)).check(matches(withText(TEST_VALUE)));

    }

    @Test
    public void testValidation() {

        activityRule.launchActivity(null);
        onView(withId(R.id.edt_apikey)).perform(replaceText(""));
        onView(withId(R.id.edt_appid)).perform(replaceText(""));
        onView(withId(R.id.edt_pub0)).perform(replaceText(""));
        onView(withId(R.id.edt_uid)).perform(replaceText(""));

        onView(withId(R.id.btn_save_config)).perform(click());

        onView(withId(R.id.edt_apikey)).check(matches(hasErrorText(not(Matchers.isEmptyOrNullString()))));
        onView(withId(R.id.edt_appid)).check(matches(hasErrorText(not(Matchers.isEmptyOrNullString()))));
        onView(withId(R.id.edt_uid)).check(matches(hasErrorText(not(Matchers.isEmptyOrNullString()))));
        onView(withId(R.id.edt_pub0)).check(matches(hasErrorText(Matchers.isEmptyOrNullString())));

    }

    @Test
    public void testActivityNavigation() {
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, null);
        Matcher<Intent> expectedIntent = anyIntent();
        activityRule.launchActivity(null);
        Intents.init();
        intending(expectedIntent).respondWith(result);
        onView(withId(R.id.btn_save_config)).perform(click());
        intended(hasComponent("fyber.jehandadk.com.offers.OffersActivity"));
        Intents.release();
    }
}