import React from 'react';
import PropTypes from 'prop-types';

import { ProgressView } from '../elements/ProgressView';

export class ProgressViewComponent extends React.Component {
  constructor(props) {
    super(props);
    
    this.state = {
      duration: 0,
    }
  }

  render() {
    return <ProgressView style={this.props.style}
                         duration={this.state.duration} />
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
  })
}