CREATE TABLE IF NOT EXISTS users (
uuid VARCHAR(255) PRIMARY KEY,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
bio VARCHAR(500),
image VARCHAR(255),
elo INT,
wins INT,
losses INT,
token VARCHAR(255)
);

ALTER TABLE users ADD COLUMN credit int;

DELETE FROM users;

CREATE TABLE IF NOT EXISTS packages (
    id VARCHAR(255) PRIMARY KEY
);

ALTER TABLE packages
    ADD owner VARCHAR(255);

ALTER TABLE packages
    ADD CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES users(uuid);

DELETE FROM packages;

CREATE TABLE IF NOT EXISTS cards (
id VARCHAR(255) PRIMARY KEY,
name VARCHAR(255) NOT NULL,
damage INT NOT NULL,
packageId VARCHAR(255),
FOREIGN KEY (packageId) REFERENCES packages(id)
);

DELETE FROM cards;

CREATE TABLE IF NOT EXISTS decks (
    id VARCHAR(255) PRIMARY KEY,
    ownerId VARCHAR(255),
    FOREIGN KEY (ownerId) REFERENCES users(uuid)
);

ALTER TABLE cards ADD COLUMN deckId int;

CREATE TABLE IF NOT EXISTS trades (
     id VARCHAR(255) PRIMARY KEY,
     card1_id VARCHAR(255) NOT NULL,
     card2_id VARCHAR(255) NOT NULL,
     card1_new_owner_id VARCHAR(255) NOT NULL,
     card2_new_owner_id VARCHAR(255) NOT NULL,
     type VARCHAR(255),
     minimum_damage int,
     FOREIGN KEY (card1_id) REFERENCES cards(id),
     FOREIGN KEY (card2_id) REFERENCES cards(id)
);