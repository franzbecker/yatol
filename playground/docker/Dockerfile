FROM ubuntu:15.04

# Install basics
RUN apt-get update
RUN apt-get install -y wget git curl zip
RUN apt-get install -y openjdk-8-jdk

COPY README.md /root/README.md
COPY dist/ /opt/playground

EXPOSE 8080

ENTRYPOINT ["/opt/playground/bin/playground"]
