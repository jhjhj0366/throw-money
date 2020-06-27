Throw Money Service
===================================
> 돈 뿌리기 서비스


### Prerequisites   
- JDK 1.8   
- Spring Boot 2.3   
    - JPA   
    - Lombok   
    - caffeine
- Gradle   
- H2   


## Structure
### Table Structure
1. 거래 정보 테이블   
   ~~~sql
   CREATE TABLE TRANSACTION (
     ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     SEND_USER_ID INT NOT NULL,
     ROOM_ID VARCHAR(200) NOT NULL,
     TOKEN CHAR(3) NOT NULL,
     THROWAMOUNT INT NOT NULL,
     THROW_DATE_TIME TIMESTAMP,
     RECEIVER_COUNT INT NOT NULL,
     USER_ID INT
   )
   ~~~


2. 받은 정보 테이블   
   ~~~sql
   CREATE TABLE RECEIVER (
    ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TOKEN CHAR(3) NOT NULL,
    RECEIVE_USER_ID INT NOT NULL,
    RECEIVE_AMT INT NOT NULL       
   )
   ~~~


## Getting Started
- Compile
   ~~~bash
   gradle complie
  
- Package
   ~~~bash
   gradle jar

- Startings
   ~~~bash
   gradle run

## API URL

| Mtehod   |Path                              | Request                        | Response                                                      | Response                   |
|----------|:---------------------------------|:------------------------------:|:-------------------------------------------------------------:|----------------------------|
| POST     | /api/v1/money/throw              | throwAmount, receiverCount     | token                                                         | 뿌리기 API                   |
| POST     | /api/v1/money/receive            | token                          | receiveAmount                                                 | 받기 API                     |
| POST     | /api/v1/money/transactions       | token                          | throwDateTime, throwAmount, receiveTotalAmount, receiversInfo | 조회 API                     |
