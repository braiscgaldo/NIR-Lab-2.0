-- SQL script for create the users table
CREATE DATABASE nirlab;

USE nirlab;

GRANT ALL PRIVILEGES ON nirlab.* TO 'brais'@'%';

CREATE TABLE IF NOT EXISTS USERS (
    username VARCHAR(32) NOT NULL PRIMARY KEY,
    password INT NOT NULL,
    name VARCHAR(32) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL
);


