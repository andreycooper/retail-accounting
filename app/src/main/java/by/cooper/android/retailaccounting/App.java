package by.cooper.android.retailaccounting;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.firebase.client.Firebase;

import net.danlew.android.joda.JodaTimeAndroid;

import by.cooper.android.retailaccounting.dagger.DaggerLoginComponent;
import by.cooper.android.retailaccounting.dagger.component.LoginComponent;
import by.cooper.android.retailaccounting.dagger.module.FirebaseModule;
import by.cooper.android.retailaccounting.dagger.module.LoginModule;


public class App extends Application {
    private LoginComponent mLoginComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        // It's important to initialize the ResourceZoneInfoProvider; otherwise
        // joda-time-android will not work.
        JodaTimeAndroid.init(this);

        mLoginComponent = DaggerLoginComponent.builder()
                .firebaseModule(new FirebaseModule(this))
                .loginModule(new LoginModule())
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getApplicationContext());
    }

    public LoginComponent getLoginComponent() {
        return mLoginComponent;
    }
}
