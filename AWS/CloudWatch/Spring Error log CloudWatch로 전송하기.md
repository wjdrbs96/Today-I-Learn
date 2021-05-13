# `Spring Error log CloudWatch로 전송하는 법`

서버를 관리하게 되면 가장 많이 하는 일 중 하나가 `로그`를 읽는 작업이라고도 할 수 있습니다. 로그는 그 당시 어떤 일이 일어났는지 확인할 수 있게 해주는 중요한 단서이기 때문에 문제가 생겼을 때 해결하는데 많은 도움이 됩니다.
그렇기 때문에 이런 로그들은 반드시 기록하고 있어야 하며 일정 기간 동안 유실되지 않도록 잘 관리해야 합니다. 

또한 모든 로그를 다 기록할 순 없기 때문에 필요한 로그들만 잘 기록해야 하고, 많은 로그들 중에서 손쉽게 필요한 로그들만 찾을 수 있도록 관리할 방법들도 필요한데요. AWS에서는 이러한 기능들을 편리하게 사용할 수 있도록 `CloudWatch`라는 서비스를 제공해주고 있습니다.
`CloudWatch Agent`가 로그도 모니터링해서 `CloudWatch logs`로 전송하는 역할도 하고 있습니다. 

그래서 이번 글에서는 `CloudWatch`와 `Spring Logback`을 사용해서 `Spring Error log`를 CloudWatch Log Group 으로 전송하는 법에 대해서 정리 해보겠습니다.

이번 글의 실습을 진행하기 위해서는 `반드시` [CloudWatch Agent 설치 및 IAM 설정](https://devlog-wjdrbs96.tistory.com/326) 여기서 `CloudWatch Agent`를 설치하고 오셔야 합니다.

<br>

## `Spring 설정하기`

Spring에서 `CloudWatch`로 Error log를 전송할 수 있도록 정말 편리하게 제공해주는 [라이브러리](https://github.com/pierredavidbelanger/logback-awslogs-appender) 가 있습니다. 해당 라이브러리를 사용하려면 아래의 의존성을 추가해주어야 합니다.

<br>

### `gradle`

```
compile group: 'ca.pjer', name: 'logback-awslogs-appender', version: '1.0.0'
```

<br>

### `maven`

```
<dependency>
    <groupId>ca.pjer</groupId>
    <artifactId>logback-awslogs-appender</artifactId>
    <version>1.4.0</version>
</dependency>
```

위의 의존성을 추가하고 프로젝트 세팅을 해보겠습니다. 

<br>

![스크린샷 2021-05-13 오전 10 57 00](https://user-images.githubusercontent.com/45676906/118066771-4661f780-b3da-11eb-8e7e-cd21bbbd3cb8.png)

저의 프로젝트 구조는 위와 같습니다. 가장 중요한 파일은 `logback.xml` 파일 인데요. `resources` 폴더 아래에 `logback.xml` 파일을 만드시면 됩니다. 

<br>

## `logback.xml`

```xml
<configuration packagingData="true">

    <!-- Register the shutdown hook to allow logback to cleanly stop appenders -->
    <!-- this is strongly recommend when using AwsLogsAppender in async mode, -->
    <!-- to allow the queue to flush on exit -->
    <shutdownHook class="ch.qos.logback.core.hook.DelayingShutdownHook"/>

    <!-- Timestamp used into the Log Stream Name -->
    <timestamp key="timestamp" datePattern="yyyyMMddHHmmssSSS"/>

    <!-- The actual AwsLogsAppender (asynchronous mode because of maxFlushTimeMillis > 0) -->
    <appender name="ASYNC_AWS_LOGS" class="ca.pjer.logback.AwsLogsAppender">

        <!-- Send only WARN and above -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>

        <!-- Nice layout pattern -->
        <layout>
            <pattern>%d{yyyyMMdd'T'HHmmss} %thread %level %logger{15} %msg%n</pattern>
        </layout>

        <!-- Hardcoded Log Group Name -->
        <logGroupName>Spring-log</logGroupName>

        <!-- Log Stream Name UUID Prefix -->
        <logStreamUuidPrefix>mystream/</logStreamUuidPrefix>

        <!-- Hardcoded AWS region -->
        <!-- So even when running inside an AWS instance in us-west-1, logs will go to us-west-2 -->
        <logRegion>ap-northeast-2</logRegion>

        <!-- Maximum number of events in each batch (50 is the default) -->
        <!-- will flush when the event queue has 50 elements, even if still in quiet time (see maxFlushTimeMillis) -->
        <maxBatchLogEvents>50</maxBatchLogEvents>

        <!-- Maximum quiet time in millisecond (0 is the default) -->
        <!-- will flush when met, even if the batch size is not met (see maxBatchLogEvents) -->
        <maxFlushTimeMillis>30000</maxFlushTimeMillis>

        <!-- Maximum block time in millisecond (5000 is the default) -->
        <!-- when > 0: this is the maximum time the logging thread will wait for the logger, -->
        <!-- when == 0: the logging thread will never wait for the logger, discarding events while the queue is full -->
        <maxBlockTimeMillis>5000</maxBlockTimeMillis>

        <!-- Retention value for log groups, 0 for infinite see -->
        <!-- https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutRetentionPolicy.html for other -->
        <!-- possible values -->

        <retentionTimeDays>0</retentionTimeDays>
    </appender>

    <!-- A console output -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyyMMdd'T'HHmmss} %thread %level %logger{15} %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Root with a threshold to INFO and above -->
    <root level="INFO">
        <!-- Append to the console -->
        <appender-ref ref="STDOUT"/>
        <!-- Append also to the (async) AwsLogsAppender -->
        <appender-ref ref="ASYNC_AWS_LOGS"/>
    </root>
</configuration>
```

전체의 코드는 위와 같은데요. 이 중에서 자세히 볼 부분에 대해서만 정리를 해보겠습니다. 

<br>

![스크린샷 2021-05-13 오전 11 02 19](https://user-images.githubusercontent.com/45676906/118067159-fdf70980-b3da-11eb-8946-fab23e2a9f0a.png)

이번 글에서는 이정도의 설정만 보면 될 거 같고 자세히 더 알고 싶다면 [여기](https://github.com/pierredavidbelanger/logback-awslogs-appender) 를 참고하시면 될 것 같습니다. 그리고 `Spring logback` 관련으로 찾아서 보아도 좋을 거 같습니다.

<br>

## `Controller 만들기`

![스크린샷 2021-05-13 오전 11 06 15](https://user-images.githubusercontent.com/45676906/118067319-46162c00-b3db-11eb-9ddb-8742be9853e8.png)

```java
@Slf4j
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        log.error("에러입니다!");
        return "hello";
    }
}
```

저는 위와 같이 해당 API가 호출되면 `error log`를 출력하도록 만들어놓았습니다. 이게 설정 끝입니다! 정말 간단한 것 같습니다.(라이브러리의 힘...) 이제 프로젝트 jar를 만들어서 EC2로 업로드 해보겠습니다.

<br>

## `Spring jar 만들기`

```
./gradlew clean build
```

![스크린샷 2021-05-13 오전 11 08 37](https://user-images.githubusercontent.com/45676906/118067496-a1481e80-b3db-11eb-86b0-80ec7c71eee1.png)

그러면 위와 같이 `jar` 파일이 만들어지는데요. 저는 이것을 `Filezila`를 사용해서 `EC2`에 올리겠습니다. 간단하게 Filezila는 어떻게 사용하는지에 대해서도 정리해보겠습니다.

<br>

### `Filezila 사용법`

![스크린샷 2021-05-13 오전 11 10 34](https://user-images.githubusercontent.com/45676906/118067776-2e8b7300-b3dc-11eb-804f-b82c853745b1.png)

![스크린샷 2021-05-13 오전 11 13 41](https://user-images.githubusercontent.com/45676906/118067914-6eeaf100-b3dc-11eb-9ae5-7fd5ca0a9984.png)

위와 같이 하면 `EC2`로 jar 파일이 전송됩니다.

<br>

![스크린샷 2021-05-13 오전 11 15 55](https://user-images.githubusercontent.com/45676906/118068022-98a41800-b3dc-11eb-8ceb-b17a72f39c7f.png)

그리고 EC2에 접속해서 확인해보면 위와 같이 jar 파일이 존재하는 것을 확인할 수 있습니다. 바로 jar를 실행시키겠습니다.

<br>

```
sudo amazon-linux-extras install java-openjdk11 (제가 설치한 자바 11버전 명령어)
sudo nohup java -jar *.jar &
```

혹시 EC2에 자바가 설치되지 않았다면 java를 설치한 후에 위의 명령어를 사용하셔야 합니다.(저는 EC2 Linux2 버전입니다. java 8을 설치하고 싶다면 [여기](https://jojoldu.tistory.com/261) 를 참고하면 좋습니다.)

<br>

![스크린샷 2021-05-13 오전 11 19 15](https://user-images.githubusercontent.com/45676906/118068307-1a944100-b3dd-11eb-8e5b-cdede5ea6985.png)

```
sudo netstat -tnlp
```

그러면 위의 명령어로 확인해보면 jar가 실행가 8080 포트에서 잘 실행되고 있는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-05-13 오전 11 21 03](https://user-images.githubusercontent.com/45676906/118068443-57f8ce80-b3dd-11eb-9d67-9a6185778f6f.png)

그리고 해당 주소로 접속해보면 위와 같이 `Controller`에서 만든 대로 잘 응답이 오는 것도 확인할 수 있습니다. 위의 API 호출이 되었기 때문에 `log.error()`로 출력했던 로그가 `CloudWatch`로 전송이 되었을 것입니다. 
정말 잘 되었는지 확인을 한번 해보겠습니다.

<br>

![스크린샷 2021-05-13 오전 11 22 44](https://user-images.githubusercontent.com/45676906/118068592-95f5f280-b3dd-11eb-9af2-b441694ece45.png)

저는 위와 같이 CloudWatch에 `Spring-log` 라는 로그그룹이 존재하는데 여기로 log를 전송했습니다.

<br>

![스크린샷 2021-05-13 오전 11 24 37](https://user-images.githubusercontent.com/45676906/118068711-cdfd3580-b3dd-11eb-9daa-337d1c0c6ac3.png)

제가 몇번 호출했던 결과들이 `CloudWatch` 로그 그룹에 잘 출력이 되는 것을 확인할 수 있습니다.