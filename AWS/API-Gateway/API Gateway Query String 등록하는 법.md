# `API Gateway Query String 등록하는 법`

이번 글에서는 API Gateway에서 Query String을 등록하는 법에 대해서 알아보겠습니다. `Query String`은 예를들면 아래와 같습니다.

```
http://localhost:8080?name=Gyunny&part=Server
```

위와 같이 `?, &` 형태로 이어져 있는 것을 말하는데요. 이런 Query String은 `API Gateway`에서 어떻게 사용하는지?에 대해서 알아보겠습니다.

<br>

![스크린샷 2021-05-18 오후 3 54 51](https://user-images.githubusercontent.com/45676906/118605713-874e7780-b7f1-11eb-83e4-2b72ccd68b07.png)

먼저 `API Gateway`는 만들어져 있다고 가정하고 진행하겠습니다. 생성되어 있지 않다면 [여기](https://devlog-wjdrbs96.tistory.com/328) 를 참고한 후에 이 글을 읽으시면 좋을 거 같습니다.

GET 메소드에게 Query String을 적용할 것이라 위의 보이는 순서대로 `메소드 요청`으로 들어가겠습니다.

<br>

![스크린샷 2021-05-18 오후 4 00 55](https://user-images.githubusercontent.com/45676906/118606425-5cb0ee80-b7f2-11eb-8bae-df9a7494d0db.png)

그리고 `URL 쿼리 문자열 파라미터`에서 위와 같이 입력을 하겠습니다. 저는 `http://localhost:8080?location=Gyunny` 형식으로 사용할 것이기 때문에 위와 같이 지정을 했습니다.

<br>

![스크린샷 2021-05-18 오후 4 03 22](https://user-images.githubusercontent.com/45676906/118606734-b6b1b400-b7f2-11eb-8461-308a3aa77bdb.png)

그리고 이번에는 `통합 요청`을 누르겠습니다.

<br>

![스크린샷 2021-05-18 오후 4 05 44](https://user-images.githubusercontent.com/45676906/118607014-0ee8b600-b7f3-11eb-98fe-bfffd8b31ab5.png)

그리고 아래에 보면 `매핑 템플릿`이라고 존재하는데요. 여기서 `application/json`을 등록하고 템플릿에다 아래의 코드를 넣겠습니다.

```json
{
  "location": "$input.params('location')"
}
```

여기에 `location`이 아니라 사용할 쿼리스트링의 이름을 적으면 됩니다.

<br>

![스크린샷 2021-05-18 오후 4 12 08](https://user-images.githubusercontent.com/45676906/118607909-16f52580-b7f4-11eb-80e2-d53ffb3e7144.png)

그리고 마지막으로 배포를 하면 사용할 수 있습니다. 

해당 쿼리 스트링은 NodeJS 기반의 람다함수에서 어떻게 꺼낼 수 있는지 정리하고 글을 마치겠습니다.

```js
exports.handler = function (event, context) {
  const { location } = event;
  console.log(location);
};
```

```
http://API-Gateway-주소?location=Gyunny
```

위와 같은 형태로 호출한다면 event에는 `{ location: Gyunny }`가 들어있기 때문에 위와 같이 쿼리스트링을 꺼낼 수 있게 됩니다.