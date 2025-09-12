package com.study.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TextScreen() }
    }

    @Preview
    @Composable
    fun TextScreen() {
        ShowTextView("hello", "world")
    }

    @Composable
    fun ShowTextView(title: String, subTitle: String) {
        Text(text = stringResource(id = R.string.app_name))
    }


}