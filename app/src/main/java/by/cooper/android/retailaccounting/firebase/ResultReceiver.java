package by.cooper.android.retailaccounting.firebase;

import com.firebase.client.FirebaseError;

import java.util.List;


public interface ResultReceiver<T> {
    void onReceive(List<T> itemList);

    void onError(FirebaseError error);
}
