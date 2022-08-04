import Flutter
import UIKit

public class SwiftYcNativePlugin: NSObject, FlutterPlugin {
    
//    var delegate : IDelegate
    
//     help: // -> https://github.com/flutter/flutter/issues/25078
    
    
    var pluginHandler = PluginHandler()
    var uiViewController: UIViewController
    
    
    init(pluginRegistrar: FlutterPluginRegistrar, uiViewController: UIViewController) {
        self.uiViewController = uiViewController

    }
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "yc_app_native", binaryMessenger: registrar.messenger())
      
    let viewController: UIViewController =   (UIApplication.shared.delegate?.window??.rootViewController)!;
      
    let instance = SwiftYcNativePlugin(pluginRegistrar: registrar, uiViewController: viewController)
    registrar.addMethodCallDelegate(instance, channel: channel)
      
    
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
//    result("iOS " + UIDevice.current.systemVersion)
      
      if(call.method == "shareMediaIntent"){
          
          self.pluginHandler.shareWhatsapp(methodCall:call,
                                           flutterResult: result,
                                           uiViewController: self.uiViewController)
          
      } else if (call.method == "setOrientation") {
          self.pluginHandler.setOrientation(
            methodCall:call,
            flutterResult: result,
            uiViewController: self.uiViewController
          )
      } else{
           result(FlutterMethodNotImplemented)
      }
  }
    
    
}
