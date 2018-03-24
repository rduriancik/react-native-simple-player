import React from 'react';
import PropTypes from 'prop-types';

import {
  View,
  Dimensions,
  Slider,
  Text
} from 'react-native';
import { PlayButton } from '../elements/PlayButton';

export class SimplePlayerView extends React.Component {

  render() {
    const WIDTH = Dimensions.get('window').width;

    const containerStyle = {
      backgroundColor: this.props.style.backgroundColor, 
      width: WIDTH,
      padding: 8,
      flexDirection: 'row',
      alignItems:'center'
    }

    const playButtonStyle = {
      iconColor: this.props.style.iconColor,
      iconSize: this.props.style.iconSize
    }

    const sliderStyle = {
      flex: 1,
    }

    const textStyle = {
      color: this.props.style.textColor, 
      textAlign: 'center', 
      textAlignVertical: 'center'
    }

    return (
      <View style={containerStyle}>
        <PlayButton isPlaying={this.props.isPlaying} 
                    style={playButtonStyle}/>
        <Slider minimumTrackTintColor={this.props.style.sliderMinTrackColor}
                thumbTintColor={this.props.style.sliderThumbColor}
                maximumTrackTintColor={this.props.style.sliderMaxTrackColor}
                style={sliderStyle}/>
        <Text style={textStyle}>00:00</Text>
      </View>
    );
  }
}

SimplePlayerView.defaultProps = {
  style: {
    backgroundColor: '#FFF',
    iconColor: '#000',
    iconSize: 50,
    sliderThumbColor: '#000',
    sliderMinTrackColor: '#000',
    sliderMaxTrackColor: '#000',
    textColor: '#FFF'
  }
}

SimplePlayerView.propTypes = {
  isPlaying: PropTypes.bool.isRequired,
  style: PropTypes.shape({
    backgroundColor: PropTypes.string,
    iconColor: PropTypes.string,
    iconSize: PropTypes.number,
    sliderThumbColor: PropTypes.string,
    sliderMinTrackColor: PropTypes.string,
    sliderMaxTrackColor: PropTypes.string,
    textColor: PropTypes.string
  }),
}