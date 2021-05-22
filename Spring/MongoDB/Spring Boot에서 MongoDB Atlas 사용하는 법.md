# `Spring Boot에서 MongoDB Atlas 사용하는 법`

이번 글에서는 `Spring Boot`로 `MongoDB` Atlas를 사용하는 법에 대해서 정리해보려 합니다. 먼저 [https://www.mongodb.com/cloud/atlas1](https://www.mongodb.com/cloud/atlas1) MongoDB Atlas 사이트에 먼저 접속하겠습니다.

![스크린샷 2021-05-17 오전 10 47 05](https://user-images.githubusercontent.com/45676906/118421998-4117ec00-b6fd-11eb-8cbe-aab725f707c2.png)

`MongoDB Atlas`를 사용하기 위해서는 `회원가입`을 먼저 해야 합니다.

<br>

![스크린샷 2021-05-17 오전 10 48 43](https://user-images.githubusercontent.com/45676906/118422102-889e7800-b6fd-11eb-9c78-a5624763c5c5.png)

위에 자기의 정보에 맞게 입력 후에 회원가입을 하겠습니다.

<br>

![스크린샷 2021-05-17 오전 10 51 00](https://user-images.githubusercontent.com/45676906/118422257-d61ae500-b6fd-11eb-8899-8d490b656c9c.png)

저는 자바를 사용할 것이기 때문에 Java를 선택하고 다음을 누르겠습니다.

<br>

![스크린샷 2021-05-17 오전 10 51 43](https://user-images.githubusercontent.com/45676906/118422438-2e51e700-b6fe-11eb-9d3f-653ac47fa474.png)

![스크린샷 2021-05-17 오전 10 55 01](https://user-images.githubusercontent.com/45676906/118422559-68bb8400-b6fe-11eb-887a-c0fbad4000fc.png)

무료 버전을 사용할 것이기 때문에 `FREE`를 선택하고 나머지는 기본 설정대로 두고 만들겠습니다.

<br>

![스크린샷 2021-05-17 오전 11 00 11](https://user-images.githubusercontent.com/45676906/118423010-1a5ab500-b6ff-11eb-981c-c96f85683e8d.png)

그리고 1~3분 정도 기다리면 클러스터가 하나 생성이 됩니다. 생성이 되었다면 `Connect`를 눌러서 DB User 계정을 생성하겠습니다.

<br>

![스크린샷 2021-05-17 오전 11 08 45](https://user-images.githubusercontent.com/45676906/118423709-91dd1400-b700-11eb-8a68-974f435adff6.png)

그리고 위에서 유저 계정을 하나 만들겠습니다. 이것은 디비에 접속할 때 중요한 정보이기 때문에 본인이 잘 기억할 수 있고 안전하게 정해서 만드는 것을 추천드립니다.

<br>

![스크린샷 2021-05-17 오전 11 23 22](https://user-images.githubusercontent.com/45676906/118424508-55aab300-b702-11eb-83f4-a4ca8cffcb7e.png)

![스크린샷 2021-05-17 오전 11 24 02](https://user-images.githubusercontent.com/45676906/118424568-6b1fdd00-b702-11eb-928a-dba1e2af0a4c.png)

<br>

![스크린샷 2021-05-17 오전 11 26 23](https://user-images.githubusercontent.com/45676906/118424788-d1a4fb00-b702-11eb-9b1e-77cab4f497b6.png)

그리고 `Java`를 사용할 것이기 때문에 자바를 선택하고 `복사 버튼`을 누르겠습니다.(application.yml 에서 사용할 예정)

```
mongodb+srv://<UserName>:<password>@<클러스터이름>.ergif.mongodb.net/<dbName>?retryWrites=true&w=majority
```


<br>

## `IP 등록하기`

![스크린샷 2021-05-17 오전 11 28 10](https://user-images.githubusercontent.com/45676906/118424894-0618b700-b703-11eb-9ea2-9733c8ee011b.png)

MongoDB Atlas는 네트워크 설정에 등록되어 있는 IP만 접근할 수 있습니다. 그래서 위와 같이 IP 주소를 등록하는 과정이 필요합니다.

<br>

![스크린샷 2021-05-17 오후 12 01 07](https://user-images.githubusercontent.com/45676906/118428884-64499800-b70b-11eb-9330-ce539776ed10.png)

위의 보이는 것처럼 클릭을 하면 본인이 사용하고 있는 IP가 자동으로 뜰 것입니다. `ADD IP Address`를 누르겠습니다.

<br>

## `데이터베이스 생성`

![스크린샷 2021-05-23 오전 2 20 41](https://user-images.githubusercontent.com/45676906/119235440-827d2100-bb6d-11eb-8aa1-92fb4d692747.png)

![스크린샷 2021-05-23 오전 2 22 10](https://user-images.githubusercontent.com/45676906/119235485-c07a4500-bb6d-11eb-9cc3-9151fd52fef7.png)

![스크린샷 2021-05-23 오전 2 23 05](https://user-images.githubusercontent.com/45676906/119235511-ddaf1380-bb6d-11eb-8a89-fcfa243f4123.png)

본인이 사용할 데이터베이스, 컬렉션 이름을 정하고 생성을 누르겠습니다.

<br>

## `Spring Boot 간단한 예제`

![스크린샷 2021-05-23 오전 2 56 07](https://user-images.githubusercontent.com/45676906/119236455-7cd60a00-bb72-11eb-88c1-5c05be33c8a3.png)

현재 제가 만든 프로젝트의 구조는 위와 같습니다. 먼저 `MongoDB 의존성`을 추가하겠습니다.

<br>

### `build.gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```

그리고 `application.yml`에 DB 설정을 하겠습니다.

<br>

### `application.yml`

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<유저이름>:<비밀번호>@<클러스터이름>.ergif.mongodb.net/<데이터베이스이름>?retryWrites=true&w=majority
```

uri에 위에서 만든 `유저이름`, `비밀번호`, `클러스터이름`, `데이터베이스이름`을 자신의 정보에 맞게 넣어주면 됩니다.

<br>

## `Document`

```java
@AllArgsConstructor
@Getter
@Document("alarm_log")
public class AlarmLog {
    @Id
    private Long id;
    private String title;
    private String message;
}
```

그리고 위에서 만든 `컬렉션`과 해당 클래스가 매칭되도록 간단하게 만들었습니다. 이것을 가지고 `MongoDB Atlas`에 INSERT를 할 것입니다.

<br>

## `Service`

```java
@RequiredArgsConstructor
@Service
public class UserService {

    private final MongoTemplate mongoTemplate;

    public void mongoInsert() {
        AlarmLog alarmLog = new AlarmLog(1L, "제목", "메세지");
        mongoTemplate.insert(alarmLog);
    }
}
```

그리고 `MongoTemplate`를 사용해서 `AlarmLog` 클래스를 INSERT 하는 아주 간단한 코드를 작성했습니다. 실제로 위의 코드를 실행하면 아래와 같이 잘 INSERT 되는 것도 확인할 수 있습니다.

<br>

![스크린샷 2021-05-23 오전 2 30 50](https://user-images.githubusercontent.com/45676906/119236636-82801f80-bb73-11eb-9fb3-2dd4fcd3df12.png)

자세한 코드가 궁금하다면 [Github](https://github.com/wjdrbs96/Spring_MongoDB_Atlas) 에서 확인할 수 있습니다.

<br>

## `에러 정리`

![스크린샷 2021-05-23 오전 2 26 43](https://user-images.githubusercontent.com/45676906/119235756-0b488c80-bb6f-11eb-8673-b39ee9651e98.png)

![스크린샷 2021-05-23 오전 2 27 14](https://user-images.githubusercontent.com/45676906/119235761-0e437d00-bb6f-11eb-8ec3-c73afea8bc25.png)

접속하다가 위와 같은 에러를 만난다면 `atlas`에서 `IP 주소`를 등록하지 않아서 그렇습니다.

<br>

![스크린샷 2021-05-23 오전 2 33 20](https://user-images.githubusercontent.com/45676906/119235850-5d89ad80-bb6f-11eb-98c7-fd210a296cf3.png)

위와 같이 네트워크 설정에서 IP 등록이 되어 있어야 합니다.

