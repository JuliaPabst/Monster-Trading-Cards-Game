CREATE TABLE IF NOT EXISTS users (
uuid VARCHAR(255) PRIMARY KEY,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
bio VARCHAR(500),
image VARCHAR(255),
elo INT,
wins INT,
losses INT,
token VARCHAR(255),
credit int
);

CREATE TABLE IF NOT EXISTS decks (
id VARCHAR(255) PRIMARY KEY,
ownerId VARCHAR(255),
FOREIGN KEY (ownerId) REFERENCES users(uuid)
);

CREATE TABLE IF NOT EXISTS cards (
id VARCHAR(255) PRIMARY KEY,
name VARCHAR(255) NOT NULL,
damage INT NOT NULL,
owner_uuid VARCHAR(255),
deck_id VARCHAR(255),
FOREIGN KEY (owner_uuid) REFERENCES users(uuid),
FOREIGN KEY(deck_id) REFERENCES decks(id)
);

CREATE TABLE IF NOT EXISTS trades (
     id VARCHAR(255) PRIMARY KEY,
     card_to_trade VARCHAR(255),
     card_type VARCHAR(255),
     minimum_damage FLOAT,
     FOREIGN KEY (card_to_trade) REFERENCES cards(id)
);