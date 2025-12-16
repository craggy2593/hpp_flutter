import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'hpp_flutter_method_channel.dart';

abstract class HppFlutterPlatform extends PlatformInterface {
  /// Constructs a HppFlutterPlatform.
  HppFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static HppFlutterPlatform _instance = MethodChannelHppFlutter();

  /// The default instance of [HppFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelHppFlutter].
  static HppFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [HppFlutterPlatform] when
  /// they register themselves.
  static set instance(HppFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
