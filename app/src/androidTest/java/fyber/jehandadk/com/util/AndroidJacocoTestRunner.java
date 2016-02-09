package fyber.jehandadk.com.util;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.Method;

import fyber.jehandadk.com.BuildConfig;
import fyber.jehandadk.com.MockApp;

// http://stackoverflow.com/questions/30337375/empty-jacoco-report-for-android-espresso/31600193#31600193
public class AndroidJacocoTestRunner extends AndroidJUnitRunner {
    static {
        System.setProperty("jacoco-agent.destfile",
                "/data/data/" + BuildConfig.APPLICATION_ID + "/coverage.ec");
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        try {
            Class rt = Class.forName("org.jacoco.agent.rt.RT");
            Method getAgent = rt.getMethod("getAgent");
            Method dump = getAgent.getReturnType().getMethod("dump", boolean.class);
            Object agent = getAgent.invoke(null);
            dump.invoke(agent, false);
        } catch (Throwable e) {
//      Timber.e(Log.getStackTraceString(e));
        }


        super.finish(resultCode, results);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, MockApp.class.getName(), context);
    }
}