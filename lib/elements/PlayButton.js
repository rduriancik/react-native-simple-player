import React from 'react';
import PropTypes from 'prop-types';

import {
  TouchableOpacity,
  View,
  Text,
  Image
} from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';

const playIcon = require('../../icons/ic_play.png');
const pauseIcon = require('../../icons/ic_pause.png');

export class PlayButton extends React.Component {
  constructor(props) {
    super(props);

    this.handlePress = this.handlePress.bind(this);
  }

  handlePress(e) {
    this.props.onPress();
  }
  
  render() {
    let icon = this.props.isPlaying ? pauseIcon : playIcon;

    return(
      <TouchableOpacity onPress={this.handlePress}>
        <Image
          source={icon}
          style={{width: this.props.style.iconSize, height: this.props.style.iconSize, tintColor: this.props.style.iconColor}}
        />
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
