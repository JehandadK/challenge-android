package fyber.jehandadk.com.prefs;

import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;
import fyber.jehandadk.com.data.models.ApiConfig;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
@SharedPreferences(name = "main", mode = SharedPreferenceMode.PRIVATE)
public interface IFyberPrefs {
    ApiConfig apiConfig();

    void apiConfig(ApiConfig apiConfig);
}
