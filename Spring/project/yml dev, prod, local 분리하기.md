# `application.yml dev, prod, local 환경 분리하는 법`

이번에 프로젝트를 진행하면서 마무리가 되었을 쯤에 `개발`, `운영`, `로컬` 환경으로 분리시키는 작업이 필요했습니다.
그래서 이번 글에서 `application.yml`을 사용할 때 `prod`, `dev`, `local`로 나눠서 작업하는 법에 대해서 간단하게 알아보겠습니다. 

<br>

<img width="217" alt="스크린샷 2021-06-12 오후 2 36 05" src="https://user-images.githubusercontent.com/45676906/121766232-9673db80-cb8b-11eb-8f81-e890be5e961a.png">

하나의 yml 파일 내에서 `---`을 통해서 구분하는 것도 가능하지만 yml 파일이 거대해지다 보니.. 위 처럼 환경마다 파일을 만들어서 총 4개의 `yml` 파일을 생성하였습니다.  

<br>

![스크린샷 2021-06-12 오후 2 40 19](https://user-images.githubusercontent.com/45676906/121766392-6c6ee900-cb8c-11eb-9068-ca4a602271cb.png)

`application.yml`을 보면 위와 같이 맨 위에 공통으로 쓰이는 설정 값들을 놓고, ---을 구분 후에 아래의 `prod`, `dev`, `local` 환경을 분리했습니다.
profile 값에 맞는 설정 값을 주고 실행하면 해당 값에 해당하는 파일이 실행되는 것입니다. (예를들어 `prod` 설정 값을 주어 실행하면 `application-prod.yml` 파일이 실행됩니다.)

그러면 `prod`, `dev`, `local` yml 파일에는 어떤 것들이 있는지 알아보겠습니다. 

<br>

![스크린샷 2021-06-12 오후 3 24 23](https://user-images.githubusercontent.com/45676906/121767301-624fe900-cb92-11eb-8800-929b9e7f7fde.png)

`dev`, `local`, `prod` 파일 모두 형식은 위와 같습니다. 위와 같이 사용하고자 하는 RDS 주소만 모두 다르게 세팅을 해놓았습니다. 환경마다 다르게 쓰이는 값들이라면 공통 파일에 넣지 말고 이처럼 각각 파일에 넣으면 될 거 같습니다.
(Redis는 application-prod.yml에는 ElastiCache를 사용하고 있고 나머지에는 local Redis를 사용하고 있습니다.)
하지만 저는 아직은 데이터베이스만 분리하면 될 거 같아서 위와 같이 해놓았습니다.

현재 가볍게 yml을 분리한 것은 이게 전부입니다. 그러면 이 상태에서 아무 것도 없이 그냥 실행 시키면 어떤 파일이 실행될까요? 

<br>

<img width="1074" alt="스크린샷 2021-06-12 오후 3 07 00" src="https://user-images.githubusercontent.com/45676906/121766877-e785ce80-cb8f-11eb-8e79-25d87f0f8e8f.png">

위와 같이 `default`로 `local` 파일이 실행되는 것을 볼 수 있습니다. 그러면 다른 `prod`, `local` 파일을 실행시키려면 어떻게 해야 할까요? 

<br>

```
./gradlew clean build
```

위의 명령어를 통해서 먼저 `jar` 파일을 생성해줍니다. 

<br>

![스크린샷 2021-06-12 오후 3 10 45](https://user-images.githubusercontent.com/45676906/121766973-6a0e8e00-cb90-11eb-8781-6763585766ab.png)

그러면 위와 같이 jar 파일이 생겼을 것입니다. 

<br>

```
java -jar -Dspring.profiles.active=prod *.jar
```

위와 같이 jar를 실행할 때 prod 값을 주어 실행하면 됩니다. profiles에 prod를 주면 `application-prod.yml` 파일이 실행될 것입니다. 

<br>

![스크린샷 2021-06-12 오후 3 17 34](https://user-images.githubusercontent.com/45676906/121767132-6d564980-cb91-11eb-9548-19309b7a415f.png)

그러면 위와 같이 `prod`가 실행되고 있는 것을 볼 수 있습니다. 

<br>

이번 글에서는 아주 단순한 예시를 들었지만, Spring에서 yml 파일로 `dev`, `prod`, `local` 환경을 분리하는 것에 대해서 알아보았습니다.   
