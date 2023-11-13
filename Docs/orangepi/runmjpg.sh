#!/bin/sh

APP_PATH=/home/orangepi/mjpg-streamer/mjpg-streamer-experimental
PICS_PATH=${APP_PATH}/pics

cd ${APP_PATH}

killall -q mjpg_streamer

if [ ! -d "${PICS_PATH}" ]
then
    mkdir ${PICS_PATH}
else
    rm -f ${PICS_PATH}/*
fi

export LD_LIBRARY_PATH="$(pwd)"
# ./mjpg_streamer -i "./input_uvc.so -d /dev/video1 -vf true -u -r HD -f 30" -o "./output_http.so -w ./www"
#./mjpg_streamer -i "./input_uvc.so -d /dev/video1 -r HD -softfps 16" -o "./output_http.so -p 8081" -o "output_file.so -f pics -d 10000"
./mjpg_streamer -i "./input_uvc.so -d /dev/video1 -r HD -softfps 16" -o "./output_http.so -p 8081"
