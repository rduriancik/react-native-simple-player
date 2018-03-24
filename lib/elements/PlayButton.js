import React from 'react';
import PropTypes from 'prop-types';

import {
  TouchableOpacity,
  View,
  Text
} from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';

export class PlayButton extends React.Component {
  constructor(props) {
    super(props);

    this.handlePress = this.handlePress.bind(this);
  }

  handlePress(e) {
    this.props.onPress();
  }
  
  render() {
    let icon = this.props.isPlaying ? "pause-circle-outline" : "play-circle-outline";

    return(
      <TouchableOpacity onPress={this.handlePress}>
        <Icon name={icon} size={this.props.style.iconSize} color={this.props.style.iconColor}/>
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
  style: PropTypes.shape({
    iconColor: PropTypes.string,
    iconSize: PropTypes.number
  }),
  onPress: PropTypes.func
}
