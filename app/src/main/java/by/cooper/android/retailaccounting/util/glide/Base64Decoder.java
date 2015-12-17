package by.cooper.android.retailaccounting.util.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.SimpleResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Base64Decoder implements ResourceDecoder<ImageVideoWrapper, Bitmap> {

    @Override
    public Resource<Bitmap> decode(ImageVideoWrapper source, int width, int height) throws IOException {
        String base64 = inputStreamToString(source.getStream());
        Bitmap bitmap = base64ToBitmap(base64);
        return new SimpleResource<>(bitmap);
    }

    @Override
    public String getId() {
        return "by.cooper.android.retailaccounting.util.glide";
    }

    private String inputStreamToString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder(stream.available());
        String line;
        while ((line = reader.readLine()) != null) {
            total.append(line);
        }
        reader.close();
        return total.toString();
    }

    private Bitmap base64ToBitmap(@NonNull final String b64) throws IOException {
        byte[] imageAsBytes = Base64.decode(b64, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
