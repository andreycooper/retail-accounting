package by.cooper.android.retailaccounting.firebase;

import android.support.annotation.NonNull;

import com.firebase.client.FirebaseError;


public class FirebaseErrorException extends Throwable {
    @NonNull
    private FirebaseError mFirebaseError;

    public FirebaseErrorException(@NonNull FirebaseError firebaseError) {
        mFirebaseError = firebaseError;
    }

    @Override
    public String getMessage() {
        return mFirebaseError.getMessage();
    }

    @NonNull
    public FirebaseError getFirebaseError() {
        return mFirebaseError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirebaseErrorException)) return false;

        FirebaseErrorException that = (FirebaseErrorException) o;

        return getFirebaseError().getCode() == that.getFirebaseError().getCode() &&
                getFirebaseError().getMessage().equals(that.getFirebaseError().getMessage()) &&
                getFirebaseError().getDetails().equals(that.getFirebaseError().getDetails());

    }

    @Override
    public int hashCode() {
        return getFirebaseError().hashCode();
    }
}
