package com.amitbharat.hindikeyboard.settings

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amitbharat.hindikeyboard.R
import com.amitbharat.hindikeyboard.databinding.ActivityAboutBinding
import com.amitbharat.hindikeyboard.utils.LocaleHelper
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.Executors

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    private val appAboutText = """
        Indic Keyboard - Modern Hindi & English Keyboard
        
        A fast, privacy-first bilingual Android keyboard with instant 1-tap English ↔ हिन्दी transliteration, smart auto-correction, emoji prediction, and clipboard history.
        
        • Developed by: Amit Bharat
        • Company: Rooys Soft Tech
        • Contact: rooyssofttech2020@gmail.com
        • Version: 1.0.0.1
    """.trimIndent()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.applyAppLanguage(this)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupLanguageToggle()
        setupActions()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupLanguageToggle() {
        val isHindi = LocaleHelper.isHindi(this)
        binding.toggleLanguageGroup.check(if (isHindi) R.id.btnLangHindi else R.id.btnLangEnglish)

        binding.toggleLanguageGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val targetLang = if (checkedId == R.id.btnLangHindi) "hi" else "en"
                if (targetLang != LocaleHelper.getLanguage(this)) {
                    LocaleHelper.setLocale(this, targetLang)
                }
            }
        }
    }

    private fun setupActions() {
        binding.btnShareApp.setOnClickListener { shareAppApk() }
        binding.btnDownloadApk.setOnClickListener { downloadAppApk() }
        binding.btnFeedback.setOnClickListener { sendFeedbackEmail() }
        binding.tvEmail.setOnClickListener { sendFeedbackEmail() }
    }

    private fun downloadAppApk() {
        Toast.makeText(this, "Downloading APK to Downloads folder...", Toast.LENGTH_SHORT).show()
        Executors.newSingleThreadExecutor().execute {
            try {
                val appInfo = applicationInfo
                val originalApk = File(appInfo.sourceDir)
                if (!originalApk.exists()) {
                    runOnUiThread { Toast.makeText(this, "Could not locate APK file.", Toast.LENGTH_LONG).show() }
                    return@execute
                }

                val fileName = "indic-keyboard-amit-bharat.apk"
                var success = false

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val values = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                        put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive")
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val downloadedUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    if (downloadedUri != null) {
                        contentResolver.openOutputStream(downloadedUri)?.use { out ->
                            FileInputStream(originalApk).use { input ->
                                input.copyTo(out)
                            }
                        }
                        success = true
                    }
                } else {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    downloadsDir.mkdirs()
                    val destApk = File(downloadsDir, fileName)
                    originalApk.copyTo(destApk, overwrite = true)
                    success = true
                }

                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Saved to Downloads: \$fileName", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to save APK.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(this, "Error saving APK: \${e.message}", Toast.LENGTH_LONG).show() }
            }
        }
    }

    private fun shareAppApk() {
        Toast.makeText(this, "Preparing APK to share...", Toast.LENGTH_SHORT).show()
        Executors.newSingleThreadExecutor().execute {
            try {
                val appInfo = applicationInfo
                val originalApk = File(appInfo.sourceDir)
                if (!originalApk.exists()) {
                    runOnUiThread { shareDescriptionFallback() }
                    return@execute
                }

                val shareDir = File(cacheDir, "shared_apk").apply { mkdirs() }
                val targetApk = File(shareDir, "indic-keyboard-amit-bharat.apk")
                originalApk.copyTo(targetApk, overwrite = true)

                val apkUri = FileProvider.getUriForFile(this, "\$packageName.fileprovider", targetApk)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.android.package-archive"
                    putExtra(Intent.EXTRA_STREAM, apkUri)
                    putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard APK - by Amit Bharat")
                    putExtra(Intent.EXTRA_TEXT, appAboutText)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                runOnUiThread {
                    startActivity(Intent.createChooser(shareIntent, "Share Indic Keyboard APK"))
                }
            } catch (e: Exception) {
                runOnUiThread { shareDescriptionFallback() }
            }
        }
    }

    private fun shareDescriptionFallback() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard")
            putExtra(Intent.EXTRA_TEXT, appAboutText)
        }
        startActivity(Intent.createChooser(intent, "Share Indic Keyboard"))
    }

    private fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:rooyssofttech2020@gmail.com")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("rooyssofttech2020@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Indic Keyboard - Feedback & Support")
            val body = """
                Hello Rooys Soft Tech Team,
                
                Feedback / Feature Request:
                
                ------------------------------
                Device: \${Build.MANUFACTURER} \${Build.MODEL}
                Android: \${Build.VERSION.RELEASE} (API \${Build.VERSION.SDK_INT})
                App: Indic Keyboard v1.0.0.1
            """.trimIndent()
            putExtra(Intent.EXTRA_TEXT, body)
        }
        try {
            startActivity(Intent.createChooser(intent, "Send Email"))
        } catch (ignored: Exception) {}
    }
}
