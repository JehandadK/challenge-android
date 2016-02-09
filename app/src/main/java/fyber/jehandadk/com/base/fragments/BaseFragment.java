package fyber.jehandadk.com.base.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import fyber.jehandadk.com.App;
import fyber.jehandadk.com.base.BaseActivity;
import fyber.jehandadk.com.modules.MainComponent;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public App getApp() {
        return getBaseActivity().getApp();
    }

    public MainComponent getMainComponent() {
        return getApp().getMainComponent();
    }
}
