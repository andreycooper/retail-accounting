package by.cooper.android.retailaccounting.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

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
import dagger.Lazy;


public abstract class BasePhoneViewModel extends BaseObservable {

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

    protected void updatePhone(@NonNull final Phone phone) {
        mLazyRepository.get().updateItem(phone).subscribe(isSaved -> {
            if (isSaved) {
                updatePhoneDone(phone);
            }
        }, this::showThrowableError);
    }

    protected void deletePhone(@NonNull final Phone phone) {
        mLazyRepository.get().deleteItem(phone).subscribe(isDeleted -> {
            if (isDeleted) {
                // TODO: notify user and close fragment
                deletePhoneDone(phone);
            }
        }, this::showThrowableError);
    }

    protected void savePhone(@NonNull final Phone phone) {
        mLazyRepository.get().saveItem(phone).subscribe(isSaved -> {
            if (isSaved) {
                savePhoneDone(phone);
            } else {
                String error = mLazyAppContext.get()
                        .getString(R.string.phone_error_already_exists, phone.getImei());
                showError(error);
            }
        }, this::showThrowableError);
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
}
