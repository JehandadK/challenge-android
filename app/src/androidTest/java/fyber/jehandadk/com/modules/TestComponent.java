package fyber.jehandadk.com.modules;

import javax.inject.Singleton;

import dagger.Component;
import fyber.jehandadk.com.base.BaseTest;

/**
 * Created by jehandad.kamal on 2/8/2016.
 */
@Singleton
@Component(modules = {MockApiModule.class, MockAppModule.class})
public interface TestComponent extends MainComponent {
    void inject(BaseTest test);
}
