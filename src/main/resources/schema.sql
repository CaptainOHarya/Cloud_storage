CREATE SCHEMA IF NOT EXISTS cloud_storage;

CREATE TABLE IF NOT EXISTS users
(
    id bigserial PRIMARY KEY,
    name varchar(50) NOT NULL,
    surname varchar(50) NOT NULL,
    age int check(age > 0 and age < 555),
    phone_number varchar(50) NOT NULL,
    login varchar(50) NOT NULL,
    password varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    UNIQUE(login, email, phone_number)
);

CREATE TABLE IF NOT EXISTS files
(
    id bigserial PRIMARY KEY,
    name varchar(50) NOT NULL,
    content_type varchar(10) NOT NULL,
    content bytea NOT NULL,
    created_date timestamp,
    size bigint NOT NULL,
    user_id bigserial,
    CONSTRAINT fk_users_files FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE ON DELETE CASCADE

);