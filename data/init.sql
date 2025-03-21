CREATE DATABASE grabber;
CREATE SCHEMA agregator;

CREATE TABLE agregator.post (
    id          serial PRIMARY KEY,
    title       varchar(100),
    description varchar(255),
    link        text UNIQUE,
    created     timestamp
);