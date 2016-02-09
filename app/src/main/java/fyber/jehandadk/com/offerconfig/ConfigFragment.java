package fyber.jehandadk.com.offerconfig;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import fyber.jehandadk.com.Constants;
import fyber.jehandadk.com.R;
import fyber.jehandadk.com.base.fragments.BaseFragment;
import fyber.jehandadk.com.data.models.ApiConfig;
import fyber.jehandadk.com.offers.OffersActivity;
import fyber.jehandadk.com.prefs.IFyberPrefs;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public class ConfigFragment extends BaseFragment implements Validator.ValidationListener {


    @NotEmpty
    @Bind(R.id.edt_uid)
    EditText edtUid;

    @NotEmpty
    @Bind(R.id.edt_apikey)
    EditText edtApiKey;

    @NotEmpty
    @Digits(integer = 6)
    @Bind(R.id.edt_appid)
    EditText edtAppId;

    @Bind(R.id.edt_pub0)
    @Optional
    EditText edtPub0;
    @Inject
    IFyberPrefs prefs;
    private Validator validator;

    private static String text(EditText edt) {
        return edt.getText().toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validator = new Validator(this);
        validator.setValidationMode(Validator.Mode.BURST);
        validator.setValidationListener(this);

        ApiConfig config = prefs.apiConfig();
        if (config == null) {
            edtApiKey.setText(Constants.DEFAULT_API_KEY);
            edtAppId.setText(Constants.DEFAULT_APP_ID);
            edtUid.setText(Constants.DEFAULT_UID);
            edtPub0.setText(Constants.DEFAULT_PUB0);
        } else {
            edtApiKey.setText(config.getApiKey());
            edtAppId.setText(config.getAppId() + "");
            edtUid.setText(config.getUid());
            edtPub0.setText(config.getPub0());
        }

    }

    @OnClick(R.id.btn_save_config)
    protected void onSaveConfig() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        ApiConfig config = new ApiConfig();
        config.setApiKey(text(edtApiKey));
        config.setAppId(integer(edtAppId));
        config.setPub0(text(edtPub0));
        config.setUid(text(edtUid));
        prefs.apiConfig(config);
        startActivity(OffersActivity.newIntent(getActivity()));
    }

    private int integer(EditText edt) {
        return Integer.parseInt(text(edt));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
