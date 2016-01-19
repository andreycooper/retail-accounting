package by.cooper.android.retailaccounting.util;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;

import by.cooper.android.retailaccounting.model.Phone;

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

    public static final class PhonesUpdateEvent {

    }

    public static final class PhoneAddedEvent {
        @NonNull
        private final Phone mPhone;

        public PhoneAddedEvent(@NonNull final Phone phone) {
            mPhone = phone;
        }

        @NonNull
        public Phone getPhone() {
            return mPhone;
        }
    }

    public static final class PhoneChangedEvent {
        @NonNull
        private final Phone mPhone;

        public PhoneChangedEvent(@NonNull final Phone phone) {
            mPhone = phone;
        }

        @NonNull
        public Phone getPhone() {
            return mPhone;
        }
    }

    public static final class PhoneRemovedEvent {
        @NonNull
        private final Phone mPhone;

        public PhoneRemovedEvent(@NonNull final Phone phone) {
            mPhone = phone;
        }

        @NonNull
        public Phone getPhone() {
            return mPhone;
        }
    }
}
