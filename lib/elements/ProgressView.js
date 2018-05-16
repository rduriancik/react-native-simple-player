import React from 'react';
import PropTypes from 'prop-types';

import {
  View,
  Slider,
  Text,
  StyleSheet
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
    return (
      <View style={styles.container}>
        <Slider minimumTrackTintColor={this.props.style.sliderMinTrackColor}
                thumbTintColor={this.props.style.sliderThumbColor}
                maximumTrackTintColor={this.props.style.sliderMaxTrackColor}
                onValueChange={this.props.onValueChange}
                value={this.props.progress}
                maximumValue={this.props.duration}
                style={styles.slider}/>
        <Text style={[styles.text, {color: this.props.style.textColor}]}>{this.formatDuration(this.props.remainingDuration)}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    alignItems:'center'
  },
  slider: {
    flex: 1,
  },
  text: {
    textAlign: 'center', 
    textAlignVertical: 'center'
  }
})

ProgressView.defaultProps = {
  style: {
    sliderThumbColor: '#000',
    sliderMinTrackColor: '#000',
    sliderMaxTrackColor: '#000',
    textColor: '#000'
  }
}

ProgressView.propTypes = {
  remainingDuration: PropTypes.number.isRequired,
  duration: PropTypes.number.isRequired,
  progress: PropTypes.number.isRequired,
  onValueChange: PropTypes.func,
  style: PropTypes.shape({
    sliderThumbColor: PropTypes.string,
    sliderMinTrackColor: PropTypes.string,
    sliderMaxTrackColor: PropTypes.string,
    textColor: PropTypes.string
  })
}