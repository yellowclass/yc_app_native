import 'package:flutter/material.dart';

import 'package:image_picker/image_picker.dart';
import 'package:yc_app_native/model/share_mime_type.enum.dart';
import 'package:yc_app_native/model/shareable_package.model.dart';
import 'package:yc_app_native/yc_app_native.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(primaryColor: Colors.yellow),
      home: const Home(),
    );
  }
}

class Home extends StatefulWidget {
  const Home({
    Key? key,
  }) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  @override
  void initState() {
    List<SharablePackageModel> shareablePkgs =
        dummyData.map((e) => SharablePackageModel.fromMap(e)).toList();
    YcAppNative.initCustomShare(shareablePkgs);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('YCAppNative'),
      ),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextButton(
              onPressed: () async {
                final imagePicker = ImagePicker();
                final pickedFile = await imagePicker.pickImage(
                  source: ImageSource.gallery,
                );

                if (pickedFile != null) {
                  // YcNative.shareMediaIntent(
                  //     contentText: "Checkout this!! https://www.yellowclass.com",
                  //     imgLocalPath: pickedFile.path,
                  //
                  // );
                  YcAppNative.launchYCShare(
                    contentText: "Checkout this!! https://www.yellowclass.com",
                    imgLocalPath: pickedFile.path,
                    launchDefaultShare: false,
                    mimeType: YCMimeType.IMAGES,
                  );
                } else {
                  YcAppNative.shareMediaIntent(
                    contentText: "Checkout this!!",
                  );
                }
                // YcAppNative.launchYCShare(
                //       contentText: "Checkout this!!",
                //       mimeType: YCMimeType.IMAGES,
                //       launchDefaultShare: false);
                // }
              },
              child: const Text("Custom Image Share"),
            ),
            TextButton(
              onPressed: () {
                YcAppNative.launchYCShare(
                  contentText: "Checkout this!! https://www.yellowclass.com",
                  imgLocalPath: "",
                  launchDefaultShare: false,
                  mimeType: YCMimeType.TEXTS,
                );
              },
              child: const Text("Custom Share"),
            ),
          ],
        ),
      ),
    );
  }
}

//DUMMY-DATA
List<Map<String, dynamic>> dummyData = <Map<String, dynamic>>[
  {
    "package": "com.facebook.katana",
    "appName": "FACEBOOK",
    "appIcon":
        "https://7xhqfps15y-flywheel.netdna-ssl.com/wp-content/uploads/2021/01/f_logo_RGB-Hex-Blue_512.png",
    "status": "ACTIVE"
  },
  {
    "package": "com.instagram.android",
    "appName": "INSTAGRAM",
    "appIcon":
        "https://7xhqfps15y-flywheel.netdna-ssl.com/wp-content/uploads/2021/01/f_logo_RGB-Hex-Blue_512.png",
    "status": "ACTIVE"
  },
  {
    "package": "com.whatsapp",
    "appName": "WHATSAPP",
    "appIcon":
        "https://7xhqfps15y-flywheel.netdna-ssl.com/wp-content/uploads/2021/01/f_logo_RGB-Hex-Blue_512.png",
    "status": "ACTIVE"
  },
  {
    "package": "com.linkedin.android",
    "appName": "LINKEDIN",
    "appIcon":
        "https://7xhqfps15y-flywheel.netdna-ssl.com/wp-content/uploads/2021/01/f_logo_RGB-Hex-Blue_512.png",
    "status": "ACTIVE"
  },
  {
    "package": "com.twitter.android",
    "appName": "TWITTER",
    "appIcon":
        "https://7xhqfps15y-flywheel.netdna-ssl.com/wp-content/uploads/2021/01/f_logo_RGB-Hex-Blue_512.png",
    "status": "ACTIVE"
  }
];
