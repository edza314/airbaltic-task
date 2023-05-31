CREATE SCHEMA IF NOT EXISTS airbaltic;
CREATE TABLE IF NOT EXISTS airbaltic.users
(
    id           BIGSERIAL PRIMARY KEY,
    first_name         VARCHAR(50),
    last_name      VARCHAR(50),
    email_id        VARCHAR(50)
    );
