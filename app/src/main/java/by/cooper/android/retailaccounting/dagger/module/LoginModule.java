package by.cooper.android.retailaccounting.dagger.module;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Named;
import javax.inject.Singleton;

import by.cooper.android.retailaccounting.firebase.auth.AuthListener;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.viewmodel.LoginViewModel;
import dagger.Module;
import dagger.Provides;

import static by.cooper.android.retailaccounting.dagger.DaggerContract.BASE_FB;

@Module
public class LoginModule {

    @NonNull
    @Provides
    @Singleton
    public AuthManager provideAuthManager(@Named(BASE_FB) @NonNull Firebase firebase,
                                          @NonNull AuthListener stateListener) {
        return new AuthManager(firebase, stateListener);
    }

    @NonNull
    @Provides
    public LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }
}
