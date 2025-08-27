CREATE TABLE trips(
    id bigint not null auto_increment PRIMARY KEY,
    owner_name VARCHAR(255) NOT NULL,
    owner_email VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    starts_at DATE NOT NULL,
    ends_at DATE NOT NULL,
    is_confirmed BOOLEAN NOT NULL
);