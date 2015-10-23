package by.cooper.android.retailaccounting.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Singleton;

import by.cooper.android.retailaccounting.dagger.Named;
import by.cooper.android.retailaccounting.firebase.auth.AuthListener;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.firebase.auth.AuthStorage;
import by.cooper.android.retailaccounting.util.UrlContract;
import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    private static final String AUTH_PREFS = "auth_prefs";
    private Context mContext;

    public FirebaseModule(Context context) {
        mContext = context.getApplicationContext();
    }

    @NonNull
    @Provides
    public Context provideAppContext() {
        return mContext;
    }

    @Provides
    @Named("auth")
    public SharedPreferences provideAuthSharedPrefs(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        return prefs;
    }

    @NonNull
    @Provides
    public AuthStorage provideAuthStorage(@Named("auth") SharedPreferences preferences) {
        return new AuthStorage(preferences);
    }

    @NonNull
    @Provides
    public Firebase provideBaseFirebase() {
        return new Firebase(UrlContract.BASE_URL);
    }

    @NonNull
    @Provides
    public AuthListener provideAuthStateListener(AuthStorage authStorage) {
        return new AuthListener(authStorage);
    }
}
