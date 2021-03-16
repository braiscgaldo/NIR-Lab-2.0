FROM mariadb:10.5.8

ENV MYSQL_ROOT_PASSWORD passwd
ENV MYSQL_DATABASE default

COPY create_dbs.sql /docker-entrypoint-initdb.d/create_dbs.sql

EXPOSE 3306


