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
    String uid;
    String locale;
    String osVersion;
    long timestamp;
    String google_ad_id;
    boolean google_ad_id_limited_tracking_enabled;
    String ip;
    String pub0;
    int page;
    String offer_types;
    String ps_time;
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
        return client.getOffers(apiKey, "json", appId, uid, osVersion, locale, System.currentTimeMillis() / 1000,
                google_ad_id, google_ad_id_limited_tracking_enabled,
                ip, pub0, page, offer_types, 0l, device);
    }

}
