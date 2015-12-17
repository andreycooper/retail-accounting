package by.cooper.android.retailaccounting.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.parceler.Parcels;

import by.cooper.android.retailaccounting.activity.PhoneActivity;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.DateTimeUtils;


public class PhoneCardViewModel extends BasePhoneViewModel {

    private static final String TAG = PhoneCardViewModel.class.getSimpleName();
    private static final String DD_MM_YYYY_PATTERN = "dd.MM.yyyy";

    @NonNull
    private Phone mPhone;

    public PhoneCardViewModel(@NonNull Phone phone) {
        mPhone = phone;
    }

    @NonNull
    public Phone getPhone() {
        return mPhone;
    }

    @Bindable
    public String getPhoneReceiveDate() {
        return DateTimeUtils.convertDateMillisToPattern(mPhone.getReceiveDate(), DD_MM_YYYY_PATTERN);
    }

    @Bindable
    public String getPhoneSoldDate() {
        return DateTimeUtils.convertDateMillisToPattern(mPhone.getSoldDate(), DD_MM_YYYY_PATTERN);
    }

    @Bindable
    public String getPhoneBrand() {
        return mPhone.getBrand();
    }

    @Bindable
    public String getPhoneModel() {
        return mPhone.getModel();
    }

    @Bindable
    public String getPhoneImei() {
        return mPhone.getImei();
    }

    @Bindable
    public String getImageUrl() {
        return mPhone.getImageUrl();
    }

    @Bindable
    public boolean isPhoneSold() {
        return mPhone.getSoldDate() > Phone.DEFAULT_DATE;
    }

    public void onDeleteClick(View view) {
        if (!TextUtils.isEmpty(mPhone.getKey())) {
            // TODO: maybe show SnackBar with cancelation
            Context context = view.getContext();
            lazyInject(context);
            getDeletePhoneDialog(context, mPhone).show();
        }
    }

    public void onEditClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, PhoneActivity.class);
        intent.putExtra(PhoneActivity.PHONE_KEY, Parcels.wrap(mPhone));
        context.startActivity(intent);
    }

    public void onCheckClick(View view) {
        lazyInject(view.getContext());
        long soldTime = DateTimeZone.UTC.convertLocalToUTC(DateTime.now().getMillis(), true);
        mPhone.setSoldDate(soldTime);
        updatePhone(mPhone);
    }

    @Override
    protected void showError(@NonNull String error) {
        // TODO: think how to show SnackBar
        Toast.makeText(mLazyAppContext.get(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void savePhoneDone(Phone phone) {
        // empty implementation because it's never called in PhoneCardViewModel
    }

    @Override
    protected void updatePhoneDone(Phone phone) {
        Log.d(TAG, "Updated phone in base: " + phone);
    }

    @Override
    protected void deletePhoneDone(Phone phone) {
        Log.d(TAG, "Deleted phone: " + phone);
    }

}
