package com.study.learnkotiln

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity

class DebugActivity : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        addPreferencesFromResource(R.xml.debug_activitys)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}