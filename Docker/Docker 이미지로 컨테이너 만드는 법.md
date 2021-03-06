# `이미지로 컨테이너를 만들기`

지금까지 `이미지를 이용해서 컨테이너를 생성한다고 배웠습니다.` 하지만 어떻게해서 이미지를 이용해 컨테이너를 생성하는지를 이번 글에서 한번 다뤄보겠습니다. 

<img width="850" alt="스크린샷 2021-03-23 오전 11 45 07" src="https://user-images.githubusercontent.com/45676906/112085085-37767a00-8bcd-11eb-81f7-c410b52eea49.png">

- `이미지`: 응용 프로그램을 실행하는데 `필요한 모든 것`을 포함하고 있습니다. 

여기서 말하는 `필요한 모든 것`이 무엇일까요?

- `컨테이너가 시작 될 때 실행되는 명령어(Dockerfile로 관리)`
- `파일 스냅샷(ex: 카카오톡이라면 카카오톡을 실행하는데 필요한 파일) - 디렉토리나 파일을 압축시킨 느낌`

<br>

## `이미지로 컨테이너 만드는 순서`

1. Docker 클라이언트에 `docker run <이미지>` 입력
2. Docker 이미지에 있는 파일 스냅샷을 컨테이너 하드 디스크에 옮긴다.

```
docker run hello-world
```

위와 같이 hello-world 이미지를 실행시킨다면 어떤 과정으로 실행될까요?

![스크린샷 2021-03-29 오전 10 25 13](https://user-images.githubusercontent.com/45676906/112775902-25875200-9079-11eb-8f22-dd20ea7d60a6.png)

처음에 이미지에 있는 `파일 스냅샷`을 컨테이너 안에 `하드디스크`로 옮기게 됩니다. 

![스크린샷 2021-03-29 오전 10 28 06](https://user-images.githubusercontent.com/45676906/112776020-7b5bfa00-9079-11eb-8bb4-86211acb9e87.png)

그리고 이미지에 있는 `시작 시 실행 될 명령어`는 컨테이너 안에 `명령어 실행`하는 곳으로 옮기게 됩니다. 

<img width="764" alt="스크린샷 2021-03-29 오전 10 29 49" src="https://user-images.githubusercontent.com/45676906/112776107-b2caa680-9079-11eb-87aa-ef05cf300f4d.png">

그래서 이미지가 컨테이너를 생성하여 프로그램이 실행되게 됩니다. 
