import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

// ignore: slash_for_doc_comments
/**
    Flutter定义了三种不同类型的Channel
    一、BasicMessageChannel：用于传递字符串和半结构化的信息，持续通信，收到消息后可以回复此消息。
    如：Native将遍历到的文件信息陆续传递到Dart，再比如：Flutter将服务器陆陆续续获取到的信息交给Native加工，
    Native处理完后返回等。
    二、MethodChannel：用于传递方法调用(method invocation)一次通信：如Flutter调用Native拍照。
    三、EventChannel：用于数据流（event stream）的通信，持续通信，收到消息后无法回复此次消息，通常用于Native向Dart的通信，
    如：手机电量变化，网络连接变化，陀螺仪，传感器等；

    这三种类型的Channel都是全双工通信，即A <=> B,Dart可以主动发送消息给platform端，并且platform接收到消息可以做出回应，
    同样platform端可以主动发消息给Dart端，dart端接收到数据后返回给platform端。
 */
class EventChannelPage extends StatefulWidget {
  final String initParam;

  const EventChannelPage({super.key, required this.initParam});

  @override
  State<EventChannelPage> createState() => _EventChannelPageState();
}

class _EventChannelPageState extends State<EventChannelPage> {
  EventChannel eventChannel = EventChannel("EventChannelPlugin");
  MethodChannel methodChannel = MethodChannel("MethodChannelPlugin");
  BasicMessageChannel basicMessageChannel = BasicMessageChannel(
    "BasicMessageChannelPlugin",
    StringCodec(),
  );

  String showMessage = "";
  StreamSubscription? streamSubscription;
  bool _isMethodChannelPlugin = false;

  @override
  void initState() {
    super.initState();
    //用于 Native → Dart 的持续数据流（事件推送)；和其他MessageChannel最大的区别是它没有回复，只能接受Native消息
    streamSubscription = eventChannel
        .receiveBroadcastStream("123")
        .listen(_onToDart, onError: onToDartError);

    //使用BasicMessageChannel接收Native消息，并向Native回复
    //setMessageHandler() → 被动接收消息，处理完后给对方一个回复。
    basicMessageChannel.setMessageHandler((message) {
      return Future(() {
        setState(() {
          showMessage = "basicMessageChannel:$message";
        });
        //给Native回复消息
        return "收到Native的消息: $message";
      });
    });
  }

  void _onToDart(message) {
    setState(() {
      showMessage = "EventChannel:$message";
    });
  }

  void onToDartError(error) {
    print(error);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: null,
      body: MediaQuery.removePadding(
        context: context,
        removeTop: true, // 去掉上方的间距
        child: Container(
          alignment: Alignment.center,
          margin: EdgeInsets.only(top: 70),
          child: Column(
            children: [
              Text(
                "Flutter与Native混合开发",
                style: TextStyle(fontSize: 20, color: Colors.red),
              ),
              SwitchListTile(
                value: _isMethodChannelPlugin,
                onChanged: (onChanged) {
                  setState(() {
                    _isMethodChannelPlugin = onChanged;
                  });
                },
                title: Text(
                  _isMethodChannelPlugin
                      ? "使用MethodChannelPlugin方式通信"
                      : "使用basicMessageChannel方式通信",
                ),
              ),
              TextField(
                style: const TextStyle(color: Colors.black), // 输入文字颜色
                onChanged: onTextChange, // 触发原生通信
                decoration: InputDecoration(
                  hintText: "请输入内容",
                  hintStyle: const TextStyle(color: Colors.grey),
                  filled: true,
                  fillColor: Colors.grey[200],
                  // 输入框背景
                  contentPadding: const EdgeInsets.symmetric(
                    vertical: 12,
                    horizontal: 16,
                  ),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12), // 圆角
                    borderSide: BorderSide.none, // 去掉边框线
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(
                      color: Colors.blue,
                      width: 2,
                    ), // 聚焦时蓝色边框
                  ),
                ),
              ),
              Text("收到初始化参数：${widget.initParam}"),
              Text("Native传来的数据：$showMessage"),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    streamSubscription?.cancel();
  }

  Future handler(message) async {}

  void onTextChange(String message) async {
    String response;
    try {
      if (_isMethodChannelPlugin) {
        //使用EventChannel向Native发送消息，并接收Native回复
        //用于 Dart → Native 的单次请求/响应，比如让 Native 执行某个动作（发短信、打开相机、返回系统信息）。
        response = await methodChannel.invokeMethod("send", message);
        setState(() {
          showMessage = response;
        });
      } else {
        //使用BasicMessageChannel向Native发送消息，并接收Native回复
        //send() → 主动发送消息，等待对方的回复。
        response = await basicMessageChannel.send(message);
        setState(() {
          showMessage = response;
        });
      }
    } on PlatformException catch (e) {
      print(e);
    }
  }
}
