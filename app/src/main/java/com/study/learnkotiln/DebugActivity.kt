package com.study.learnkotiln

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import com.study.flutterhybrid.MainFlutterActivity
import com.study.flutterhybrid.SingleFlutterViewActivity
import com.study.jetpack.mvi.MviExampleActivity
import studykotlin.dongnao.CoroutineExceptionHandlerActivity
import studykotlin.dongnao.MainScopeActivity
import studykotlin.dongnao.MainScopeActivity2
import studykotlin.dongnao.flow.ArticleActivity
import studykotlin.dongnao.flow.DownLoadActivity
import studykotlin.dongnao.flow.NumberStateFlowActivity
import studykotlin.dongnao.flow.RoomUserActivity
import studykotlin.dongnao.flow.SharedFlowActivity


class DebugActivity : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        addPreferencesFromResource(R.xml.debug_activitys)

        setOnclickListener()
    }

    private fun setOnclickListener() {
        setupPreferenceClickListener("MainScopeActivity", MainScopeActivity::class.java)
        setupPreferenceClickListener("MainScopeActivity2", MainScopeActivity2::class.java)
        setupPreferenceClickListener(
            "CoroutineExceptionHandlerActivity",
            CoroutineExceptionHandlerActivity::class.java
        )
        setupPreferenceClickListener(
            "DownLoadActivity",
            DownLoadActivity::class.java
        )
        setupPreferenceClickListener(
            "RoomUserActivity",
            RoomUserActivity::class.java
        )
        setupPreferenceClickListener(
            "ArticleActivity",
            ArticleActivity::class.java
        )
        setupPreferenceClickListener(
            "NumberStateFlowActivity",
            NumberStateFlowActivity::class.java
        )
        setupPreferenceClickListener(
            "SharedFlowActivity",
            SharedFlowActivity::class.java
        )
        setupPreferenceClickListener(
            "MviExampleActivity",
            MviExampleActivity::class.java
        )
        setupPreferenceClickListener(
            "FlutterHybridActivity",
            MainFlutterActivity::class.java
        )
    }

    private fun <T : Activity> setupPreferenceClickListener(key: String, activityClass: Class<T>) {
        findPreference(key)?.setOnPreferenceClickListener {
            startActivity(Intent(this@DebugActivity, activityClass))
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}