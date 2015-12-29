package by.cooper.android.retailaccounting.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.parceler.Transient;

import javax.inject.Inject;

import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.firebase.FirebaseException;
import by.cooper.android.retailaccounting.firebase.PhonesRepository;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.util.DateTimeUtils;
import by.cooper.android.retailaccounting.util.ImageHandler;
import dagger.Lazy;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public abstract class BasePhoneViewModel extends BaseObservable {

    public static final String TAG = "BasePhoneViewModel";
    @Inject
    @Transient
    Lazy<Context> mLazyAppContext;
    @Inject
    @Transient
    Lazy<PhonesRepository> mLazyRepository;

    protected void inject(Context context) {
        App.get(context).getPhonesComponent().inject(this);
    }

    protected void lazyInject(Context context) {
        if (mLazyAppContext == null || mLazyRepository == null) {
            inject(context);
        }
    }

    protected void updatePhone(@NonNull final Phone phone, @NonNull final ImageHandler imageHandler) {
        if (TextUtils.isEmpty(imageHandler.getCurrentPhotoPath())) {
            updatePhone(phone);
        } else {
            final PhonesRepository repository = mLazyRepository.get();
            repository.savePhoneImage(phone, imageHandler)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(imageUrl -> Log.d(TAG, "saved image: " + imageUrl))
                    .filter(imageUrl -> !TextUtils.isEmpty(imageUrl))
                    .map(imageUrl -> {
                        phone.setImageUrl(imageUrl);
                        return phone;
                    })
                    .flatMap(repository::updateItem)
                    .doOnNext(isUpdated -> Log.d(TAG, "phone is updated: " + isUpdated))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isUpdated -> onUpdateCompleted(phone, isUpdated), this::showThrowableError);
        }
    }

    protected void updatePhone(@NonNull final Phone phone) {
        mLazyRepository.get()
                .updateItem(phone)
                .subscribe(isUpdated -> onUpdateCompleted(phone, isUpdated), this::showThrowableError);
    }

    protected void deletePhone(@NonNull final Phone phone) {
        mLazyRepository.get().deleteItem(phone).subscribe(isDeleted -> {
            if (isDeleted) {
                // TODO: notify user and close fragment
                deletePhoneDone(phone);
            }
        }, this::showThrowableError);
    }

    protected void savePhone(@NonNull final Phone phone, @NonNull final ImageHandler imageHandler) {
        if (TextUtils.isEmpty(imageHandler.getCurrentPhotoPath())) {
            savePhone(phone);
        } else {
            final PhonesRepository repository = mLazyRepository.get();
            repository.savePhoneImage(phone, imageHandler)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(imageUrl -> Log.d(TAG, "saved image: " + imageUrl))
                    .filter(imageUrl -> !TextUtils.isEmpty(imageUrl))
                    .map(imageUrl -> {
                        phone.setImageUrl(imageUrl);
                        return phone;
                    })
                    .flatMap(repository::saveItem)
                    .doOnNext(isSaved -> Log.d(TAG, "phone is saved: " + isSaved))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isSaved -> onSavedCompleted(phone, isSaved), this::showThrowableError);
        }
    }

    protected void savePhone(@NonNull final Phone phone) {
        mLazyRepository.get()
                .saveItem(phone)
                .subscribe(isSaved -> onSavedCompleted(phone, isSaved), this::showThrowableError);
    }

    protected MaterialDialog getDeletePhoneDialog(@NonNull Context context, @NonNull final Phone phone) {
        return new MaterialDialog.Builder(context)
                .title(R.string.dialog_title_delete)
                .content(context.getString(R.string.dialog_content_delete, phone.getBrand(), phone.getModel()))
                .positiveText(R.string.dialog_positive)
                .negativeText(R.string.dialog_negative)
                .onPositive((materialDialog, dialogAction) -> deletePhone(phone))
                .onNegative((materialDialog, dialogAction) -> materialDialog.dismiss())
                .build();
    }

    protected DatePickerDialog getReceivePickerDialog(@NonNull final Phone phone,
                                                      DatePickerDialog.OnDateSetListener listener) {
        DateTime receiveDateTime = new DateTime(DateTimeZone.UTC
                .convertUTCToLocal(phone.getReceiveDate()));
        DatePickerDialog dialog = DatePickerDialog.newInstance(listener,
                receiveDateTime.getYear(),
                receiveDateTime.getMonthOfYear() - 1,
                receiveDateTime.getDayOfMonth());
        dialog.setMaxDate(DateTimeUtils.getCalendarInLocal());
        return dialog;
    }

    protected DatePickerDialog getSoldPickerDialog(@NonNull final Phone phone,
                                                   DatePickerDialog.OnDateSetListener listener) {
        DateTime nowLocalDateTime = DateTime.now(DateTimeZone.getDefault());
        DatePickerDialog dialog = DatePickerDialog.newInstance(listener,
                nowLocalDateTime.getYear(),
                nowLocalDateTime.getMonthOfYear() - 1,
                nowLocalDateTime.getDayOfMonth());
        dialog.setMinDate(DateTimeUtils.getCalendarInLocal(phone.getReceiveDate()));
        dialog.setMaxDate(DateTimeUtils.getCalendarInLocal());
        return dialog;
    }

    protected abstract void showError(@NonNull String error);

    protected abstract void savePhoneDone(Phone phone);

    protected abstract void updatePhoneDone(Phone phone);

    protected abstract void deletePhoneDone(Phone phone);

    private void onSavedCompleted(@NonNull Phone phone, Boolean isSaved) {
        if (isSaved) {
            savePhoneDone(phone);
        } else {
            String error = mLazyAppContext.get()
                    .getString(R.string.phone_error_already_exists, phone.getImei());
            showError(error);
        }
    }

    private void onUpdateCompleted(@NonNull Phone phone, Boolean isUpdated) {
        if (isUpdated) {
            updatePhoneDone(phone);
        }
    }

    @NonNull
    private String getFirebaseErrorString(@NonNull FirebaseException ex) {
        return mLazyAppContext.get().getString(R.string.phone_error_firebase_exception,
                ex.getFirebaseError().getDetails());
    }

    private void showThrowableError(@NonNull Throwable throwable) {
        if (throwable instanceof FirebaseException) {
            String error = getFirebaseErrorString((FirebaseException) throwable);
            showError(error);
        } else {
            // TODO: show unknown error to user!
        }
    }
}
