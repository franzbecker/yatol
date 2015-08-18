FROM ubuntu:15.04

# Install basics
RUN apt-get update
RUN apt-get install -y wget git curl zip
RUN apt-get install -y apache2

# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Expose the ports we're interested in
EXPOSE 80
#EXPOSE 9990

CMD tail -f /var/log/apache2/access.log
