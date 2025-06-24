CREATE SCHEMA IF NOT EXISTS bot;

DROP TABLE IF EXISTS bot."Users";

CREATE TABLE IF NOT EXISTS bot."Users"
(
    user_id         VARCHAR(255) PRIMARY KEY NOT NULL,
    state           VARCHAR(255)             NOT NULL,
    created         TIMESTAMPTZ              NOT NULL
);