-- 시퀀스 삭제
DROP SEQUENCE USER_SEQ;
DROP SEQUENCE NOTICE_SEQ;
DROP SEQUENCE NOTICE_IMAGE_SEQ;
DROP SEQUENCE RESERVATION_SEQ;

-- 시퀀스 생성
CREATE SEQUENCE USER_SEQ NOCACHE;
CREATE SEQUENCE NOTICE_SEQ NOCACHE;
CREATE SEQUENCE NOTICE_IMAGE_SEQ NOCACHE;
CREATE SEQUENCE RESERVATION_SEQ NOCACHE;

-- 테이블 삭제
DROP TABLE NOTICE_IMAGE_T;
DROP TABLE NOTICE_T;
DROP TABLE RESERVATION_T;
DROP TABLE USER_T;

-- 회원 테이블
CREATE TABLE USER_T (
	USER_NO	        NUMBER	            NOT NULL,
    ID              VARCHAR2(20 BYTE)	NOT NULL UNIQUE,
	PW	            VARCHAR2(64 BYTE)	NOT NULL,
	NAME	        VARCHAR2(50 BYTE),
	GENDER	        VARCHAR2(2 BYTE),
    EMAIL           VARCHAR2(50 BYTE),
	HP	            VARCHAR2(20 BYTE),
    HP_SMS_YN       VARCHAR2(20 BYTE),
	JOINED_AT	    DATE
);

-- 공지
CREATE TABLE NOTICE_T (
	NOTICE_NO	NUMBER	            NOT NULL,
    USER_NO	    NUMBER	            NOT NULL,
	TITLE	    VARCHAR2(500 BYTE)	NOT NULL,
	CONTENTS	CLOB,
	HIT	        NUMBER              DEFAULT 0,
	CREATED_AT	VARCHAR2(30 BYTE)
);

-- 공지 이미지
CREATE TABLE NOTICE_IMAGE_T (
	NOTICE_NO	        NUMBER	            NOT NULL,
	IMAGE_PATH	        VARCHAR2(100 BYTE),
	FILESYSTEM_NAME 	VARCHAR2(100 BYTE)
);

-- 휴일 예약
CREATE TABLE RESERVATION_T (
    RESERVATION_NO   NUMBER             PRIMARY KEY,
    USER_NO          NUMBER             NOT NULL,
    RESERVATION_DATE DATE               NOT NULL,
    STATUS           VARCHAR2(20 BYTE)  DEFAULT 'PENDING', -- 예약 상태(PENDING: 대기, APPROVED: 승인, REJECTED: 거절)
    CREATED_AT       TIMESTAMP DEFAULT  SYSTIMESTAMP
);

-- PK
ALTER TABLE USER_T ADD CONSTRAINT PK_USER_T PRIMARY KEY (USER_NO);

ALTER TABLE NOTICE_T ADD CONSTRAINT PK_NOTICE_T PRIMARY KEY (NOTICE_NO);

-- FK

ALTER TABLE NOTICE_IMAGE_T ADD CONSTRAINT FK_NOTICE_T_TO_NOTICE_IMAGE_T_1 FOREIGN KEY (NOTICE_NO) 
REFERENCES NOTICE_T (NOTICE_NO) ON DELETE CASCADE;

ALTER TABLE NOTICE_T ADD CONSTRAINT FK_USER_T_TO_NOTICE_T_1 FOREIGN KEY (USER_NO) 
REFERENCES USER_T (USER_NO) ON DELETE CASCADE;

ALTER TABLE RESERVATION_T ADD CONSTRAINT FK_RESERVATION_USER FOREIGN KEY (USER_NO) 
REFERENCES USER_T (USER_NO) ON DELETE CASCADE;


------ INSERT ------

-- 반드시 모든 sql쿼리 하나라도 수정하면, 전체 쿼리 F5로 재실행 해야함.
-- 이전에 실행했던 쿼리에 수정 전 쿼리가 있으면, 실제 웹에서 동작시 에러남.

INSERT INTO USER_T VALUES(1, 'a', STANDARD_HASH('1', 'SHA256'), 'PB마스터', 'M', 'pb@naver.com', '01013134545', 'Y', TO_DATE('250207', 'YYMMDD'));
INSERT INTO USER_T VALUES(2, 'b', STANDARD_HASH('2', 'SHA256'), 'PB알바', 'Y', 'pbAlba@naver.com', '01034345656', 'Y', TO_DATE('250309', 'YYMMDD'));

COMMIT;