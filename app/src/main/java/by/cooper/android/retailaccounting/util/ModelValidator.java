package by.cooper.android.retailaccounting.util;


import android.content.Context;
import android.support.v4.util.SimpleArrayMap;

import by.cooper.android.retailaccounting.model.Commodity;

public interface ModelValidator<T extends Commodity> {
    boolean isModelValid(Context context, T model);

    SimpleArrayMap<String, String> getErrorsMap();
}
