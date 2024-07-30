CREATE TABLE verify_participant(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    timer TIMESTAMP NOT NULL,
    participant_id UUID,

    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE
);