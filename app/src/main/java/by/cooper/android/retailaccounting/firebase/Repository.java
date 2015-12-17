package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.List;

import by.cooper.android.retailaccounting.model.Commodity;
import by.cooper.android.retailaccounting.util.UrlContract;
import rx.Observable;


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

    public Observable<List<T>> requestItems() {
        return Observable.create(subscriber -> {
            final ResultReceiver<T> resultReceiver = new ResultReceiver<T>() {
                @Override
                public void onReceive(List<T> itemList) {
                    if (subscriber.isUnsubscribed()) return;

                    subscriber.onNext(itemList);
                    subscriber.onCompleted();
                }

                @Override
                public void onError(FirebaseError error) {
                    subscriber.onError(new FirebaseException(error));
                }
            };

            mRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
        });
    }

    // TODO: convert it to Observable
    public void requestItems(@NonNull ResultReceiver<T> resultReceiver, @NonNull String field, @NonNull String fieldQuery) {
        Query queryRef = mRef.orderByChild(field).startAt(fieldQuery);
        queryRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
    }

    // TODO: convert it to Observable
    public void requestItems(@NonNull ResultReceiver<T> resultReceiver, @NonNull String field, @NonNull String fieldQuery, int limit) {
        if (limit > 0) {
            Query queryRef = mRef.orderByChild(field).startAt(fieldQuery).limitToFirst(limit);
            queryRef.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, mClazz));
        } else {
            requestItems(resultReceiver, field, fieldQuery);
        }
    }

    public Observable<String> saveImage(@NonNull final String image) {
        final Firebase imageRef = mImagesRef.push();
        return Observable.create(subscriber -> {
            final Firebase.CompletionListener completionListener = (error, firebase) -> {
                if (subscriber.isUnsubscribed()) return;

                if (error != null) {
                    subscriber.onError(new FirebaseException(error));
                } else {
                    subscriber.onNext(UrlContract.buildImageUrl(firebase.getKey()));
                }
            };
            imageRef.setValue(image, completionListener);
        });
    }

    public abstract Observable<Boolean> saveItem(@NonNull T object);

    public abstract Observable<Boolean> updateItem(@NonNull T object);

    public abstract Observable<Boolean> deleteItem(@NonNull T object);
}
