## `Intellij에서 Rebase 하는 법`

<img width="570" alt="스크린샷 2022-09-09 오후 3 58 08" src="https://user-images.githubusercontent.com/45676906/189290950-8d806376-3631-49dc-bb67-945bf8f7be7b.png">

현재 나는 `feature/test2` 브랜치에 존재하고, `main` 브랜치를 `rebase` 해야 하는 상황이다. 즉, `main` 브랜치에 최신 변경사항

그럴 때는 위처럼 `Intellij`에서 쉽게 `rebase` 할 수 있다. 

<br>

## `충돌이 발생했다면?`

![스크린샷 2022-09-09 오후 3 58 41](https://user-images.githubusercontent.com/45676906/189291689-5ae62b2e-9436-4682-8506-28ea17f427f5.png)

`rebase` 하는 과정에서 `충돌`이 발생하는 경우가 있다면, 위와 같은 화면을 만날 수 있다.

- `Accept Yours`: 충돌이 난 파일에서 내가 수정한 내용으로만 파일을 사용할 때 누르면 된다.
- `Accept Theirs`: 충돌이 난 상대방의 수정 내용으로만 파일을 사용할 때 누르면 된다.

<br>

충돌을 해결할 때는 위에 보이는 충돌이 난 파일을 `더블 클릭` 하면 된다.

<br>

<img width="1115" alt="스크린샷 2022-09-09 오후 3 59 17" src="https://user-images.githubusercontent.com/45676906/189292032-1d89aeb6-70dd-429a-8e2e-8b86dd4e6e92.png">

- `맨 왼쪽`: 내가 수정한 사항
- `맨 오른쪽`: `rebase` 하고자 하는 브랜치의 파일
- `가운데`: 합치고자 하는 형상

<br>

충돌이 난 파일을 해결하고자 하는 방향으로 해결하면 된다.(버튼을 통해서 지울건 지우고 합칠건 합치면 된다.)

<br>

<img width="360" alt="스크린샷 2022-09-09 오후 3 59 32" src="https://user-images.githubusercontent.com/45676906/189292455-1d112dbd-1619-4cf0-a046-fdb0f0ac7ec3.png">

충돌이 정상적으로 해결이 되었다면 위와 같은 화면을 볼 수 있을 것이고, 충돌을 해결하면 됩니다.

<br>

```
git push -f origin main
```

`rebase`를 했기 때문에 포스 푸시를 하자.