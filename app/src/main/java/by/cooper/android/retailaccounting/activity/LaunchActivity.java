package by.cooper.android.retailaccounting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Named;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.firebase.auth.AuthStorage;

public class LaunchActivity extends AppCompatActivity {

    private static final String LOG_TAG = LaunchActivity.class.getSimpleName();

    @Inject
    AuthStorage mAuthStorage;
    @Inject
    AuthManager mAuthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getLoginComponent().inject(this);
        if (mAuthStorage.isAuthenticated() && mAuthManager.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
