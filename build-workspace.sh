#!/bin/bash
sudo apt update
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository 'deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable'
sudo apt update
apt-cache policy docker-ce
yes Y | sudo apt install docker-ce
curl -L 'https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)' -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
curl --silent --output docker-compose-run-linux.yml https://raw.githubusercontent.com/GustavoMoraesFonseca/south-system-democracy/develop/docker-composes/docker-compose-run-linux.yml
DB_ADMIN=root
DB_ADMIN_PASSWORD=admin123
DB_IP=172.31.29.21
DB_USER=backend-service
DB_USER_PASSWORD=Back@Service
export DB_ADMIN
export DB_ADMIN_PASSWORD
export DB_IP
export DB_USER
export DB_USER_PASSWORD
docker-compose -f docker-compose-run-linux.yml up -d
docker exec -it mysql /bin/bash
apt-get update
yes Y | apt install apt-transport-https ca-certificates curl software-properties-common
curl --silent --output southsystem.sql https://raw.githubusercontent.com/GustavoMoraesFonseca/south-system-democracy/develop/southsystem.sql
mysql -u root -padmin123
/. southsystem.sql