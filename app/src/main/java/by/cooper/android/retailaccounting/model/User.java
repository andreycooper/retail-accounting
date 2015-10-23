package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;

import com.firebase.client.AuthData;

import org.parceler.Parcel;

/**
 * Created by Andrey Bondarenko on 16.10.15.
 */
@Parcel
public class User {
    private String mUid;
    private String mProvider;
    private String mEmail;
    private String mPass;
    private long mExpireDate;
    private String mProfileImageUrl;

    public User() {
    }

    public static User createUser(@NonNull AuthData authData) {
        User user = new User();
        user.setUid(authData.getUid());
        user.setProvider(authData.getProvider());
        user.setExpireDate(authData.getExpires());
        user.setEmail((String) authData.getProviderData().get("email"));
        user.setProfileImageUrl((String) authData.getProviderData().get("profileImageURL"));
        return user;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public long getExpireDate() {
        return mExpireDate;
    }

    public void setExpireDate(long expireDate) {
        mExpireDate = expireDate;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPass() {
        return mPass;
    }

    public void setPass(String pass) {
        mPass = pass;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        mProfileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUid='" + mUid + '\'' +
                ", mProvider='" + mProvider + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPass='" + mPass + '\'' +
                ", mExpireDate=" + mExpireDate +
                ", mProfileImageUrl='" + mProfileImageUrl + '\'' +
                '}';
    }
}
