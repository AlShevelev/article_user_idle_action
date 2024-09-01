import 'package:flutter/material.dart';
import 'package:user_idle/additional_to_main_entry_point_interactor.dart';
import 'package:user_idle/additional_to_main_entry_point_messages.dart';

@pragma('vm:entry-point')
void userIdleActionEntryPoint() {
  print('USER_ID_ACT: Dart: userIdleActionEntryPoint is executed');

  AdditionalToMainEntryPointInteractor.send(
    AdditionalToMainEntryPointMessage.userIdle,
  );
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'A user action on idle'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _actionText = '';

  @override
  void initState() {
    super.initState();

    AdditionalToMainEntryPointInteractor.receive(
        () => setState(() {
          print('USER_ID_ACT: Dart: The user\'s action is executed in the main entry points');
          _actionText = 'fired!';
        })
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'Waiting for action...',
            ),
            Text(
              _actionText,
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
    );
  }
}
