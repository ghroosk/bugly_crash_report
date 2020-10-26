import 'dart:async';
import 'dart:isolate';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class BuglyCrashReport {
  static const MethodChannel _channel =
      const MethodChannel('bugly_crash_report');

  static void initCrashReport(appID) {
    _channel.invokeMethod("initCrashReport", {'app_id': appID});
  }

  ///异常上报
  static void postException<T>(
    T callback(), {
    FlutterExceptionHandler handler, //异常捕捉，用于自定义打印异常
    String filterRegExp, //异常上报过滤正则，针对message
    bool isDebug = false,
  }) {
    // This captures errors reported by the Flutter framework.
    FlutterError.onError = (details) {
      Zone.current.handleUncaughtError(details.exception, details.stack);
    };
    Isolate.current.addErrorListener(new RawReceivePort((dynamic pair) {
      var isolateError = pair as List<dynamic>;
      var _error = isolateError.first;
      var _stackTrace = isolateError.last;
      Zone.current.handleUncaughtError(_error, _stackTrace);
    }).sendPort);
    // This captures errors reported by the App.
    runZonedGuarded<Future<Null>>(() async {
      callback();
    }, (error, stackTrace) {
      _reportError(isDebug, error, stackTrace);
    });
  }

//  static void postException(error, stack) {
//    _channel.invokeMethod("postException",{'crash_message':error.toString(),'crash_detail':stack.toString()});
//  }

  /// Reports [error] along with its [stackTrace] to Sentry.io.
  static Future<Null> _reportError(
      bool isDebug, dynamic error, dynamic stack) async {
    print('Caught error: $error');
    print('Caught stack: $stack');
    if (isDebug) {
      print('is debug, not report to Bugly.');
      return;
    }
    print('Reporting to Bugly...');
    _channel.invokeMethod("postException",
        {'crash_message': error.toString(), 'crash_detail': stack.toString()});
  }
}
