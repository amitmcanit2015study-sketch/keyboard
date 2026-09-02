package com.amitbharat.hindikeyboard.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amitbharat.hindikeyboard.R
import com.amitbharat.hindikeyboard.databinding.ActivitySettingsBinding
import com.amitbharat.hindikeyboard.theme.ThemeType
import com.amitbharat.hindikeyboard.utils.LocaleHelper
import com.amitbharat.hindikeyboard.utils.PreferencesManager
import com.amitbharat.hindikeyboard.utils.ThemeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: PreferencesManager

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferencesManager.getInstance(this)
        ThemeUtils.applyTheme(this)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupThemeViews()
        setupSwitches()
    }

    private fun setupThemeViews() {
        updateThemeSummaries()

        binding.cardAppTheme.setOnClickListener { showAppThemeDialog() }
        binding.cardKeyboardTheme.setOnClickListener { showKeyboardThemeDialog() }
    }

    private fun updateThemeSummaries() {
        val appThemeMode = prefs.getAppThemeMode()
        binding.tvCurrentAppTheme.text = when (appThemeMode) {
            1 -> "Light"
            2 -> "Dark"
            else -> "System Default"
        }

        val kbTheme = prefs.getKeyboardTheme()
        binding.tvCurrentKeyboardTheme.text = when (kbTheme) {
            ThemeType.SYSTEM_DEFAULT -> "System Default"
            ThemeType.MATERIAL_LIGHT -> "Material Light"
            ThemeType.MATERIAL_DARK -> "Material Dark"
            ThemeType.AMOLED_BLACK -> "AMOLED Black"
            ThemeType.ROYAL_INDIGO -> "Royal Indigo"
            ThemeType.EMERALD_GREEN -> "Emerald Green"
        }
    }

    private fun showAppThemeDialog() {
        val options = arrayOf("System Default", "Light", "Dark")
        val current = prefs.getAppThemeMode()

        MaterialAlertDialogBuilder(this)
            .setTitle("Select App Theme")
            .setSingleChoiceItems(options, current) { dialog, which ->
                prefs.setAppThemeMode(which)
                ThemeUtils.applyTheme(this)
                updateThemeSummaries()
                dialog.dismiss()
            }
            .show()
    }

    private fun showKeyboardThemeDialog() {
        val themes = arrayOf(
            ThemeType.SYSTEM_DEFAULT,
            ThemeType.MATERIAL_LIGHT,
            ThemeType.MATERIAL_DARK,
            ThemeType.AMOLED_BLACK,
            ThemeType.ROYAL_INDIGO,
            ThemeType.EMERALD_GREEN
        )
        val names = arrayOf(
            "System Default",
            "Material Light",
            "Material Dark",
            "AMOLED Black",
            "Royal Indigo",
            "Emerald Green"
        )
        val currentTheme = prefs.getKeyboardTheme()
        val currentIndex = themes.indexOf(currentTheme).coerceAtLeast(0)

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Keyboard Visual Theme")
            .setSingleChoiceItems(names, currentIndex) { dialog, which ->
                prefs.setKeyboardTheme(themes[which])
                updateThemeSummaries()
                dialog.dismiss()
            }
            .show()
    }

    private fun setupSwitches() {
        binding.switchNumberRow.isChecked = prefs.isNumberRowEnabled()
        binding.switchNumberRow.setOnCheckedChangeListener { _, isChecked ->
            prefs.setNumberRowEnabled(isChecked)
        }

        binding.switchVibration.isChecked = prefs.isVibrationEnabled()
        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            prefs.setVibrationEnabled(isChecked)
        }

        binding.switchSound.isChecked = prefs.isSoundEnabled()
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.setSoundEnabled(isChecked)
        }

        binding.switchAutoCorrect.isChecked = prefs.isAutoCorrectEnabled()
        binding.switchAutoCorrect.setOnCheckedChangeListener { _, isChecked ->
            prefs.setAutoCorrectEnabled(isChecked)
        }

        binding.switchEmojiSuggestions.isChecked = prefs.isEmojiSuggestionsEnabled()
        binding.switchEmojiSuggestions.setOnCheckedChangeListener { _, isChecked ->
            prefs.setEmojiSuggestionsEnabled(isChecked)
        }

        binding.switchClipboard.isChecked = prefs.isClipboardEnabled()
        binding.switchClipboard.setOnCheckedChangeListener { _, isChecked ->
            prefs.setClipboardEnabled(isChecked)
        }
    }
}
