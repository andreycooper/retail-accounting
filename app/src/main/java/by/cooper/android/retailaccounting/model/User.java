package by.cooper.android.retailaccounting.model;

import org.parceler.Parcel;

/**
 * Created by Andrey Bondarenko on 16.10.15.
 */
@Parcel
public class User {
    private String mEmail;
    private String mPass;

    public User() {
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
}
