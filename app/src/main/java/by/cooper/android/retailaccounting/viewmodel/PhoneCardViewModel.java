package by.cooper.android.retailaccounting.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.activity.PhoneActivity;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.DateTimeUtils;


public class PhoneCardViewModel extends BaseObservable {

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
    public boolean isPhoneSold() {
        return mPhone.getSoldDate() > Phone.DEFAULT_DATE;
    }

    public void onDeleteClick(View view) {
        // TODO: implement deleting phone from Firebase
        Toast.makeText(view.getContext(),
                "Delete phone with IMEI: " + mPhone.getImei() + "\nKey in DB: " + mPhone.getKey(),
                Toast.LENGTH_SHORT).show();
    }

    public void onEditClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, PhoneActivity.class);
        intent.putExtra(PhoneActivity.PHONE_KEY, Parcels.wrap(mPhone));
        context.startActivity(intent);
    }

    public void onCheckClick(View view) {
        // TODO: implement sale phone
        Toast.makeText(view.getContext(),
                "Sold phone with IMEI: " + mPhone.getImei() + "\nKey in DB: " + mPhone.getKey(),
                Toast.LENGTH_SHORT).show();
    }

}
