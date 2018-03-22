import React from 'react';
import PropTypes from 'prop-types';

import {
  View,
  Dimensions,
} from 'react-native';
import { PlayButton } from '../elements/PlayButton';

export class SimplePlayerView extends React.Component {

  render() {
    const WIDTH = Dimensions.get('window').width;

    return (
      <View 
        style={{backgroundColor: this.props.style.backgroundColor, 
                width: WIDTH}}>
        <PlayButton isPlaying={this.props.isPlaying}/>
      </View>
    );
  }
}

SimplePlayerView.defaultProps = {
  style: {
    backgroundColor: '#FFF',
  }
}

SimplePlayerView.propTypes = {
  isPlaying: PropTypes.bool.isRequired,
  style: PropTypes.any,
}