#! /bin/bash
docker create --name jenkins -p 8080:8080 -v "/yatol":"/yatol" --add-host dockerhost:`/sbin/ip route | awk '/docker/ {print $9}'` --privileged -p 2375 -e PORT=2375 -t yatol/jenkins /usr/local/bin/jenkins.sh
