package by.cooper.android.retailaccounting.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Named;

import by.cooper.android.retailaccounting.firebase.auth.AuthListener;
import by.cooper.android.retailaccounting.firebase.auth.AuthStorage;
import by.cooper.android.retailaccounting.util.UrlContract;
import dagger.Module;
import dagger.Provides;

import static by.cooper.android.retailaccounting.dagger.DaggerContract.BASE_FB;
import static by.cooper.android.retailaccounting.dagger.DaggerContract.IMAGES_FB;
import static by.cooper.android.retailaccounting.util.UrlContract.IMAGES_URL;

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
        return context.getApplicationContext().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
    }

    @NonNull
    @Provides
    public AuthStorage provideAuthStorage(@Named("auth") SharedPreferences preferences) {
        return new AuthStorage(preferences);
    }

    @NonNull
    @Provides
    @Named(BASE_FB)
    public Firebase provideBaseFirebase() {
        return new Firebase(UrlContract.BASE_URL);
    }

    @NonNull
    @Provides
    @Named(IMAGES_FB)
    public Firebase provideImagesFirebase() {
        Firebase ref = new Firebase(IMAGES_URL);
        ref.keepSynced(true);
        return ref;
    }

    @NonNull
    @Provides
    public AuthListener provideAuthStateListener(AuthStorage authStorage) {
        return new AuthListener(authStorage);
    }
}
