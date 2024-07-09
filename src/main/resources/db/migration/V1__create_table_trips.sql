CREATE TABLE trips(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    owner_name VARCHAR(255) NOT NULL,
    owner_email VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    is_confirmed BOOLEAN NOT NULL
);