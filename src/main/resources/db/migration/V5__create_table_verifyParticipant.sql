CREATE TABLE verify_participant(
    id bigint not null auto_increment PRIMARY KEY,
    timer TIMESTAMP NOT NULL,
    participant_id BIGINT,

    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE
);