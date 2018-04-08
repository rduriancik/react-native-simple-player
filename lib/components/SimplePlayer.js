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
    this.checkLoudMusicDialog = this.checkLoudMusicDialog.bind(this);
    this.handleOnHeadsetPluggedEvent = this.handleOnHeadsetPluggedEvent.bind(this);
    this.handleOnNearEarEvent = this.handleOnNearEarEvent.bind(this);
    this.loadAudioFile = thils.loadAudioFile.bind(this);
  }

  componentDidMount() {
    this.loadAudioFile();
    
    DeviceEventEmitter.addListener(
      RNSimplePlayerEvents.ON_HEADSET_PLUGGED,
      this.handleOnHeadsetPluggedEvent); 
    DeviceEventEmitter.addListener(
      RNSimplePlayerEvents.ON_NEAR_EAR,
      this.handleOnNearEarEvent);
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeAllListeners(
      RNSimplePlayerEvents.ON_HEADSET_PLUGGED);
    DeviceEventEmitter.removeAllListeners(
      RNSimplePlayerEvents.ON_NEAR_EAR);

    NativeModules.RNSimplePlayerUtils.hideLoudMusicDialog();
  }

  componentDidUpdate(prevProps, prevState, snapshot){
    if(prevProps.filePath != this.props.filePath) {
     this.loadAudioFile();
    }
  }

  loadAudioFile() {
    RNSimplePlayer.loadAudioFile(this.props.filePath).then(result => 
      this.setState({
        hasFile: result,
      })
    );
  }

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

  handleOnProgressChanged(position) {
    console.log(position);
    // RNSimplePlayer.seekTo(position);
  }

  changeState() {
    if(!this.state.hasFile) {
      this.props.onFileNotFound();
    } else if(this.props.preventLoudMusic && !this.state.hasHeadset && !this.state.hasNearEar) {
      RNSimplePlayerUtils.showLoudMusicDialog();
    } else {
      this.play()
    }
  }

  async checkLoudMusicDialog(canPlay) {
    let isLoudMusicDialogShown = await RNSimplePlayerUtils.isLoudMusicDialogShown();
    if(canPlay && isLoudMusicDialogShown) {
      RNSimplePlayerUtils.hideLoudMusicDialog();
      this.play();
    }

    let isPlaying = await RNSimplePlayer.isPlaying();
    if(!canPlay && isPlaying) {
      RNSimplePlayer.pause();
      RNSimplePlayerUtils.showLoudMusicDialog();
    }
  }

  play() {
    RNSimplePlayer.play();
    this.setState({
      isPlaying: !this.state.isPlaying
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