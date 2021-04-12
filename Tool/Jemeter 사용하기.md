# `Mac JMeter 설치하기`

```
brew install jmeter
```

brew를 통해서 jmeter 설치를 진행하겠습니다. 

```
jmeter
```

설치가 되었으면 위와 같이 터미널에서 입력하면 `JMeter`가 실행 됩니다. 

![스크린샷 2021-04-12 오전 10 41 46](https://user-images.githubusercontent.com/45676906/114329993-aebf7e00-9b7b-11eb-95d3-31a88a48903e.png)

<br> <br>

# `JMeter 사용하는 법`

![스크린샷 2021-04-12 오전 10 42 19](https://user-images.githubusercontent.com/45676906/114330064-d6164b00-9b7b-11eb-9e70-4ac5f5e437e9.png)

`Test plan`을 클릭하고 오른쪽 마우스를 누른 후에 `Add -> Threads -> Thread Group`을 선택해서 들어가겠습니다. 

![스크린샷 2021-04-12 오전 10 43 58](https://user-images.githubusercontent.com/45676906/114330278-5341c000-9b7c-11eb-8229-7a03284c38af.png)

그러면 위와 같이 `몇명이 동시 접속할 것이며`, `각각의 유저들이 몇번 반복해서 접속할 것인가?`를 정할 수 있습니다. (Loop Count에서 infinite를 체크하면 무한으로 반복하겠다는 뜻입니다. 저는 infinite로 놓고 테스트를 할 것입니다.)

![스크린샷 2021-04-12 오전 10 49 23](https://user-images.githubusercontent.com/45676906/114330508-d531e900-9b7c-11eb-9425-ffa0fdf6c44a.png)

그리고 이제 어떤 서버에 부하를 줄 것인지를 설정하기 위해서 `Thread-Group`에서 오른쪽 마우스를 누르고 `Add -> Sampler -> HTTP Request`를 누르겠습니다.

![스크린샷 2021-04-12 오전 10 51 32](https://user-images.githubusercontent.com/45676906/114330736-4bcee680-9b7d-11eb-9989-7ad366c34873.png)

- `HTTP 프로토콜`을 사용할 것이기 때문에 위와 같이 `HTTP Request`를 선택했습니다. 
- HTTP를 사용할 것이라면 Web Server 아래에는 아무 것도 적지 않아도 됩니다.
- 저는 {서버 주소}/loadtest.php 로 접속해서 부하테스트를 할 것이기 때문에 위와 같이 설정했습니다.

![스크린샷 2021-04-12 오전 10 58 30](https://user-images.githubusercontent.com/45676906/114331100-3e662c00-9b7e-11eb-8dfb-e472cde4de34.png)

그리고 서버 부하 테스트 시작을 해보겠습니다. 

![스크린샷 2021-04-12 오전 11 39 51](https://user-images.githubusercontent.com/45676906/114333766-14b00380-9b84-11eb-943e-18416b35467f.png)

![스크린샷 2021-04-12 오전 11 40 07](https://user-images.githubusercontent.com/45676906/114333775-18438a80-9b84-11eb-9b38-dc40bba2d05c.png)

그러면 위와 같이 요청이 계속 가는 것을 볼 수 있고 그래프로도 볼 수 있습니다. 실제로 EC2 CPU 사용률이 어떻게 되는지 보기 위해서 EC2로 접속해보겠습니다. 

```
top (EC2 Linux2 에서 CPU 사용량 확인)
```


![스크린샷 2021-04-12 오전 11 41 49](https://user-images.githubusercontent.com/45676906/114333865-5476eb00-9b84-11eb-88f5-63dcb41bce56.png)

그러면 위와 같이 CPU 사용량이 `95.3`인 것을 볼 수 있습니다. 