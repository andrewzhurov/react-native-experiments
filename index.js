'use strict';

const ToastAndroid = require('react-native').NativeModules.ToastAndroid;
const PlayerAndroid = require('react-native').NativeModules.PlayerAndroid;
const LightSensorAndroid = require('react-native').NativeModules.LightSensorAndroid;
const P2pAndroid = require('react-native').NativeModules.P2pAndroid;

export default { ToastAndroid,
                 PlayerAndroid,
                 LightSensorAndroid,
                 P2pAndroid,
               }


