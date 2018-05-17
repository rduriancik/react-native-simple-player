
[![npm](https://img.shields.io/npm/v/react-native-simple-player.svg)](https://www.npmjs.com/package/react-native-simple-player)

# react-native-simple-player

A simple audio player for React Native applications. Currently supports only Android. 

---

## Features

- Load sounds from local directories
- Play/Pause/Seek
- Prevention of loud music
- Fully Customizable

## Getting started

`$ npm install react-native-simple-player --save`

### Mostly automatic installation

`$ react-native link react-native-simple-player`

### Manual installation

<!---
#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-simple-player` and add `RNSimplePlayer.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSimplePlayer.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<
--->
#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNSimplePlayerPackage;` to the imports at the top of the file
  - Add `new RNSimplePlayerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-simple-player'
  	project(':react-native-simple-player').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-simple-player/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-simple-player')
  	```

**Grant Access Permission for**

**Android 5.0**
If you're going to access external storage (say, SD card storage) for `Android 5.0` (or lower) devices, you might have to add the following line to `AndroidManifest.xml`.

```diff
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.rnfetchblobtest"
    android:versionCode="1"
    android:versionName="1.0">      

+   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />   
    ...

```

**Android 6.0+**

Beginning in Android 6.0 (API level 23), users grant permissions to apps while the app is running, not when they install the app. So adding permissions in `AndroidManifest.xml` won't work for Android 6.0+ devices. To grant permissions in runtime, you might use [PermissionAndroid API](https://facebook.github.io/react-native/docs/permissionsandroid.html).

<!---
#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNSimplePlayer.sln` in `node_modules/react-native-simple-player/windows/RNSimplePlayer.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Simple.Player.RNSimplePlayer;` to the usings at the top of the file
  - Add `new RNSimplePlayerPackage()` to the `List<IReactPackage>` returned by the `Packages` method
--->

#### iOS & Windows are not supported yet

## Usage

The library exposes a **SimplePlayer** component which is responsible for playback. Now, it only supports loading of sounds from local directories.

```javascript
import { SimplePlayer } from 'react-native-simple-player';
```

### `<SimplePlayer />`

#### Props
- `filePath` - the full path to an audio file
- `onFileNotFound` - a callback function when the file is not found
- `style` - (optional) a style object for the player
- `preventLoudMusic` - (optional) whether to prevent playing of loud music or not (true/false)
- `preventLoudMusicAlert` - (optional) an object representing title, message and button texts in the loud music alert

#### Style
- `backgroundColor` - color of the player's background (default #FFF)
- `textColor` - color of the minutes (default #000)
- `iconColor` - color of the play icon (default #000)
- `iconSize` - size of the play icon (default #000)
- `sliderMinTrackColor` - the color used for the track to the left of the button (default #000)
- `sliderThumbColor` - color of the foreground switch grip (default #000)
- `sliderMaxTrackColor` - the color used for the track to the right of the button (default #000)

<!---![simple_player](https://user-images.githubusercontent.com/13890361/40172196-f01e973e-59cd-11e8-8d4b-32692b253c06.png)--->

<img src="https://user-images.githubusercontent.com/13890361/40172196-f01e973e-59cd-11e8-8d4b-32692b253c06.png" width="720" alt="simple_player">

##### 'preventLoudMusicAlert' 
- `title` - text of the title
- `message` - text of the message
- `buttonText` - text of the button

<!---![loud_dialog](https://user-images.githubusercontent.com/13890361/40172230-066b4ad2-59ce-11e8-9f73-95c3d5334662.png)--->

<img src="https://user-images.githubusercontent.com/13890361/40172230-066b4ad2-59ce-11e8-9f73-95c3d5334662.png" width="360" alt="loud_dialog">
