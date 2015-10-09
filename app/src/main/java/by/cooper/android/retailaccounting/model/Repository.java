package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;

import by.cooper.android.retailaccounting.firebase.ResponseReceiver;


public interface Repository<T> {

    void requestItems(@NonNull ResponseReceiver<T> responseReceiver);

    void putItem(@NonNull T object);
}
