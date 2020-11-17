## `multipart/form-data`란?

HTTP 방식으로 서버 - 클라이언트가 통신을 한다. 이 때 클라이언트가 Request Body에 데이터를 넘어서 서버로 전달을 해주기도 하는데 Body의 데이터 타입이
어떤 타입인지를 명시를 Request Header에서 할 수 있고 그 중에서 `content-type`에서 할 수 있다.

<br>

예를들어, body의 내용이 text라면 header에 text/plain으로 명시하고, jpg이미지라면 image/jpeg, 그리고 일반적인 form에서 submit된 데이터들은 `application/x-www.form-urlencorded`이다. 

<br>

### 그런데 만약 `사진`과 `사진을 설명할 수 있는 문장`을 같이 서버로 보내야 한다면 어떤 타입을 써야할까?

그러면 Request Header에 타입을 2개를 명시해주어야 할까? 아쉽게도 그럴 수는 없다. 그렇기 때문에 나온 데이터 타입이 `Multipart`이다.
따라서 `사진과 form 데이터의 submit` 데이터를 같이 보내는 상황에서 `multipart/form-data`를 Request Header에 적어주면 된다.