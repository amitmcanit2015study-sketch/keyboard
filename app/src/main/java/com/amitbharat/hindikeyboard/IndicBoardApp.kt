package com.amitbharat.hindikeyboard

import android.app.Application
import com.amitbharat.hindikeyboard.database.KeyboardDatabase

class IndicBoardApp : Application() {

    companion object {
        lateinit var instance: IndicBoardApp
            private set
        lateinit var database: KeyboardDatabase
            private set
    }

    @Override
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = KeyboardDatabase.getDatabase(this)
    }
}
