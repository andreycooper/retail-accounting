package by.cooper.android.retailaccounting.viewmodel;


import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.parceler.Parcel;
import org.parceler.Transient;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.activity.ScannerActivity;
import by.cooper.android.retailaccounting.fragment.BasePhoneFragment;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.DateTimeUtils;
import by.cooper.android.retailaccounting.util.Objects;
import by.cooper.android.retailaccounting.util.TextWatcherAdapter;
import dagger.Lazy;


@Parcel(Parcel.Serialization.FIELD)
public class PhoneViewModel extends BaseObservable implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PhoneViewModel";
    private static final String RECEIVE_DATE_PICKER_TAG = "ReceiveDateTimePicker";
    private static final String SOLD_DATE_PICKER_TAG = "SoldDateTimePicker";

    Phone mPhone;
    String mDatePickerTag;

    @Bindable
    ObservableField<String> mBrandError = new ObservableField<>();
    @Bindable
    ObservableField<String> mModelError = new ObservableField<>();
    @Bindable
    ObservableField<String> mImeiError = new ObservableField<>();

    @Inject
    @Transient
    Lazy<Resources> mLazyResources;

    @Inject
    @Transient
    Lazy<Context> mLazyContext;

    @Transient
    WeakReference<BasePhoneFragment> mFragmentWeakReference;

    /**
     * Parceler lib uses this constructor only
     *
     * @see <a href="http://parceler.org/">http://parceler.org/</a>
     */
    PhoneViewModel() {
    }

    public PhoneViewModel(@NonNull final BasePhoneFragment phoneFragment) {
        this(phoneFragment.getActivity());
        mFragmentWeakReference = new WeakReference<>(phoneFragment);
    }

    private PhoneViewModel(@NonNull final Context context) {
        App.get(context).getAppComponent().inject(this);
        mPhone = new Phone();
    }

    public PhoneViewModel(@NonNull final BasePhoneFragment phoneFragment, @NonNull final Phone phone) {
        this(phoneFragment.getActivity(), phone);
        mFragmentWeakReference = new WeakReference<>(phoneFragment);
    }

    public PhoneViewModel(@NonNull final Context context, @NonNull final Phone phone) {
        App.get(context).getAppComponent().inject(this);
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
            return mLazyResources.get().getString(R.string.phone_item_label_not_sold);
        }
    }

    public TextWatcher getBrandWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable str) {
                if (!Objects.equals(mPhone.getBrand(), str.toString())) {
                    mBrandError.set(null);
                    mPhone.setBrand(str.toString());
                }
            }
        };
    }

    public TextWatcher getModelWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable str) {
                if (!Objects.equals(mPhone.getModel(), str.toString())) {
                    mModelError.set(null);
                    mPhone.setModel(str.toString());
                }
            }
        };
    }

    public TextWatcher getImeiWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable str) {
                if (!Objects.equals(mPhone.getImei(), str.toString())) {
                    mImeiError.set(null);
                    if (isCorrectImei(str.toString())) {
                        mPhone.setImei(str.toString());
                    }
                }
            }
        };
    }

    public View.OnClickListener getOnDateClickListener() {
        return v -> {
            DatePickerDialog dialog;
            if (v.getId() == R.id.receive_date_image_view) {
                mDatePickerTag = RECEIVE_DATE_PICKER_TAG;
                dialog = getReceivePickerDialog();
            } else {
                mDatePickerTag = SOLD_DATE_PICKER_TAG;
                dialog = getSoldPickerDialog();
            }
            dialog.show(mFragmentWeakReference.get().getFragmentManager(), mDatePickerTag);
        };
    }

    public View.OnClickListener getOnBarcodeClickListener() {
        return v -> {
            // TODO: implement barcode scanner
            Log.d(TAG, "mPhone: " + mPhone.toString());
            IntentIntegrator
                    .forFragment(mFragmentWeakReference.get())
                    .setCaptureActivity(ScannerActivity.class)
                    .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                    .initiateScan();
        };
    }

    public View.OnClickListener getOnSoldDeleteClickListener() {
        return v -> {
            Log.d(TAG, "onSoldDeleteClick()");
            mPhone.setSoldDate(Phone.DEFAULT_DATE);
            notifyPropertyChanged(by.cooper.android.retailaccounting.BR.soldDate);
        };
    }

    public View.OnClickListener getOnFabClickListener() {
        return v -> {
            // TODO: implement taking photo
            Log.d(TAG, "onFabClick()");
        };
    }

    public void onResume(@NonNull final BasePhoneFragment fragment) {
        mFragmentWeakReference = new WeakReference<>(fragment);
        lazyInject(fragment);
        if (mDatePickerTag != null) {
            DatePickerDialog dialog = (DatePickerDialog) fragment.getFragmentManager()
                    .findFragmentByTag(mDatePickerTag);
            if (dialog != null) {
                dialog.setOnDateSetListener(this);
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDatePickerTag = null;
        long time = DateTimeUtils.getUtcTimeFromFields(year, monthOfYear, dayOfMonth);
        if (view.getTag().equals(RECEIVE_DATE_PICKER_TAG)) {
            mPhone.setReceiveDate(time);
            notifyPropertyChanged(by.cooper.android.retailaccounting.BR.receiveDate);
        } else {
            mPhone.setSoldDate(time);
            notifyPropertyChanged(by.cooper.android.retailaccounting.BR.soldDate);
        }
    }

    private String getConvertedDate(final long millis) {
        return DateTimeUtils.convertDateMillisToPattern(DateTimeZone.UTC.convertUTCToLocal(millis),
                mLazyResources.get().getString(R.string.phone_date_pattern));
    }

    private void lazyInject(BasePhoneFragment fragment) {
        if (mLazyResources == null || mLazyContext == null) {
            App.get(fragment.getActivity()).getAppComponent().inject(this);
        }
    }

    private boolean isCorrectImei(final String imei) {
        return imei.matches("\\d{15}");
    }

    private DatePickerDialog getReceivePickerDialog() {
        DateTime receiveDateTime = new DateTime(DateTimeZone.UTC
                .convertUTCToLocal(mPhone.getReceiveDate()));
        DatePickerDialog dialog = DatePickerDialog.newInstance(this,
                receiveDateTime.getYear(),
                receiveDateTime.getMonthOfYear() - 1,
                receiveDateTime.getDayOfMonth());
        dialog.setMaxDate(DateTimeUtils.getCalendarInLocal());
        return dialog;
    }

    private DatePickerDialog getSoldPickerDialog() {
        DateTime nowLocalDateTime = DateTime.now(DateTimeZone.getDefault());
        DatePickerDialog dialog = DatePickerDialog.newInstance(this,
                nowLocalDateTime.getYear(),
                nowLocalDateTime.getMonthOfYear() - 1,
                nowLocalDateTime.getDayOfMonth());
        dialog.setMinDate(DateTimeUtils.getCalendarInLocal(mPhone.getReceiveDate()));
        dialog.setMaxDate(DateTimeUtils.getCalendarInLocal());
        return dialog;
    }

}
