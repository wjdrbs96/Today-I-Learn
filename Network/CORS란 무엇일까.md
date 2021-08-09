# `들어가기 전에`

주소가 A인 사이트에서 주소가 B인 사이트에 API로 정보를 받아오기 위해 프론트에서 HTTP 요청을 보냈을 때 미리 어떤 설정을 해주지 않으면 `CORS`로 요청이 막히게 됩니다. 
여기서도 PostMan 같은걸로는 되는데 `브라우저`에서 요청을 하면 안되는 것을 경험할 수 있습니다.

즉, `크롬`, `사파리`와 같이 브라우저에서 요청을 막는 것입니다. 브라우저가 우리가 방문하는 사이트를 믿지 못해서 막고 있는 것인데요. 왜 이렇게 막는 것일까요?

만약 문제가 없는 올바른 A 라는 사이트를 이용하면서 `로그인`을 한 후에 `자동 로그인`을 이용하고 있다고 생각해보겠습니다. 자동 로그인을 유지하기 위해서는 `쿠키`, `세션` or `JWT`와 같은 정보들을 서버와의 인증을 위한 통신이 필요할 것입니다.(중요한 정보일텐데요.)

그런데 만약 어떤 문제가 있는 해킹하는 사람들이 똑같은 형태의 B 라는 사이트를 만들어 우리에게 접속하도록 보내서 우리가 접속했다고 가정하겠습니다. 그러면 A 사이트를 접속할 때처럼 B 사이트를 접속할 때는 `중요한 인증 정보`를 보내게 될 것인데요. 즉, 중요한 정보들이 `탈취 당하게 되는 것`입니다.
이렇게 탈취 당한 정보를 통해서 해커들이 실제 A 사이트에서 나쁜 일을 할 수 있게되는 것입니다.

그래서 이렇게 CORS와 같은 것들이 필요했던 것인데요. 그런데 정확히 말하면 CORS는 풀어주는 것이고, 막는 것은 `SOP(Same Origin Policy)`가 하는 것입니다. 즉, 동일한 URL 끼리만 API 요청이 가능하도록 하는 것입니다.

<br> <bR>

## `CORS란 무엇일까?`

CORS(Cross Origin Resource Sharing)는 추가 HTTP 헤더를 사용하여 한 출처에서 실행 중인 웹 애플리케이션이 다른 출처의 선택한 자원에 접근할 수 있는 권한을 부여하도록 브라우저에 알려주는 체제입니다. 
즉, 다른 출처간의 요청이 가능하도록 허용해주는 것입니다.

<br> <br>

## `URL 구조`

![스크린샷 2021-08-09 오후 1 46 37](https://user-images.githubusercontent.com/45676906/128661369-002c475d-1d1f-4a60-89d3-c4b1f3e6fab3.png)

여기서 `Protocol` + `Host` + `Port`가 같으면 `동일한 출처`라고 얘기를 합니다.

| URL | 결과 | 이유 |
|-----------|-------|------|
| https://gyunny.io/test | 같은 출처 | Protocol, Host, Port 동일 |
| https://gyunny.io/test?q=work | 같은 출처 | Protocol, Host, Port 동일 |
| http://gyunny.io/test | 다른 출처 | Protocol 다름 |
| http://gyunny.com/test | 다른 출처 | Host 다름 |

<br> <br>

# CORS 동작 원리

## 단순 요청(Simple Requests)

<img width="791" alt="스크린샷 2021-08-09 오후 2 15 28" src="https://user-images.githubusercontent.com/45676906/128662896-89904d60-9b83-460a-9aa4-d0a963dd5ef3.png">

단순 요청은 서버에 API를 요청하고, 서버는 `Access-Control-Allow-Origin` 헤더를 포함한 응답을 브라우저에 보냅니다. 브라우저는 `Access-Control-Allow-Origin` 헤더를 확인해서 CORS 동작을 수행할지 판단합니다.

<br>

### Simple request 조건

- 요청 메서드(method)는 GET, HEAD, POST 중 하나여야 합니다.
- Accept, Accept-Language, Content-Language, Content-Type, DPR, Downlink, Save-Data, Viewport-Width, Width를 제외한 헤더를 사용하면 안 됩니다.
- Content-Type 헤더는 application/x-www-form-urlencoded, multipart/form-data, text/plain 중 하나를 사용해야 합니다.

<br> <br>

## 프리플라이트(Preflighted) 요청

Preflight 요청은 서버에 예비 요청을 보내서 안전한지 판단한 후 본 요청을 보내는 방법입니다. 

<img width="566" alt="스크린샷 2021-08-09 오후 2 33 35" src="https://user-images.githubusercontent.com/45676906/128663868-79411a72-7317-47c1-b1f5-25597da7da22.png">

Preflight 요청은 실제 리소스를 요청하기 전에 OPTIONS라는 메서드를 통해 실제 요청을 전송할지 판단합니다. OPTIONS 메서드로 서버에 예비 요청을 먼저 보내고, 서버는 이 예비 요청에 대한 응답으로 `Access-Control-Allow-Origin` 헤더를 포함한 응답을 브라우저에 보냅니다. 브라우저는 단순 요청과 동일하게 Access-Control-Allow-Origin 헤더를 확인해서 CORS 동작을 수행할지 판단합니다.

- Reference: [https://beomy.github.io/tech/browser/cors/](https://beomy.github.io/tech/browser/cors/)
