package com.study.flutterhybrid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.study.flutterhybrid.fragment.MainFragment;
import com.study.learnkotiln.R;

public class MainFlutterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutterhybrid);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_layout,
                        MainFragment.newInstance(),
                        MainFragment.class.getName())
                .commit();
    }
}
