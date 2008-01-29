ALTER TABLE CHANNEL ADD LAST_MODIFIED DATETIME DEFAULT GETDATE()

UPDATE CHANNEL SET LAST_MODIFIED = GETDATE()

ALTER TABLE PERSON ADD LAST_LOGIN DATETIME DEFAULT GETDATE()

UPDATE PERSON SET LAST_LOGIN = GETDATE()

ALTER TABLE MESSAGE ADD ATTACHMENT SMALLINT DEFAULT 0

CREATE TABLE ATTACHMENT
    (ID VARCHAR(255) NOT NULL PRIMARY KEY,
     MESSAGE_ID VARCHAR(255) NOT NULL,
     ATTACHMENT_DATA IMAGE,
     ATTACHMENT_SIZE INTEGER,
     ATTACHMENT_TYPE VARCHAR(40))