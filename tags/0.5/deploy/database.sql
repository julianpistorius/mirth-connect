DROP TABLE logs IF EXISTS;
CREATE TABLE logs
	(id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	channel VARCHAR(4000) NOT NULL,  
	tstamp timestamp NOT NULL,
	message VARCHAR(4000) NOT NULL);

DROP TABLE messages IF EXISTS;
CREATE TABLE messages
	(id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	channel VARCHAR(4000) NOT NULL,
	tstamp timestamp NOT NULL,
	source VARCHAR(4000) NOT NULL,
	event VARCHAR(4000) NOT NULL,
	msgid VARCHAR(4000) NOT NULL,
	bytesize INTEGER NOT NULL,
	content VARCHAR(4000) NOT NULL,
	contentxml VARCHAR(4000) NOT NULL);

DROP TABLE statistics IF EXISTS;
CREATE TABLE statistics
	(id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	channel VARCHAR(4000) NOT NULL,
	sent INTEGER NOT NULL,
	errors INTEGER NOT NULL,
	received INTEGER NOT NULL);