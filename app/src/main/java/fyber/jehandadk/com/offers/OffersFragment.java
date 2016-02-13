package fyber.jehandadk.com.offers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindInt;
import fyber.jehandadk.com.R;
import fyber.jehandadk.com.api.IFyberClient;
import fyber.jehandadk.com.api.OffersRequestBuilder;
import fyber.jehandadk.com.base.fragments.ListFragment;
import fyber.jehandadk.com.data.models.ApiConfig;
import fyber.jehandadk.com.data.models.Offer;
import fyber.jehandadk.com.data.models.OfferListResponse;
import fyber.jehandadk.com.prefs.IFyberPrefs;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public class OffersFragment extends ListFragment implements OffersAdapter.ItemClickedListener {


    @Inject
    IFyberClient api;

    @Inject
    IFyberPrefs prefs;

    @BindInt(R.integer.cols_products_grid)
    int coloumns;


    OffersAdapter adapter;
    OffersRequestBuilder builder;
    OfferListResponse data;

    @Inject
    @Named("external_ip")
    Observable<String> ip;

    @Inject
    @Named("google_ad_id")
    Observable<String> googleAdId;
    Subscriber subscription = new Subscriber<OfferListResponse>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            setErrMessage(e.getMessage());
        }

        @Override
        public void onNext(OfferListResponse offerListResponse) {
            if (offerListResponse == null) {
                setErrMessage("No Offers");
                return;
            }
            setData(offerListResponse);
        }
    };
    Subscriber ipAndAdIdSubscription = new Subscriber<OffersRequestBuilder>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            setErrMessage(e.getMessage());
        }

        @Override
        public void onNext(OffersRequestBuilder offerListResponse) {
        }
    };

    /**
     * So that all constants related to extras that need to be set or are required by this fragment
     * are bound to this class only.
     *
     * @return
     */
    public static Fragment newFragment() {
        return new OffersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainComponent().inject(this);
        createBuilder();
    }

    private void createBuilder() {
        ApiConfig config = prefs.apiConfig();
        builder = OffersRequestBuilder.newBuilder().apiKey(config.getApiKey()).appId(config.getAppId()).offerTypes("112").pub0(config.getPub0()).uid(config.getUid());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OffersAdapter(this);
        list.setAdapter(adapter);
        list.setHasFixedSize(false);
        list.setLayoutManager(new StaggeredGridLayoutManager(coloumns, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreReq();
    }

    @Override
    protected void retry() {
        loadPreReq();
    }

    protected void loadPreReq() {
        onLoadingStarted();
        // ip and googleAdId are both caches and singletons, no need to worry :)
        ip.zipWith(googleAdId, (ip, adId) -> {
            loadData(ip, adId);
            return builder;
        })
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(ipAndAdIdSubscription);
    }

    private void loadData(@NonNull String ip, @NonNull String adId) {
        builder.googleAdId(adId).limitedTrackingEnabled(true).page(1).ip(ip);
        builder.build(api)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscription);
    }

    private void setData(OfferListResponse data) {
        onLoadingFinished();
        adapter.setAll(data.getOffers());
        this.data = data;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Could use composite observables
        subscription.unsubscribe();
        ipAndAdIdSubscription.unsubscribe();

    }

    @Override
    public void onItemClicked(Offer Offer) {

    }

}
