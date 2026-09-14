package com.amitbharat.keyboard.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.databinding.ActivityMainBinding;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import com.amitbharat.keyboard.utils.LocaleHelper;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private KeyboardPreferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new KeyboardPreferences(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupSetupWizard();
        setupThemePicker();
        setupToggles();
        setupTestField();
    }

    private void setupTestField() {
        binding.etTestInput.setOnClickListener(v -> {
            binding.etTestInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.etTestInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        binding.etTestInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(binding.etTestInput, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateKeyboardStatus();
        binding.etTestInput.postDelayed(() -> {
            binding.etTestInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.etTestInput, InputMethodManager.SHOW_FORCED);
            }
        }, 300);
    }

    private void setupToolbar() {
        binding.btnAbout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
        binding.cardAboutBanner.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
    }

    private void setupSetupWizard() {
        binding.btnEnableKeyboard.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.btnSwitchKeyboard.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showInputMethodPicker();
            }
        });

        updateKeyboardStatus();
    }

    private void updateKeyboardStatus() {
        boolean isEnabled = isKeyboardEnabled();
        boolean isSelected = isKeyboardSelected();

        if (isEnabled) {
            binding.ivStep1Done.setVisibility(View.VISIBLE);
            binding.btnEnableKeyboard.setText(R.string.step1_done);
            binding.btnEnableKeyboard.setEnabled(false);
        } else {
            binding.ivStep1Done.setVisibility(View.GONE);
            binding.btnEnableKeyboard.setText(R.string.step1_btn);
            binding.btnEnableKeyboard.setEnabled(true);
        }

        if (isSelected) {
            binding.ivStep2Done.setVisibility(View.VISIBLE);
            binding.btnSwitchKeyboard.setText(R.string.step2_done);
            binding.chipActiveStatus.setText("Active");
            binding.chipActiveStatus.setTextColor(getColor(R.color.color_success));
        } else {
            binding.ivStep2Done.setVisibility(View.GONE);
            binding.btnSwitchKeyboard.setText(R.string.step2_btn);
            binding.chipActiveStatus.setText("Not Active");
            binding.chipActiveStatus.setTextColor(getColor(R.color.color_warning));
        }
    }

    private boolean isKeyboardEnabled() {
        String packageId = getPackageName();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return false;
        List<InputMethodInfo> enabledMethods = imm.getEnabledInputMethodList();
        for (InputMethodInfo imi : enabledMethods) {
            if (imi.getPackageName().equals(packageId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKeyboardSelected() {
        String defaultIme = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        return defaultIme != null && defaultIme.contains(getPackageName());
    }

    private void setupThemePicker() {
        String currentTheme = preferences.getTheme();
        int checkedId = R.id.btnThemeSystem;
        if (KeyboardPreferences.THEME_LIGHT.equals(currentTheme)) {
            checkedId = R.id.btnThemeLight;
        } else if (KeyboardPreferences.THEME_DARK.equals(currentTheme)) {
            checkedId = R.id.btnThemeDark;
        } else if (KeyboardPreferences.THEME_BLUE.equals(currentTheme)) {
            checkedId = R.id.btnThemeBlue;
        } else if (KeyboardPreferences.THEME_PURPLE.equals(currentTheme)) {
            checkedId = R.id.btnThemePurple;
        } else if (KeyboardPreferences.THEME_GREEN.equals(currentTheme)) {
            checkedId = R.id.btnThemeGreen;
        } else if (KeyboardPreferences.THEME_AMOLED.equals(currentTheme)) {
            checkedId = R.id.btnThemeAmoled;
        }

        binding.toggleThemeGroup.check(checkedId);

        binding.toggleThemeGroup.addOnButtonCheckedListener((group, checkedButtonId, isChecked) -> {
            if (isChecked) {
                String chosenTheme = KeyboardPreferences.THEME_SYSTEM;
                if (checkedButtonId == R.id.btnThemeLight) {
                    chosenTheme = KeyboardPreferences.THEME_LIGHT;
                } else if (checkedButtonId == R.id.btnThemeDark) {
                    chosenTheme = KeyboardPreferences.THEME_DARK;
                } else if (checkedButtonId == R.id.btnThemeBlue) {
                    chosenTheme = KeyboardPreferences.THEME_BLUE;
                } else if (checkedButtonId == R.id.btnThemePurple) {
                    chosenTheme = KeyboardPreferences.THEME_PURPLE;
                } else if (checkedButtonId == R.id.btnThemeGreen) {
                    chosenTheme = KeyboardPreferences.THEME_GREEN;
                } else if (checkedButtonId == R.id.btnThemeAmoled) {
                    chosenTheme = KeyboardPreferences.THEME_AMOLED;
                }
                preferences.setTheme(chosenTheme);
            }
        });
    }

    private void setupToggles() {
        binding.switchSound.setChecked(preferences.isSoundEnabled());
        binding.switchSound.setOnCheckedChangeListener((btn, isChecked) -> preferences.setSoundEnabled(isChecked));

        binding.switchVibrate.setChecked(preferences.isVibrateEnabled());
        binding.switchVibrate.setOnCheckedChangeListener((btn, isChecked) -> preferences.setVibrateEnabled(isChecked));

        binding.switchPopup.setChecked(preferences.isPopupEnabled());
        binding.switchPopup.setOnCheckedChangeListener((btn, isChecked) -> preferences.setPopupEnabled(isChecked));

        binding.switchAutoCap.setChecked(preferences.isAutoCapEnabled());
        binding.switchAutoCap.setOnCheckedChangeListener((btn, isChecked) -> preferences.setAutoCapEnabled(isChecked));
    }
}
