#!/bin/bash
dir=`pwd`
echo $dir
mvn clean install

java  -jar target/renren-fast.jar
