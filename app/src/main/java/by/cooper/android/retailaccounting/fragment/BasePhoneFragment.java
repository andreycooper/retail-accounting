package by.cooper.android.retailaccounting.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.databinding.FragmentPhoneBinding;
import by.cooper.android.retailaccounting.firebase.SuggestionReceiver;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


public abstract class BasePhoneFragment extends Fragment implements SuggestionReceiver {
    protected static final String PHONE = "by.cooper.android.retailaccounting.PHONE";
    protected static final String PHONE_VIEW_MODEL = "by.cooper.android.retailaccounting.PHONE_VIEW_MODEL";
    protected static final String LOG_TAG = "PhoneFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 972;

    @Nullable
    protected Phone mPhone;
    @Nullable
    protected PhoneViewModel mViewModel;

    private ArrayAdapter<String> mBrandSuggestionAdapter;
    private ArrayAdapter<String> mModelSuggestionAdapter;

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
        setSuggestionAdapters(binding);
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
            case R.id.action_done:
                if (mViewModel != null) {
                    mViewModel.onActionDoneClick();
                }
                return true;
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap thumbnail = (Bitmap) extras.get("data");
            if (mViewModel != null && thumbnail != null) {
                mViewModel.onThumbnailReceived(thumbnail);
            }
        }

    }

    @Override
    public void onBrandsReceived(List<String> brandList) {
        mBrandSuggestionAdapter.clear();
        mBrandSuggestionAdapter.addAll(brandList);
    }

    @Override
    public void onModelsReceived(List<String> modelList) {
        mModelSuggestionAdapter.clear();
        mModelSuggestionAdapter.addAll(modelList);
    }

    @Override
    public void clearSuggestions() {
        mBrandSuggestionAdapter.clear();
        mModelSuggestionAdapter.clear();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected abstract PhoneViewModel getViewModel();

    protected abstract int getTitle();

    protected abstract int getMenu();

    private void initUi(View rootView) {
        initToolBar(rootView);
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

    private void setSuggestionAdapters(FragmentPhoneBinding binding) {
        mBrandSuggestionAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<>());
        mModelSuggestionAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<>());
        binding.brandEditText.setAdapter(mBrandSuggestionAdapter);
        binding.modelEditText.setAdapter(mModelSuggestionAdapter);
    }

}
