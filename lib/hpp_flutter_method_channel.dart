import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'hpp_flutter_platform_interface.dart';

/// An implementation of [HppFlutterPlatform] that uses method channels.
class MethodChannelHppFlutter extends HppFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('hpp_flutter');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
