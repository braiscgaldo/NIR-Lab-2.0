-- Script user database creations (only change name and passwd)

-- Create user with all IPs
CREATE USER "brais"@"%" IDENTIFIED BY 'password1';

CREATE DATABASE brais_data_problems;

GRANT ALL PRIVILEGES ON brais_data_problems.* TO brais@"%";

FLUSH PRIVILEGES;

-- Create tables

USE brais_data_problems;

-- Problem table
CREATE TABLE IF NOT EXISTS Problem (
    problem_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    problem_name VARCHAR(64) NOT NULL
);

-- Meditions table (IMPORTANT - medition data is in MongoDB)
CREATE TABLE IF NOT EXISTS Meditions (
    medition_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    problem_id int NOT NULL,
    medition_name VARCHAR(64) NOT NULL,
    medition_data VARCHAR(500) NOT NULL,
    CONSTRAINT `problem_medition`
        FOREIGN KEY (problem_id) REFERENCES Problem (problem_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

-- Problem Label table
CREATE TABLE IF NOT EXISTS Problem_Label (
    problem_label_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    problem_id int NOT NULL,
    problem_label_name VARCHAR(64) NOT NULL,
    problem_label_type VARCHAR(10) NOT NULL,
    CONSTRAINT `problem_label`
        FOREIGN KEY (problem_id) REFERENCES Problem (problem_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

-- Medition Label
CREATE TABLE IF NOT EXISTS Medition_Label (
    medition_label_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    medition_id int NOT NULL,
    medition_label_value VARCHAR(128) NOT NULL,
    CONSTRAINT `medition_label`
        FOREIGN KEY (medition_id) REFERENCES Meditions (medition_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

-- Models
CREATE TABLE IF NOT EXISTS Model (
    model_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    problem_id int NOT NULL,
    model_name VARCHAR(64) NOT NULL,
    model_data VARCHAR(500) NOT NULL,
    CONSTRAINT `problem_model`
        FOREIGN KEY (problem_id) REFERENCES Problem (problem_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

-- Configuration Files
CREATE TABLE IF NOT EXISTS Configuration_Train (
    configuration_train_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    problem_id int NOT NULL,
    configuration_train_name VARCHAR(64) NOT NULL,
    configuration_train_data VARCHAR(500) NOT NULL,
    CONSTRAINT `problem_configuration_train`
        FOREIGN KEY (problem_id) REFERENCES Problem (problem_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);
