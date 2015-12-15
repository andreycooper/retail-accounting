package by.cooper.android.retailaccounting.viewmodel;


import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.utilities.Base64;
import com.google.zxing.integration.android.IntentIntegrator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.activity.ScannerActivity;
import by.cooper.android.retailaccounting.firebase.SuggestionReceiver;
import by.cooper.android.retailaccounting.fragment.BasePhoneFragment;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.DateTimeUtils;
import by.cooper.android.retailaccounting.util.ModelValidator;
import by.cooper.android.retailaccounting.util.Objects;
import by.cooper.android.retailaccounting.util.PhoneModelValidator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static by.cooper.android.retailaccounting.util.PhoneModelValidator.BRAND_ERROR;
import static by.cooper.android.retailaccounting.util.PhoneModelValidator.IMEI_ERROR;
import static by.cooper.android.retailaccounting.util.PhoneModelValidator.MODEL_ERROR;


@Parcel(Parcel.Serialization.FIELD)
public class PhoneViewModel extends BasePhoneViewModel implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PhoneViewModel";
    private static final String RECEIVE_DATE_PICKER_TAG = "ReceiveDateTimePicker";
    private static final String SOLD_DATE_PICKER_TAG = "SoldDateTimePicker";
    public static final int COMPRESS_QUALITY = 90;

    Phone mPhone;
    String mDatePickerTag;

    @Bindable
    public ObservableField<String> brandError = new ObservableField<>();
    @Bindable
    public ObservableField<String> modelError = new ObservableField<>();
    @Bindable
    public ObservableField<String> imeiError = new ObservableField<>();

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
        inject(context);
        mPhone = new Phone();
    }

    public PhoneViewModel(@NonNull final BasePhoneFragment phoneFragment, @NonNull final Phone phone) {
        this(phoneFragment.getActivity(), phone);
        mFragmentWeakReference = new WeakReference<>(phoneFragment);
    }

    public PhoneViewModel(@NonNull final Context context, @NonNull final Phone phone) {
        inject(context);
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
        if (mPhone.getReceiveDate() <= Phone.DEFAULT_DATE) {
            mPhone.setReceiveDate(new DateTime(DateTimeZone.UTC).getMillis());
        }
        return getConvertedDate(mPhone.getReceiveDate());
    }

    @Bindable
    public String getSoldDate() {
        if (mPhone.getSoldDate() > 0) {
            return getConvertedDate(mPhone.getSoldDate());
        } else {
            return mLazyAppContext.get().getString(R.string.phone_item_label_not_sold);
        }
    }

    public void onBrandChanged(Editable str) {
        final String brand = str.toString().trim();
        if (!Objects.equals(mPhone.getBrand(), brand)) {
            brandError.set(null);
            mPhone.setBrand(brand);
            if (!TextUtils.isEmpty(brand)) {
                final SuggestionReceiver suggestionReceiver = mFragmentWeakReference.get();
                mLazyRepository.get()
                        .getBrandSuggestions(brand)
                        .subscribe(suggestionReceiver::onBrandsReceived, throwable -> {
                            suggestionReceiver.clearSuggestions();
                        });
            }
        }
    }

    public void onBrandItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, parent.getAdapter().getItem(position).toString());
    }

    public void onModelChanged(Editable str) {
        final String model = str.toString().trim();
        if (!Objects.equals(mPhone.getModel(), model)) {
            modelError.set(null);
            mPhone.setModel(model);
            final String brand = mPhone.getBrand().trim();
            if (!TextUtils.isEmpty(brand)) {
                final SuggestionReceiver suggestionReceiver = mFragmentWeakReference.get();
                mLazyRepository.get()
                        .getModelSuggestionsByBrand(brand, model)
                        .subscribe(suggestionReceiver::onModelsReceived, throwable -> {
                            suggestionReceiver.clearSuggestions();
                        });
            }
        }
    }

    public void onModelItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, parent.getAdapter().getItem(position).toString());
    }

    public void onImeiChanged(Editable str) {
        final String imei = str.toString().trim();
        if (!Objects.equals(mPhone.getImei(), imei)) {
            imeiError.set(null);
            mPhone.setImei(imei);
        }
    }

    public void onDateClick(View view) {
        DatePickerDialog dialog;
        if (view.getId() == R.id.receive_date_image_view) {
            mDatePickerTag = RECEIVE_DATE_PICKER_TAG;
            dialog = getReceivePickerDialog(mPhone, this);
        } else {
            mDatePickerTag = SOLD_DATE_PICKER_TAG;
            dialog = getSoldPickerDialog(mPhone, this);
        }
        dialog.show(mFragmentWeakReference.get().getFragmentManager(), mDatePickerTag);
    }

    public void onBarcodeClick(View view) {
        IntentIntegrator
                .forFragment(mFragmentWeakReference.get())
                .setCaptureActivity(ScannerActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                .initiateScan();
    }

    public void onSoldDeleteClick(View view) {
        mPhone.setSoldDate(Phone.DEFAULT_DATE);
        notifyPropertyChanged(by.cooper.android.retailaccounting.BR.soldDate);
    }

    public void onPhotoFabClick(View view) {
        if (!TextUtils.isEmpty(mPhone.getImageUrl())) {
            MaterialDialog dialog = new MaterialDialog.Builder(mFragmentWeakReference.get().getActivity())
                    .title(R.string.dialog_title_photo)
                    .content(R.string.dialog_content_photo)
                    .positiveText(R.string.dialog_positive)
                    .negativeText(R.string.dialog_negative)
                    .onPositive((materialDialog, dialogAction) -> mFragmentWeakReference.get().dispatchTakePictureIntent())
                    .onNegative((materialDialog, dialogAction) -> materialDialog.dismiss()).build();
            dialog.show();
        } else {
            mFragmentWeakReference.get().dispatchTakePictureIntent();
        }
    }

    public void onResume(@NonNull final BasePhoneFragment fragment) {
        mFragmentWeakReference.clear();
        mFragmentWeakReference = new WeakReference<>(fragment);
        lazyInject(fragment.getActivity());
        if (mDatePickerTag != null) {
            DatePickerDialog dialog = (DatePickerDialog) fragment.getFragmentManager()
                    .findFragmentByTag(mDatePickerTag);
            if (dialog != null) {
                dialog.setOnDateSetListener(this);
            }
        }
    }

    public void onBarcodeScan(@NonNull String barcode) {
        if (!Objects.equals(mPhone.getImei(), barcode)) {
            if (PhoneModelValidator.isCorrectImei(barcode)) {
                setImei(barcode);
            } else {
                String error = mLazyAppContext.get().getString(R.string.phone_error_error_scan_barcode, barcode);
                showError(error);
            }
        }
    }

    public void onThumbnailReceived(@NonNull final Bitmap thumbnail) {
        // TODO: Show progress while saving image!
        Observable.just(thumbnail)
                .map(bitmap -> {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream);
                    bitmap.recycle();
                    return stream.toByteArray();
                })
                .map(Base64::encodeBytes)
                .flatMap(image -> mLazyRepository.get().saveImage(image))
                .doOnNext(imageUrl -> Log.d(TAG, imageUrl))
                .filter(imageUrl -> !TextUtils.isEmpty(imageUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPhone::setImageUrl,
                        throwable -> {
                            Log.e(TAG, throwable.getMessage());
                            throwable.printStackTrace();
                            thumbnail.recycle();
                        }, thumbnail::recycle);
    }

    public void onActionDoneClick() {
        // TODO: maybe show confirm dialogs?
        ModelValidator<Phone> validator = new PhoneModelValidator();
        if (validator.isModelValid(mLazyAppContext.get(), mPhone)) {
            if (TextUtils.isEmpty(mPhone.getKey())) {
                savePhone(mPhone);
            } else {
                updatePhone(mPhone);
            }
        } else {
            brandError.set(validator.getErrorsMap().get(BRAND_ERROR));
            modelError.set(validator.getErrorsMap().get(MODEL_ERROR));
            imeiError.set(validator.getErrorsMap().get(IMEI_ERROR));
        }
    }

    public void onActionDeleteClick() {
        if (!TextUtils.isEmpty(mPhone.getKey())) {
            // TODO: maybe show SnackBar with cancelation
            getDeletePhoneDialog(mFragmentWeakReference.get().getActivity(), mPhone).show();
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

    @Override
    protected void savePhoneDone(Phone phone) {
        Log.d(TAG, "Saved phone: " + phone);
        mFragmentWeakReference.get().getActivity().onBackPressed();
    }

    @Override
    protected void updatePhoneDone(Phone phone) {
        Log.d(TAG, "Updated phone: " + phone);
        mFragmentWeakReference.get().getActivity().onBackPressed();
    }

    @Override
    protected void deletePhoneDone(Phone phone) {
        Log.d(TAG, "Deleted phone: " + phone);
        mFragmentWeakReference.get().getActivity().onBackPressed();
    }

    @Override
    protected void showError(@NonNull String error) {
        View rootView = mFragmentWeakReference.get().getView();
        if (rootView != null) {
            Snackbar.make(rootView, error, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mLazyAppContext.get(), error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setImei(@NonNull String imei) {
        imeiError.set(null);
        mPhone.setImei(imei);
        notifyPropertyChanged(by.cooper.android.retailaccounting.BR.imei);
        notifyPropertyChanged(by.cooper.android.retailaccounting.BR.imeiError);
    }

    private String getConvertedDate(final long millis) {
        return DateTimeUtils.convertDateMillisToPattern(DateTimeZone.UTC.convertUTCToLocal(millis),
                mLazyAppContext.get().getString(R.string.phone_date_pattern));
    }

}
