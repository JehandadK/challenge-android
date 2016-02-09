package fyber.jehandadk.com.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicInteger;

import fyber.jehandadk.com.App;
import fyber.jehandadk.com.R;
import fyber.jehandadk.com.base.listeners.LoadingListener;
import fyber.jehandadk.com.helpers.EspressoIdlingResource;
import fyber.jehandadk.com.modules.MainComponent;

public abstract class BaseActivity extends AppCompatActivity implements LoadingListener {

    private static final String KEY_PROGRESS_COUNT = "progress_count";
    AtomicInteger loaderCount = new AtomicInteger(0);
    ProgressDialog mProgressDialog;
    private boolean isResumed = false;

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (loaderCount.get() > 0) showProgressDialog();
        else hideProgressDialog();
    }


    @Override
    public void onLoadingStarted() {
        EspressoIdlingResource.increment();
        int i = loaderCount.incrementAndGet();
        if (i > 0 && isResumed && mProgressDialog == null) showProgressDialog();

    }

    @Override
    public void onLoadingFinished() {
        EspressoIdlingResource.decrement();
        if (loaderCount.decrementAndGet() == 0)
            hideProgressDialog();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        mProgressDialog = null;
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.msg_loading));
        mProgressDialog.show();
    }


    public App getApp() {
        return (App) getApplication();
    }

    public MainComponent getMainComponent() {
        return getApp().getMainComponent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_PROGRESS_COUNT, loaderCount.intValue());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loaderCount = new AtomicInteger(savedInstanceState.getInt(KEY_PROGRESS_COUNT));
    }
}
