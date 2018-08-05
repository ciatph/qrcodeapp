# qrcodeapp

These are experiments on using Google's old [Mobile vision API](https://developers.google.com/vision/) - for scanner for reading QR codes back when it was still a stand-alone from the new ML Kit. 

The app contains (2) demos:

1. **Single Scan** opens a full screen camera, and then exits after detecting a QR code. 

2. **Continous Scan** which uses a library built on top of the Mobile Vision API by [nisrulz](https://github.com/nisrulz/qreader) can scan a series of QR codes.

The Mobile Vision API is now part of the **[ML Kit](https://firebase.google.com/docs/ml-kit/android/read-barcodes)**, and may have similar implementation to the approaches described here.

## Dependencies

The following settings and software versions have been used for this project.

1. **Android Studio** version 3.1.3
2. **Gradle Version** 4.4
3. **Android Plugin Version** 3.1.0
4. **Gradle dependencies:**
   - com.google.firebase:firebase-ml-vision:16.0.0
   - com.google.firebase:firebase-core:16.0.1
   - com.google.firebase:firebase-database:16.0.1
   - com.android.support:appcompat-v7:27.+
5. **SDK Tools**
   - Android SDK Build-Tools version 28.0.1
   - Android SDK Platform-Tools version 28.0.0
   - Android SDK Tools version 26.1.1
   - Android Emulator version 27.3.9
   - Google Play Services version 49
   - Google USB Driver version 11.0.0
   - Android Support Repository version 47.0.0

## Installation

1. Clone this repository into your PC.
2. Open using Android Studio (preferably AS version 3.1.3).
3. Wait for files to sync.
4. In AS, press the **Sync Project with Gradle Files** button.
5. Connect the app to your Google firebase account.
   - In Android Studio, click on `Tools > Firebase`
   - Select `Realtime Database > Save and retrieve data`
   - Click Connect to Firebase - Login using your google account
   - Create a Firebase project if it does not yet exist
5. Run and build on an emulator or device.

**Date Created:** 20180404<br>
**Date Modified:** 20180405