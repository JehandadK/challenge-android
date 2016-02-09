package fyber.jehandadk.com.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fyber.jehandadk.com.data.models.OfferListResponse;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * A strongly typed HTTP Client, it should not hold default values.
 * Should be usable in different situations for e.g. I havent hardcoded xml or json format.
 * Limiting capabilities given by apis is not a good idea.
 * For e.g. @Nullable and @NonNull is a big help for api consumers.
 */
public interface IFyberClient {

    String HEADER_API_KEY = "X-Fyber-API-Key";
    String QUERY_HASH_KEY = "hashkey";
    String HASH_NAME = "X-Sponsorpay-Response-Signature";


    @GET("/feed/v1/offers.{format}")
    Observable<OfferListResponse> getOffers(@Header(HEADER_API_KEY) String apiKey, // Why? Because Unlike in most cases api key is not constant
                                            @NonNull @Path("format") String format,
                                            @NonNull @Query("appid") Integer appid,
                                            @NonNull @Query("uid") String uid,
                                            @NonNull @Query("os_version") String osVersion,
                                            @NonNull @Query("locale") String locale,
                                            // @NonNull @Query("hashkey") String hashkey,
                                            @NonNull @Query("timestamp") Long timestamp,
                                            @NonNull @Query("google_ad_id") String googleAdId,
                                            @NonNull @Query("google_ad_id_limited_tracking_enabled") Boolean googleAdIdLimitedTrackingEnabled,
                                            @Nullable @Query("ip") String ip,
                                            @Nullable @Query("pub0") String pub0,
                                            @Nullable @Query("page") Integer page,
                                            @Nullable @Query("offer_types") String offerTypes,
                                            @Nullable @Query("ps_time") Long psTime,
                                            @Nullable @Query("device") String device);


    @GET("http://ifcfg.me/ip")
    Observable<ResponseBody> getIp();
}
