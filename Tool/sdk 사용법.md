# `SDK 으로 Java 버전 관리하기`

SDMMAN!은 대부분의 유닉스 기반 시스템에서 여러가지 `SDK(Software Development Kits)`의 병렬 버전을 관리하기 위한 도구입니다.

SDKMAN!을 이용해서 여러 Java 버전을 관리할 수 있는 방법에 대해 알아보겠습니다.

<br>

### `설치(MacOS 기준)`

```
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk version (설치 되었는지 확인)
SDKMAN 5.10.0+617 (sdk 버전)
```

### `삭제(MacOs 기준)`

```
rm -rf ~/.sdkman
```

<br>

### `Java 버전 확인`

```
sdk list java
```

![스크린샷 2021-02-09 오전 11 34 22](https://user-images.githubusercontent.com/45676906/107307824-cd958b80-6aca-11eb-9d9a-c19cd17ca827.png)

위와 같이 `Java version`을 확인할 수 있습니다. 

<br>

### `Java 설치`

```
sdk install java Version-Dist
ex) sdk install java 7.0.282-zulu
```

<br>

### `Java 삭제`

```
sdk uninstall java Version-Dist
sdk uninstall java 8.0.192-zulu
```

<br>

### `설치된 Java 버전 변경`

```
sdk use java Version-Dist
ex) sdk use java 11.0.1-zulu

# 현재 자바 버전 확인
sdk current 
Using:

java: 11.0.10.hs-adpt
```

<br>

# `Reference`

- [https://phoby.github.io/sdkman/](https://phoby.github.io/sdkman/)