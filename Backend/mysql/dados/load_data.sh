#!/bin/bash

docker exec -i mysql1 mysql -uroot -proot < sql/drop.sql
docker exec -i mysql1 mysql -uroot -proot < sql/create_tables.sql


files=$(ls  data_load/)

for file in $files; do
	echo "start $file"
	awk -F ";" '{print $1";"$2";"$3";"$4";"$5";"$6";"$7";"$8";"$9";"$10";"$11";"$12";"$13";"$14}' data_load/$file > data_load/dados.csv
	docker cp data_load/dados.csv mysql1:/var/lib/mysql-files/dados
	docker exec -i mysql1 mysql  -uroot -proot < sql/load_month.sql
	rm -rf data_load/dados.csv	
	echo "done $file"
done

# Calculate the statistic
docker exec -i mysql1 mysql -uroot -proot < sql/statistics.sql

# Calcule the 
docker exec -i mysql1 mysql -uroot -proot < sql/service_time.sql


#docker exec -i mysql1 mysql -uroot -proot < sql/drop_data_input.sql
