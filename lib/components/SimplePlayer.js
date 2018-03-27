import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';
import { NativeModules } from 'react-native';
const { RNSimplePlayer } = NativeModules;

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
    }
    this.changeState = this.changeState.bind(this);
  }

  changeState() {
    RNSimplePlayer.showLoudMusicDialog();
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