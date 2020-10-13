# `Git Study 1주차`

<br>

## 1. `GitKraken` 사용법

<img width="1240" alt="1" src="https://user-images.githubusercontent.com/45676906/95776411-3debf600-0cff-11eb-8529-693d666a0a00.png">

<br>

`Github`의 Repository와 연결이 되어 있는 프로젝트를 `Open a repo`를 통해서 열어보자.

<br>

![스크린샷 2020-10-13 오후 10 19 03](https://user-images.githubusercontent.com/45676906/95865859-1d28ac80-0da2-11eb-8f23-ac7934213ef1.png)

<br>

그러면 위와 같은 화면을 볼 수 있을 것이다. 이제 간단한 파일을 만들어서 `GitKraken` 실습을 해보자.

<br>

```
1. vi README.md  
2. a 입력 후 => 파일 수정
3. ESC + wq (저장 후 나가기)
4. ls => 파일이 생성되었는지 확인(생성되었다면 성공)
```

![스크린샷 2020-10-13 오후 10 20 51](https://user-images.githubusercontent.com/45676906/95866041-5cef9400-0da2-11eb-9171-0d0d01692955.png)

<br>

그리고 나서 `GitKraken`을 보면 아래와 같이 나온다.

![스크린샷 2020-10-13 오후 10 22 26](https://user-images.githubusercontent.com/45676906/95866197-94f6d700-0da2-11eb-84c9-382e15a53ffb.png)

<br>

오른쪽에 `Unstaged Files`를 보면 우리가 만든 `README.md`파일이 있는 것을 볼 수 있다. `README.md`파일은 현재 `스테이지`영역에
올라온 상태가 아니라는 뜻이다.

<br>

![스크린샷 2020-10-13 오후 10 25 36](https://user-images.githubusercontent.com/45676906/95866727-3d0ca000-0da3-11eb-9a86-46e79b158179.png)

<br>

그리고 위의 `Stage All changes`, `Stage File`의 버튼중에 하나를 눌러서 스테이지 영역으로 올릴 수 있다. (파일 하나만 올릴 것이라면 `Stage File`을 누르고 전부다 올릴 것이라면 `Stage All changes`를 누르면 된다.)

<br>

![스크린샷 2020-10-13 오후 10 36 37](https://user-images.githubusercontent.com/45676906/95868002-bd7fd080-0da4-11eb-8566-345feee6500f.png)

<br>

이제 파일이 스테이지 영역에 올라왔기 때문에 스테이지 영역의 커밋하기 위해 `Commit Message`를 작성해보자.

<br>

![스크린샷 2020-10-13 오후 10 40 12](https://user-images.githubusercontent.com/45676906/95868401-1fd8d100-0da5-11eb-943a-2b90f1fe1f3b.png)


이제 `Github`에 `push`를 했으니 잘 되었는지 `Github`에서 확인을 해보자. 잘 되었다면 `GitKraken`을 이용해서 `Git`을 사용하는 간단한 실습을 해보았다.


