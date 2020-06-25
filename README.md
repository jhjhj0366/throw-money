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
   ~~~

2. 받은 정보 테이블   
   ~~~sql
   ~~~


## Getting Started
- Compile
   ~~~bash
   ~~~
  
- Package
   ~~~bash
   ~~~

- Starting
   ~~~bash
   ~~~


## API URL

| Mtehod   |Path                              | Request                        | Response                                                      | Response                   |
|----------|:---------------------------------|:------------------------------:|:-------------------------------------------------------------:|----------------------------|
| POST     | /api/v1/money/throw              | throwAmount, receiverCount     | token                                                         | 뿌리기 API                   |
| POST     | /api/v1/money/receive            | token                          | receiveAmount                                                 | 받기 API                     |
| POST     | /api/v1/money/transactions       | token                          | throwDateTime, throwAmount, receiveTotalAmount, receiversInfo | 조회 API                     |
