package by.cooper.android.retailaccounting.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.viewmodel.UserViewModel;
import dagger.Lazy;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String USER_VIEW_MODEL = "user_view_model";
    @Inject
    Lazy<UserViewModel> mLazyUserViewModel;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getLoginComponent().inject(this);
        by.cooper.android.retailaccounting.databinding.ActivityLoginBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (savedInstanceState != null) {
            mUserViewModel = Parcels.unwrap(savedInstanceState.getParcelable(USER_VIEW_MODEL));
        } else {
            mUserViewModel = mLazyUserViewModel.get();
        }
        binding.setUserModel(mUserViewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_VIEW_MODEL, Parcels.wrap(mUserViewModel));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserViewModel.subscribeLoginEvents(this);
    }

    @Override
    protected void onStop() {
        mUserViewModel.unsubscribeLoginEvents();
        super.onStop();
    }
}