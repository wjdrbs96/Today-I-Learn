## `gradle property로 의존성 버전 관리하는 법`

![스크린샷 2022-02-13 오전 2 30 51](https://user-images.githubusercontent.com/45676906/153721697-22b16dc6-0036-4297-a52e-d176613e4748.png)

위와 같이 프로젝트 최상단에 `gradle.properties` 라는 파일을 하나 만들겠습니다. 

<br>

```
swaggerVersion=3.0.0
jasyptVersion=3.0.3
javaJwtVersion=3.18.3
mysqlVersion=8.0.27
```

`property`에는 위처럼 버전을 넣겠습니다. 그리고 `build.gradle`에서 버전을 사용해보겠습니다. 

<br>

![스크린샷 2022-02-13 오전 2 34 29](https://user-images.githubusercontent.com/45676906/153721844-77bfc7f7-1d3c-465a-b8b3-007ffb962579.png)

그리고 `build.gradle`에 위처럼 `property`에 있는 버전을 사용하면 됩니다. 