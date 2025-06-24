package com.study.flutterhybrid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.study.learnkotiln.R;

import io.flutter.embedding.android.FlutterFragment;

public class MultiFlutterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutli);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        FlutterFragment fragment1 = FlutterFragment.withNewEngine().initialRoute("route1").build();
        FlutterFragment fragment2 = FlutterFragment.withNewEngine().initialRoute("route2").build();

        transaction.add(R.id.frameLayout1, fragment1)
                .add(R.id.frameLayout2, fragment2);
        transaction.commit();
    }
}
