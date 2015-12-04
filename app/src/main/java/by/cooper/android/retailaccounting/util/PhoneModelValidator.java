package by.cooper.android.retailaccounting.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.model.Phone;

public final class PhoneModelValidator implements ModelValidator<Phone> {
    public static final String BRAND_ERROR = "brand_error";
    public static final String MODEL_ERROR = "model_error";
    public static final String IMEI_ERROR = "imei_error";

    private SimpleArrayMap<String, String> mErrorsMap;

    public static boolean isCorrectImei(@NonNull final String imei) {
        return imei.matches("\\d{15}");
    }

    public PhoneModelValidator() {
        initErrorsMap();
    }

    private void initErrorsMap() {
        mErrorsMap = new SimpleArrayMap<>(3);
        mErrorsMap.put(BRAND_ERROR, null);
        mErrorsMap.put(MODEL_ERROR, null);
        mErrorsMap.put(IMEI_ERROR, null);
    }

    @Override
    public boolean isModelValid(Context context, Phone phone) {
        boolean isValid = true;
        if (TextUtils.isEmpty(phone.getBrand())) {
            isValid = false;
            mErrorsMap.put(BRAND_ERROR, context.getString(R.string.phone_error_empty_brand));
        }
        if (TextUtils.isEmpty(phone.getModel())) {
            isValid = false;
            mErrorsMap.put(MODEL_ERROR, context.getString(R.string.phone_error_empty_model));
        }
        if (TextUtils.isEmpty(phone.getImei())) {
            isValid = false;
            mErrorsMap.put(IMEI_ERROR, context.getString(R.string.phone_error_empty_imei));
        } else if (!isCorrectImei(phone.getImei())) {
            isValid = false;
            mErrorsMap.put(IMEI_ERROR, context.getString(R.string.phone_error_not_correct_imei));
        }
        return isValid;
    }

    @Override
    public SimpleArrayMap<String, String> getErrorsMap() {
        return mErrorsMap;
    }
}
