package fyber.jehandadk.com.offers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import fyber.jehandadk.com.R;
import fyber.jehandadk.com.base.BaseActivity;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public class OffersActivity extends BaseActivity {

    public static Intent newIntent(Activity act) {
        return new Intent(act, OffersActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
    }
}
