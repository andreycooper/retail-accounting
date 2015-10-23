package by.cooper.android.retailaccounting.viewmodel;

import android.app.Activity;
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
import com.firebase.client.FirebaseError;

import org.parceler.Parcel;
import org.parceler.Transient;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.firebase.FirebaseErrorException;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.model.User;
import by.cooper.android.retailaccounting.util.Objects;
import by.cooper.android.retailaccounting.util.TextWatcherAdapter;
import dagger.Lazy;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;


@Parcel(Parcel.Serialization.BEAN)
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

    @Inject
    @Transient
    public Lazy<AuthManager> mLazyAuthManager;
    @Transient
    private CompositeSubscription mLoginSubscription;

    public UserViewModel() {
        mLoginSubscription = new CompositeSubscription();
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
            if (mLazyAuthManager == null) {
                App.get(view.getContext()).getLoginComponent().inject(UserViewModel.this);
            }
            if (validate(view.getResources())) {
                mLazyAuthManager.get().login(email.get(), password.get());
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

    public void setLoginSuccess(View view, AuthData authData) {
        Log.d(TAG, "authData:" + authData);
        // TODO: show next screen!
        Snackbar.make(view, "Proceed to login", Snackbar.LENGTH_SHORT).show();
    }

    public void setLoginError(View view, FirebaseErrorException throwable) {
        Resources resources = view.getResources();
        String errorMessage;
        switch (throwable.getFirebaseError().getCode()) {
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

    public void subscribeLoginEvents(Activity activity) {
        if (mLazyAuthManager == null) {
            App.get(activity).getLoginComponent().inject(UserViewModel.this);
        }
        View rootView = activity.findViewById(android.R.id.content);
        mLoginSubscription.add(mLazyAuthManager.get().getLoginObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authDataNotification -> {
                    switch (authDataNotification.getKind()) {
                        case OnNext:
                            Log.d(TAG, "onNext()");
                            setLoginSuccess(rootView, authDataNotification.getValue());
                            break;
                        case OnError:
                            Log.d(TAG, "onError()");
                            Throwable throwable = authDataNotification.getThrowable();
                            if (throwable instanceof FirebaseErrorException) {
                                setLoginError(rootView, (FirebaseErrorException) throwable);
                            }
                            break;
                    }
                }));
    }

    public void unsubscribeLoginEvents() {
        mLoginSubscription.clear();
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