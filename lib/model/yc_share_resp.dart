import 'dart:convert';

class YCShareResponse {
  String appName;
  bool launch;

  YCShareResponse({required this.appName, required this.launch});

  factory YCShareResponse.fromMap(String str) {
    final Map<String, dynamic> d = jsonDecode(str);
    return YCShareResponse(
      appName: d["app"] ?? "",
      launch: d['launch'] ?? false,
    );
  }
}
