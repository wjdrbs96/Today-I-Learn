# `H2 DataBase 에러 해결하기`

이번 글에서는 `Spring`에서 `H2 Database`를 사용할 때 만난 에러를 해결하는 과정에 대해서 간단하게 정리해보겠습니다. H2 DataBase는 설치가 되어 있다고 가정한 상태로 글을 시작해보겠습니다.

설치를 한 후에 `h2` 디렉토리를 보면 `bin` 디렉토리가 존재할 것입니다. bin 디렉토리 안에는 `h2.sh`라는 파일이 존재할 것인데요. 

<img width="569" alt="스크린샷 2021-08-24 오후 1 36 12" src="https://user-images.githubusercontent.com/45676906/130556503-a2e64faf-0edf-4d16-8ce2-d1fffede20b9.png">

`h2.sh` 파일을 `sh h2.sh`로 실행하면 아래와 같은 화면을 볼 수 있을 것입니다. 안뜬다면 `http://localhost:8082`로 접속하면 보일 것입니다.

<br>

![스크린샷 2021-08-24 오후 1 32 28](https://user-images.githubusercontent.com/45676906/130556247-aa63283d-183c-475a-95b7-0990acec7997.png)

그리고 바로 `연결` 버튼을 누르면 위의 보이는 빨간색 에러를 볼 수 있을 것인데요.

```
Database "/Users/choejeong-gyun/test" not found, and IFEXISTS=true, so we cant auto-create it [90146-199] 90146/90146
```

즉, test 라는 이름의 스키마가 존재하지 않는다는 것입니다. 이것을 생성하는 과정이 필요합니다. 

<br>

![스크린샷 2021-08-24 오후 2 04 13](https://user-images.githubusercontent.com/45676906/130558787-81e3b268-c103-4daa-ae5d-58d981ba5bc3.png)

```
jdbc:h2:~/test
```

그래서 위와 같이 `JDBC URL`에 적은 후에 `연결`을 누르면 될 거 같지만 여전히 같은 에러가 발생합니다..

<br> 

![스크린샷 2021-08-24 오후 2 08 14](https://user-images.githubusercontent.com/45676906/130559138-da280441-cb8f-4e0c-a532-c9afdc93cf84.png)

그래서 프로젝트에서 위의 `persistence.xml`에서 `jdbc:h2:~/test` 이렇게 수정하고 실행하면 아래와 같이 `test` 스키마가 자동으로 생기는 것을 볼 수 있습니다.

<img width="509" alt="스크린샷 2021-08-24 오후 2 11 18" src="https://user-images.githubusercontent.com/45676906/130559353-0d094765-685d-4386-b208-71f536855705.png">

그리고 이제 다시 한번 접속을 해보겠습니다. 

<br>

![스크린샷 2021-08-24 오후 2 12 12](https://user-images.githubusercontent.com/45676906/130559435-c4e1e723-19ca-49d1-8190-aee46229578d.png)

그러면 위와 같이 접속이 잘 되는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-08-24 오후 2 13 09](https://user-images.githubusercontent.com/45676906/130559532-19573774-b861-4e1d-bc56-c13a628acf69.png)

위와 같이 `H2(Server)`로 바꾸고 다시 접속해보아도 접속이 잘 되는 것을 확인할 수 있습니다. 