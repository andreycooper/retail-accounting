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
import by.cooper.android.retailaccounting.util.Events.PhoneAddedEvent;
import by.cooper.android.retailaccounting.util.Events.PhoneChangedEvent;
import by.cooper.android.retailaccounting.util.Events.PhoneRemovedEvent;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

import static by.cooper.android.retailaccounting.dagger.DaggerContract.IMAGES_FB;
import static by.cooper.android.retailaccounting.dagger.DaggerContract.PHONES_FB;
import static by.cooper.android.retailaccounting.dagger.DaggerContract.PHONES_LISTENER;

@Module
public class PhonesModule {

    public static final String TAG = "PhonesModule";

    @NonNull
    @Provides
    @AppScope
    @Named(PHONES_LISTENER)
    public ChildEventListener providePhonesListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                final Phone phone = dataSnapshot.getValue(Phone.class);
                phone.setKey(dataSnapshot.getKey());
                EventBus.getDefault().postSticky(new PhoneAddedEvent(phone));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChild) {
                final Phone phone = dataSnapshot.getValue(Phone.class);
                phone.setKey(dataSnapshot.getKey());
                EventBus.getDefault().postSticky(new PhoneChangedEvent(phone));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Phone phone = dataSnapshot.getValue(Phone.class);
                phone.setKey(dataSnapshot.getKey());
                EventBus.getDefault().postSticky(new PhoneRemovedEvent(phone));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved()");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "onCancelled()");
            }
        };
    }

    @NonNull
    @Provides
    @AppScope
    @Named(PHONES_FB)
    public Firebase providePhonesFirebase(@Named(PHONES_LISTENER) @NonNull final ChildEventListener phonesListener) {
        Firebase phonesRef = new Firebase(Phone.getUrlPath());
        phonesRef.keepSynced(true);
        phonesRef.addChildEventListener(phonesListener);
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
