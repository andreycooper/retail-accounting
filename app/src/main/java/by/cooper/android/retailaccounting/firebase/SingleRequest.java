package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import by.cooper.android.retailaccounting.model.Commodity;


public class SingleRequest<T extends Commodity> implements ValueEventListener {

    private ResultReceiver<T> mReceiver;
    private Class<T> mClassType;

    public SingleRequest(@NonNull ResultReceiver<T> receiver, @NonNull Class<T> classType) {
        mReceiver = receiver;
        mClassType = classType;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<T> itemList = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            System.out.println(snapshot);
            T object = snapshot.getValue(mClassType);
            object.setKey(snapshot.getKey());
            itemList.add(object);
        }
        mReceiver.onReceive(itemList);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        mReceiver.onError(firebaseError);
    }
}
