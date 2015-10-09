package by.cooper.android.retailaccounting;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;

import by.cooper.android.retailaccounting.model.Phone;


public class AddPhoneFragment extends Fragment {
    private static final String PHONE = "by.cooper.android.retailaccounting.PHONE";
    private static final String LOG_TAG = AddPhoneFragment.class.getSimpleName();

    private Phone mPhone;

    public static AddPhoneFragment newInstance(Phone phone) {
        AddPhoneFragment fragment = new AddPhoneFragment();
        Bundle args = new Bundle();
        args.putParcelable(PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }

    public AddPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhone = getArguments().getParcelable(PHONE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_phone, container, false);
        initUi(rootView);
        return rootView;
    }

    private void initUi(View rootView) {
        initToolBar(rootView);
        initImageView(rootView, mPhone);
        initFab(rootView);
    }

    private void initFab(View rootView) {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        RxView.clickEvents(fab).subscribe(clickEvent -> {
            if (BuildConfig.DEBUG) {
                clickEvent.toString();
                Log.d("AddPhoneFragment", "clickEvent.view().getId():" + clickEvent.view().getId());
                Log.d("AddPhoneFragment", "FAB clicked!");

            }
        });
    }

    private void initImageView(View rootView, Phone phone) {
        ImageView imageView = (ImageView) rootView.findViewById(R.id.backdrop);

        if (phone != null && !TextUtils.isEmpty(phone.getImageUrl())) {
            imageView.setVisibility(View.VISIBLE);
            // TODO: load Image from Firebase and decode from Base64;
        } else {
            imageView.setVisibility(View.GONE);
        }

    }

    private void initToolBar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(R.string.title_add_phone);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
