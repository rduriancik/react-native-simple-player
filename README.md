
# react-native-simple-player

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
<!---
#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNSimplePlayer.sln` in `node_modules/react-native-simple-player/windows/RNSimplePlayer.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Simple.Player.RNSimplePlayer;` to the usings at the top of the file
  - Add `new RNSimplePlayerPackage()` to the `List<IReactPackage>` returned by the `Packages` method
--->

## Usage
```javascript
import RNSimplePlayer from 'react-native-simple-player';

// TODO: What to do with the module?
RNSimplePlayer;
```
  
