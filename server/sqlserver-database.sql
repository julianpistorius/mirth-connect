IF  EXISTS (SELECT NAME FROM SYS.DATABASES WHERE NAME = N'MIRTHDB')
	DROP DATABASE MIRTHDB;
	
CREATE DATABASE MIRTHDB;

GO

USE MIRTHDB;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'EVENT') AND type = (N'U'))
    DROP TABLE EVENT;

CREATE TABLE EVENT
	(ID INTEGER IDENTITY (0, 1) NOT NULL PRIMARY KEY,
	DATE_CREATED DATETIME DEFAULT GETDATE(),
	EVENT TEXT NOT NULL,
	EVENT_LEVEL VARCHAR(40) NOT NULL,
	DESCRIPTION TEXT,
	ATTRIBUTES TEXT);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'MESSAGE') AND type = (N'U'))
    ALTER TABLE MESSAGE DROP CONSTRAINT CHANNEL_ID_FK;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'CHANNEL') AND type = (N'U'))
    DROP TABLE CHANNEL;

CREATE TABLE CHANNEL
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	DESCRIPTION TEXT,
	IS_ENABLED SMALLINT,
	VERSION VARCHAR(40),
	REVISION INTEGER,
	DIRECTION VARCHAR(40),
	PROTOCOL VARCHAR(40),
	MODE VARCHAR(40),
	SOURCE_CONNECTOR TEXT,
	DESTINATION_CONNECTORS TEXT,
	PROPERTIES TEXT,
	PREPROCESSING_SCRIPT TEXT);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'MESSAGE') AND type = (N'U'))
    DROP TABLE MESSAGE;

CREATE TABLE MESSAGE
	(SEQUENCE_ID INTEGER IDENTITY (0, 1) NOT NULL PRIMARY KEY,
	ID VARCHAR(255) NOT NULL,
	CHANNEL_ID VARCHAR(255) NOT NULL,
	SOURCE VARCHAR(255),
	TYPE VARCHAR(255),
	DATE_CREATED DATETIME NOT NULL,
	VERSION VARCHAR(40),
	IS_ENCRYPTED SMALLINT NOT NULL,
	STATUS VARCHAR(40),
	RAW_DATA TEXT,
	RAW_DATA_PROTOCOL VARCHAR(40),
	TRANSFORMED_DATA TEXT,
	TRANSFORMED_DATA_PROTOCOL VARCHAR(40),
	ENCODED_DATA TEXT,
	ENCODED_DATA_PROTOCOL VARCHAR(40),
	CONNECTOR_MAP TEXT,
	CHANNEL_MAP TEXT,
	RESPONSE_MAP TEXT,
	CONNECTOR_NAME VARCHAR(255),
	ERRORS TEXT,
	CORRELATION_ID VARCHAR(255),
	UNIQUE (ID));
	
ALTER TABLE MESSAGE
ADD CONSTRAINT CHANNEL_ID_FK
FOREIGN KEY (CHANNEL_ID)
REFERENCES CHANNEL (ID) ON DELETE CASCADE;

CREATE INDEX MESSAGE_INDEX1 ON MESSAGE(SEQUENCE_ID, DATE_CREATED, CHANNEL_ID);

CREATE INDEX MESSAGE_INDEX2 ON MESSAGE(STATUS, CHANNEL_ID);

CREATE INDEX MESSAGE_INDEX3 ON MESSAGE(CORRELATION_ID, CHANNEL_ID);

CREATE INDEX MESSAGE_INDEX4 ON MESSAGE(CHANNEL_ID);
	
IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'SCRIPT') AND type = (N'U'))
    DROP TABLE SCRIPT;

CREATE TABLE SCRIPT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	SCRIPT TEXT);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'TEMPLATE') AND type = (N'U'))
    DROP TABLE TEMPLATE;

CREATE TABLE TEMPLATE
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	TEMPLATE TEXT);
	
IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'PERSON') AND type = (N'U'))
    DROP TABLE PERSON;

CREATE TABLE PERSON
	(ID INTEGER IDENTITY (0, 1) NOT NULL PRIMARY KEY,
	USERNAME VARCHAR(40) NOT NULL,
	PASSWORD VARCHAR(40) NOT NULL,
	SALT VARCHAR(40) NOT NULL,
	FULLNAME VARCHAR(255),
	EMAIL VARCHAR(255),
	PHONENUMBER VARCHAR(40),
	DESCRIPTION VARCHAR(255),
	LOGGED_IN SMALLINT NOT NULL);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'CHANNEL_ALERT') AND type = (N'U'))
    ALTER TABLE CHANNEL_ALERT DROP CONSTRAINT ALERT_ID_CA_FK;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'ALERT_EMAIL') AND type = (N'U'))
    ALTER TABLE ALERT_EMAIL DROP CONSTRAINT ALERT_ID_AE_FK;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'ALERT') AND type = (N'U'))
    DROP TABLE ALERT;

CREATE TABLE ALERT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	IS_ENABLED SMALLINT NOT NULL,
	EXPRESSION TEXT,
	TEMPLATE TEXT);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'CHANNEL_ALERT') AND type = (N'U'))
    DROP TABLE CHANNEL_ALERT;
	
CREATE TABLE CHANNEL_ALERT
	(CHANNEL_ID VARCHAR(255) NOT NULL,
	ALERT_ID VARCHAR(255) NOT NULL);
ALTER TABLE CHANNEL_ALERT
ADD CONSTRAINT ALERT_ID_CA_FK
FOREIGN KEY (ALERT_ID)
REFERENCES ALERT (ID) ON DELETE CASCADE;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'ALERT_EMAIL') AND type = (N'U'))
    DROP TABLE ALERT_EMAIL;

CREATE TABLE ALERT_EMAIL
	(ALERT_ID VARCHAR(255) NOT NULL,
	EMAIL VARCHAR(255) NOT NULL);
ALTER TABLE ALERT_EMAIL
ADD CONSTRAINT ALERT_ID_AE_FK
FOREIGN KEY (ALERT_ID)
REFERENCES ALERT(ID) ON DELETE CASCADE;

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'TRANSPORT') AND type = (N'U'))
    DROP TABLE TRANSPORT;

CREATE TABLE TRANSPORT
	(NAME VARCHAR(255) NOT NULL PRIMARY KEY,
	CLASS_NAME VARCHAR(255) NOT NULL,
	PROTOCOL VARCHAR(255) NOT NULL,
	TRANSFORMERS VARCHAR(255) NOT NULL,
	TYPE VARCHAR(255) NOT NULL);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'CONFIGURATION') AND type = (N'U'))
    DROP TABLE CONFIGURATION;

CREATE TABLE CONFIGURATION
	(ID INTEGER IDENTITY (0, 1) NOT NULL PRIMARY KEY,
	DATE_CREATED DATETIME DEFAULT GETDATE(),
	DATA TEXT NOT NULL);

IF EXISTS(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'ENCRYPTION_KEY') AND type = (N'U'))
    DROP TABLE ENCRYPTION_KEY;

CREATE TABLE ENCRYPTION_KEY
	(DATA TEXT NOT NULL);

INSERT INTO PERSON (USERNAME, PASSWORD, SALT, LOGGED_IN) VALUES('admin', 'NdgB6ojoGb/uFa5amMEyBNG16mE=', 'Np+FZYzu4M0=', 0);

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('FTP Reader', 'com.webreach.mirth.server.mule.providers.ftp.FtpConnector', 'ftp', 'ByteArrayToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('SFTP Reader', 'com.webreach.mirth.server.mule.providers.sftp.SftpConnector', 'sftp', 'ByteArrayToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('JMS Reader', 'com.webreach.mirth.server.mule.providers.jms.JmsConnector', 'jms', 'JMSMessageToObject ObjectToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('SOAP Listener', 'com.webreach.mirth.server.mule.providers.soap.axis.AxisConnector', 'axis', 'SOAPRequestToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('File Reader', 'com.webreach.mirth.server.mule.providers.file.FileConnector', 'file', 'ByteArrayToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Database Reader', 'com.webreach.mirth.server.mule.providers.jdbc.JdbcConnector', 'jdbc', 'ResultMapToXML', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('LLP Listener', 'com.webreach.mirth.server.mule.providers.mllp.MllpConnector', 'mllp', 'ByteArrayToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('TCP Listener', 'com.webreach.mirth.server.mule.providers.tcp.TcpConnector', 'tcp', 'ByteArrayToString', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Channel Reader', 'com.webreach.mirth.server.mule.providers.vm.VMConnector', 'vm', '', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('HTTP Listener', 'com.webreach.mirth.server.mule.providers.http.HttpConnector', 'http', 'HttpRequestToString HttpStringToXML', 'LISTENER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('FTP Writer', 'com.webreach.mirth.server.mule.providers.ftp.FtpConnector', 'ftp', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('SFTP Writer', 'com.webreach.mirth.server.mule.providers.sftp.SftpConnector', 'sftp', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('JMS Writer', 'com.webreach.mirth.server.mule.providers.jms.JmsConnector', 'jms', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('SOAP Sender', 'com.webreach.mirth.server.mule.providers.soap.axis.AxisConnector', 'axis', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Document Writer', 'com.webreach.mirth.server.mule.providers.doc.DocumentConnector', 'doc', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('File Writer', 'com.webreach.mirth.server.mule.providers.file.FileConnector', 'file', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Database Writer', 'com.webreach.mirth.server.mule.providers.jdbc.JdbcConnector', 'jdbc', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('LLP Sender', 'com.webreach.mirth.server.mule.providers.mllp.MllpConnector', 'mllp', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('TCP Sender', 'com.webreach.mirth.server.mule.providers.tcp.TcpConnector', 'tcp', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Channel Writer', 'com.webreach.mirth.server.mule.providers.vm.VMConnector', 'vm', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('Email Sender', 'com.webreach.mirth.server.mule.providers.email.SmtpConnector', 'smtp', '', 'SENDER');

INSERT INTO TRANSPORT (NAME, CLASS_NAME, PROTOCOL, TRANSFORMERS, TYPE) VALUES ('HTTP Sender', 'com.webreach.mirth.server.mule.providers.http.HttpConnector', 'http', '', 'SENDER');
