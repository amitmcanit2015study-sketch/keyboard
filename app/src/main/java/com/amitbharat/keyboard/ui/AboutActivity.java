package com.amitbharat.keyboard.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.databinding.ActivityAboutBinding;
import com.amitbharat.keyboard.utils.FileUtils;
import com.amitbharat.keyboard.utils.LocaleHelper;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;

    private static final String APP_ABOUT_TEXT = "Indic Keyboard - Google Indic-Style Soft Keyboard\n\n"
            + "Indic Keyboard is a fast, lightweight, and private soft keyboard with intelligent Hinglish-to-Hindi transliteration and English suggestion dictionary.\n\n"
            + "• Developed by: Amit Bharat\n"
            + "• Company: Rooys Soft Tech\n"
            + "• Contact: rooyssofttech2020@gmail.com\n"
            + "• Version: 1.0.0\n\n"
            + "Install the attached APK to get started!";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupLanguageToggle();
        setupActions();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupLanguageToggle() {
        boolean isHindi = LocaleHelper.isHindi(this);
        binding.toggleLanguageGroup.check(isHindi ? R.id.btnLangHindi : R.id.btnLangEnglish);

        binding.toggleLanguageGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                String targetLang = (checkedId == R.id.btnLangHindi) ? "hi" : "en";
                if (!targetLang.equals(LocaleHelper.getLanguage(this))) {
                    LocaleHelper.setLocale(this, targetLang);
                }
            }
        });
    }

    private void setupActions() {
        binding.btnShareApp.setOnClickListener(v -> shareAppApk());
        binding.btnDownloadApk.setOnClickListener(v -> downloadAppApk());
        binding.btnFeedback.setOnClickListener(v -> sendFeedbackEmail());
        binding.tvEmail.setOnClickListener(v -> sendFeedbackEmail());
    }

    private void downloadAppApk() {
        Toast.makeText(this, "Downloading APK to Downloads folder...", Toast.LENGTH_SHORT).show();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ApplicationInfo appInfo = getApplicationInfo();
                File originalApk = new File(appInfo.sourceDir);

                if (!originalApk.exists()) {
                    runOnUiThread(() -> Toast.makeText(this, "Could not find app APK file.", Toast.LENGTH_LONG).show());
                    return;
                }

                String fileName = "indic-keyboard-amit-bharat.apk";
                boolean success = false;
                Uri downloadedUri = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    android.content.ContentValues values = new android.content.ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    downloadedUri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (downloadedUri != null) {
                        try (InputStream in = new FileInputStream(originalApk);
                             OutputStream out = getContentResolver().openOutputStream(downloadedUri)) {
                            if (out != null) {
                                byte[] buffer = new byte[8192];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                out.flush();
                                success = true;
                            }
                        }
                    }
                } else {
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs();
                    }
                    File destApk = new File(downloadsDir, fileName);
                    FileUtils.copyFile(originalApk, destApk);
                    android.media.MediaScannerConnection.scanFile(
                            this,
                            new String[]{destApk.getAbsolutePath()},
                            new String[]{"application/vnd.android.package-archive"},
                            null
                    );
                    downloadedUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", destApk);
                    success = true;
                }

                final boolean isSaved = success;
                final Uri finalUri = downloadedUri;

                runOnUiThread(() -> {
                    if (isSaved) {
                        Snackbar.make(
                                binding.getRoot(),
                                "Saved to Downloads: " + fileName,
                                Snackbar.LENGTH_LONG
                        ).setAction("Open Downloads", v -> {
                            try {
                                Intent intent = new Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (Exception e) {
                                if (finalUri != null) {
                                    try {
                                        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                                        viewIntent.setDataAndType(finalUri, "application/vnd.android.package-archive");
                                        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(viewIntent);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }).show();
                    } else {
                        Toast.makeText(this, "Failed to save APK to Downloads.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error saving APK: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void shareAppApk() {
        Toast.makeText(this, "Preparing Indic Keyboard APK to share...", Toast.LENGTH_SHORT).show();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ApplicationInfo appInfo = getApplicationInfo();
                File originalApk = new File(appInfo.sourceDir);

                if (!originalApk.exists()) {
                    runOnUiThread(this::shareAppDescriptionFallback);
                    return;
                }

                File shareDir = new File(getCacheDir(), "shared_apk");
                if (!shareDir.exists()) {
                    shareDir.mkdirs();
                }
                File targetApk = new File(shareDir, "indic-keyboard-amit-bharat.apk");
                FileUtils.copyFile(originalApk, targetApk);

                Uri apkUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        targetApk
                );

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/vnd.android.package-archive");
                shareIntent.putExtra(Intent.EXTRA_STREAM, apkUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard APK - by Amit Bharat (Rooys Soft Tech)");
                shareIntent.putExtra(Intent.EXTRA_TEXT, APP_ABOUT_TEXT);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                runOnUiThread(() -> {
                    startActivity(Intent.createChooser(shareIntent, "Share Indic Keyboard APK & Details"));
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Sharing description...", Toast.LENGTH_SHORT).show();
                    shareAppDescriptionFallback();
                });
            }
        });
    }

    private void shareAppDescriptionFallback() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard - Hindi & English");
        intent.putExtra(Intent.EXTRA_TEXT, APP_ABOUT_TEXT);
        startActivity(Intent.createChooser(intent, "Share Indic Keyboard"));
    }

    private void sendFeedbackEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:rooyssofttech2020@gmail.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rooyssofttech2020@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard App - Feedback & Support");
        String body = "Hello Rooys Soft Tech Team,\n\n"
                + "Feedback / Feature Request / Bug Report:\n\n\n"
                + "------------------------------\n"
                + "Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n"
                + "Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n"
                + "App Version: 1.0.0\n";
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception ignored) {}
    }
}
