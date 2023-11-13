#!/bin/bash

# setup camera
v4l2-ctl -d 1 -c focus_automatic_continuous=0
v4l2-ctl -d 1 -c focus_absolute=0

# start camera
/home/orangepi/mjpg-streamer/mjpg-streamer-experimental/runme.sh

APP_PATH=/home/orangepi/sbda/
VER=1.0-SNAPSHOT

# kill all java processes
javaPID="$(pidof java)"
if [ ! -z "$javaPID" ]; then
  echo "Killing java at PID: ${javaPID}"
  kill -9 "${javaPID}"
fi

cd ${APP_PATH}
java -jar sbda-${VER}.jar
