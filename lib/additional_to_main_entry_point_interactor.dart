import 'dart:isolate';
import 'dart:ui';

import 'package:user_idle/additional_to_main_entry_point_messages.dart';

class AdditionalToMainEntryPointInteractor {
  static const _portName = 'mainPort';

  static void receive(Function actionToExecute) {
    final receiver = ReceivePort()
    // ignore: avoid_annotating_with_dynamic
      ..listen((dynamic message) async {
        if (message is String) {
          switch (message) {
            case AdditionalToMainEntryPointMessage.userIdle:
              {
                actionToExecute();
              }
          }
        }
      });

    IsolateNameServer.registerPortWithName(receiver.sendPort, _portName);
  }

  static void send(String message) =>
      IsolateNameServer.lookupPortByName(_portName)?.send(message);
}
