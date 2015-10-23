package by.cooper.android.retailaccounting.dagger.component;

import javax.inject.Singleton;

import by.cooper.android.retailaccounting.activity.LoginActivity;
import by.cooper.android.retailaccounting.dagger.module.FirebaseModule;
import by.cooper.android.retailaccounting.dagger.module.LoginModule;
import by.cooper.android.retailaccounting.viewmodel.UserViewModel;
import dagger.Component;

@Component(modules = {
        FirebaseModule.class,
        LoginModule.class})
@Singleton
public interface LoginComponent {
    void inject(UserViewModel viewModel);

    void inject(LoginActivity activity);
}
