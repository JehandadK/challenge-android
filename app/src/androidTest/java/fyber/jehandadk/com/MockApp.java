package fyber.jehandadk.com;

import fyber.jehandadk.com.modules.DaggerTestComponent;
import fyber.jehandadk.com.modules.MockApiModule;
import fyber.jehandadk.com.modules.MockAppModule;

/**
 * Created by jehandad.kamal on 2/8/2016.
 */
public class MockApp extends App {

    @Override
    protected void createComponent() {
        mainComponent = DaggerTestComponent.builder()
                .mockAppModule(new MockAppModule(this))
                .mockApiModule(new MockApiModule())
                .build();
    }
}
