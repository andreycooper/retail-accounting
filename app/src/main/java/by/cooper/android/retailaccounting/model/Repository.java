package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;

import by.cooper.android.retailaccounting.firebase.ResultReceiver;


public interface Repository<T> {

    void requestItems(@NonNull ResultReceiver<T> resultReceiver);

    void putItem(@NonNull T object);
}
