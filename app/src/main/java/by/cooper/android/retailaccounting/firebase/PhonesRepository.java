package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.cooper.android.retailaccounting.model.Phone;
import rx.Observable;

import static by.cooper.android.retailaccounting.dagger.DaggerContract.IMAGES_FB;
import static by.cooper.android.retailaccounting.dagger.DaggerContract.PHONES_FB;
import static by.cooper.android.retailaccounting.util.CommodityContract.BRAND;
import static by.cooper.android.retailaccounting.util.PhoneContract.IMEI;


public final class PhonesRepository extends Repository<Phone> {

    private static final String TAG = PhonesRepository.class.getSimpleName();

    @Inject
    public PhonesRepository(@Named(PHONES_FB) @NonNull Firebase phonesRef,
                            @Named(IMAGES_FB) @NonNull Firebase imagesRef) {
        super(phonesRef, Phone.class, imagesRef);
    }

    @Override
    public void putItem(@NonNull Phone phone) {
        final Firebase ref = getFirebase();
        Query refQuery = ref.orderByChild(IMEI).equalTo(phone.getImei());
        refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getChildren().iterator().hasNext()) {
                    Firebase phoneRef = ref.push();
                    phoneRef.setValue(phone, (firebaseError, firebase) -> {
                        if (firebaseError != null) {
                            Log.d(TAG, "Phone could not be saved. " + firebaseError.getMessage());
                        } else {
                            Log.d(TAG, "Phone saved successfully.");
                        }
                    });
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

    @Override
    public void updateItem(@NonNull String key, @NonNull Phone phone) {
        final Firebase updateRef = getFirebase().child(key);
        updateRef.setValue(phone, (firebaseError, firebase) -> {
            if (firebaseError != null) {
                Log.d(TAG, "Phone could not be updated. " + firebaseError.getMessage());
            } else {
                Log.d(TAG, "Phone updated successfully.");
            }
        });
    }

    @Override
    public void deleteItem(@NonNull String key, @NonNull Phone phone) {

    }

    public Observable<List<String>> getModelSuggestionsByBrand(@NonNull final String brand,
                                                               @NonNull final String model) {
        final Query refQuery = getFirebase().orderByChild(BRAND).equalTo(brand);
        return Observable.create(subscriber -> {
            final ResultReceiver<Phone> resultReceiver = new ResultReceiver<Phone>() {
                @Override
                public void onReceive(List<Phone> phoneList) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    Observable.from(phoneList)
                            .filter(phone -> phone.getModel().toLowerCase().startsWith(model.toLowerCase()))
                            .distinct(Phone::getModel)
                            .map(Phone::getModel)
                            .doOnNext(s -> Log.i(TAG, s))
                            .toList()
                            .subscribe(models -> {
                                subscriber.onNext(models);
                                subscriber.onCompleted();
                            });
                }

                @Override
                public void onError(FirebaseError error) {
                    subscriber.onError(new FirebaseException(error));
                }
            };
            refQuery.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, Phone.class));
        });
    }

    public Observable<List<String>> getBrandSuggestions(@NonNull final String brand) {
        final Query refQuery = getFirebase().orderByChild(BRAND).startAt(brand);
        return Observable.create(subscriber -> {
            final ResultReceiver<Phone> resultReceiver = new ResultReceiver<Phone>() {
                @Override
                public void onReceive(List<Phone> phoneList) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    Observable.from(phoneList)
                            .distinct(Phone::getBrand)
                            .map(Phone::getBrand)
                            .toList()
                            .subscribe(brands -> {
                                subscriber.onNext(brands);
                                subscriber.onCompleted();
                            });
                }

                @Override
                public void onError(FirebaseError error) {
                    subscriber.onError(new FirebaseException(error));
                }
            };
            refQuery.addListenerForSingleValueEvent(new SingleRequest<>(resultReceiver, Phone.class));
        });
    }
}
