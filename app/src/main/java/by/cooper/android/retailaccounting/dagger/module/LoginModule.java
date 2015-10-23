package by.cooper.android.retailaccounting.dagger.module;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Singleton;

import by.cooper.android.retailaccounting.firebase.auth.AuthListener;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.viewmodel.UserViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @NonNull
    @Provides
    @Singleton
    public AuthManager provideAuthManager(Firebase firebase, AuthListener stateListener) {
        return new AuthManager(firebase, stateListener);
    }

    @Provides
    public UserViewModel provideUserViewModel() {
        return new UserViewModel();
    }
}
