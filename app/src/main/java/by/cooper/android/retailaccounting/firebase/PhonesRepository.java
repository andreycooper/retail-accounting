package by.cooper.android.retailaccounting.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.model.Repository;
import by.cooper.android.retailaccounting.util.PhoneContract;


public final class PhonesRepository implements Repository<Phone> {

    public PhonesRepository(@NonNull Context context) {
        Firebase.setAndroidContext(context.getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public static PhonesRepository create(@NonNull Context context) {
        return new PhonesRepository(context);
    }

    @Override
    public void requestItems(@NonNull ResponseReceiver<Phone> responseReceiver) {
        Firebase ref = new Firebase(Phone.getUrlPath());
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new SingleRequest<>(responseReceiver, Phone.class));
    }

    @Override
    public void putItem(@NonNull Phone phone) {
        Firebase ref = new Firebase(Phone.getUrlPath());
        ref.keepSynced(true);
        Query refQuery = ref.orderByChild(PhoneContract.IMEI).equalTo(phone.getImei());
        refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getChildren().iterator().hasNext()) {
                    ref.push().setValue(phone);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
