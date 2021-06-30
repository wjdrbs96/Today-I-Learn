# `Spring Logback으로 Slack에 Error log 전송하는 법`

[Spring logback으로 Slack에 Error log 알림 보내는 법](https://devlog-wjdrbs96.tistory.com/327) 에 대해서 정리한 적이 있습니다. 이 때는 단순히 `log.error()`에 출력된 것을 Slack으로 보내는 것을 해보았는데요. 
이번 글에서는 `log.error()`로 찍히는 것을 보내는 것은 같지만, 실제 클라이언트의 요청 URL, URI, RequestBody의 값을 어떤 것들을 출력하는지와 Response 값은 어떻게 오는지에 대해 Slack으로 알림을 보내는 법에 대해서 정리해보겠습니다. 

그러기 위해서는 위의 블로그에서 먼저 기본 설정들을 다 하고 오셔야 합니다. 

<br>

## `Spring Interceptor란?`

클라이언트의 요청 URL, URI, RequestBody, Response 등등을 로그로 출력하기 위해서 `Interceptor`를 사용할 것입니다. 

![interceptor](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

Interceptor는 위의 보이는 그림처럼 AOP 앞단에 위치해있습니다. 그럼 이제 Interceptor 설정부터 해서 어떻게 사용해서 log를 출력하는지 알아보겠습니다. 

<br>

