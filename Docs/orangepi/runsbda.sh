#!/bin/bash

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
