#!/bin/bash

# start camera stream
/home/orangepi/sbda/runcamera.sh &

# start java web interface
/home/orangepi/sbda/runsbda.sh &