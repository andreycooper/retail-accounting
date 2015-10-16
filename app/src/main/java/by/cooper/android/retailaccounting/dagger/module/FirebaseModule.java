package by.cooper.android.retailaccounting.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import by.cooper.android.retailaccounting.firebase.AuthManager;
import by.cooper.android.retailaccounting.util.UrlContract;
import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

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
    public SharedPreferences provideSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    @NonNull
    @Provides
    public AuthManager.AuthStorage provideAuthStorage(SharedPreferences preferences) {
        return new AuthManager.AuthStorage(preferences);
    }

    @NonNull
    @Provides
    public Firebase provideBaseFirebase() {
        return new Firebase(UrlContract.BASE_URL);
    }

    @NonNull
    @Provides
    public Firebase.AuthStateListener provideAuthStateListener(AuthManager.AuthStorage authStorage) {
        return new AuthManager.AuthListener(authStorage);
    }
}
