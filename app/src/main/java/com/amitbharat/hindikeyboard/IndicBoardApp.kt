package com.amitbharat.hindikeyboard

import android.app.Application
import com.amitbharat.hindikeyboard.database.KeyboardDatabase
import com.amitbharat.hindikeyboard.utils.ThemeUtils

class IndicBoardApp : Application() {

    companion object {
        lateinit var instance: IndicBoardApp
            private set
        lateinit var database: KeyboardDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = KeyboardDatabase.getDatabase(this)
        ThemeUtils.applyTheme(this)
    }
}
