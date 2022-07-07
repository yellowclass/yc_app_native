class PluginResponse<T> {
  T? result;
  bool isSuccess;
  String msg;

  PluginResponse({
    required this.isSuccess,
    required this.result,
    required this.msg,
  });

  factory PluginResponse.success(T result) => PluginResponse(
        isSuccess: true,
        result: result,
        msg: "",
      );

  factory PluginResponse.error(String msg, {T? data}) => PluginResponse(
        isSuccess: false,
        result: data,
        msg: msg,
      );
}
