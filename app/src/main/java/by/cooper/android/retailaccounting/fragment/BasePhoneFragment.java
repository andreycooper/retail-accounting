package by.cooper.android.retailaccounting.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


public abstract class BasePhoneFragment extends Fragment {
    public static final String PHONE = "by.cooper.android.retailaccounting.PHONE";
    private static final String LOG_TAG = BasePhoneFragment.class.getSimpleName();

    @Nullable
    protected Phone mPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhone = Parcels.unwrap(getArguments().getParcelable(PHONE));
        }
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        by.cooper.android.retailaccounting.databinding.FragmentPhoneBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_phone, container, false);
        binding.setPhoneViewModel(getViewModel());
        View rootView = binding.getRoot();
        initUi(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(getMenu(), menu);
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

    protected abstract PhoneViewModel getViewModel();

    protected abstract int getTitle();

    protected abstract int getMenu();

    private void initUi(View rootView) {
        initToolBar(rootView);
        initImageView(rootView, mPhone);
        initFab(rootView);
    }

    private void initFab(View rootView) {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
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
            supportActionBar.setTitle(getTitle());
        }
    }

}
