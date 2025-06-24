import 'package:flutter/material.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  // 获取 Android 原生传入的 initialRoute（默认是 '/'）
  final route = WidgetsBinding.instance.platformDispatcher.defaultRouteName;
  print("🟡 Flutter started with route: $route");

  runApp(MyApp(initialRoute: route));
}

class MyApp extends StatelessWidget {
  final String initialRoute;

  const MyApp({super.key, required this.initialRoute});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Counter Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: _getHomeByRoute(initialRoute),
    );
  }

  Widget _getHomeByRoute(String route) {
    switch (route) {
      case 'my_engine_id':
        return const CounterPage();
      case 'route1':
        return const CounterPage();
      case 'route2':
        return const MyApp2();
      default:
        return const CounterPage(); // 默认就是计数页面
    }
  }
}

class MyApp2 extends StatelessWidget {
  const MyApp2({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter MyApp2',
      home: Center(
        child: Text(
          "第二个widget页面",
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: 20,
            decoration: TextDecoration.none,
            color: Colors.yellow,
          ),
        ),
      ),
    );
  }
}

class CounterPage extends StatefulWidget {
  const CounterPage({super.key});

  @override
  State<CounterPage> createState() => _CounterPageState();
}

class _CounterPageState extends State<CounterPage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
      print("🔁 setState called, _counter = $_counter");
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Counter Page')),
      body: Center(
        child: Text('$_counter', style: const TextStyle(fontSize: 48)),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
