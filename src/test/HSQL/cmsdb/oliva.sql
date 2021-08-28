CREATE SCHEMA PUBLIC AUTHORIZATION DBA
CREATE MEMORY TABLE ACCOUNTS(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,USER_ID BIGINT DEFAULT 0 NOT NULL,DOMAIN_ID BIGINT DEFAULT 0 NOT NULL,ACCOUNT VARCHAR(255) DEFAULT '' NOT NULL,COMMENT VARCHAR(255) DEFAULT '' NOT NULL)
CREATE MEMORY TABLE ADDRESSES(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,USER_ID BIGINT DEFAULT 0 NOT NULL,NAME VARCHAR(255) DEFAULT '' NOT NULL,EMAIL VARCHAR(255) DEFAULT '' NOT NULL,COMMENT VARCHAR(255) DEFAULT '' NOT NULL)
CREATE MEMORY TABLE USERS(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,NAME VARCHAR(255) DEFAULT '' NOT NULL,COMMENT VARCHAR(255) DEFAULT '' NOT NULL)
CREATE MEMORY TABLE DOMAINS(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,NAME VARCHAR(255) DEFAULT 'bossmail.me' NOT NULL,COMMENT VARCHAR(255) DEFAULT '' NOT NULL)
ALTER TABLE ACCOUNTS ALTER COLUMN ID RESTART WITH 2
ALTER TABLE ADDRESSES ALTER COLUMN ID RESTART WITH 8
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 2
ALTER TABLE DOMAINS ALTER COLUMN ID RESTART WITH 1
CREATE USER SA PASSWORD ""
GRANT DBA TO SA
SET WRITE_DELAY 20
SET SCHEMA PUBLIC
INSERT INTO ACCOUNTS VALUES(0,0,0,'grabko','CREATED AUTOMATICALLY')
INSERT INTO ACCOUNTS VALUES(1,1,0,'berkyt','CREATED AUTOMATICALLY')
INSERT INTO ADDRESSES VALUES(2,0,'Kostya Grabko','grabko@mail.ru','Test ')
INSERT INTO ADDRESSES VALUES(3,1,'\u0437\u0438\u043d\u0443\u0440','zinnur-aminaev','')
INSERT INTO ADDRESSES VALUES(4,1,'\u0437\u0438\u043d\u0443\u0440','zinnur-aminaev@mail.ru','')
INSERT INTO ADDRESSES VALUES(5,1,'\u0437\u0438\u043d\u0443\u0440','zinnur-aminaev@mail.ru','')
INSERT INTO USERS VALUES(0,'grabko@bossmail.me','CREATED AUTOMATICALLY')
INSERT INTO USERS VALUES(1,'berkyt@bossmail.me','CREATED AUTOMATICALLY')
INSERT INTO DOMAINS VALUES(0,'bossmail.me','CREATED AUTOMATICALLY')
