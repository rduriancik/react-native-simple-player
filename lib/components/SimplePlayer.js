import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';
import { 
  NativeModules,
  DeviceEventEmitter,
  ToastAndroid
} from 'react-native';

const {RNSimplePlayerEvents, RNSimplePlayerUtils } = NativeModules;

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
      hasHeadset: false,
      hasNearEar: false,
    }

    this.changeState = this.changeState.bind(this);
    this.startPlaying = this.startPlaying.bind(this);
    this.checkLoudMusicDialog = this.checkLoudMusicDialog.bind(this);
    this.handleOnHeadsetPluggedEvent = this.handleOnHeadsetPluggedEvent.bind(this);
    this.handleOnNearEarEvent = this.handleOnNearEarEvent.bind(this);
  }

  componentWillMount() {
    DeviceEventEmitter.addListener(
                       RNSimplePlayerEvents.ON_HEADSET_PLUGGED,
                       this.handleOnHeadsetPluggedEvent); 
    DeviceEventEmitter.addListener(
                       RNSimplePlayerEvents.ON_NEAR_EAR,
                       this.handleOnNearEarEvent); 
  }

  // componentWillUnmount() {
  //   console.log("will unmount");
  //   DeviceEventEmitter.removeListener(
  //                       NativeModules.RNSimplePlayerEvents.ON_HEADSET_PLUGGED,
  //                       this.handleOnHeadsetPluggedEvent);
  //   DeviceEventEmitter.removeListener(
  //                       NativeModules.RNSimplePlayerEvents.ON_NEAR_EAR,
  //                       this.handleOnNearEarEvent);
  //   NativeModules.RNSimplePlayerUtils.hideLoudMusicDialog();
  // }

  handleOnHeadsetPluggedEvent(event) {
      this.setState({
        hasHeadset: event.state,
      });

    if(this.props.preventLoudMusic) {
      this.checkLoudMusicDialog(event.state);
    }
  }

  handleOnNearEarEvent(event) {
      this.setState({
        hasNearEar: event.state,
      });

    if(this.props.preventLoudMusic) {
      this.checkLoudMusicDialog(event.state);
    }
  }

  changeState() {
    if(this.props.preventLoudMusic && !this.state.hasHeadset && !this.state.hasNearEar) {
      RNSimplePlayerUtils.showLoudMusicDialog();
    } else {
      this.startPlaying()
    }
  }

  async checkLoudMusicDialog(canPlay) {
    let isLoudMusicDialogShown = await RNSimplePlayerUtils.isLoudMusicDialogShown();
    if(canPlay && isLoudMusicDialogShown) {
      RNSimplePlayerUtils.hideLoudMusicDialog();
      this.startPlaying();
    }

    // TODO handle unplugging
  }

  startPlaying() {
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

SimplePlayer.defaultProps = {
  preventLoudMusic: false,
}

SimplePlayer.propTypes = {
  style: PropTypes.any,
  preventLoudMusic: PropTypes.bool,
}

module.exports = SimplePlayer