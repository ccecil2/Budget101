budget.db


CREATE TABLE IF NOT EXISTS User(
    userName VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20) NOT NULL,
    picture BLOB
);

CREATE TABLE IF NOT EXISTS Category(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(20) NOT NULL,
    alarm INTEGER NOT NULL,
    max DOUBLE DEFAULT 0.0,
    type INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS Record(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    amount DOUBLE NOT NULL,
    comment VARCHAR(50),
    date DATETIME NOT NULL,
    FK_Category INTEGER,
    FOREIGN KEY(FK_Category) REFERENCES Category(ID)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Split(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    otherUser VARCHAR(20),
    status INTEGER NOT NULL,
    FK_Record INTEGER NOT NULL,
    FOREIGN KEY(FK_Record) REFERENCES Record(ID)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);