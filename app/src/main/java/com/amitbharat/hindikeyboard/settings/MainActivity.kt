package com.amitbharat.hindikeyboard.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.amitbharat.hindikeyboard.R
import com.amitbharat.hindikeyboard.databinding.ActivityMainBinding
import com.amitbharat.hindikeyboard.theme.ThemeType
import com.amitbharat.hindikeyboard.utils.LocaleHelper
import com.amitbharat.hindikeyboard.utils.PreferencesManager
import com.amitbharat.hindikeyboard.utils.ThemeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: PreferencesManager

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferencesManager.getInstance(this)
        ThemeUtils.applyTheme(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        updateKeyboardStatusUI()
    }

    private fun setupToolbar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.action_theme -> {
                    showThemeDialog()
                    true
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.action_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateKeyboardStatusUI() {
        val isEnabled = isKeyboardEnabled()
        val isSelected = isKeyboardSelected()

        if (!isEnabled) {
            binding.cardStep1.visibility = View.VISIBLE
            binding.cardStep2.visibility = View.GONE
            binding.cardActiveSuccess.visibility = View.GONE
        } else if (!isSelected) {
            binding.cardStep1.visibility = View.GONE
            binding.cardStep2.visibility = View.VISIBLE
            binding.cardActiveSuccess.visibility = View.GONE
        } else {
            binding.cardStep1.visibility = View.GONE
            binding.cardStep2.visibility = View.GONE
            binding.cardActiveSuccess.visibility = View.VISIBLE
        }
    }

    private fun isKeyboardEnabled(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledMethods = imm.enabledInputMethodList
        return enabledMethods.any { it.packageName == packageName }
    }

    private fun isKeyboardSelected(): Boolean {
        val currentIme = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        return currentIme != null && currentIme.contains(packageName)
    }

    private fun setupActions() {
        binding.btnEnableIme.setOnClickListener {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        }

        binding.btnSelectIme.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        binding.btnSwitchKeyboard.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        binding.btnQuickTheme.setOnClickListener {
            showThemeDialog()
        }

        binding.btnOpenSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnOpenAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun showThemeDialog() {
        val options = arrayOf("System Default (Recommended)", "Light", "Dark", "Keyboard Visual Themes…")
        val current = prefs.getAppThemeMode()

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.pref_theme_title))
            .setSingleChoiceItems(options, current.coerceIn(0, 2)) { dialog, which ->
                if (which < 3) {
                    prefs.setAppThemeMode(which)
                    ThemeUtils.applyTheme(this)
                    dialog.dismiss()
                } else {
                    dialog.dismiss()
                    showKeyboardVisualThemeDialog()
                }
            }
            .show()
    }

    private fun showKeyboardVisualThemeDialog() {
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
            .setTitle("Keyboard Visual Theme")
            .setSingleChoiceItems(names, currentIndex) { dialog, which ->
                prefs.setKeyboardTheme(themes[which])
                dialog.dismiss()
            }
            .show()
    }
}
