FROM ubuntu:14.04
RUN apt-get update && apt-get install -y wget git curl zip
RUN wget -q -O - https://jenkins-ci.org/debian/jenkins-ci.org.key | apt-key add -
RUN sh -c 'echo deb http://pkg.jenkins-ci.org/debian binary/ > /etc/apt/sources.list.d/jenkins.list'
RUN apt-get update && apt-get install -y jenkins
RUN rm -rf /var/lib/apt/lists/*
