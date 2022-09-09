## `Intellij에서 Squash Commit 하는 법`

![스크린샷 2022-09-09 오후 4 13 45](https://user-images.githubusercontent.com/45676906/189292940-b7348425-c816-423f-85f4-0bbd1d3c3e04.png)

`Intellij`에서 `Git` 버튼을 누르면 위처럼 현재 `Git Tree`와 `Commit`들을 볼 수 있습니다.

<br>

![스크린샷 2022-09-09 오후 4 14 58](https://user-images.githubusercontent.com/45676906/189293156-b63df384-259e-4d4e-9cb0-a7197b3a6015.png)

위처럼 `Squash Commit` 하고자 하는 커밋들을 여러 개 선택한 후에 오른쪽 마우스를 누르면 위와 같은 화면을 만날 수 있다.

여기서 `Squash Commits` 버튼을 누르자.

<br>

<img width="522" alt="스크린샷 2022-09-09 오후 4 16 31" src="https://user-images.githubusercontent.com/45676906/189293386-7c59425f-6282-46fd-945d-a3cf2f1c4eb8.png">

여기서 원하는 커밋을 두고 나머지는 지우면 된다. 또는 새로운 커밋 메세지를 작성하자.

<br>

<img width="821" alt="스크린샷 2022-09-09 오후 4 17 18" src="https://user-images.githubusercontent.com/45676906/189293547-5cf39692-9646-489c-a867-12c403cc12f4.png">

그러면 위처럼 커밋이 하나로 합쳐진 것을 확인할 수 있다.

> 참고로 위의 커밋들이 이미 origin에 push된 상태였다면 포스 푸시를 진행하도록 하자.

```
git push -f origin main
```