import React from 'react';
import PropTypes from 'prop-types';

import {
  View,
  Dimensions,
  Slider,
  Text,
  StyleSheet
} from 'react-native';
import { PlayButton } from '../elements/PlayButton';
import { ProgressViewComponent } from '../components/ProgressViewComponent';

export class SimplePlayerView extends React.Component {
  render() {
    const WIDTH = Dimensions.get('window').width;

    const playButtonStyle = {
      iconColor: this.props.style.iconColor,
      iconSize: this.props.style.iconSize
    }

    const progressViewStyle = {
      sliderThumbColor: this.props.style.sliderThumbColor,
      sliderMinTrackColor: this.props.style.sliderMinTrackColor,
      sliderMaxTrackColor: this.props.style.sliderMaxTrackColor,
      textColor: this.props.style.textColor
    }

    return (
      <View style={[styles.container, {width: WIDTH, backgroundColor: this.props.style.backgroundColor}]}>
        <PlayButton isPlaying={this.props.isPlaying} 
                    style={playButtonStyle}
                    onPress={this.props.onPress}/>
        <ProgressViewComponent style={progressViewStyle}
                               onProgressChanged={this.props.onProgressChanged}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    padding: 8,
    flexDirection: 'row',
    alignItems:'center'
  }
})

SimplePlayerView.defaultProps = {
  style: {
    backgroundColor: '#FFF',
    iconColor: '#000',
    iconSize: 50,
    sliderThumbColor: '#000',
    sliderMinTrackColor: '#000',
    sliderMaxTrackColor: '#000',
    textColor: '#000'
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
  onPress: PropTypes.func,
  onProgressChanged: PropTypes.func.isRequired,
}