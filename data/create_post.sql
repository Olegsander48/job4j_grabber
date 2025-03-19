CREATE DATABASE storage;
CREATE SCHEMA agregator;

CREATE TABLE agregator.post (
    id      serial PRIMARY KEY,
    name    varchar(30),
    text    varchar(255),
    link    text,
    created timestamp
);