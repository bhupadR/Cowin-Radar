#!/bin/bash

echo > MyStop.sh
echo > /opt/conf/cowin-radar/logs/init.out
echo > /opt/conf/cowin-radar/logs/stop.log
java -jar /opt/conf/cowin-radar/target/CowinRadar-0.0.1-SNAPSHOT.jar &> /opt/conf/cowin-radar/logs/init.out &
MyPID=$!
echo $MyPID
echo "Cowin Radar started."
echo "kill $MyPID &> /opt/conf/cowin-radar/logs/stop.log" > MyStop.sh