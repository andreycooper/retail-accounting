package by.cooper.android.retailaccounting.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @Inject
    UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getLoginComponent().inject(this);
        by.cooper.android.retailaccounting.databinding.ActivityLoginBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUserModel(mUserViewModel);

    }
}