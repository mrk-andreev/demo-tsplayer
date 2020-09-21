#! /bin/bash


sudo apt-get update -y && sudo apt-get -y upgrade
curl -fsSL https://get.docker.com -o get-docker.sh && sh get-docker.sh
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

