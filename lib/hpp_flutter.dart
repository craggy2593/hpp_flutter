import 'dart:async';

import 'package:flutter/services.dart';

class HppResult {
  final bool success;
  final String result;
  final int code;

  HppResult(this.success, this.result, this.code);
}

class HppFlutter {
  static const MethodChannel _channel = const MethodChannel('hpp_flutter');

  static Future<HppResult> showPaymentWindow({
    String hppRequestProducerURL,
    String hppResponseConsumerURL,
    String hppURL,
  }) async {
    final payload = {
      'HPPRequestProducerURL': hppRequestProducerURL,
      'HPPResponseConsumerURL': hppResponseConsumerURL,
      'HPPURL': hppURL,
    };

    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'showPaymentWindow',
      [payload],
    );
    var result = HppResult(result['success'], result['result'], result['code']);
    return result;
  }
}
