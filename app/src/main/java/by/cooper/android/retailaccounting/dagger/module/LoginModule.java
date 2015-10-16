package by.cooper.android.retailaccounting.dagger.module;

import by.cooper.android.retailaccounting.firebase.AuthManager;
import by.cooper.android.retailaccounting.viewmodel.UserViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    public UserViewModel provideUserViewModel(AuthManager authManager){
        return new UserViewModel(authManager);
    }
}
