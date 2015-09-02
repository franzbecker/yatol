function dockerInstall() {
	if [ ! -f /usr/bin/docker ]; then
		# Taken from https://get.docker.com/ubuntu/
		# Check that HTTPS transport is available to APT
		if [ ! -e /usr/lib/apt/methods/https ]; then
			apt-get install -y apt-transport-https
		fi

		# Add the repository to your APT sources
		echo deb https://get.docker.com/ubuntu docker main > /etc/apt/sources.list.d/docker.list

		# Then import the repository key
		apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9

		# Install docker
		apt-get update -qq
		apt-get install -qqy lxc-docker
	else
		echo " > Docker already installed"
	fi
}

function dockerEnableRemoteApi() {
	if [ ! -f /etc/systemd/system/docker-tcp.socket ]; then
		# Taken from https://coreos.com/os/docs/latest/customizing-docker.html
		## TODO: can we make this echo stuff nicer?
		touch /etc/systemd/system/docker-tcp.socket
		echo "[Unit]" > /etc/systemd/system/docker-tcp.socket
		echo "Description=Docker Socket for the API" >> /etc/systemd/system/docker-tcp.socket
		echo "" >> /etc/systemd/system/docker-tcp.socket
		echo "[Socket]" >> /etc/systemd/system/docker-tcp.socket
		echo "ListenStream=2375" >> /etc/systemd/system/docker-tcp.socket
		echo "BindIPv6Only=both" >> /etc/systemd/system/docker-tcp.socket
		echo "Service=docker.service" >> /etc/systemd/system/docker-tcp.socket
		echo "" >> /etc/systemd/system/docker-tcp.socket
		echo "[Install]" >> /etc/systemd/system/docker-tcp.socket
		echo "WantedBy=sockets.target" >> /etc/systemd/system/docker-tcp.socket

		echo 'DOCKER_HOST="tcp://dockerhost:2375"' >> /etc/environment

		echo "" >> /etc/hosts
		echo "# Make localhost known as dockerhost" >> /etc/hosts
		echo "127.0.0.1 dockerhost" >> /etc/hosts

		service docker stop
		systemctl enable docker-tcp.socket
		systemctl start docker-tcp.socket
		systemctl start docker
	else
		echo " > Docker remote API already enabled"
	fi
}

function gradleEnableDaemon() {
	if [ ! -f ~/.gradle/gradle.properties ]; then
		if [ ! -e ~/.gradle ]; then
			mkdir ~/.gradle
		fi
		touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties
	else
		echo " > gradle.properties already exists, no changes were made to it"
	fi
}

function addDockerDeleteShellScript() {
	touch deleteAllDockerImages.sh
	chown vagrant:vagrant deleteAllDockerImages.sh
	chmod u+rwx deleteAllDockerImages.sh
	echo "#!/bin/bash" > deleteAllDockerImages.sh
	echo "# Stop and remove all containers" >> deleteAllDockerImages.sh
	echo "docker stop \$(docker ps -a -q)" >> deleteAllDockerImages.sh
	echo "docker rm \$(docker ps -a -q)" >> deleteAllDockerImages.sh
}

function setUpBash() {
	echo "# if running bash" > .profile
	echo "if [ -n \"\$BASH_VERSION\" ]; then" >> .profile
	echo "    # include .bashrc if it exists"  >> .profile
	echo "    if [ -f \"\$HOME/.bashrc\" ]; then"  >> .profile
	echo "	. \"\$HOME/.bashrc\""  >> .profile
	echo "    fi"  >> .profile
	echo "fi"  >> .profile
 	echo ""  >> .profile
	echo "# set PATH so it includes user's private bin if it exists" >> .profile
	echo "if [ -d \"\$HOME/bin\" ] ; then" >> .profile
	echo "    PATH=\"\$HOME/bin:\$PATH\"" >> .profile
	echo "fi" >> .profile
	echo "" >> .profile

	GSCRIPT=$(curl https://gist.githubusercontent.com/franzbecker/b4bb77f66d36472548bf/raw/7541ed987dd0bf5a98555ace1f198ac0a514fd9b/gradle.bash)
	echo "$GSCRIPT" >> .profile
	echo "" >> .profile
	echo "# Stops and removes all running docker images" >> .profile
	echo "alias dadf='~/deleteAllDockerImages.sh'" >> .profile
	echo "" >> .profile
	echo "# show all files" >> .profile
	echo "alias ll='ls -al'" >> .profile
	echo "" >> .profile
	echo "export CLICOLOR=1" >> .profile
	echo "export LSCOLORS=ExFxBxDxCxegedabagacad" >> .profile

}

echo "Provisioning virtual machine..."

echo "Install VirtualBox"
echo deb http://download.virtualbox.org/virtualbox/debian vivid contrib > /etc/apt/sources.list.d/virtualbox.list
wget -q https://www.virtualbox.org/download/oracle_vbox.asc -O- | apt-key add -
apt-get update
apt-get install -qqy virtualbox-5.0

#echo "Calling apt-get update"
#apt-get update -qq

echo "Installing language pack"
apt-get install -qqy language-pack-de

echo "Installing openjdk-8-jdk"
apt-get install -qqy openjdk-8-jdk

echo "Installing Docker"
dockerInstall

echo "Enabling Docker remote API"
dockerEnableRemoteApi

echo "Enable Gradle daemon"
gradleEnableDaemon

echo "Add delete all docker container script"
addDockerDeleteShellScript

echo "Setup bash profile"
setUpBash
