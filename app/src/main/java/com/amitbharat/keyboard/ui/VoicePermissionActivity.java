package com.amitbharat.keyboard.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.amitbharat.keyboard.ime.IndicKeyboardService;

/**
 * Lightweight transparent activity to safely request RECORD_AUDIO runtime permission
 * without crashing on any Android version (uses Activity instead of AppCompatActivity with Translucent theme).
 */
public class VoicePermissionActivity extends Activity {

    private static final int REQUEST_AUDIO_PERMISSION = 2026;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            notifyServiceStartVoice();
            finish();
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_AUDIO_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Microphone enabled for voice typing", Toast.LENGTH_SHORT).show();
            notifyServiceStartVoice();
        } else {
            Toast.makeText(this, "Microphone permission is required for voice typing", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void notifyServiceStartVoice() {
        IndicKeyboardService service = IndicKeyboardService.getActiveInstance();
        if (service != null) {
            service.startVoiceInputFromPermission();
        }
    }
}
