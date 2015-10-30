package by.cooper.android.retailaccounting.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import by.cooper.android.retailaccounting.BR;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.Utils;


public class PhoneViewModel extends BaseObservable {

    private Phone mPhone;

    public PhoneViewModel() {
        mPhone = new Phone();
    }

    public PhoneViewModel(@NonNull Phone phone) {
        mPhone = phone;
    }

    @Bindable
    public String getBrand() {
        return mPhone.getBrand();
    }

    @Bindable
    public String getModel() {
        return mPhone.getModel();
    }

    @Bindable
    public String getImei() {
        return mPhone.getImei();
    }

    @Bindable
    public String getReceiveDate() {
        if (mPhone.getReceiveDate() <= 0) {
            mPhone.setReceiveDate(new DateTime(DateTimeZone.UTC).getMillis());
        }
        return getConvertedDate(mPhone.getReceiveDate());
    }

    @Bindable
    public String getSoldDate() {
        if (mPhone.getSoldDate() > 0) {
            return getConvertedDate(mPhone.getSoldDate());
        } else {
            return "Not Sold Yet";
        }
    }

    private String getConvertedDate(long millis) {
        DateTimeZone tz = DateTimeZone.UTC;
        return Utils.convertDateMillisToPattern(tz.convertUTCToLocal(millis), "EEE, dd MMM  yyyy");
    }

    public void setBrand(String brand) {
        mPhone.setBrand(brand);
        notifyPropertyChanged(BR.brand);
    }

    public void setModel(String model) {
        mPhone.setModel(model);
        notifyPropertyChanged(BR.model);
    }

    public View.OnClickListener getOnBarcodeClickListener() {
        return v -> Log.d("PhoneViewModel", "mPhone: " + mPhone.toString());
    }
}
