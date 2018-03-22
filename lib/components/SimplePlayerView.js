import React from 'react';
import {
  View,
} from 'react-native';
import { PlayButton } from '../elements/PlayButton';

export class SimplePlayerView extends React.Component {

  render() {
    return (
      <View>
        <PlayButton isPlaying={this.props.isPlaying}/>
      </View>
    );
  }
}