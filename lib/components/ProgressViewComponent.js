import React from 'react';
import PropTypes from 'prop-types';

import {
  NativeModules,
  DeviceEventEmitter
} from 'react-native';
import { ProgressView } from '../elements/ProgressView';

const { RNSimplePlayer } = NativeModules;

export class ProgressViewComponent extends React.Component {
  constructor(props) {
    super(props);
    
    this.state = {
      duration: 0,
      position: 0,
    };

    this.handleValueChange = this.handleValueChange.bind(this);
  }

  componentDidMount() {
    DeviceEventEmitter.addListener(
      RNSimplePlayer.ON_POSITION_CHANGED,
      (event) => this.setState({
        position: event.position
      })
    );

    DeviceEventEmitter.addListener(
      RNSimplePlayer.ON_DURATION_CHANGED,
      (event) => this.setState({
        duration: event.duration
      })
    );
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeAllListeners(
      RNSimplePlayer.ON_POSITION_CHANGED
    );

    DeviceEventEmitter.removeAllListeners(
      RNSimplePlayer.ON_DURATION_CHANGED
    );
  }

  handleValueChange(value) {
    this.setState({
      position: value
    });
    this.props.onProgressChanged(value);
  }

  render() {
    let computedDuration = this.state.duration - this.state.position;

    return <ProgressView style={this.props.style}
                         remainingDuration={computedDuration}
                         duration={this.state.duration}
                         progress={this.state.position}
                         onValueChange={this.handleValueChange} />
  }
}

ProgressViewComponent.defaultProps = {
  style: {
    sliderThumbColor: '#000',
    sliderMinTrackColor: '#000',
    sliderMaxTrackColor: '#000',
    textColor: '#FFF'
  }
}

ProgressViewComponent.propTypes = {
  style: PropTypes.shape({
    sliderThumbColor: PropTypes.string,
    sliderMinTrackColor: PropTypes.string,
    sliderMaxTrackColor: PropTypes.string,
    textColor: PropTypes.string
  }),
  onProgressChanged: PropTypes.func.isRequired,
}