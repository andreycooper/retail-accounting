package by.cooper.android.retailaccounting.firebase.auth;


import android.support.annotation.NonNull;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.inject.Inject;
import javax.inject.Named;

import by.cooper.android.retailaccounting.util.Events;
import de.greenrobot.event.EventBus;

public class AuthManager {
    private static final String TAG = "AuthManager";

    @NonNull
    private final Firebase mBaseRef;

    @Inject
    public AuthManager(@Named("base") @NonNull Firebase firebase, @NonNull AuthListener authListener) {
        mBaseRef = firebase;
        mBaseRef.addAuthStateListener(authListener);
    }

    public void login(@NonNull String email, @NonNull String password) {
        mBaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                EventBus.getDefault().postSticky(new Events.FirebaseLoginEvent(authData));
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                EventBus.getDefault().postSticky(new Events.FirebaseLoginEvent(firebaseError));
            }
        });
    }

    public boolean isLoggedIn() {
        AuthData authData = mBaseRef.getAuth();
        return authData != null;
    }

}
