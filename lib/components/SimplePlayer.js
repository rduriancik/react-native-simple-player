import React from 'react';
import PropTypes from 'prop-types';
import { SimplePlayerView } from './SimplePlayerView';

class SimplePlayer extends React.Component {

  render() {
    return <SimplePlayerView isPlaying={this.props.isPlaying} />;
  }
}

SimplePlayer.propTypes = {
  isPlaying: PropTypes.bool.isRequired,
}

module.exports = SimplePlayer