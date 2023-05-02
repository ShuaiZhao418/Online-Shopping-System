# install mariadb
sudo yum update -y
sudo yum install -y mariadb-server
sudo systemctl start mariadb
sudo systemctl enable mariadb
# change database passoword
mysql -u root -e "USE mysql; UPDATE user SET Password=PASSWORD('123456') WHERE User='root' AND Host='localhost'; FLUSH PRIVILEGES;"
