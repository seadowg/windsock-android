#!/bin/bash

set -e

export TERM=xterm-256color

export ANDROID_HOME=/usr/local/android-sdk-linux
cd windsock-android
./gradlew clean test
