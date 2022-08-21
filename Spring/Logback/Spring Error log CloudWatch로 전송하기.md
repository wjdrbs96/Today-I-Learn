# `Spring Error log CloudWatch로 전송하는 법`

서버를 관리하게 되면 가장 많이 하는 일 중 하나가 `로그`를 읽는 작업이라고도 할 수 있습니다. 로그는 그 당시 어떤 일이 일어났는지 확인할 수 있게 해주는 중요한 단서이기 때문에 문제가 생겼을 때 해결하는데 많은 도움이 됩니다.
그렇기 때문에 이런 로그들은 반드시 기록하고 있어야 하며 일정 기간 동안 유실되지 않도록 잘 관리해야 합니다. 

또한 모든 로그를 다 기록할 순 없기 때문에 필요한 로그들만 잘 기록해야 하고, 많은 로그들 중에서 손쉽게 필요한 로그들만 찾을 수 있도록 관리할 방법들도 필요한데요. AWS에서는 이러한 기능들을 편리하게 사용할 수 있도록 `CloudWatch`라는 서비스를 제공해주고 있습니다.
`CloudWatch Agent`가 로그도 모니터링해서 `CloudWatch logs`로 전송하는 역할도 하고 있습니다. 

그래서 이번 글에서는 `CloudWatch`와 `Spring Logback`을 사용해서 `Spring Error log`를 CloudWatch Log Group 으로 전송하는 법에 대해서 정리 해보겠습니다.

<br>

## `Spring 설정하기`

Spring에서 `CloudWatch`로 Error log를 전송할 수 있도록 정말 편리하게 제공해주는 [라이브러리](https://github.com/pierredavidbelanger/logback-awslogs-appender) 가 있습니다. 해당 라이브러리를 사용하려면 아래의 의존성을 추가해주어야 합니다.

<br>

### `gradle`

```
implementation "ca.pjer:logback-awslogs-appender:1.6.0"
```

<br>

### `maven`

```
<dependency>
    <groupId>ca.pjer</groupId>
    <artifactId>logback-awslogs-appender</artifactId>
    <version>1.6.0</version>
</dependency>
```

위의 의존성을 추가하고 프로젝트 세팅을 해보겠습니다. (버전은 시기에 따라 달라질 수 있습니다.)

<br>

![스크린샷 2021-05-13 오전 10 57 00](https://user-images.githubusercontent.com/45676906/118066771-4661f780-b3da-11eb-8e7e-cd21bbbd3cb8.png)

저의 프로젝트 구조는 위와 같습니다. 가장 중요한 파일은 `logback.xml` 파일 인데요. `resources` 폴더 아래에 `logback.xml` 파일을 만드시면 됩니다. 

<br>

## `AWS IAM CloudWatch 권한 추가하기`

<img src="https://user-images.githubusercontent.com/45676906/185792273-bad9eec8-aa4e-4154-8f21-9dd2154d90ae.png" width="300px" alt="스크린샷 2022-08-21 오후 10 04 59">

<br>

<img src="https://user-images.githubusercontent.com/45676906/185792369-13678955-8d5e-44b1-ad7a-0771e314700c.png" width="300px" alt="스크린샷 2022-08-21 오후 10 07 26">

`CloudWatchFullAccess` 권한을 추가하겠습니다.

<br>

## `logback.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter"/>
    <conversionRule conversionWord="wex"
                    converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter"/>
    <conversionRule conversionWord="wEx"
                    converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter"/>
    <property name="LOG_PATTERN"
              value="${LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){blue} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <springProperty name="AWS_ACCESS_KEY" source="cloud.aws.credentials.accessKey"/>
    <springProperty name="AWS_SECRET_KEY" source="cloud.aws.credentials.secretKey"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>${LOG_PATTERN}</Pattern>
        </layout>
    </appender>

    <appender name="aws_cloud_watch_log" class="ca.pjer.logback.AwsLogsAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <layout>
            <pattern>[%thread] [%date] [%level] [%file:%line] - %msg%n</pattern>
        </layout>
        <logGroupName>Marryting-log</logGroupName>
        <logStreamUuidPrefix>Marryting-log-</logStreamUuidPrefix>
        <logRegion>ap-northeast-2</logRegion>
        <maxBatchLogEvents>50</maxBatchLogEvents>
        <maxFlushTimeMillis>30000</maxFlushTimeMillis>
        <maxBlockTimeMillis>5000</maxBlockTimeMillis>
        <retentionTimeDays>0</retentionTimeDays>
        <accessKeyId>${AWS_ACCESS_KEY}</accessKeyId>
        <secretAccessKey>${AWS_SECRET_KEY}</secretAccessKey>
    </appender>

    <springProfile name="local,dev">
        <root level="info">
            <appender-ref ref="CONSOLE"/>
        </root>
        
        <logger name="com.amazonaws.util.EC2MetadataUtils" level="error" additivity="false">
        </logger>

        <logger name="mashup.spring.jsmr" level="debug" additivity="false">
            <appender-ref ref="CONSOLE"/>
        </logger>

        <logger name="mashup.spring.jsmr" level="error" additivity="false">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="aws_cloud_watch_log"/>
        </logger>
    </springProfile>

    <springProfile name="prod">
        <root level="info">
            <appender-ref ref="CONSOLE"/>
        </root>

        <logger name="com.amazonaws.util.EC2MetadataUtils" level="error" additivity="false">
        </logger>

        <logger name="mashup.spring.jsmr" level="debug" additivity="false">
            <appender-ref ref="CONSOLE"/>
        </logger>
    </springProfile>
</configuration>
```

전체 Logback 코드는 위와 같은데요. 코드에 대해서 일부분 정리해보겠습니다. 

<br>

<img width="1389" alt="스크린샷 2022-08-21 오후 10 20 14" src="https://user-images.githubusercontent.com/45676906/185792930-f9b2c67d-75c5-4270-87e4-33c735fb9f56.png">

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <property name="LOG_PATTERN"
              value="${LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){blue} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
</configuration>
```

위처럼 `property` 라는 속성을 통해서 로그의 패턴을 어떻게 출력할 것인지 정의할 수 있습니다. 

<br>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>${LOG_PATTERN}</Pattern>
        </layout>
    </appender>
</configuration>
```

그리고 위에서 정의한 로그 패턴을 출력할 `appender`를 하나 정의합니다.

<br>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <springProperty name="AWS_ACCESS_KEY" source="cloud.aws.credentials.accessKey"/>
    <springProperty name="AWS_SECRET_KEY" source="cloud.aws.credentials.secretKey"/>
</configuration>
```

`springProperty` 속성을 사용하면 `logback.xml` 파일에서 `application.yml`에 있는 `AWS IAM AccessKey, SecretKey` 값을 읽어와서 사용할 수 있습니다.

<br>

<img width="751" alt="스크린샷 2022-08-21 오후 10 30 34" src="https://user-images.githubusercontent.com/45676906/185793314-02a68962-10a7-4511-b5c9-a34a5e22e2f7.png">

이제 CloudWatch로 Spring Log를 전송하는 `ca.pjer:logback-awslogs-appender` 라이브러리를 활용하는 appender 부분을 알아보겠습니다.

- logGroupName: CloudWatch log Group Name
- logStreamUuidPrefix: Marryting-Api-log-d880c850-c43c-4313-b0f6-8d32139470b 와 같은 로그 스트림의 UUID가 생깁니다.
- logRegion: CloudWatch AWS Region
- maxBatchLogEvents: 배치의 최대 이벤트 갯수를 설정하는 것이며 1 ~ 10000사이 값만 설정이 가능하다. 이벤트 대기열에 갯수가 50개가 되면 AWS Cloud Watch로 로그가 전송됩니다.
- maxFlushTimeMillis: 마지막 플러시가 발생된 이후 지정된 시간이 지나면 AWS Cloud Watch로 로그가 전송된다. 0일 경우 로그를 동기로 전송하고 0보다 큰값일 경우 비동기로 로그가 전송됩니다. 
- maxBlockTimeMillis: 로그가 전송되는 동안 코드가 계속 실행되는 것을 차단하고 값을 0으로 세팅하면 전송중에 발생되는 모든 로그를 버립니다. 
- retentionTimeDays: 로그그룹의 보존기간을 얘기합니다. 0으로 세팅하면 보존기간은 무기한으로 보존됩니다.
- accessKeyId: AWS IAM Access Key (위에서 SpringProperty로 yml에서 읽어온 것 사용하기)
- secretAccessKey: AWS IAM Secret Key (위에서 SpringProperty로 yml에서 읽어온 것 사용하기)

<br>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <springProfile name="local,dev">
        <root level="info">
            <appender-ref ref="CONSOLE"/>
        </root>
        <logger name="com.amazonaws.util.EC2MetadataUtils" level="error" additivity="false">
        </logger>

        <logger name="mashup.spring.jsmr" level="debug" additivity="false">
            <appender-ref ref="CONSOLE"/>
        </logger>

        <logger name="mashup.spring.jsmr" level="error" additivity="false">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="aws_cloud_watch_log"/>
        </logger>
    </springProfile>
</configuration>
```

마지막으로 `springProfile`을 사용하면 `active profile` 값에 따라 logback 설정을 커스텀할 수 있습니다. 

<br>

```
<logger name="mashup.spring.jsmr" level="error" additivity="false">
    <appender-ref ref="CONSOLE"/>
    <appender-ref ref="aws_cloud_watch_log"/>
</logger>
```

예를들어, 위처럼 로그 레벨 error에 해당할 때 위에서 정의한 appender 2개를 추가해서 정의할 수 있는 형식입니다.  

<br>

## `Error 발생시키기`

위처럼 logback을 작성하면 `log.error()`로 출력되는 에러들을 `CloudWatch`로 전송하게 되는데요. 

<img width="1285" alt="스크린샷 2022-08-21 오후 10 46 25" src="https://user-images.githubusercontent.com/45676906/185793962-97fb55b7-f93e-4fe0-aaf8-1302b43147ab.png">

`log.error()`가 출력되는 어떤 것들을 실행시켜보겠습니다.(저는 현재 진행하고 있는 프로젝트를 실행시켰습니다.)

<br>

![스크린샷 2022-08-21 오후 10 47 39](https://user-images.githubusercontent.com/45676906/185794019-f9b860c5-3376-4531-a1a1-5c9517d4843d.png)

그러면 위처럼 CloudWatch에 로그가 잘 쌓이는 것을 확인할 수 있습니다. 

<br>

## `Reference`

- [https://github.com/pierredavidbelanger/logback-awslogs-appender](https://github.com/pierredavidbelanger/logback-awslogs-appender)