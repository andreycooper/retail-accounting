package by.cooper.android.retailaccounting.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


public abstract class BasePhoneFragment extends Fragment {
    public static final String PHONE = "by.cooper.android.retailaccounting.PHONE";
    public static final String PHONE_VIEW_MODEL = "by.cooper.android.retailaccounting.PHONE_VIEW_MODEL";
    private static final String LOG_TAG = BasePhoneFragment.class.getSimpleName();

    @Nullable
    protected Phone mPhone;
    @Nullable
    protected PhoneViewModel mViewModel;

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
        if (savedInstanceState != null && savedInstanceState.containsKey(PHONE_VIEW_MODEL)) {
            mViewModel = Parcels.unwrap(savedInstanceState.getParcelable(PHONE_VIEW_MODEL));
        }
        if (mViewModel == null) {
            mViewModel = getViewModel();
        }
        binding.setPhoneViewModel(mViewModel);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel != null) {
            mViewModel.onResume(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PHONE_VIEW_MODEL, Parcels.wrap(mViewModel));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String content = result.getContents();
            if (mViewModel != null && content != null) {
                mViewModel.onBarcodeScan(content);
            }
        }
    }


    protected abstract PhoneViewModel getViewModel();

    protected abstract int getTitle();

    protected abstract int getMenu();

    private void initUi(View rootView) {
        initToolBar(rootView);
        initImageView(rootView, mPhone);
    }

    // TODO: move this method into viewModel and create bind adapter "imageUrl"
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
