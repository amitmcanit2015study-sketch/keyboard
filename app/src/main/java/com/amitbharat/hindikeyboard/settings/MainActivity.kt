package com.amitbharat.hindikeyboard.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.amitbharat.hindikeyboard.databinding.ActivityMainBinding
import com.amitbharat.hindikeyboard.utils.LocaleHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.applyAppLanguage(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActions()
    }

    override fun onResume() {
        super.onResume()
        updateKeyboardStatusUI()
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

        binding.btnOpenSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnOpenAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}
