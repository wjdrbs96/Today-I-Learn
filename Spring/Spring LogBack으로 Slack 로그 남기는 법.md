# `Spring LogBack으로 Slack Log 남기는 법`

이번 글에서는 `Spring LogBack`을 사용해서 Slack 채널에 로그를 보내는 법에 대해서 정리할 것입니다. 먼저 로그를 보내고자 하는 채널의 `WebhookUrl`을 받아오겠습니다.

<img width="1039" alt="1 오후 5 36 54" src="https://user-images.githubusercontent.com/45676906/117268732-96006a80-ae92-11eb-8126-b38a2c6969e1.png">

Slack에서 해당 로그가 찍히도록 할 채널을 만들고 들어가겠습니다. 그러면 위와 같이 오른쪽 위에 `느낌표` 같은 표시가 존재합니다. 

<br>

![2 오후 5 36 54](https://user-images.githubusercontent.com/45676906/117268967-cfd17100-ae92-11eb-90d3-f9a8b077aea8.png)

그리고 `앱 추가`를 누르겠습니다. 

<br>

![3](https://user-images.githubusercontent.com/45676906/117269074-ed9ed600-ae92-11eb-8c5a-172aecef353f.png)

그리고 `Incoming WebHooks`를 검색해보면 위와 같이 뜰 것입니다. 여기서 `설치` 버튼을 누르겠습니다. 

<br>

![4](https://user-images.githubusercontent.com/45676906/117269205-0b6c3b00-ae93-11eb-8714-a4218ed1a069.png)

그러면 위와 같은 화면이 뜰텐데 `Slack에 추가` 버튼을 누르겠습니다. 

<br>

![5](https://user-images.githubusercontent.com/45676906/117269406-348ccb80-ae93-11eb-92c5-8f2dcf19c779.png)

위의 화면에서도 채널 이름이 맞는지 확인한 후에 `수신 웹후크 통한 앱 추가`를 누르겠습니다.

<br>

![6](https://user-images.githubusercontent.com/45676906/117269621-67cf5a80-ae93-11eb-9fb8-e031f2c53cda.png)

그러면 위와 같이 `웹 후크 URL`이 생성이 되는데요. 이것을 복사해서 가지고 있겠습니다. (아래에서 사용합니다.)

<br>

![7](https://user-images.githubusercontent.com/45676906/117269812-91888180-ae93-11eb-86ba-e1fe91e43927.png)

그리고 마지막으로 저장을 누르면 `Slack WebHooks URL` 얻는 것까지는 완료했습니다. 이제 Spring 코드에서 Log 찍히는 것을 Slack에 저장하는 것을 해보겠습니다. 

<br>

## `gradle`

```
implementation 'com.github.maricn:logback-slack-appender:1.4.0'
```

<br>

## `maven`

```
<dependency>
    <groupId>com.github.maricn</groupId>
    <artifactId>logback-slack-appender</artifactId>
    <version>1.4.0</version>
</dependency>
```

가장 처음으로 위의 의존성을 추가하겠습니다. 저는 `gradle` 프로젝트를 만들어서 진행해보겠습니다. 

![스크린샷 2021-05-12 오후 1 50 04](https://user-images.githubusercontent.com/45676906/117919971-00009000-b329-11eb-94c8-5dfa64196bd3.png)

위에서 복사했던 `WebHook Url`을 `application.yml`에 설정하겠습니다.

<br>

### `application.yml`


![스크린샷 2021-05-12 오후 1 53 32](https://user-images.githubusercontent.com/45676906/117920212-71404300-b329-11eb-9bb5-537cd0a7532b.png)

```yaml
logging:
  slack:
    webhook-uri: 위에서 발급받은 웹 훅 URL
  config: classpath:logback-spring.xml
```

gradle 프로젝트를 만든 후에 `resources` 디렉토리 아래에 `logback-spring.xml` 이라는 파일을 하나 만들겠습니다. 그리고 `logback-spring.xml` 설정을 하겠습니다. 

<br>

## `logback-spring.xml 설정하기`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <springProperty name="SLACK_WEBHOOK_URI" source="logging.slack.webhook-uri"/>
    <appender name="SLACK" class="com.github.maricn.logback.SlackAppender">
        <webhookUri>${SLACK_WEBHOOK_URI}</webhookUri>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %msg %n</pattern>
        </layout>
        <username>Cake-Server-log</username>
        <iconEmoji>:stuck_out_tongue_winking_eye:</iconEmoji>
        <colorCoding>true</colorCoding>
    </appender>

    <!-- Consol appender 설정 -->
    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <Pattern>%d %-5level %logger{35} - %msg%n</Pattern>
        </encoder>
    </appender>

    <appender name="ASYNC_SLACK" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="SLACK"/>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
    </appender>

    <root level="INFO">
        <appender-ref ref="Console" />
        <appender-ref ref="ASYNC_SLACK"/>
    </root>
</configuration>
```

전체 코드는 위와 같습니다. 하나씩 어떤 의미인지 간단하게 알아보겠습니다. 

<br>

![스크린샷 2021-05-12 오후 2 05 13](https://user-images.githubusercontent.com/45676906/117921286-71d9d900-b32b-11eb-96b2-e2ed0b0da4c1.png)

- `webhookUrl`: application.yml 등록한 webhookUrl이 ${SLACK_WEBHOOK_URI}에 값이 들어가게 됩니다.
- 나머지 설정들은 자유롭게 하면 되는데 저는 `시간`, `이모지`, `이름`이 슬랙 채널에 뜨도록 하고 싶어서 위와 같이 설정을 하였습니다.
- `pattern`을 통해서 해당 로그가 어떠한 형식으로 적을지 정할 수 있습니다. 

<br>

![스크린샷 2021-05-12 오후 2 15 13](https://user-images.githubusercontent.com/45676906/117922000-b0bc5e80-b32c-11eb-8d86-b7e35f001213.png)

그리고 위와 같이 어떤 로그를 슬랙 채널에 알림을 보내고 세세하게 관리를 할 수 있습니다. 저는 `log.error()`의 로그만 슬랙 채널에 보내도록 위와 같이 설정하였습니다. Console에 대한 설정은 굳이 하지 않아도 되긴 하는데 그러면 Spirng Boot를 실행했을 때 아무 로그가 뜨지를 않아서.. 설정해놓았습니다.


<br>

## `Controller 작성`

```java
@Slf4j
@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        log.error("Gyunny Spring Study");
        log.info("Bobae is Babo");
        log.debug("MARU Server");
        return "test";
    }
}
```

위와 같이 `error`, `info`, `debug`로 3가지의 로그를 출력하도록 하고 `http://localhost:8080`을 호출해보겠습니다. 

<br>

![스크린샷 2021-05-12 오후 1 58 38](https://user-images.githubusercontent.com/45676906/117920655-37237100-b32a-11eb-83a2-40f2d3546500.png)

그러면 저는 `logback-spring.xml`에 `Error`만 출력되도록 했기 때문에 `Slack Channel`에 위와 같이 `log.error()`만 알람이 가는 것을 볼 수 있습니다.