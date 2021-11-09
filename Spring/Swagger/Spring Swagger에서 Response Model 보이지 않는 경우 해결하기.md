## `Spring Swagger에서 Response Model 보이지 않는 경우 해결하기`

이번 글에서는 `Spring Swagger`를 사용할 때 `Response Model`이 보이지 않아서 생기는 불편함을 해결했던 방법에 대해서 공유해보려 합니다. 

<img width="1451" alt="스크린샷 2021-11-09 오후 1 00 10" src="https://user-images.githubusercontent.com/45676906/140859707-de51d0dc-73d4-4834-b418-c3713c9aae31.png">

Swagger에서 Response Model이 보이지 않는다는 것은 위와 같은 상황인데요. 위의 Swagger는 제가 개발하고 있는 API 인데, 어떤 응답이 오는지 실행을 해보기 전까지는 어떤 형식으로 응답이 오는지 알 수 없습니다. 그래서 클라이언트 입장에서는 API 마다 실행해본 후에 `응답 형식`을 확인해야 한다는 단점이 존재했습니다. 

그래서 클라이언트가 매번 실행해서 확인하기 힘들어 따로 API 문서를 작성해야 하는 상황이 생겼습니다.(`Swagger를 사용할 때의 장점`을 활용하지 못하는 상황이,,,)  

<br>

![스크린샷 2021-11-09 오후 1 10 17](https://user-images.githubusercontent.com/45676906/140860652-98e3ea94-2e18-419f-ac40-b82cd13ef2a1.png)

그래서 제가 원하는 상황은 위의 사진처럼 API 호출을 해보지 않아도 `Swagger`에 어떤 응답 형식으로 오는지 바로 알 수 있도록 하는 것이 이번 글의 목표입니다. 바로 알아보러 고고싱~! 

<br> <br>

## `Response Model이 보이지 않는 이유`

![스크린샷 2021-11-09 오후 1 07 31](https://user-images.githubusercontent.com/45676906/140860369-ee027419-a213-4cb7-870c-46c0dffc40be.png)

Swagger에서 Response Model이 보이지 않을 때는 클라이언트에게 `Response DTO`를 응답으로 내려줄 때 `DefaultResponse`로 감싸서 보내주고 있습니다. 

<br>

![스크린샷 2021-11-09 오후 1 13 34](https://user-images.githubusercontent.com/45676906/140860978-359aa6f1-0e0b-4fe9-a9e1-646da62150af.png)

`DefaultResponse` 코드의 일부를 보면 내부적으로 `Response Body`에 데이터를 담기 위해 `Map`을 사용하고 있고 `ResponseEntity`가 `DefaultResponse`를 다시 감싸고 있는 것을 볼 수 있습니다. ([ResponseEntity가 무엇인지 모르겠다면?](https://devlog-wjdrbs96.tistory.com/182))

즉, 이렇게 `ResponseEntity`가 ResponseDTO를 바로 사용하면 괜찮은데, 한번 더 감싸고 있는 `DefaultResponse`를 사용하고 있기 때문에 `Swagger`에서는 `Response Model`을 알 수 없기에 나오지 않는 것이었습니다. 

<br> <br>

## `Response Model이 보이도록 수정하기`

제가 말하는 문제를 해결하는 방법은 다양하게 있을 것이라 생각이 됩니다. 왜냐하면 개발하는 프로젝트마다 `서버 - 클라이언트` 사이에 어떤 통신 규격으로 개발할지 조금씩 차이가 있을 수 있기 때문입니다. 예를들어 어떤 프로젝트는 `Response Body`에 `HTTP Status Code`를 넣어서 보내는 곳도 있고, 어떤 곳은 `Response Body`에 넣지 않고 `Response Header`에 존재하는 것을 사용하는 것처럼 차이가 있을 것입니다. 

그래서 제가 해결하고자 하는 방법은 `ResponseEntity`를 `DefaultResponse`를 감싸지 않고 응답을 보내자 입니다. 

![스크린샷 2021-11-09 오후 1 30 49](https://user-images.githubusercontent.com/45676906/140862505-9cab34c2-44d3-47cb-b6b7-0e5c68569794.png)

이번에는 `ApiResponse` 라는 이름으로 `Response DTO`를 감싸는 용도로 만들었고, `Map`을 사용하지 않고 `제네릭`을 사용하였습니다.

<br>

![스크린샷 2021-11-09 오후 1 37 25](https://user-images.githubusercontent.com/45676906/140863109-7b241985-d5d4-4c0c-b139-2360145bc949.png)

그리고 `Controller`에서 `Response DTO`를 한번만 감싼 `ApiResponse`를 활용해서 바로 응답을 내려주는 식으로 수정하였습니다. (지금은 임시 코드라서 DTO를 직접 생성해서 내려주고 있습니다.)

<br>

<img width="1310" alt="스크린샷 2021-11-09 오후 1 38 57" src="https://user-images.githubusercontent.com/45676906/140863271-f48b1f42-61e5-4795-8329-fdf2929e56f3.png">

그리고 `Swagger`를 실행해서 확인해보면 위와 같이 `Response Model`이 잘 보이는 것을 볼 수 있습니다. 

<br>

이번 글의 코드가 궁금하다면 [Github](https://github.com/wjdrbs96/blog-code/tree/master/Spring_Swagger_Response/src/main/java/com/swagger) 에서 확인하실 수 있습니다. 