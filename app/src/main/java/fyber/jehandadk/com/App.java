package fyber.jehandadk.com;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import fyber.jehandadk.com.modules.ApiModule;
import fyber.jehandadk.com.modules.AppModule;
import fyber.jehandadk.com.modules.DaggerMainComponent;
import fyber.jehandadk.com.modules.MainComponent;

/**
 * Created by jehandad.kamal on 2/2/2016.
 */
public class App extends Application {


    protected MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        createComponent();
    }

    protected void createComponent() {
        mainComponent = DaggerMainComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
