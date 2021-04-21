# `Spring Boot에서 Local, dev 환경 나눠보기`

Spring Boot 프로젝트를 만들면 아래와 같은 형태로 만들어질 것입니다. 

![스크린샷 2021-04-21 오후 5 34 23](https://user-images.githubusercontent.com/45676906/115523331-eb9c1b00-a2c7-11eb-90fa-890c9a12fc0b.png)

그리고 `resources/application.properties`가 존재합니다. 이 파일은 DB, 세션 등등의 어떤 중요한 설정들을 담당하는 파일입니다. 해당 파일에서 `dev`, `local` 환경을 분리시킬 수 있게 설정할 수도 있습니다.
(Default는 .properties 인데 .yml 파일로 수정하겠습니다.)

![스크린샷 2021-04-21 오후 5 40 04](https://user-images.githubusercontent.com/45676906/115524090-b6dc9380-a2c8-11eb-8a72-6a4246999ecd.png)

<br> <br>

## `local 환경 만들기`

![스크린샷 2021-04-21 오후 5 41 47](https://user-images.githubusercontent.com/45676906/115524197-d96eac80-a2c8-11eb-9ab7-2b9618fedf72.png)

그리고 `applcation.yml` 파일에 위와 같이 적겠습니다. 

- `spring.profiles.active`: 여기에 지정한 `local`, `dev`로 개발환경, 로컬 환경을 분리시킬 수 있습니다. 

```
./gradlew clean build
``` 

위의 명령어를 통해서 jar 파일을 만들겠습니다. 

![스크린샷 2021-04-21 오후 5 45 58](https://user-images.githubusercontent.com/45676906/115524818-7c272b00-a2c9-11eb-94ae-f08b68d4263d.png)

그러면 위와 같이 `build/libs/` 경로에 jar 파일이 생긴 것을 볼 수 있습니다. 

```
java -jar -Dspring.profiles.active=dev ./build/libs/*.jar
```

그리고 위와 같이 `dev` 환경으로 jar를 실행시켜 보겠습니다. 

![스크린샷 2021-04-21 오후 5 48 18](https://user-images.githubusercontent.com/45676906/115525188-d3c59680-a2c9-11eb-826e-3192f1659241.png)

그러면 위와 같이 `8083 포트`로 톰캣이 실행되는 것을 볼 수 있습니다. 

<br> <br>

## `개발 환경 만들기`

![스크린샷 2021-04-21 오후 5 49 36](https://user-images.githubusercontent.com/45676906/115525385-01aadb00-a2ca-11eb-8c13-34b3b69502b6.png)

`applcation.yml`에 위와 같이 `---`를 기준으로 나누어 작성하겠습니다. `---`를 기준으로 값들이 구분됩니다.

```
./gradlew clean build
java -jar -Dspring.profiles.active=local ./build/libs/*.jar
``` 

이번에도 위의 명령어를 통해서 jar를 실행시켜보겠습니다.

![스크린샷 2021-04-21 오후 5 52 11](https://user-images.githubusercontent.com/45676906/115525764-5b130a00-a2ca-11eb-970c-590d9dfa7ea2.png)

그러면 이번에는 `8082`로 톰캣이 실행되는 것을 볼 수 있습니다. 이렇게 실제로 `운영`, `개발`, `실제` 디비나 설정들이 다 다를 것이기 때문에 나눠서 사용할 수 있습니다. 
