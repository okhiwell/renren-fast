#!/bin/bash
docker ps -a | grep Up | awk -F' ' '{print $1}' | xargs docker stop

