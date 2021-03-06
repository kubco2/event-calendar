CREATE TABLE users (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    nick VARCHAR(50) NOT NULL UNIQUE ,
    password VARCHAR(60) NOT NULL
) TYPE=innoDB;

CREATE TABLE events (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    owner INT UNSIGNED NOT NULL ,
    place VARCHAR(100) NOT NULL ,
    description TEXT NOT NULL ,
    timeFrom DATETIME NOT NULL ,
    timeTo DATETIME NOT NULL ,
    shared TINYINT(1) NOT NULL,
    INDEX owner_id (owner),
    FOREIGN KEY (owner) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
) TYPE=innoDB;

CREATE TABLE calendar (
    eventId INT UNSIGNED NOT NULL ,
    userId INT UNSIGNED NOT NULL ,
    CONSTRAINT id PRIMARY KEY (eventId,userId),
    FOREIGN KEY (eventId) REFERENCES events(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
) TYPE=innoDB;
