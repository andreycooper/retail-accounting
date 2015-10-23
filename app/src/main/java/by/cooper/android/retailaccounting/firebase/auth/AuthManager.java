package by.cooper.android.retailaccounting.firebase.auth;


import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.firebase.FirebaseErrorException;
import rx.Notification;
import rx.Observable;
import rx.subjects.PublishSubject;

public class AuthManager {
    private static final String TAG = "AuthManager";

    @NonNull
    private final Firebase mBaseRef;
    @NonNull
    private final PublishSubject<Notification<AuthData>> mLoginPublishSubject;

    @Inject
    public AuthManager(@NonNull Firebase firebase, @NonNull AuthListener authListener) {
        mBaseRef = firebase;
        mBaseRef.addAuthStateListener(authListener);
        mLoginPublishSubject = PublishSubject.create();
    }

    public void login(@NonNull String email, @NonNull String password) {
        mBaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d(TAG, "onAuthenticated");
                mLoginPublishSubject.onNext(Notification.createOnNext(authData));
                mLoginPublishSubject.cache();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.d(TAG, "onAuthenticationError");
                mLoginPublishSubject.onNext(Notification
                        .createOnError(new FirebaseErrorException(firebaseError)));
                mLoginPublishSubject.cache();
            }
        });
    }

    public boolean isLoggedIn() {
        AuthData authData = mBaseRef.getAuth();
        return authData != null;
    }

    @NonNull
    public Observable<Notification<AuthData>> getLoginObservable() {
        return mLoginPublishSubject;
    }

}
