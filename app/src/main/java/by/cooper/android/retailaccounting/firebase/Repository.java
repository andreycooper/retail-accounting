package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import by.cooper.android.retailaccounting.model.Commodity;


public abstract class Repository<T extends Commodity> {

    private final Firebase mRef;
    private final Class<T> mClazz;

    public Repository(Firebase ref, Class<T> clazz) {
        mRef = ref;
        mClazz = clazz;
    }

    public Firebase getFirebase() {
        return mRef;
    }

    public void requestItems(@NonNull ResultReceiver<T> resultReceiver) {
        mRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
    }

    public void requestItems(@NonNull ResultReceiver<T> resultReceiver, @NonNull String field, @NonNull String fieldQuery) {
        Query queryRef = mRef.orderByChild(field).startAt(fieldQuery);
        queryRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
    }

    public void requestItems(@NonNull ResultReceiver<T> resultReceiver, @NonNull String field, @NonNull String fieldQuery, int limit) {
        if (limit > 0) {
            Query queryRef = mRef.orderByChild(field).startAt(fieldQuery).limitToFirst(limit);
            queryRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
        } else {
            requestItems(resultReceiver, field, fieldQuery);
        }
    }

    public abstract void putItem(@NonNull T object);
}
