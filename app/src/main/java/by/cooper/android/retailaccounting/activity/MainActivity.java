package by.cooper.android.retailaccounting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import by.cooper.android.retailaccounting.R;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
