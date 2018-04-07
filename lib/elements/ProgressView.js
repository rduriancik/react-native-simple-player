import React from 'react';
import PropTypes from 'prop-types';

import {
  View,
  Slider,
  Text
} from 'react-native';

export class ProgressView extends React.Component {

  formatDuration(duration) {
    let seconds = Math.trunc((duration / 1000) % 60);
    let minutes = Math.trunc((duration / (1000 * 60)) % 60);

    return this.padWithZeros(minutes) + ":" + this.padWithZeros(seconds);
  }

  padWithZeros(num) {
    var numPad = "00" + num;
    return numPad.substr(numPad.length - 2);
  }

  render() {
    const containerStyle = {
      flex: 1,
      flexDirection: 'row',
      alignItems:'center'
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
        <Slider minimumTrackTintColor={this.props.style.sliderMinTrackColor}
                thumbTintColor={this.props.style.sliderThumbColor}
                maximumTrackTintColor={this.props.style.sliderMaxTrackColor}
                onSlidingComplete={this.props.onProgressChanged}
                onValueChange={this.props.onValueChange}
                value={this.props.progress}
                maximumValue={this.props.duration}
                style={sliderStyle}/>
        <Text style={textStyle}>{this.formatDuration(this.props.remainingDuration)}</Text>
      </View>
    );
  }
}

ProgressView.defaultProps = {
  style: {
    sliderThumbColor: '#000',
    sliderMinTrackColor: '#000',
    sliderMaxTrackColor: '#000',
    textColor: '#FFF'
  }
}

ProgressView.propTypes = {
  remainingDuration: PropTypes.number.isRequired,
  duration: PropTypes.number.isRequired,
  progress: PropTypes.number.isRequired,
  onProgressChanged: PropTypes.func.isRequired,
  onValueChange: PropTypes.func,
  style: PropTypes.shape({
    sliderThumbColor: PropTypes.string,
    sliderMinTrackColor: PropTypes.string,
    sliderMaxTrackColor: PropTypes.string,
    textColor: PropTypes.string
  })
}