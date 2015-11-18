package by.cooper.android.retailaccounting.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.fragment.AddPhoneFragment;
import by.cooper.android.retailaccounting.fragment.EditPhoneFragment;
import by.cooper.android.retailaccounting.model.Phone;

public class PhoneActivity extends AppCompatActivity {

    public static final String PHONE_KEY = "by.cooper.android.retailaccounting.PHONE";
    public static final String PHONE_FRAGMENT = "PhoneFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        if (savedInstanceState == null) {
            Phone phone = Parcels.unwrap(getIntent().getParcelableExtra(PHONE_KEY));
            showFragment(getPhoneFragment(phone));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private Fragment getPhoneFragment(@Nullable final Phone phone) {
        if (phone == null) {
            return AddPhoneFragment.newInstance();
        } else {
            return EditPhoneFragment.newInstance(phone);
        }
    }

    private void showFragment(@NonNull final Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, fragment, PHONE_FRAGMENT).addToBackStack(null).commit();
    }
}
