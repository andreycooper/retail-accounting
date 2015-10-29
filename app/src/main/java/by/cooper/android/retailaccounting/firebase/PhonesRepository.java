package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import javax.inject.Inject;
import javax.inject.Named;

import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.model.Repository;
import by.cooper.android.retailaccounting.util.PhoneContract;


public final class PhonesRepository implements Repository<Phone> {

    private Firebase mRef;

    @Inject
    public PhonesRepository(@Named("phones") @NonNull Firebase ref) {
        mRef = ref;
    }

    @Override
    public void requestItems(@NonNull ResultReceiver<Phone> resultReceiver) {
        mRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, Phone.class));
    }

    @Override
    public void putItem(@NonNull Phone phone) {
        Query refQuery = mRef.orderByChild(PhoneContract.IMEI).equalTo(phone.getImei());
        refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getChildren().iterator().hasNext()) {
                    Firebase phoneRef = mRef.push();
                    phoneRef.setValue(phone);
                    String phoneKey = phoneRef.getKey();
                    phone.setKey(phoneKey);
                    // TODO: maybe save to local DB?
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
