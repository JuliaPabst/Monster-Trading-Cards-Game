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

DELETE FROM users;

CREATE TABLE IF NOT EXISTS packages (
    id VARCHAR(255) PRIMARY KEY
);

ALTER TABLE packages
    ADD owner VARCHAR(255);

ALTER TABLE packages
    ADD CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES users(uuid);

CREATE TABLE IF NOT EXISTS cards (
id VARCHAR(255) PRIMARY KEY,
name VARCHAR(255) NOT NULL,
damage INT NOT NULL,
packageId VARCHAR(255),
FOREIGN KEY (packageId) REFERENCES packages(id)
);
