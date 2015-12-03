package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import by.cooper.android.retailaccounting.model.Commodity;

import static by.cooper.android.retailaccounting.util.UrlContract.IMAGES_URL;
import static by.cooper.android.retailaccounting.util.UrlContract.SLASH;


public abstract class Repository<T extends Commodity> {

    private static final String TAG = Repository.class.getSimpleName();

    private final Firebase mImagesRef;
    private final Firebase mRef;
    private final Class<T> mClazz;

    public Repository(Firebase ref, Class<T> clazz, Firebase imagesRef) {
        mRef = ref;
        mClazz = clazz;
        mImagesRef = imagesRef;
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

    public String putImage(@NonNull final String image) {
        Firebase imageRef = mImagesRef.push();
        imageRef.setValue(image, (firebaseError, firebase) -> {
            if (firebaseError != null) {
                Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
            } else {
                Log.d(TAG, "Data saved successfully.");
            }
        });
        return IMAGES_URL + SLASH + imageRef.getKey();
    }

    public abstract void putItem(@NonNull T object);
}
