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

import static by.cooper.android.retailaccounting.util.CommodityContract.BRAND;
import static by.cooper.android.retailaccounting.util.PhoneContract.IMEI;


public final class PhonesRepository extends Repository<Phone> {

    private static final String TAG = PhonesRepository.class.getSimpleName();

    @Inject
    public PhonesRepository(@Named("phones") @NonNull Firebase ref) {
        super(ref, Phone.class);
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

    public Observable<List<String>> getModelSuggestionsByBrand(@NonNull final String brand, @NonNull final String model) {
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
