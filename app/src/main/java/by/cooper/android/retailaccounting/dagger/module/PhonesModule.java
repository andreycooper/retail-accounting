package by.cooper.android.retailaccounting.dagger.module;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Named;

import by.cooper.android.retailaccounting.dagger.AppScope;
import by.cooper.android.retailaccounting.firebase.PhonesRepository;
import by.cooper.android.retailaccounting.model.Phone;
import dagger.Module;
import dagger.Provides;

@Module
public class PhonesModule {

    @NonNull
    @Provides
    @AppScope
    @Named("phones")
    public Firebase providePhonesFirebase() {
        Firebase phonesRef = new Firebase(Phone.getUrlPath());
        phonesRef.keepSynced(true);
        return phonesRef;
    }

    @NonNull
    @Provides
    @AppScope
    public PhonesRepository providePhonesRepository(@Named("phones") @NonNull Firebase phonesRef) {
        return new PhonesRepository(phonesRef);
    }
}
