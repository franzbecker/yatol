function dockerInstall() {
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
}

function dockerEnableRemoteApi() {
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

	service docker stop
	systemctl enable docker-tcp.socket
	systemctl start docker-tcp.socket
	systemctl start docker
}

echo "Provisioning virtual machine..."

echo "Calling apt-get update"
apt-get update -qq

echo "Installing language pack"
apt-get install -qqy language-pack-de

echo "Installing openjdk-8-jdk"
apt-get install -qqy openjdk-8-jdk

echo "Installing Docker"
dockerInstall

echo "Enabling Docker remote API"
dockerEnableRemoteApi

echo "Explicitly disable Gradle daemon"
touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties
