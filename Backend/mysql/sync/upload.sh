#!/bin/bash

rm -rf dump.sql
docker exec -it mysql1 mysqldump -uroot -proot SOSUPA | grep -v "Warning" > dump.sql
scp -i KeyAWS.pem dump.sql   ec2-user@ec2-54-191-67-232.us-west-2.compute.amazonaws.com:~/dados/
