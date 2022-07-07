class SharablePackageModel {
  SharablePackageModel({
    required this.package,
    required this.appName,
    required this.appIcon,
  });

  String package;
  String appName;
  String appIcon;

  factory SharablePackageModel.fromMap(Map<String, dynamic> json) =>
      SharablePackageModel(
        package: json["package"],
        appName: json["appName"],
        appIcon: json["appIcon"],
      );

  Map<String, String> toMap() => {
        "package": package,
        "appName": appName,
        "appIcon": appIcon,
      };

  @override
  String toString() {
    return 'SharablePackageModel{package: $package, appName: $appName, appIcon: $appIcon}';
  }
}
