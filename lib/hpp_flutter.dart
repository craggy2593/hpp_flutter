
import 'hpp_flutter_platform_interface.dart';

class HppFlutter {
  Future<String?> getPlatformVersion() {
    return HppFlutterPlatform.instance.getPlatformVersion();
  }
}
