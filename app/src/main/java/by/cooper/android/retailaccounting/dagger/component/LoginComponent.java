package by.cooper.android.retailaccounting.dagger.component;

import by.cooper.android.retailaccounting.activity.LoginActivity;
import by.cooper.android.retailaccounting.dagger.module.FirebaseModule;
import by.cooper.android.retailaccounting.dagger.module.LoginModule;
import dagger.Component;

@Component(modules = {
        FirebaseModule.class,
        LoginModule.class})
public interface LoginComponent {

    void inject(LoginActivity activity);
}
