package by.cooper.android.retailaccounting.dagger.module;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

import javax.inject.Named;

import by.cooper.android.retailaccounting.dagger.AppScope;
import by.cooper.android.retailaccounting.firebase.PhonesRepository;
import by.cooper.android.retailaccounting.model.Phone;
import dagger.Module;
import dagger.Provides;

import static by.cooper.android.retailaccounting.dagger.DaggerContract.IMAGES_FB;
import static by.cooper.android.retailaccounting.dagger.DaggerContract.PHONES_FB;

@Module
public class PhonesModule {

    @NonNull
    @Provides
    @AppScope
    @Named(PHONES_FB)
    public Firebase providePhonesFirebase() {
        Firebase phonesRef = new Firebase(Phone.getUrlPath());
        phonesRef.keepSynced(true);
        return phonesRef;
    }

    @NonNull
    @Provides
    @AppScope
    public PhonesRepository providePhonesRepository(@Named(PHONES_FB) @NonNull Firebase phonesRef,
                                                    @Named(IMAGES_FB) @NonNull Firebase imagesRef) {
        return new PhonesRepository(phonesRef, imagesRef);
    }
}
