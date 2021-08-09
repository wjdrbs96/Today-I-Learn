# `OSI 7 Layer vs TCP/IP 4 Layer 비교`

이번 글에서는 `OSI 7 Layer`와 `TCP/IP 4 Layer`를 비교하는 것을 정리해보겠습니다.

<br>

## `OSI 7 Layer`

표준 규격을 정하는 단체인 `ISO`라는 국제표준화기구가 있는데, 이 단체에서 `OSI 모델`이라는 표준 규격을 제정했습니다. 

![osi](https://camo.githubusercontent.com/e6b9d8fdfe30279caf9e8d7ba00e389d30cd7099d0343f0b865aea8c6d7e8b86/68747470733a2f2f6d656469612e766c70742e75732f696d616765732f786c646b737073342f706f73742f39383066653564302d666366652d343339352d393134382d3061313130343735626132362f696d6167652e706e67)

위의 그림처럼 총 7개의 Layer로 구성된 모델입니다. 각 Layer의 특징을 간단하게 정리하면 아래와 같습니다.

| 계층 | 이름 | 설명 |
|------|-----------|---------------------|
| 7계층 | 응용 계층(Application Layer) | 이메일 & 파일 전송, 웹 사이트 조회 등 애플리케이션에 대한 서비스를 제공 |
| 6계층 | 표현 계층(Presentation Layer) | 문자 코드, 압축, 암호화 등의 데이터를 반환 |
| 5계층 | 세션 계층(Session Layer) | 세션 체결, 통신 방식을 결정 |
| 4계층 | 전송 계층(Transport Layer) | 신뢰할 수 있는 통신을 구현 |
| 3계층 | 네트워크 계층(Network Layer) | 다른 네트워크와 통신하기 위한 경로 설정 및 논리 주소를 결정 |
| 2계층 | 데이터 링크 계층(Data Link Layer) | 네트워크 기기 간의 데이터 전송 및 물리적인 주소를 결정 |
| 1계층 | 물리 계층(Physical Layer) | 시스템 간의 물리적인 연결과 전기 신호를 변환 및 제어 |

<br> <br>

## `TCP/IP 4 Layer`

TCP/IP는 위의 그림에서 볼 수 있듯이 4개의 계층만 존재하는 모델입니다. 

<img width="483" alt="스크린샷 2021-08-09 오후 8 10 35" src="https://user-images.githubusercontent.com/45676906/128697652-b7340b68-5d4f-47d0-a1dd-ce5d2e769423.png">

위의 그림에서 볼 수 있듯이 TCP/IP 모델은 4 Layer 인 것을 볼 수 있는데요.

| OSI 7 계층 | TCI/IP 계층 |
|----------|-----------|
| 세션 + 표현 + 응용 | 응용 계층(Application Layer) |
| 전송 계층 | 전송 계층(Transport Layer) |
| 네트워크 계층 | 인터넷 계층(Internet Layer) |
| 물리 + 데이터 계층 | 네트워크 계층(Network Layer) |
