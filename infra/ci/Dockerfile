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

# Install Docker from Docker Inc. repositories.
RUN curl -sSL https://get.docker.com/ubuntu/ | sh

# Install the magic wrapper.
ADD ./wrapdocker /usr/local/bin/wrapdocker
RUN chmod +x /usr/local/bin/wrapdocker


# Define additional metadata for our image.
VOLUME /var/lib/docker
CMD ["wrapdocker"]

ENTRYPOINT ["/usr/local/bin/jenkins.sh"]
