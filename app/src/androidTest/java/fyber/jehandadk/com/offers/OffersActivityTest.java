package fyber.jehandadk.com.offers;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Inject;

import fyber.jehandadk.com.R;
import fyber.jehandadk.com.api.IFyberClient;
import fyber.jehandadk.com.base.BaseTest;
import fyber.jehandadk.com.data.models.ApiConfig;
import fyber.jehandadk.com.data.models.Offer;
import fyber.jehandadk.com.data.models.OfferListResponse;
import fyber.jehandadk.com.data.models.Thumbnail;
import fyber.jehandadk.com.offerconfig.ConfigActivity;
import retrofit2.Response;
import rx.Observable;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

/**
 * Created by jehandad.kamal on 2/10/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class OffersActivityTest extends BaseTest {


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
    public ActivityTestRule<OffersActivity> activityRule = new ActivityTestRule<>(
            OffersActivity.class,
            true,     // initialTouchMode
            false);


    @Test
    public void testDisplayOffers() {
        Offer offer = new Offer();
        offer.setOfferId(1);
        String teaser1 = "teaser1";
        offer.setTeaser(teaser1);
        offer.setTitle("title1");

        Offer offer2 = new Offer();
        offer2.setOfferId(1);
        offer2.setTeaser("teaser2");
        offer2.setTitle("title2");

        Thumbnail thumb = new Thumbnail();
        thumb.setHires("http://cdn.fyber.com/assets/1808/icon175x175-2_square_175.png");
        thumb.setLowres("http://cdn.fyber.com/assets/1808/icon175x175-2_square_60.png");

        offer.setThumbnail(thumb);
        offer2.setThumbnail(thumb);

        OfferListResponse response = new OfferListResponse();
        response.getOffers().add(offer);
        response.getOffers().add(offer2);


        Mockito.when(api.getOffers(anyString(), anyString(), eq(TEST_APP_ID), anyString(), anyString(),
                anyString(), anyLong(), anyString(), anyBoolean(), anyString(),
                anyString(), anyInt(), anyString(), anyLong(), anyString()))
                .then(invocation -> Observable.just(response));
        Mockito.when(pref.apiConfig()).thenReturn(config);
        activityRule.launchActivity(null);

        onView(withId(R.id.list))
                .check(matches(isDisplayed()));
        onView(withId(R.id.layout_list_error)).check(matches(not(isDisplayed())));
        onView(withId(R.id.list))
                .check(matches(hasDescendant(withText(teaser1))));

    }

    @Test
    public void testDisplayError() {

        Mockito.when(api.getOffers(anyString(), anyString(), eq(TEST_APP_ID), anyString(), anyString(),
                anyString(), anyLong(), anyString(), anyBoolean(), anyString(),
                anyString(), anyInt(), anyString(), anyLong(), anyString()))
                .then(invocation -> Observable.error(new Exception("No Offers")));
        Mockito.when(pref.apiConfig()).thenReturn(config);
        activityRule.launchActivity(null);

        onView(withId(R.id.list))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.layout_list_error)).check(matches(isDisplayed()));

    }


}