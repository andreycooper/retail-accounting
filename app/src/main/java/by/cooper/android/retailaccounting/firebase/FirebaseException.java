package by.cooper.android.retailaccounting.firebase;

import com.firebase.client.FirebaseError;

/**
 * Created by Andrey Bondarenko on 2.12.15.
 */
public class FirebaseException extends Throwable{
    private final FirebaseError mFirebaseError;

    public FirebaseException(FirebaseError firebaseError) {
        super(firebaseError.getDetails());
        mFirebaseError = firebaseError;
    }

    public FirebaseError getFirebaseError() {
        return mFirebaseError;
    }
}
