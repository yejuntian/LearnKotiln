package com.study.learnkotiln

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 假业务网络入口：代码生成器会自动向 Application 注册 LifecycleCallback
        // 并在冷启动时（MainActivity初次 onResume 时）静默触发一闪而过的透明假 Activity 网络。
        // 无需手动点击按钮触发，彻底解耦。
    }
}