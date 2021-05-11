# `Spring LogBack으로 Slack Log 찍는 법`

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

가장 처음으로 위의 의존성을 추가하겠습니다.

<br>

