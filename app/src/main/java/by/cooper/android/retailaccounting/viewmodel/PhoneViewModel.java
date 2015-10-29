package by.cooper.android.retailaccounting.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.Utils;


public class PhoneViewModel extends BaseObservable {

    private static final String DD_MM_YYYY_PATTERN = "dd.MM.yyyy";

    @NonNull
    private Phone mPhone;

    public PhoneViewModel(@NonNull Phone phone) {
        mPhone = phone;
    }

    @NonNull
    public Phone getPhone() {
        return mPhone;
    }


    public String getPhoneReceiveDate() {
        return Utils.convertDateMillisToPattern(mPhone.getReceiveDate(), DD_MM_YYYY_PATTERN);
    }

    public String getPhoneSoldDate() {
        final long soldDate = mPhone.getSoldDate();
        if (soldDate > Phone.DEFAULT_DATE) {
            return Utils.convertDateMillisToPattern(soldDate, DD_MM_YYYY_PATTERN);
        } else {
            // TODO: get String from Resources
            return "Not sold yet";
        }
    }

    public String getPhoneBrand() {
        return mPhone.getBrand();
    }

    public String getPhoneModel() {
        return mPhone.getModel();
    }

    public String getPhoneImei() {
        return mPhone.getImei();
    }

    public boolean isPhoneSold() {
        return mPhone.getSoldDate() > Phone.DEFAULT_DATE;
    }

    public View.OnClickListener getOnDeleteClickListener() {
        return view -> Toast.makeText(view.getContext(),
                "Delete phone with IMEI: " + mPhone.getImei() + "\nKey in DB: " + mPhone.getKey(),
                Toast.LENGTH_SHORT).show();
    }

    public View.OnClickListener getOnEditClickListener() {
        return view -> Toast.makeText(view.getContext(),
                "Edit phone with IMEI: " + mPhone.getImei() + "\nKey in DB: " + mPhone.getKey(),
                Toast.LENGTH_SHORT).show();
    }

    public View.OnClickListener getOnCheckClickListener() {
        return view -> Toast.makeText(view.getContext(),
                "Sold phone with IMEI: " + mPhone.getImei() + "\nKey in DB: " + mPhone.getKey(),
                Toast.LENGTH_SHORT).show();
    }

}
