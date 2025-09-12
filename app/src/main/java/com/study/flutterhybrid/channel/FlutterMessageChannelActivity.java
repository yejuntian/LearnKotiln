package com.study.flutterhybrid.channel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.study.learnkotiln.R;

import io.flutter.embedding.android.FlutterFragment;

public class FlutterMessageChannelActivity extends AppCompatActivity implements IShowMessage {
    private IShowMessage iShowMessage;
    TextView textShow;
    EditText input;
    String title;
    boolean useEventChannel;
    private BasicMessageChannelPlugin basicMessageChannelPlugin;
    private EventChannelPlugin eventChannelPlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_channel);
        iShowMessage = this;

        FlutterFragment fragment = FlutterFragment.withNewEngine().initialRoute("eventChannel").build();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flutter_container, fragment).commitNow();
        initUI();
        initFlutterPlugin(fragment);
    }

    private void initFlutterPlugin(FlutterFragment fragment) {
        if (fragment.getFlutterEngine() == null) return;
        eventChannelPlugin = EventChannelPlugin.registerWith(fragment.getFlutterEngine());
        //注册Flutter plugin
        MethodChannelPlugin.registerWith(this, fragment.getFlutterEngine());
        basicMessageChannelPlugin = BasicMessageChannelPlugin.registerWith(this, fragment.getFlutterEngine());
    }

    private void initUI() {
        textShow = findViewById(R.id.textShow);
        input = findViewById(R.id.input);
        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mode_basic_message_channel) {
                    useEventChannel = false;
                } else if (checkedId == R.id.mode_event_channel) {
                    useEventChannel = true;
                }
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iShowMessage.sendMessage(charSequence.toString(), useEventChannel);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onShowMessage(String message) {
        textShow.setText("收到Dart消息:" + message + "\n isEventChannel:" + useEventChannel);
    }

    @Override
    public void sendMessage(String message, boolean useEventChannel) {
        if (useEventChannel) {
            eventChannelPlugin.send(message);
        } else {
            basicMessageChannelPlugin.send(message, this::onShowMessage);
        }
    }
}
