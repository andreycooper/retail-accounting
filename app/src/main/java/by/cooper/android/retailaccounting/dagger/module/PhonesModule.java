package by.cooper.android.retailaccounting.dagger.module;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.inject.Named;

import by.cooper.android.retailaccounting.dagger.AppScope;
import by.cooper.android.retailaccounting.firebase.PhonesRepository;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.Events.PhonesUpdateEvent;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

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
        phonesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("PhonesModule", "onChildAdded(): " + s);
                EventBus.getDefault().postSticky(new PhonesUpdateEvent());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("PhonesModule", "onChildChanged(): " + s);
                EventBus.getDefault().postSticky(new PhonesUpdateEvent());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("PhonesModule", "onChildRemoved()");
                EventBus.getDefault().postSticky(new PhonesUpdateEvent());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("PhonesModule", "onChildMoved(): " + s);
                EventBus.getDefault().postSticky(new PhonesUpdateEvent());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
