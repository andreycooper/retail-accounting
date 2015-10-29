package by.cooper.android.retailaccounting.util;


import android.support.annotation.Nullable;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;

public final class Events {
    private Events() {
    }

    public static final class FirebaseLoginEvent {
        private boolean mIsSuccess;
        @Nullable
        private FirebaseError mFirebaseError;
        @Nullable
        private AuthData mAuthData;

        public FirebaseLoginEvent(@Nullable AuthData authData) {
            if (authData != null) {
                mIsSuccess = true;
                mAuthData = authData;
            }
        }

        public FirebaseLoginEvent(@Nullable FirebaseError firebaseError) {
            if (firebaseError != null) {
                mIsSuccess = false;
                mFirebaseError = firebaseError;
            }
        }

        public boolean isSuccess() {
            return mIsSuccess;
        }

        @Nullable
        public FirebaseError getFirebaseError() {
            return mFirebaseError;
        }

        @Nullable
        public AuthData getAuthData() {
            return mAuthData;
        }
    }
}
