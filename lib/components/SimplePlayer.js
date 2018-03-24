import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
    }
    this.changeState = this.changeState.bind(this);
  }

  changeState() {
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