import React from 'react';
import PropTypes from 'prop-types';

import {
  TouchableOpacity,
  View,
  Text
} from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';

export class PlayButton extends React.Component {
  
  render() {
    let icon = this.props.isPlaying ? "pause-circle-outline" : "play-circle-outline";

    return(
      <TouchableOpacity>
        <Icon name={icon} size={this.props.iconSize} color={this.props.iconColor}/>
      </TouchableOpacity>
    );
  }
}

PlayButton.defaultProps = {
  iconColor: '#000000',
  iconSize: 50,
}

PlayButton.propTypes = {
  isPlaying: PropTypes.bool.isRequired,
  iconColor: PropTypes.string,
  iconSize: PropTypes.number
}