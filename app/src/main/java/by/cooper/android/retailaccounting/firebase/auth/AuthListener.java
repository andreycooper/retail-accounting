package by.cooper.android.retailaccounting.firebase.auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.dagger.Named;
import by.cooper.android.retailaccounting.model.User;


public class AuthListener implements Firebase.AuthStateListener {

    public static final String TAG = "AuthListener";
    private AuthStorage mAuthStorage;

    @Inject
    public AuthListener(@Named("auth") @NonNull AuthStorage authStorage) {
        mAuthStorage = authStorage;
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if (authData != null) {
            updateUserInfo(authData);
        } else {
            deleteUserInfo();
        }
    }

    private void updateUserInfo(@NonNull AuthData authData) {
        Log.d(TAG, "Updating user: " + authData.toString());
        mAuthStorage.putUser(User.createUser(authData));
    }

    private void deleteUserInfo() {
        Log.d(TAG, "User logoff");
        mAuthStorage.resetAuth();
    }
}
