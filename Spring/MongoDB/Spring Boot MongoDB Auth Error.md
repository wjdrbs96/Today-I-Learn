# `Spring Boot MongoDB 연결시 권한 에러 해결`

## `gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```

<br>

## `application.yml`

```yaml
spring:
  data:
    mongodb:
      username: root
      password: root
      host: localhost
      port: 27017
```

처음에 위와 같이 yml 세팅을 하고 몽고디비 INSERT 하는 코드를 작성하고 요청을 보내면 아래와 같은 에러가 발생합니다.

![스크린샷 2021-05-23 오전 1 44 53](https://user-images.githubusercontent.com/45676906/119234396-a9852400-bb68-11eb-9b50-524fb3e30962.png)

![스크린샷 2021-05-23 오전 1 44 59](https://user-images.githubusercontent.com/45676906/119234400-abe77e00-bb68-11eb-9348-08492abeefe1.png)

```
com.mongodb.MongoCommandException: Command failed with error 17 (ProtocolError): 'Attempt to switch database target during SASL authentication.' on server localhost:27017. 
The full response is {"ok": 0.0, "errmsg": "Attempt to switch database target during SASL authentication.", "code": 17, "codeName": "ProtocolError"}
```

권한 에러가 뜬다고 뜨는데.. yml 파일을 아래와 같이 바꾸면 에러가 해결됩니다.

<br>

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://root:root@localhost:27017/yapp?authSource=admin
```

<img width="451" alt="스크린샷 2021-05-23 오전 1 47 25" src="https://user-images.githubusercontent.com/45676906/119234428-d46f7800-bb68-11eb-9c97-d57bc36e5a23.png">

서버도 잘 실행되고 디비에도 정상적으로 INSERT가 된 것도 확인할 수 있습니다.