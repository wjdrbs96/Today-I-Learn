# Octotree 이란?

`Octotree`는 Github를 사용할 때 도움을 주는 Chrome extention 이다. [여기](https://chrome.google.com/webstore/detail/octotree-github-code-tree/bkhaagjahfmjljalopjnoealnfndnagc)
에서 다운을 하고 적용을 시킨 후에 `Github` 레포지토리 아무 곳이나 들어가보자.


![스크린샷 2020-11-03 오전 12 28 45](https://user-images.githubusercontent.com/45676906/97886216-ac0a6280-1d6b-11eb-9c82-2aca9e59350c.png)

`Octotree`를 적용한 후에 Github Repository를 보면 왼쪽 중간에 어떤 버튼이 생긴 것을 볼 수 있다. 이것을 누르면 아래와 같은 것을 볼 수 있다.

<img width="1792" alt="스크린샷 2020-11-03 오전 12 30 59" src="https://user-images.githubusercontent.com/45676906/97886407-e8d65980-1d6b-11eb-8287-9927a7a9c389.png">

그러면 위의 그림과 같이 왼쪽에 해당 Repository의 폴더 구조를 IDE에서 보는 것처럼 보기 쉽게 볼 수 있다. 

<br>

## 만약 `private Repository`에도 적용하고 싶다면 어떻게 해야 할까?

Github에서 제공하는 AccessToken을 발급받아야 한다. 이것이 무엇인지 모르겠다면 [여기](https://velopert.com/2389) 와 [여기](https://showerbugs.github.io/2017-11-16/OAuth-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C) 를 참고하자. 

<br>

이제 Github에서 AccessToken을 발급받아 보자. 

<img width="1792" alt="스크린샷 2020-11-03 오전 12 32 45" src="https://user-images.githubusercontent.com/45676906/97886703-466aa600-1d6c-11eb-935b-77975619307b.png">

먼저 본인의 Github에서 `Setting`을 들어가보자. 

![스크린샷 2020-11-03 오전 12 35 16](https://user-images.githubusercontent.com/45676906/97886856-82057000-1d6c-11eb-872a-c4941db7aa46.png)

그리고 `Developer settings`를 클릭하자.

<img width="1792" alt="스크린샷 2020-11-03 오전 12 38 00" src="https://user-images.githubusercontent.com/45676906/97888079-0efcf900-1d6e-11eb-80ae-057736d53f2e.png">

그리고 위와 같이 아래 `Generate new Token` 버튼을 눌러보자. 

<img width="1792" alt="스크린샷 2020-11-03 오전 12 48 14" src="https://user-images.githubusercontent.com/45676906/97888703-c8f46500-1d6e-11eb-9ef4-7b148302442f.png">

그리고 이번에 이름을 적어주고, 토큰을 어떤 용도로 사용하지 체크해주면 된다. 그리고 아래 `생성`버튼을 눌러보자.

![스크린샷 2020-11-03 오전 12 54 57](https://user-images.githubusercontent.com/45676906/97889074-40c28f80-1d6f-11eb-8b65-0235ddd6a65e.png)

그러면 토큰이 생성된 것을 확인할 수 있다. 발급 받은 토큰을 복사하자.

<img width="1792" alt="스크린샷 2020-11-03 오전 12 56 19" src="https://user-images.githubusercontent.com/45676906/97889341-9dbe4580-1d6f-11eb-864d-5ff859410d86.png">

그리고 `private Repository`로 돌아와서 위의 설정 버튼을 누르고 복사한 `AccessToken`을 붙여넣으면 된다. 