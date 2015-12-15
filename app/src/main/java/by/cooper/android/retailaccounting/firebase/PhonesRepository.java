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
    public Observable<Boolean> saveItem(@NonNull Phone phone) {
        final Firebase ref = getFirebase();
        final Query refQuery = ref.orderByChild(IMEI).equalTo(phone.getImei());
        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) return;
            final Firebase.CompletionListener completionListener = (error, firebase) -> {
                if (error != null) {
                    subscriber.onError(new FirebaseException(error));
                } else {
                    Log.d(TAG, "Phone key: " + firebase.getKey());
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            };
            refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.getChildren().iterator().hasNext()) {
                        Firebase phoneRef = ref.push();
                        phoneRef.setValue(phone, completionListener);
                        String phoneKey = phoneRef.getKey();
                        phone.setKey(phoneKey);
                        Log.d(TAG, "Phone: " + phone);
                        // TODO: maybe save to local DB?
                    } else {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onCancelled(FirebaseError error) {
                    subscriber.onError(new FirebaseException(error));
                }
            });
        });
    }

    @Override
    public Observable<Boolean> updateItem(@NonNull Phone phone) {
        final Firebase updateRef = getFirebase().child(phone.getKey());
        return Observable.create(subscriber -> {
            final Firebase.CompletionListener completionListener = (error, firebase) -> {
                if (subscriber.isUnsubscribed()) return;
                if (error != null) {
                    subscriber.onError(new FirebaseException(error));
                } else {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            };
            updateRef.setValue(phone, completionListener);
        });
    }

    @Override
    public Observable<Boolean> deleteItem(@NonNull Phone phone) {
        final Firebase deleteRef = getFirebase().child(phone.getKey());
        return Observable.create(subscriber -> {
            final Firebase.CompletionListener completionListener = (error, firebase) -> {
                if (subscriber.isUnsubscribed()) return;
                if (error != null) {
                    subscriber.onError(new FirebaseException(error));
                } else {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            };
            deleteRef.removeValue(completionListener);
        });
    }

    public Observable<List<String>> getModelSuggestionsByBrand(@NonNull final String brand,
                                                               @NonNull final String model) {
        final Query refQuery = getFirebase().orderByChild(BRAND).equalTo(brand);
        return Observable.create(subscriber -> {
            final ResultReceiver<Phone> resultReceiver = new ResultReceiver<Phone>() {
                @Override
                public void onReceive(List<Phone> phoneList) {
                    if (subscriber.isUnsubscribed()) return;
                    Observable.from(phoneList)
                            .filter(phone -> phone.getModel().toLowerCase().startsWith(model.toLowerCase()))
                            .distinct(Phone::getModel)
                            .map(Phone::getModel)
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
                    if (subscriber.isUnsubscribed()) return;
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
