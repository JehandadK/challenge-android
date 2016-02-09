package fyber.jehandadk.com.api;

import java.util.Calendar;
import java.util.Locale;

import fyber.jehandadk.com.data.models.OfferListResponse;

/**
 * Because we can test a builder but we cant every other place where this request is built,
 * and since there are a lot of params it can be easily misued.
 */
public class OffersRequestBuilder {

    int appId;
    String apiKey;
    //The Fyber Application ID for your application.	157	yes
    String uid;
    //The unique User ID, as used internally in your application.	player1	yes
    String locale;
    //The locale used for the offer descriptions.	de	yes
    String osVersion;
    //Current version of the users Operating System, retrieve via android.os.Build.VERSION.RELEASE	4.1.1	yes
    long timestamp;
    //The time the request is being sent by the device.	1312471066	yes
    String google_ad_id;
    //The Google advertising ID obtained via AdvertisingIdClient.getAdvertisingIdInfo(mContext).getId();	38400000-8cf0-11bd-b23e-10b96e40000d	yes
    boolean google_ad_id_limited_tracking_enabled;
    //Retrieves whether the user has limit ad tracking enabled or not. Obtained via AdvertisingIdClient.getAdvertisingIdInfo(mContext).isLimitAdTrackingEnabled()	true or false	Yes
    String ip;
    //The IP address of the device of your user. If the parameter is not given, the IP address of the request will be used.	212.45.111.17	no
    String pub0;
    int page;
    //The page of the response set that you are requesting.	1	no
    String offer_types;//Filter the results based on type of offer.	112	no
    String ps_time;    //The creation date of the user’s account in your game in Unix Timestamp format.	1312211903	no
    String device = "phone";

    private OffersRequestBuilder() {
        this.locale = Locale.getDefault().getLanguage() == null ? Locale.US.getLanguage() : Locale.getDefault().getLanguage();
        timestamp = Calendar.getInstance().getTimeInMillis();
        osVersion = android.os.Build.VERSION.RELEASE;
        // TODO: Set device
        // TODO: set ps time
    }

    public static OffersRequestBuilder newBuilder() {
        return new OffersRequestBuilder();
    }

    public OffersRequestBuilder uid(String uid) {
        this.uid = uid;
        return this;
    }

    public OffersRequestBuilder locale(String locale) {
        this.locale = locale;
        return this;
    }

    public OffersRequestBuilder googleAdId(String google_ad_id) {
        this.google_ad_id = google_ad_id;
        return this;
    }

    public OffersRequestBuilder limitedTrackingEnabled(boolean google_ad_id_limited_tracking_enabled) {
        this.google_ad_id_limited_tracking_enabled = google_ad_id_limited_tracking_enabled;
        return this;
    }

    public OffersRequestBuilder ip(String ip) {
        this.ip = ip;
        return this;
    }

    public OffersRequestBuilder pub0(String pub0) {
        this.pub0 = pub0;
        return this;
    }

    public OffersRequestBuilder page(int page) {
        this.page = page;
        return this;
    }

    public OffersRequestBuilder appId(int appId) {
        this.appId = appId;
        return this;
    }

    public OffersRequestBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public OffersRequestBuilder offerTypes(String offer_types) {
        this.offer_types = offer_types;
        return this;
    }

    public OffersRequestBuilder psTime(String ps_time) {
        this.ps_time = ps_time;
        return this;
    }

    public rx.Observable<OfferListResponse> build(IFyberClient client) {
        //TODO: Validate all params before creating
        // TODO: Should we create a hashKey here? We can but code looks ugly.
        return client.getOffers(apiKey, "json", appId, uid, osVersion, locale, System.currentTimeMillis() / 1000l,
                google_ad_id, google_ad_id_limited_tracking_enabled,
                ip, pub0, page, offer_types, 0l, device);
    }

}
