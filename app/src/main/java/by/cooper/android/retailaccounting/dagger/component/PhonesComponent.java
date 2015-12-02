package by.cooper.android.retailaccounting.dagger.component;

import by.cooper.android.retailaccounting.activity.HomeActivity;
import by.cooper.android.retailaccounting.dagger.AppScope;
import by.cooper.android.retailaccounting.dagger.module.PhonesModule;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;
import dagger.Component;


@Component(
        dependencies = {LoginComponent.class},
        modules = {PhonesModule.class})
@AppScope
public interface PhonesComponent extends LoginComponent {

    void inject(HomeActivity activity);

    void inject(PhoneViewModel phoneViewModel);
}
