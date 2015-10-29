package by.cooper.android.retailaccounting.dagger.component;

import by.cooper.android.retailaccounting.activity.HomeActivity;
import by.cooper.android.retailaccounting.dagger.AppScope;
import by.cooper.android.retailaccounting.dagger.module.PhonesModule;
import dagger.Component;


@Component(
        dependencies = {LoginComponent.class},
        modules = {PhonesModule.class})
@AppScope
public interface PhonesComponent extends LoginComponent {

    void inject(HomeActivity activity);
}
