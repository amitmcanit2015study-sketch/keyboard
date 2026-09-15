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
import com.amitbharat.keyboard.engine.LanguageItem;
import com.amitbharat.keyboard.utils.LocaleHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
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
        setupLanguageSelection();
        setupToggles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateKeyboardStatus();
    }

    private void setupToolbar() {
        binding.btnAbout.setOnClickListener(v -> {
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

    private void setupLanguageSelection() {
        List<LanguageItem> allLangs = LanguageItem.getAllLanguages();
        List<String> selected = new ArrayList<>(preferences.getSelectedLanguages());

        updateLanguageCountBadge(selected.size());
        binding.chipGroupLanguages.removeAllViews();

        for (LanguageItem lang : allLangs) {
            Chip chip = new Chip(this);
            chip.setText(lang.getDisplayName());
            chip.setCheckable(true);
            chip.setClickable(true);
            boolean isChecked = selected.contains(lang.getCode());
            chip.setChecked(isChecked);

            chip.setOnCheckedChangeListener((buttonView, checked) -> {
                List<String> currentSelected = new ArrayList<>(preferences.getSelectedLanguages());
                if (checked) {
                    if (currentSelected.size() >= 2) {
                        chip.setChecked(false);
                        Snackbar.make(
                                binding.getRoot(),
                                "Maximum 2 languages can be active at a time",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    if (!currentSelected.contains(lang.getCode())) {
                        currentSelected.add(lang.getCode());
                        preferences.setSelectedLanguages(currentSelected);
                        updateLanguageCountBadge(currentSelected.size());
                    }
                } else {
                    if (currentSelected.size() <= 1) {
                        chip.setChecked(true);
                        Snackbar.make(
                                binding.getRoot(),
                                "At least 1 language must remain active",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    currentSelected.remove(lang.getCode());
                    preferences.setSelectedLanguages(currentSelected);
                    updateLanguageCountBadge(currentSelected.size());
                }
            });

            binding.chipGroupLanguages.addView(chip);
        }
    }

    private void updateLanguageCountBadge(int count) {
        if (binding.tvLanguageCountBadge != null) {
            binding.tvLanguageCountBadge.setText(count + "/2 selected");
        }
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
