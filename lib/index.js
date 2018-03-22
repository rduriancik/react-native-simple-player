
import { NativeModules } from 'react-native';

const { RNSimplePlayer } = NativeModules;

// export default RNSimplePlayer;
module.exports.SimplePlayer = require("./components/SimplePlayer");