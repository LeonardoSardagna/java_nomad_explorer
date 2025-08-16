CREATE TABLE activities(
id bigint not null auto_increment PRIMARY KEY,
title VARCHAR(255) NOT NULL,
occurs_at TIMESTAMP NOT NULL,
trip_id BIGINT,
FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);