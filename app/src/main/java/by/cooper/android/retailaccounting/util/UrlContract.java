package by.cooper.android.retailaccounting.util;


import android.support.annotation.NonNull;

public final class UrlContract {

    public static final String BASE_URL = "https://retail-accounting.firebaseio.com";
    public static final String IMAGE_PATH = "/images";
    public static final String IMAGES_URL = BASE_URL + IMAGE_PATH;
    public static final String SLASH = "/";
    public static final String JSON_EXTENSION = ".json";

    public static String buildImageUrl(@NonNull final String key){
        return IMAGES_URL + SLASH + key + JSON_EXTENSION;
    }

    private UrlContract() {
    }
}
