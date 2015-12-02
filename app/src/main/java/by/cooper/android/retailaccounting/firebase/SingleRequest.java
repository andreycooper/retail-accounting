package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import by.cooper.android.retailaccounting.model.Commodity;
import rx.Observable;


public class SingleRequest<T extends Commodity> implements ValueEventListener {

    private final ResultReceiver<T> mReceiver;
    private final Class<T> mClassType;

    public SingleRequest(@NonNull ResultReceiver<T> receiver, @NonNull Class<T> classType) {
        mReceiver = receiver;
        mClassType = classType;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Observable.from(dataSnapshot.getChildren())
                .map(snapshot -> {
                    System.out.println(snapshot);
                    T object = snapshot.getValue(mClassType);
                    object.setKey(snapshot.getKey());
                    return object;
                })
                .toList()
                .subscribe(mReceiver::onReceive);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        mReceiver.onError(firebaseError);
    }
}
