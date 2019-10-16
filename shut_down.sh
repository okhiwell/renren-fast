#!/bin/bash
ps -aux | grep  target/renren-fast.jar | grep -v grep | cut -c 9-15 | xargs kill -9

echo `ps -aux | grep  target/renren-fast.jar | grep -v grep | cut -c 9-15`



#pid=`ps -aux | grep  target/renren-fast.jar | grep -v grep | cut -c 9-15`
#echo $pid
#if [ -n $pid ] ;then
# kill -9 $pid
#fi
#nohup java -javaagent:/data/tools/pinpoint-agent/pinpoint-bootstrap-1.7.2.jar -Dpinpoint.agentId=renren-fast1 -Dpinpoint.applicationName=renren-fast -jar /data/renren-fast.jar >> /data/output.log 2>&1 &
