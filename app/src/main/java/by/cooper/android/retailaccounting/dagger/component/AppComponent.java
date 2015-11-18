package by.cooper.android.retailaccounting.dagger.component;

import by.cooper.android.retailaccounting.dagger.module.AppModule;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;
import dagger.Component;

@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(PhoneViewModel phoneViewModel);
}
