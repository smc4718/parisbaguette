-- 시퀀스 삭제
DROP SEQUENCE USER_SEQ;
DROP SEQUENCE NOTICE_SEQ;
DROP SEQUENCE NOTICE_IMAGE_SEQ;

-- 시퀀스 생성
CREATE SEQUENCE USER_SEQ;
CREATE SEQUENCE NOTICE_SEQ;
CREATE SEQUENCE NOTICE_IMAGE_SEQ;

-- 테이블 삭제
DROP TABLE NOTICE_IMAGE_T;
DROP TABLE NOTICE_T;
DROP TABLE USER_T;

-- 회원 테이블
CREATE TABLE USER_T (
	USER_NO	    NUMBER	            NOT NULL,
	EMAIL	    VARCHAR2(100 BYTE)	NOT NULL,
	PW	        VARCHAR2(64 BYTE)	NOT NULL,
	NAME	    VARCHAR2(50 BYTE)	NULL,
	GENDER	    VARCHAR2(2 BYTE)	NULL,
	MOBILE	    VARCHAR2(15 BYTE)	NULL,
	JOINED_AT	DATE	            NULL
);

-- 공지
CREATE TABLE NOTICE_T (
	NOTICE_NO	NUMBER	            NOT NULL,
	USER_NO	    NUMBER	            NOT NULL,
	TITLE	    VARCHAR2(500 BYTE)	NOT NULL,
	CONTENTS	CLOB	            NULL,
	HIT	        NUMBER	            NULL,
	CREATED_AT	VARCHAR2(30 BYTE)	NULL
);

-- 공지 이미지
CREATE TABLE NOTICE_IMAGE_T (
	NOTICE_NO	        NUMBER	            NOT NULL,
	IMAGE_PATH	        VARCHAR2(100 BYTE)	NULL,
	FILESYSTEM_NAME 	VARCHAR2(100 BYTE)	NULL
);

-- PK
ALTER TABLE USER_T ADD CONSTRAINT PK_USER_T PRIMARY KEY (USER_NO);

ALTER TABLE NOTICE_T ADD CONSTRAINT PK_NOTICE_T PRIMARY KEY (NOTICE_NO);

-- FK
ALTER TABLE NOTICE_T ADD CONSTRAINT FK_USER_T_TO_NOTICE_T_1 FOREIGN KEY (USER_NO) 
REFERENCES USER_T (USER_NO) ON DELETE CASCADE;

ALTER TABLE NOTICE_IMAGE_T ADD CONSTRAINT FK_NOTICE_T_TO_NOTICE_IMAGE_T_1 FOREIGN KEY (NOTICE_NO) 
REFERENCES NOTICE_T (NOTICE_NO) ON DELETE CASCADE;

------ INSERT ------

INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'pb@naver.com', STANDARD_HASH('1735', 'SHA256'), 'pb마스터', 'M', 01013134545, TO_DATE('250207', 'YYMMDD'));