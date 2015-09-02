#! /bin/bash
docker create --name jenkins -p 8080:8080 --privileged=true -v /dev/:/dev/ -v "/yatol":"/yatol" -v "/home/vagrant/.vagrant.d":"/root/.vagrant.d" --add-host dockerhost:`/sbin/ip route | awk '/docker/ {print $9}'` -t yatol/jenkins /usr/local/bin/jenkins.sh
