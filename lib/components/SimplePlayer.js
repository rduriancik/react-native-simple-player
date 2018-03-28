import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';
import { 
  NativeModules,
  DeviceEventEmitter,
  ToastAndroid
} from 'react-native';

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
    }
    this.changeState = this.changeState.bind(this);
  }

  componentWillMount() {
    DeviceEventEmitter.addListener(
                       NativeModules.RNSimplePlayerEvents.ON_HEADSET_PLUGGED,
                       function(e) {
                         ToastAndroid.show("Headset event: " + e.state, ToastAndroid.LONG);
                       }); 
    DeviceEventEmitter.addListener(
                       NativeModules.RNSimplePlayerEvents.ON_NEAR_EAR,
                       function(e) {
                         ToastAndroid.show("Near ear event: " + e.state, ToastAndroid.LONG);
                       }); 
  }

  componentWillUnmount() {
    // DeviceEventEmitter.removeListener()
  }

  changeState() {
    NativeModules.RNSimplePlayerUtils.showLoudMusicDialog();
    this.setState({
      isPlaying: !this.state.isPlaying
    })
  }

  render() {
    return <SimplePlayerView 
              isPlaying={this.state.isPlaying} 
              style={this.props.style} 
              onPress={this.changeState}
            />;
  }
}

SimplePlayer.propTypes = {
  style: PropTypes.any,
}

module.exports = SimplePlayer