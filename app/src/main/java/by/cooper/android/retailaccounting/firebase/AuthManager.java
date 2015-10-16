package by.cooper.android.retailaccounting.firebase;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.Map;

import javax.inject.Inject;

public class AuthManager {

    private Firebase mBaseRef;
    private final AuthListener mAuthListener;

    @Inject
    public AuthManager(@NonNull Firebase firebase, @NonNull AuthListener authListener) {
        mBaseRef = firebase;
        mAuthListener = authListener;
        mBaseRef.addAuthStateListener(mAuthListener);
    }

    public void login(@NonNull String email, @NonNull String password, @NonNull Firebase.AuthResultHandler authResultHandler) {
        mBaseRef.authWithPassword(email, password, authResultHandler);
    }

    public boolean isLoggedIn() {
        AuthData authData = mBaseRef.getAuth();
        return authData != null;
    }

    public static class AuthListener implements Firebase.AuthStateListener {

        public static final String TAG = "AuthListener";
        private AuthStorage mAuthStorage;

        @Inject
        public AuthListener(@NonNull AuthStorage authStorage) {
            mAuthStorage = authStorage;
        }

        @Override
        public void onAuthStateChanged(AuthData authData) {
            if (authData != null) {
                // TODO: user is logged in update info
                updateUserInfo(authData);
            } else {
                // TODO: user is not logged in
                deleteUserInfo();
            }
        }

        private void updateUserInfo(@NonNull AuthData authData) {
            Log.d(TAG, "Updating user: " + authData.toString());
            for (Map.Entry<String, Object> entry : authData.getProviderData().entrySet()) {
                Log.d(TAG, entry.getKey() + " : " + entry.getValue());
            }
        }

        private void deleteUserInfo() {
            Log.d(TAG, "User logoff");
        }
    }

    public static final class AuthStorage {
        private final static String EMAIL_KEY = "email";

        private SharedPreferences mPrefs;

        @Inject
        public AuthStorage(SharedPreferences prefs) {
            mPrefs = prefs;
        }

        private boolean isAuthenticated() {
            String email = mPrefs.getString(EMAIL_KEY, null);
            return !TextUtils.isEmpty(email);
        }

        public void resetAuth() {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.remove(EMAIL_KEY);
            editor.apply();
        }

        public void setEmail(@NonNull String email) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(EMAIL_KEY, email);
            editor.apply();
        }

        @Nullable
        public String getEmail() {
            return mPrefs.getString(EMAIL_KEY, null);
        }
    }
}
