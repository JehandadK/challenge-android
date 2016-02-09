package fyber.jehandadk.com.base;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;

import javax.inject.Inject;

import fyber.jehandadk.com.App;
import fyber.jehandadk.com.modules.TestComponent;
import fyber.jehandadk.com.prefs.IFyberPrefs;

public class BaseTest {
    //  @Inject
//  protected GoogleApiClientBridge googleApiClientBridge;
//
//  @Inject
//  protected DatabaseApi databaseApi;
//
    @Inject
    protected IFyberPrefs pref;

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        App app
                = (App) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = (TestComponent) app.getMainComponent();
        component.inject(this);

//    Mockito.reset(googleApiClientBridge);
//
//    databaseApi.clear();

//    SharedPreferences.Editor editor = pref.edit();
//    editor.clear();
//    editor.apply();
    }
}