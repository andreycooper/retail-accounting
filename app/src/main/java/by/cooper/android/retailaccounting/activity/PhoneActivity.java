package by.cooper.android.retailaccounting.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.fragment.AddPhoneFragment;
import by.cooper.android.retailaccounting.fragment.EditPhoneFragment;
import by.cooper.android.retailaccounting.model.Phone;

public class PhoneActivity extends AppCompatActivity {

    public static final String PHONE_KEY = "by.cooper.android.retailaccounting.PHONE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Fragment fragment;
        Phone phone = Parcels.unwrap(getIntent().getParcelableExtra(PHONE_KEY));
        if (phone == null) {
            fragment = AddPhoneFragment.newInstance();
        } else {
            fragment = EditPhoneFragment.newInstance(phone);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
