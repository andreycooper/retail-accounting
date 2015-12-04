package by.cooper.android.retailaccounting.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.util.Events.FirebaseLoginEvent;
import by.cooper.android.retailaccounting.viewmodel.LoginViewModel;
import dagger.Lazy;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String USER_VIEW_MODEL = "user_view_model";
    @Inject
    Lazy<LoginViewModel> mLazyLoginViewModel;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getLoginComponent().inject(this);
        by.cooper.android.retailaccounting.databinding.ActivityLoginBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (savedInstanceState != null) {
            mLoginViewModel = Parcels.unwrap(savedInstanceState.getParcelable(USER_VIEW_MODEL));
        } else {
            mLoginViewModel = mLazyLoginViewModel.get();
        }
        binding.setLoginViewModel(mLoginViewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_VIEW_MODEL, Parcels.wrap(mLoginViewModel));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(FirebaseLoginEvent loginEvent) {
        EventBus.getDefault().removeStickyEvent(loginEvent);
        mLoginViewModel.onReceiveLoginEvent(this, loginEvent);
    }
}