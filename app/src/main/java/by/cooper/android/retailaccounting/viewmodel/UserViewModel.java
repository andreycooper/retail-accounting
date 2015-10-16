package by.cooper.android.retailaccounting.viewmodel;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.firebase.AuthManager;
import by.cooper.android.retailaccounting.model.User;
import by.cooper.android.retailaccounting.util.Objects;
import by.cooper.android.retailaccounting.util.TextWatcherAdapter;


public class UserViewModel extends BaseObservable {
    public static final int EMPTY_RESOURCE_ID = 0;
    public static final String TAG = "UserViewModel";

    @Bindable
    public ObservableField<String> email = new ObservableField<>();
    @Bindable
    public ObservableField<String> emailError = new ObservableField<>();
    @Bindable
    public ObservableField<String> password = new ObservableField<>();
    @Bindable
    public ObservableField<String> passwordError = new ObservableField<>();

    private AuthManager mAuthManager;

    @Inject
    public UserViewModel(AuthManager authManager) {
        mAuthManager = authManager;
    }

    public void setUser(@NonNull User user) {
        email.set(user.getEmail());
        password.set(user.getPass());
    }

    public TextWatcher getEmailWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable str) {
                if (!Objects.equals(email.get(), str.toString())) {
                    emailError.set(null);
                    email.set(str.toString());
                }
            }
        };
    }

    public TextWatcher getPasswordWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable str) {
                if (!Objects.equals(password.get(), str.toString())) {
                    passwordError.set(null);
                    password.set(str.toString());
                }
            }
        };
    }

    public View.OnClickListener getLoginClickListener() {
        return view -> {
            if (validate(view.getResources())) {
                Snackbar.make(view.getRootView(), "Email and pass is Ok", Snackbar.LENGTH_SHORT).show();
                mAuthManager.login(email.get(), password.get(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.d(TAG, "User with email: " + email.get() + " login OK");
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d(TAG, firebaseError.toString());
                    }
                });
            } else {
                Snackbar.make(view.getRootView(), "Enter correct fields", Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    public View.OnFocusChangeListener getEmailFocusListener() {
        return (view, hasFocus) -> {
            if (hasFocus) {
                setEmailError(view.getResources(), EMPTY_RESOURCE_ID);
            }
        };
    }

    public View.OnFocusChangeListener getPasswordFocusListener() {
        return (view, hasFocus) -> {
            if (hasFocus) {
                setPasswordError(view.getResources(), EMPTY_RESOURCE_ID);
            }
        };
    }

    private boolean validate(Resources resources) {
        int emailErrorRes = getEmailErrorStringId(email.get());
        int passwordErrorRes = getPasswordErrorStringId(password.get());
        setErrors(resources, emailErrorRes, passwordErrorRes);
        return emailErrorRes == EMPTY_RESOURCE_ID && passwordErrorRes == EMPTY_RESOURCE_ID;
    }

    private void setErrors(Resources resources, int emailErrorRes, int passwordErrorRes) {
        setEmailError(resources, emailErrorRes);
        setPasswordError(resources, passwordErrorRes);
    }

    private void setPasswordError(Resources resources, int passwordErrorRes) {
        passwordError.set(passwordErrorRes != EMPTY_RESOURCE_ID ? resources.getString(passwordErrorRes) : null);
    }

    private void setEmailError(Resources resources, int emailErrorRes) {
        emailError.set(emailErrorRes != EMPTY_RESOURCE_ID ? resources.getString(emailErrorRes) : null);
    }

    private int getEmailErrorStringId(@Nullable String email) {
        if (TextUtils.isEmpty(email)) {
            return R.string.mandatory_field;
        } else if (isValidEmail(email)) {
            return R.string.invalid_email;
        } else {
            return EMPTY_RESOURCE_ID;
        }
    }

    private int getPasswordErrorStringId(@Nullable String password) {
        if (TextUtils.isEmpty(password)) {
            return R.string.mandatory_field;
        } else {
            return EMPTY_RESOURCE_ID;
        }
    }

    private boolean isValidEmail(@NonNull String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
