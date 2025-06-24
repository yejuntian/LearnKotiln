package com.study.flutterhybrid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.study.learnkotiln.R;

import io.flutter.embedding.android.FlutterFragment;

public class SingleFlutterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutterhybrid);
        FlutterFragment flutterFragment = FlutterFragment
                .withNewEngine().initialRoute("my_engine_id").build();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, flutterFragment)
                .commit();

    }
}
