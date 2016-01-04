package by.cooper.android.retailaccounting.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.parceler.Parcel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import by.cooper.android.retailaccounting.model.Phone;

@Parcel
public class ImageHandler {

    public static final String DELIMITER = "_";
    public static final String JPG_EXTENSION = ".jpg";
    public static final String FILE_SCHEME = "file://";

    public static final int MAX_DIMENSION = 1024;
    public static final int COMPRESS_QUALITY = 90;

    String mCurrentPhotoPath;

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public File createImageFile(@NonNull final Phone phone) throws IOException {
        String imageFileName = createImageFileName(phone);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                JPG_EXTENSION,  /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String createImageFileName(@NonNull final Phone phone) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        StringBuilder imageFileName = new StringBuilder();
        if (!TextUtils.isEmpty(phone.getBrand())) {
            imageFileName.append(phone.getBrand()).append(DELIMITER);
        }
        if (!TextUtils.isEmpty(phone.getModel())) {
            imageFileName.append(phone.getModel()).append(DELIMITER);
        }
        if (!TextUtils.isEmpty(phone.getImei())) {
            imageFileName.append(phone.getImei()).append(DELIMITER);
        }
        imageFileName.append(timeStamp);
        return imageFileName.toString();
    }

    @Nullable
    private BitmapFactory.Options getBitmapOptions(@NonNull File imageFile) {
        if (imageFile.length() > 0) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            return bmOptions;
        } else {
            return null;
        }
    }

    @Nullable
    public Bitmap getBitmap() {
        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
            return null;
        }

        File imageFile = new File(mCurrentPhotoPath);
        if (imageFile.length() > 0) {
            BitmapFactory.Options bitmapOptions = getBitmapOptions(imageFile);
            if (bitmapOptions != null) {
                bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions);
                bitmapOptions.inJustDecodeBounds = false;
                return BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);
            }
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        int inSampleSize = 1;
        int maxDimension = Math.max(options.outWidth, options.outHeight);
        if (maxDimension > MAX_DIMENSION) {
            final int halfDimension = maxDimension / 2;
            while (((halfDimension / inSampleSize) > MAX_DIMENSION)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
