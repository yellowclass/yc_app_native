//
//  PluginHandler.swift
//  yc_native
//
//  Created by Shivam Kumar on 12/7/21.
//

import Foundation

class PluginHandler {
    
//    var call: FlutterMethodCall

//    var result : FlutterResult

//    init() {
//
//    }
    
     func shareWhatsapp(methodCall: FlutterMethodCall,
                        flutterResult : @escaping FlutterResult,
                        uiViewController: UIViewController) {
         
         if let args = methodCall.arguments as? [String:Any]{
                let mTitleString = args["mContentText"] as! String
//                let mURLString = args["mURL"] as! String
                let mLocalImgPathString = args["mImagePath"] as! String


             var shareAll = [Any]()

             if(!mTitleString.isEmpty){
                 shareAll.append(mTitleString)
             }



             if FileManager.default.fileExists(atPath: mLocalImgPathString) {

                 //let imageUrl = URL(string: mLocalImgPathString)!

                 //let imageData = try! Data(contentsOf: imageUrl)

                 //let localFile = NSURL(fileURLWithPath: mLocalImgPathString)

                 let _uiImage = UIImage(contentsOfFile: mLocalImgPathString)
                 shareAll.append(_uiImage!)
             }

//             if(!mURLString.isEmpty){
//                 let myWebsite = NSURL(string: mURLString)
//                 shareAll.append(myWebsite)
//             }
//


             let activityViewController = UIActivityViewController(activityItems: shareAll,
                                                                   applicationActivities: nil)

             activityViewController.popoverPresentationController?.sourceView = uiViewController.view
             uiViewController.present(activityViewController, animated: true, completion: nil)

             self.successResult(flutterResult: flutterResult, data: "launching")

            } else {
                NSLog("Can't use comgooglemaps:// on this device.")
            }
        
         
         
    }
    
    func setOrientation(
        methodCall: FlutterMethodCall,
        flutterResult : @escaping FlutterResult,
        uiViewController: UIViewController
    ) {
        if let args = methodCall.arguments as? [String:Any] {
            let isPortrait = args["isPortrait"] as? Bool
            var value = UIInterfaceOrientation.portrait.rawValue
            if isPortrait == true {
                value = UIInterfaceOrientation.portrait.rawValue
            } else {
                value = UIInterfaceOrientation.landscapeLeft.rawValue
            }
            UIDevice.current.setValue(value, forKey: "orientation")
            self.successResult(flutterResult: flutterResult, data: "Success")
        }
    }
    
    private func successResult(flutterResult : @escaping FlutterResult,data:String){
        
        let _dictResult : [String:String] = ["status":"0", "data":data]
        flutterResult(_dictResult);
        
    }
    
    private func failureResult(flutterResult : @escaping FlutterResult,data:String){
        
        let _dictResult : [String:String] = ["status":"0", "data":data]
        flutterResult(_dictResult);
        
    }
    
}
