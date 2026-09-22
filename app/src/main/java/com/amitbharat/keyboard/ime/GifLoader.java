package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * High-performance native loader and player for animated GIFs.
 * Utilizes Android P+ ImageDecoder with AnimatedImageDrawable for 60fps hardware-accelerated
 * playback, with graceful fallback.
 */
public class GifLoader {

    private static final String TAG = "GifLoader";

    /**
     * Ensure the asset GIF is copied to the app's cache directory so FileProvider
     * can share it and ImageDecoder can read it.
     */
    public static File getOrExtractGifFile(Context context, String assetFileName) {
        File dir = new File(context.getCacheDir(), "gifs");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, assetFileName);
        if (!file.exists() || file.length() == 0) {
            try (InputStream in = context.getAssets().open("gifs/" + assetFileName);
                 OutputStream out = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error extracting gif asset: " + assetFileName, e);
                return null;
            }
        }
        return file;
    }

    /**
     * Load and animate a GIF file into an ImageView.
     */
    public static void loadGif(ImageView imageView, String assetFileName) {
        Context context = imageView.getContext();
        File file = getOrExtractGifFile(context, assetFileName);
        if (file == null || !file.exists()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(file);
                Drawable drawable = ImageDecoder.decodeDrawable(source);
                imageView.setImageDrawable(drawable);
                if (drawable instanceof AnimatedImageDrawable) {
                    ((AnimatedImageDrawable) drawable).setRepeatCount(AnimatedImageDrawable.REPEAT_INFINITE);
                    ((AnimatedImageDrawable) drawable).start();
                }
                return;
            } catch (Exception e) {
                Log.w(TAG, "ImageDecoder failed, falling back to Bitmap", e);
            }
        }

        // Fallback for older API levels
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e(TAG, "BitmapFactory fallback failed", e);
        }
    }
}
