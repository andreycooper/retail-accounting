package by.cooper.android.retailaccounting.view;


import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import by.cooper.android.retailaccounting.util.glide.Base64Decoder;

public class ViewBindingAdapter {

    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .asBitmap()
                    .decoder(new Base64Decoder())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }

    @BindingAdapter({"android:imageUrl", "android:placeHolder"})
    public static void loadImage(ImageView imageView, String imageUrl, Drawable placeHolder) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .asBitmap()
                .placeholder(placeHolder)
                .decoder(new Base64Decoder())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
