/* PRIMARY ID SEQUENCE */
DROP SEQUENCE SEQ_CONFIGURATION IF EXISTS;
CREATE SEQUENCE SEQ_CONFIGURATION START WITH 1 INCREMENT BY 1;

/* MESSAGES */
DROP TABLE MESSAGES IF EXISTS;
CREATE TABLE MESSAGES
	(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	CHANNEL_ID INTEGER NOT NULL,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	SENDING_FACILITY VARCHAR(4000) NOT NULL,
	EVENT VARCHAR(4000) NOT NULL,
	CONTROL_ID VARCHAR(4000) NOT NULL,
	MESSAGE VARCHAR(4000) NOT NULL,
	STATUS VARCHAR(4000) NOT NULL);

/* EVENTS */
DROP TABLE EVENTS IF EXISTS;
CREATE TABLE EVENTS
	(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	EVENT VARCHAR(4000) NOT NULL,
	EVENT_LEVEL VARCHAR(4000) NOT NULL,
	DESCRIPTION VARCHAR(4000),
	ATTRIBUTES VARCHAR(4000));

/* CHANNELS */
DROP TABLE CHANNELS IF EXISTS;
CREATE TABLE CHANNELS
	(ID INTEGER NOT NULL PRIMARY KEY,
	CHANNEL_NAME VARCHAR(4000) NOT NULL,
	CHANNEL_DATA VARCHAR(4000) NOT NULL);

/* USERS */
DROP TABLE USERS IF EXISTS;
CREATE TABLE USERS
	(ID INTEGER NOT NULL PRIMARY KEY,
	USERNAME VARCHAR(4000) NOT NULL,
	PASSWORD VARCHAR(4000) NOT NULL);

/* TRANSPORTS */
DROP TABLE TRANSPORTS IF EXISTS;
CREATE TABLE TRANSPORTS
	(NAME VARCHAR(4000) NOT NULL PRIMARY KEY,
	CLASS_NAME VARCHAR(4000) NOT NULL,
	PROTOCOL VARCHAR(4000) NOT NULL,
	TRANSFORMERS VARCHAR(4000) NOT NULL,
	TYPE VARCHAR(4000) NOT NULL,
	INBOUND BOOLEAN NOT NULL,
	OUTBOUND BOOLEAN NOT NULL);

/* CONFIGURATIONS */
DROP TABLE CONFIGURATIONS IF EXISTS;
CREATE TABLE CONFIGURATIONS
	(ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	DATA VARCHAR(4000) NOT NULL);
	
/* ENCRYPTION KEYS */
DROP TABLE KEYS IF EXISTS;
CREATE TABLE KEYS
	(DATA VARCHAR(4000) NOT NULL);
	
/* ADD ADMIN ACCOUNT */
INSERT INTO USERS VALUES(0, 'admin', 'abc12345');

/* ADD TRANSPORTS */

INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('LLP Listener', 'org.mule.providers.tcp.TcpConnector', 'tcp', 'ByteArrayToString', 'LISTENER', true, false);
INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('Database Reader', 'org.mule.providers.jdbc.JdbcConnector', 'jdbc', 'ResultMapToXML', 'LISTENER', false, true);
INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('File Reader', 'org.mule.providers.file.FileConnector', 'file', '', 'LISTENER', true, false);

INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('LLP Sender', 'org.mule.providers.tcp.TcpConnector', 'tcp', '', 'SENDER', true, true);
INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('Database Writer', 'org.mule.providers.jdbc.JdbcConnector', 'jdbc', '', 'SENDER', true, false);
INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('File Writer', 'org.mule.providers.file.FileConnector', 'file', '', 'SENDER', true, true);

/* INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('JMS Writer', 'org.mule.providers.jms.JmsConnector', 'jms', '', 'SENDER'); */
/* INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('HTTP Listener', 'org.mule.providers.http.HttpsConnector', 'http', 'HttpRequestToString', 'LISTENER'); */
/* INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('HTTPS Listener', 'org.mule.providers.http.HttpConnector', 'https', 'HttpRequestToString', 'LISTENER'); */
/* INSERT INTO TRANSPORTS (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE, INBOUND, OUTBOUND) VALUES ('Email Sender', 'org.mule.providers.smtp.SmtpConnector', 'smtp', '', 'SENDER'); */
