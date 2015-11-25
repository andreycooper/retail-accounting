package by.cooper.android.retailaccounting.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;

import org.parceler.Parcel;
import org.parceler.Transient;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.activity.HomeActivity;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.util.Events.FirebaseLoginEvent;
import by.cooper.android.retailaccounting.util.Objects;
import dagger.Lazy;


@Parcel(Parcel.Serialization.FIELD)
public class LoginViewModel extends BaseObservable {
    public static final int EMPTY_RESOURCE_ID = 0;
    public static final String TAG = "LoginViewModel";

    // non private fields for @Parcel
    @Bindable
    public ObservableField<String> email = new ObservableField<>();
    @Bindable
    public ObservableField<String> emailError = new ObservableField<>();
    @Bindable
    public ObservableField<String> password = new ObservableField<>();
    @Bindable
    public ObservableField<String> passwordError = new ObservableField<>();
    @Bindable
    public ObservableBoolean isLoginInProgress = new ObservableBoolean(false);

    @Inject
    @Transient
    public Lazy<AuthManager> mLazyAuthManager;

    public LoginViewModel() {
    }

    public void onEmailChanged(Editable str) {
        if (!Objects.equals(email.get(), str.toString())) {
            emailError.set(null);
            email.set(str.toString());
        }
    }

    public void onPasswordChanged(Editable str) {
        if (!Objects.equals(password.get(), str.toString())) {
            passwordError.set(null);
            password.set(str.toString());
        }
    }

    public void onClickLogin(View view) {
        if (mLazyAuthManager == null) {
            App.get(view.getContext()).getLoginComponent().inject(LoginViewModel.this);
        }
        String emailString = email.get() != null ? email.get().trim() : "";
        String passwordString = password.get() != null ? password.get().trim() : "";
        if (validate(view.getResources(), emailString, passwordString)) {
            mLazyAuthManager.get().login(emailString, passwordString);
            isLoginInProgress.set(true);
        }
    }

    public void onEmailFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            setEmailError(view.getResources(), EMPTY_RESOURCE_ID);
        }
    }

    public void onPasswordFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            setPasswordError(view.getResources(), EMPTY_RESOURCE_ID);
        }
    }

    public void onReceiveLoginEvent(@NonNull Activity activity, @NonNull FirebaseLoginEvent loginEvent) {
        isLoginInProgress.set(false);
        View rootView = activity.findViewById(android.R.id.content);
        if (loginEvent.isSuccess()) {
            setLoginSuccess(rootView, loginEvent.getAuthData());
        } else {
            if (loginEvent.getFirebaseError() != null) {
                setLoginError(rootView, loginEvent.getFirebaseError());
            }
        }
    }

    private void setLoginSuccess(@NonNull View view, AuthData authData) {
        Context context = view.getContext();
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    private void setLoginError(@NonNull View view, @NonNull FirebaseError firebaseError) {
        Resources resources = view.getResources();
        String errorMessage;
        switch (firebaseError.getCode()) {
            case FirebaseError.USER_DOES_NOT_EXIST:
                errorMessage = resources.getString(R.string.firebase_error_user_does_not_exist);
                emailError.set(errorMessage);
                return;
            case FirebaseError.INVALID_EMAIL:
                errorMessage = resources.getString(R.string.firebase_error_invalid_email);
                emailError.set(errorMessage);
                return;
            case FirebaseError.INVALID_PASSWORD:
                errorMessage = resources.getString(R.string.firebase_error_invalid_password);
                passwordError.set(errorMessage);
                return;
            case FirebaseError.DISCONNECTED:
                errorMessage = resources.getString(R.string.firebase_error_disconnected);
                break;
            case FirebaseError.NETWORK_ERROR:
                errorMessage = resources.getString(R.string.firebase_error_network_error);
                break;
            default:
                errorMessage = resources.getString(R.string.firebase_error_unknown_error);
                break;
        }
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    private boolean validate(@NonNull Resources resources, @Nullable String email, @Nullable String password) {
        int emailErrorRes = getEmailErrorStringId(email);
        int passwordErrorRes = getPasswordErrorStringId(password);
        setErrors(resources, emailErrorRes, passwordErrorRes);
        return emailErrorRes == EMPTY_RESOURCE_ID && passwordErrorRes == EMPTY_RESOURCE_ID;
    }

    private void setErrors(@NonNull Resources resources, int emailErrorRes, int passwordErrorRes) {
        setEmailError(resources, emailErrorRes);
        setPasswordError(resources, passwordErrorRes);
    }

    private void setPasswordError(@NonNull Resources resources, int passwordErrorRes) {
        passwordError.set(passwordErrorRes != EMPTY_RESOURCE_ID ? resources.getString(passwordErrorRes) : null);
    }

    private void setEmailError(@NonNull Resources resources, int emailErrorRes) {
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
