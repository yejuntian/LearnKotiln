package com.study.flutterhybrid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.study.flutterhybrid.MultiFlutterViewActivity;
import com.study.flutterhybrid.SingleFlutterViewActivity;

public class MainFragment extends ListFragment {

    public static Fragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] array = new String[]{
                "单个FlutterView页面",//0
                "多个FlutterView页面"
        };

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final String item = arrayAdapter.getItem(position);
//        Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
        Intent gotoAct;
        switch (position) {
            case 0:
                gotoAct = new Intent(getActivity(), SingleFlutterViewActivity.class);
                startActivity(gotoAct);
                break;
            case 1:
                gotoAct = new Intent(getActivity(), MultiFlutterViewActivity.class);
                startActivity(gotoAct);
                break;
            default:
                break;
        }
    }
}
