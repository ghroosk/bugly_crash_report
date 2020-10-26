package com.ghroosk.bugly_crash_report;

import androidx.annotation.NonNull;
import com.tencent.bugly.crashreport.CrashReport;

import io.flutter.BuildConfig;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** BuglyCrashReportPlugin */
public class BuglyCrashReportPlugin implements MethodCallHandler {
  public final Registrar registrar;

//  @Override
//  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
//    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "bugly_crash_report");
//    channel.setMethodCallHandler(new BuglyCrashReportPlugin(flutterPluginBinding.getFlutterEngine().getDartExecutor()));
//  }

  private BuglyCrashReportPlugin(Registrar registrar) {
    this.registrar = registrar;
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "bugly_crash_report");
    channel.setMethodCallHandler(new BuglyCrashReportPlugin(registrar));
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("initCrashReport")) {
      String appID = call.argument("app_id");
      CrashReport.initCrashReport(registrar.activity().getApplicationContext(), appID, false);
      result.success(0);
    } else if(call.method.equals("postException")) {
      String message = call.argument("crash_message");
      String detail = call.argument("crash_detail");
      CrashReport.postException(4,"Flutter Exception",message,detail,null);
      result.success(0);
    }else {
      result.notImplemented();
    }
  }

//  @Override
//  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
//  }
}
