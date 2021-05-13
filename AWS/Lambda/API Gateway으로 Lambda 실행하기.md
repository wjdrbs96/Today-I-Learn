# `API Gateway으로 Lambda 함수 실행하는 법`

이번 글에서는 `API Gateway`를 사용해서 `Lambda 함수`를 실행하는 아주 간단한 실습에 대해서 정리해보겠습니다. (다음 글에서는 좀 더 복잡한 API 역할을 하는 Lambda 함수를 만들어서 진행해보겠습니다.)

<br>


## `IAM 역할 만들기`

![스크린샷 2021-05-12 오후 4 13 08](https://user-images.githubusercontent.com/45676906/117933870-ff71f480-b33c-11eb-98f9-ec47cf58a63a.png)

![스크린샷 2021-05-12 오후 4 15 14](https://user-images.githubusercontent.com/45676906/117934196-5e376e00-b33d-11eb-8de6-7a719c0b5562.png)

`Lambda`에 접근할 수 있도록 `AWS Lambda_FullAccess` 권한을 추가하겠합니다.

<br>

![스크린샷 2021-05-12 오후 4 16 30](https://user-images.githubusercontent.com/45676906/117934314-82934a80-b33d-11eb-983a-0587fe6d365c.png)

<br>

## `Lambda 함수 만들기`

![스크린샷 2021-05-12 오후 4 22 39](https://user-images.githubusercontent.com/45676906/117935130-5e843900-b33e-11eb-8c99-a0e644cbb3b0.png)

`Lambda 함수`에서 `런타임`에는 원하는 거 아무거나 선택한 후에 위에서 만든 역할을 넣어주고 만들겠습니다.

<br>

## `API Gateway 만들기`

API Gateway를 통해서 서버 API 만들듯이 만들 것입니다. 여기서 만든 API가 호출되면 위에서 만든 람다 함수의 결과가 반환이 될 것입니다. 

![스크린샷 2021-05-12 오후 4 24 53](https://user-images.githubusercontent.com/45676906/117935393-a2773e00-b33e-11eb-9859-d99c56ebfae1.png)

<br>

![스크린샷 2021-05-12 오후 4 26 24](https://user-images.githubusercontent.com/45676906/117935588-d81c2700-b33e-11eb-8242-91a3cb426fb4.png)

<br>

## `메소드 생성`

![스크린샷 2021-05-12 오후 4 27 27](https://user-images.githubusercontent.com/45676906/117935756-04d03e80-b33f-11eb-8180-77c130ac6da3.png)

![스크린샷 2021-05-12 오후 4 29 12](https://user-images.githubusercontent.com/45676906/117935944-3c3eeb00-b33f-11eb-8c22-3679fe6bf64d.png)

지금은 아주 간단한 예제이기 때문에 `GET`으로 하는 것이 좋을 거 같은데 저는 그냥 `POST`로 했습니다. (GET, POST 원하는 거 아무거나로 해도 될 거 같습니다!) 그리고 API URI 경로도 설정해주겠습니다.

<br>

![스크린샷 2021-05-12 오후 4 30 01](https://user-images.githubusercontent.com/45676906/117936074-61335e00-b33f-11eb-9893-64b2a8c94b84.png)



![스크린샷 2021-05-12 오후 4 31 31](https://user-images.githubusercontent.com/45676906/117936275-a0fa4580-b33f-11eb-8611-dbef3febd2fe.png)

![스크린샷 2021-05-12 오후 4 33 22](https://user-images.githubusercontent.com/45676906/117936445-d30ba780-b33f-11eb-8cc0-042726778689.png)

![스크린샷 2021-05-12 오후 4 34 30](https://user-images.githubusercontent.com/45676906/117936582-fb93a180-b33f-11eb-836e-cd62e2a4aa51.png)

![스크린샷 2021-05-12 오후 4 36 47](https://user-images.githubusercontent.com/45676906/117936904-5d540b80-b340-11eb-88f9-ff3e9efe0576.png)

<br>

## `PostMan 테스트`

![스크린샷 2021-05-12 오후 4 38 14](https://user-images.githubusercontent.com/45676906/117937248-bae85800-b340-11eb-9319-ab3c7dbd8688.png)

그리고 바로 위에서 복사한 경로로 API 호출을 해보면 위와 같이 Lambda 함수에 Default로 존재하는 코드의 응답 값이 잘 반환되는 것을 볼 수 있습니다.