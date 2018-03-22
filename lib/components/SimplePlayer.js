import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';

class SimplePlayer extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      isPlaying: false,
    }
  }

  render() {
    return <SimplePlayerView 
              isPlaying={this.state.isPlaying} 
              style={this.props.style} 
            />;
  }
}

SimplePlayer.propTypes = {
  style: PropTypes.any,
}

module.exports = SimplePlayer