import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from '../elements/SimplePlayerView';
import { 
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

const { RNSimplePlayerEvents, RNSimplePlayerUtils, RNSimplePlayer } = NativeModules;

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
      hasHeadset: false,
      hasNearEar: false,
      hasFile: false
    }

    this.changeState = this.changeState.bind(this);
    this.play = this.play.bind(this);
    this.pause = this.pause.bind(this);
    this.checkLoudMusicDialog = this.checkLoudMusicDialog.bind(this);
    this.handleOnHeadsetPluggedEvent = this.handleOnHeadsetPluggedEvent.bind(this);
    this.handleOnNearEarEvent = this.handleOnNearEarEvent.bind(this);
    this.loadAudioFile = this.loadAudioFile.bind(this);
  }

  componentDidMount() {
    this.loadAudioFile();
    
    if(this.props.preventLoudMusic) {
      RNSimplePlayerEvents.startEvents();
      console.log("events started");
      DeviceEventEmitter.addListener(
        RNSimplePlayerEvents.ON_HEADSET_PLUGGED,
        this.handleOnHeadsetPluggedEvent); 
      DeviceEventEmitter.addListener(
        RNSimplePlayerEvents.ON_NEAR_EAR,
        this.handleOnNearEarEvent);
    }
  }

  componentWillUnmount() {
    if(this.props.preventLoudMusic) {
      DeviceEventEmitter.removeAllListeners(
        RNSimplePlayerEvents.ON_HEADSET_PLUGGED);
      DeviceEventEmitter.removeAllListeners(
        RNSimplePlayerEvents.ON_NEAR_EAR);

        RNSimplePlayerEvents.stopEvents();
        RNSimplePlayerUtils.turnScreenOn();
    }

    RNSimplePlayerUtils.hideLoudMusicDialog();

    if(this.state.hasFile) {
      RNSimplePlayer.release();
    }
  }

  componentDidUpdate(prevProps, prevState, snapshot){
    if(prevProps.filePath != this.props.filePath) {
     this.loadAudioFile();
    }
  }

  loadAudioFile() {
    RNSimplePlayer.loadAudioFile(this.props.filePath, this.props.preventLoudMusic).then(result => 
      this.setState({
        hasFile: result,
      })
    );
  }

  handleOnHeadsetPluggedEvent(event) {
    console.log("headset event: " + event.state);
    this.setState({
      hasHeadset: event.state,
    });

    this.checkLoudMusicDialog();
  }

  handleOnNearEarEvent(event) {
    console.log("near ear event: " + event.state);
    let isNearEar = event.state;
    this.setState({
      hasNearEar: isNearEar,
    });

    if (this.state.isPlaying && !isNearEar) {
      RNSimplePlayerUtils.turnScreenOn();
    }

    this.checkLoudMusicDialog();
  }

  handleOnProgressChanged(position) {
    RNSimplePlayer.seekTo(position);
  }

  changeState() {
    console.log("Change state click");

    if(!this.state.hasFile) {
      this.props.onFileNotFound();
    } else if(this.props.preventLoudMusic && !this.state.hasHeadset && !this.state.hasNearEar) {
      RNSimplePlayerUtils.showLoudMusicDialog();
    } else if (!this.state.isPlaying){
      this.play();
    } else {
      this.pause();
    }
  }

  async checkLoudMusicDialog() {
    let canPlay = this.state.hasHeadset || this.state.hasNearEar;
    let isLoudMusicDialogShown = await RNSimplePlayerUtils.isLoudMusicDialogShown();
    if(canPlay && isLoudMusicDialogShown) {
      RNSimplePlayerUtils.hideLoudMusicDialog();
      this.play();
    }

    let isPlaying = await RNSimplePlayer.isPlaying();
    if(!canPlay && isPlaying) {
      this.pause();
      RNSimplePlayerUtils.showLoudMusicDialog();
    }
  }

  play() {
    if(this.props.preventLoudMusic && this.state.hasNearEar) {
      RNSimplePlayerUtils.turnScreenOff();
    }
    RNSimplePlayer.play();
    this.setState({
      isPlaying: true
    })
  }

  pause() {
    RNSimplePlayer.pause();
    this.setState({
      isPlaying: false
    })
  }

  render() {
    return <SimplePlayerView 
              isPlaying={this.state.isPlaying} 
              style={this.props.style} 
              onPress={this.changeState}
              onProgressChanged={this.handleOnProgressChanged}
            />;
  }
}

SimplePlayer.defaultProps = {
  preventLoudMusic: false,
}

SimplePlayer.propTypes = {
  style: PropTypes.any,
  preventLoudMusic: PropTypes.bool,
  filePath: PropTypes.string.isRequired,
  onFileNotFound: PropTypes.func.isRequired
}

module.exports = SimplePlayer