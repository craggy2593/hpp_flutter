import 'package:flutter_test/flutter_test.dart';
import 'package:hpp_flutter/hpp_flutter.dart';
import 'package:hpp_flutter/hpp_flutter_platform_interface.dart';
import 'package:hpp_flutter/hpp_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockHppFlutterPlatform
    with MockPlatformInterfaceMixin
    implements HppFlutterPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final HppFlutterPlatform initialPlatform = HppFlutterPlatform.instance;

  test('$MethodChannelHppFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelHppFlutter>());
  });

  test('getPlatformVersion', () async {
    HppFlutter hppFlutterPlugin = HppFlutter();
    MockHppFlutterPlatform fakePlatform = MockHppFlutterPlatform();
    HppFlutterPlatform.instance = fakePlatform;

    expect(await hppFlutterPlugin.getPlatformVersion(), '42');
  });
}
