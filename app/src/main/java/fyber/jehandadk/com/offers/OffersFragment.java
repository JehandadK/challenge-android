package fyber.jehandadk.com.offers;

import android.os.Bundle;
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
    private int pages = 1;


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
        list.setLayoutManager(new StaggeredGridLayoutManager(coloumns, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void retry() {
        loadData();
    }

    protected void loadData() {
        onLoadingStarted();
        Observable<OffersRequestBuilder> obs = ip.zipWith(googleAdId, (ip, adId) -> {
            return builder.googleAdId(adId).limitedTrackingEnabled(true).page(pages).ip(ip);
        }).timeout(5, TimeUnit.SECONDS);
        obs.map((builder) -> builder.build(api).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<OfferListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                setErrMessage(e.getMessage());
            }

            @Override
            public void onNext(OfferListResponse offerListResponse) {
                setData(offerListResponse);
            }
        })).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscription -> isAdded(), throwable -> {
            setErrMessage(throwable.getMessage());
        });
    }

    private void setData(OfferListResponse data) {
        onLoadingFinished();
        pages++;
        adapter.setAll(data.getOffers());
        this.data = data;
    }

    @Override
    public void onItemClicked(Offer Offer) {

    }
}
