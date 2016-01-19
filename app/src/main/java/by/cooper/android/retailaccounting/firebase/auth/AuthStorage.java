package by.cooper.android.retailaccounting.firebase.auth;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.model.User;


public final class AuthStorage {
    private static final String EMAIL_KEY = "email";
    private static final String UID_KEY = "uid";
    private static final String PROVIDER_KEY = "provider";
    private static final String EXPIRES_KEY = "expires";
    private static final String PROFILE_IMAGE_URL_KEY = "profileImageURL";

    private SharedPreferences mPrefs;

    @Inject
    public AuthStorage(SharedPreferences prefs) {
        mPrefs = prefs;
    }

    public boolean isAuthenticated() {
        long expiresDate = mPrefs.getLong(EXPIRES_KEY, 0) * 1000;
        if (expiresDate < System.currentTimeMillis()) {
            resetAuth();
            return false;
        }
        return true;
    }

    public void resetAuth() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public void putUser(@NonNull User user) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(UID_KEY, user.getUid());
        editor.putString(PROVIDER_KEY, user.getProvider());
        editor.putLong(EXPIRES_KEY, user.getExpireDate());
        editor.putString(PROFILE_IMAGE_URL_KEY, user.getProfileImageUrl());
        editor.apply();
    }

    @Nullable
    public User getUser() {
        if (isAuthenticated()) {
            User user = new User();
            user.setUid(mPrefs.getString(UID_KEY, null));
            user.setProvider(mPrefs.getString(PROVIDER_KEY, null));
            user.setEmail(mPrefs.getString(EMAIL_KEY, null));
            user.setExpireDate(mPrefs.getLong(EXPIRES_KEY, 0L));
            user.setProfileImageUrl(mPrefs.getString(PROFILE_IMAGE_URL_KEY, null));
            return user;
        } else {
            return null;
        }
    }

    @Nullable
    public String getEmail() {
        return mPrefs.getString(EMAIL_KEY, null);
    }
}
