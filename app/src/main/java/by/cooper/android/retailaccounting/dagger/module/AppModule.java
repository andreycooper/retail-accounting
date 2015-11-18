package by.cooper.android.retailaccounting.dagger.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @NonNull
    private Context mAppContext;

    public AppModule(@NonNull Context context) {
        mAppContext = context.getApplicationContext();
    }

    @NonNull
    @Provides
    public Context provideAppContext() {
        return mAppContext;
    }

    @NonNull
    @Provides
    public Resources provideResources() {
        return mAppContext.getResources();
    }
}
