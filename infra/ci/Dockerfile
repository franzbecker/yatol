FROM ubuntu:15.04

# Install basics
RUN apt-get update
RUN apt-get install -y wget git curl zip
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -qqy  \
    apt-transport-https \
    ca-certificates \
    lxc  \
    iptables
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Install Jenkins
# see https://github.com/jenkinsci/docker/blob/master/Dockerfile)
ENV JENKINS_HOME /var/jenkins_home
ENV JENKINS_VERSION 1.609.2
ENV JENKINS_SHA 59215da16f9f8a781d185dde683c05fcf11450ef
VOLUME /var/jenkins_home
RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d

RUN curl -fL http://mirrors.jenkins-ci.org/war-stable/$JENKINS_VERSION/jenkins.war -o /usr/share/jenkins/jenkins.war \
  && echo "$JENKINS_SHA /usr/share/jenkins/jenkins.war" | sha1sum -c -

EXPOSE 8080

ENV COPY_REFERENCE_FILE_LOG $JENKINS_HOME/copy_reference_file.log
ENV JENKINS_UC https://updates.jenkins-ci.org

COPY jenkins.sh /usr/local/bin/jenkins.sh

# Handle plugins
COPY plugins.sh /usr/local/bin/plugins.sh
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

# Explicitly configure gradle to not use the gradle daemon
RUN mkdir ~/.gradle/ -p && touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties
COPY jenkins_home $JENKINS_HOME

# Install VirtualBox
RUN echo deb http://download.virtualbox.org/virtualbox/debian vivid contrib > /etc/apt/sources.list.d/virtualbox.list
RUN wget -q https://www.virtualbox.org/download/oracle_vbox.asc -O- | apt-key add -
RUN apt-get update
RUN apt-get install -qqy virtualbox-5.0

# Install Vagrant
RUN apt-get install -qqy vagrant

ENTRYPOINT ["/usr/local/bin/jenkins.sh"]
