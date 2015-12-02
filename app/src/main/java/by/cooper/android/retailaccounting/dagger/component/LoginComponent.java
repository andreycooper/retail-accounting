package by.cooper.android.retailaccounting.dagger.component;

import android.content.Context;

import javax.inject.Singleton;

import by.cooper.android.retailaccounting.activity.LaunchActivity;
import by.cooper.android.retailaccounting.activity.LoginActivity;
import by.cooper.android.retailaccounting.dagger.module.FirebaseModule;
import by.cooper.android.retailaccounting.dagger.module.LoginModule;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.firebase.auth.AuthStorage;
import by.cooper.android.retailaccounting.viewmodel.LoginViewModel;
import dagger.Component;

@Component(modules = {
        FirebaseModule.class,
        LoginModule.class})
@Singleton
public interface LoginComponent {

    Context appContext();

    AuthStorage authStorage();

    AuthManager authManager();

    LoginViewModel loginViewModel();

    void inject(LaunchActivity activity);

    void inject(LoginViewModel viewModel);

    void inject(LoginActivity activity);
}
