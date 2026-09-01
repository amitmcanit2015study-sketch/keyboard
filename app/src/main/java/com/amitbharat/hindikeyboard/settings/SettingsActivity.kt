package com.amitbharat.hindikeyboard.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amitbharat.hindikeyboard.databinding.ActivitySettingsBinding
import com.amitbharat.hindikeyboard.utils.LocaleHelper

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.applyAppLanguage(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        // Pref Switches defaults
        binding.switchNumberRow.isChecked = true
        binding.switchVibration.isChecked = true
        binding.switchAutoCorrect.isChecked = true
        binding.switchEmojiSuggestions.isChecked = true
        binding.switchClipboard.isChecked = true
    }
}
