#!/bin/bash

# setup camera
v4l2-ctl -d 1 -c focus_automatic_continuous=0
v4l2-ctl -d 1 -c focus_absolute=0

# start camera
/home/orangepi/mjpg-streamer/mjpg-streamer-experimental/runmjpg.sh &
