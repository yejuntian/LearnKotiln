package com.study.flutterhybrid.channel;

import android.app.Activity;
import android.widget.Toast;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;

/**
 * BasicMessageChannel
 * 用于传递字符串和半结构化的信息，持续通信，如：Native将遍历到的文件信息陆续传递到Dart
 * ，在比如：Flutter将从服务端陆陆续获取到信息交个Native加工，Native处理完返回等
 * Author: CrazyCodeBoy
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class BasicMessageChannelPlugin implements BasicMessageChannel.MessageHandler<String> {

    private final Activity activity;
    private final BasicMessageChannel<String> messageChannel;

    public static BasicMessageChannelPlugin registerWith(Activity activity, FlutterEngine flutterEngine) {
        return new BasicMessageChannelPlugin(activity, flutterEngine);
    }

    private BasicMessageChannelPlugin(Activity activity, FlutterEngine flutterEngine) {
        this.activity = activity;
        this.messageChannel = new BasicMessageChannel<>(
                flutterEngine.getDartExecutor().getBinaryMessenger(),
                "BasicMessageChannelPlugin",
                StringCodec.INSTANCE
        );
        //设置消息处理器，处理来自Dart的消息
        messageChannel.setMessageHandler(this);
    }

    @Override
    public void onMessage(String message, BasicMessageChannel.Reply<String> reply) {
        reply.reply("BasicMessageChannel收到：" + message);
        if (activity instanceof IShowMessage) {
            ((IShowMessage) activity).onShowMessage(message);
        }
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 向 Dart 发送消息
     */
    public void send(String message, BasicMessageChannel.Reply<String> callback) {
        messageChannel.send(message, callback);
    }
}
