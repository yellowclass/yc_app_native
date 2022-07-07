#import "YcNativePlugin.h"
#if __has_include(<yc_native/yc_native-Swift.h>)
#import <yc_native/yc_native-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "yc_native-Swift.h"
#endif

@implementation YcNativePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftYcNativePlugin registerWithRegistrar:registrar];
}
@end
