## OAuth란?

> OAuth 2.0은 다양한 플랫폼 환경에서 권한 부여를 위한 산업 표준 프로토콜이다.

<br>

<img src="https://user-images.githubusercontent.com/45676906/95337006-57023a80-08ec-11eb-85ef-e2382a7ff310.png">

<br>

특정 서비스를 이용하다 보면 위와 같이 `네이버, 페이스북` 등 외부 서비스로 로그인을 했던 경험이 있을 것이다. 이렇게 외부 서비에서도 인증을 가능하게 하고
그 서비스의 API를 이용하게 해주는 것, 이것을 바로 `OAuth`라고 한다. 

<br>

### `OAuth 배경`

<img src="https://user-images.githubusercontent.com/45676906/95337742-1fe05900-08ed-11eb-8f62-a284e74b8173.png">

<br>

위의 그림을 보면서 이해해보자. 현재 사용자가 A의 어플리케이션을 이용하고 있을 때, 위에서 봤던 것처럼 외부서비스인 `네이버, 카카오, 페이스북`등을 로그인
해서 이용한다고 가정해보자. 

<br>

이 때 사용자가 입력한 `ID, Password`를 A 서비스가 알도록 하는 것은 보안상 매우 취약하다. 일단 A 서비스에 대한 신뢰가 없어
안전하다는 보장이 없다.

 
### 비밀번호 인증방식의 문제

- 신뢰 : 사용자가 애플리케이션에 ID/PW를 제공하기 꺼려한다.
- 접근범위가 늘어남에 따른 위험 부담 : IP/PW를 모두 알고 있는 애플리케이션은 모든 권한을 가진다.
- 신뢰성의 제한 - PW를 변경한다면 애플리케이션은 동작을 하지 못하게 된다.
- 폐기문제 : 권한을 폐기할 수 있는 유일한 방법이 PW를 변경하는 것이다.

<br>

### `Oauth 1.0`

<img src="https://user-images.githubusercontent.com/45676906/95339151-b6f9e080-08ee-11eb-9a31-9c1f6650739e.png">

<br>

### OAuth 1.0 단점

- 구현이 복잡하고 웹이 아닌 어플리케이션의 지원이 부족하다.
- HMAC을 통해 암호화를 하는 번거로운 과정을 겪는다.
- AccessToken이 만료되지 않는다.

<br>

### OAuth 2.0 

- 기능의 단순화, 기능과 규모의 확장성 등을 지원하기 위해 만들어 졌다
- 1.0a는 만들어진 다음 표준이 된 반면 2.0은 처음부터 표준 프로세스로 만들어짐.
- https가 필수여서 간단해 졌다.
- 암호화는 https에 맡김.
- 1.0a는 인증방식이 한가지였지만 2는 다양한 인증방식을 지원한다.
- API 서버에서 인증서버를 분리 할 수 있도록 해 놓았다.
  
<br>

### OAuth 2.0 구성

- Resource owner : 사용자
- Client : 앱
- Resource server (API server) : 자원을 호스팅하는 서버
- Authorization Server : 사용자의 동의를 받아서 권한을 부여하는 서버

<br>

### OAuth 인증프로세스 

<img src="https://user-images.githubusercontent.com/45676906/95340069-b31a8e00-08ef-11eb-846e-d778adc797b0.png">

<br>

자체 서비스에서는 발급받은 `AccessToken`만 내부적으로 저장하고 관리하면 된다.

<br>

### 인증 종류

- `OAuth 2.0의 인증종류는 아래 4가지이다.`   
    - Authorization Code Grant
    - Implicit Grant
    - Resource Owner Password Credentials Grant
    - Client Credentials Grant
    
<br>
    
|  | Confidential Client | Public Client |
|------|---|---|
|3-legged| Authorization Code | Implicit |
|2-legged| Resource Owner Password Credentials | Client Credentials |

<br>

### Authorization Code Grant

- 서버사이드 코드로 인증하는 방식
- 권한서버가 클라이언트와 리소스서버간의 중재역할
- AccessToken을 바로 클라이언트로 전달하지 않아 잠재적 유출을 방지한다. 
- 로그인시에 페이지 URL에 response_type=code 라고 넘긴다. 

<br>

### Implicit Grant

- token과 scope에 대한 스펙 등은 다르지만 OAuth 1.0a과 가장 비슷한 인증방식
- Public Client인 브라우저 기반의 어플리케이션(Javascript application)이나 모바일 어플리케이션에서 이 방식을 사용하면 된다.
- OAuth 2.0에서 가장 많이 사용되는 방식이다.
- 권한코드 없이 바로 발급되서 보안에 취약
- 주로 Read only인 서비스에 사용.
- 로그인시에 페이지 URL에 response_type=token 라고 넘긴다

<br>

### Password Credentials Grant

- Client에 아이디/패스워드를 저장해 놓고 아이디/패스워드로 직접 access token을 받아오는 방식이다.
- Client 를 믿을 수 없을 때에는 사용하기에 위험하기 때문에 API 서비스의 공식 어플리케이션이나 믿을 수 있는 Client에 한해서만 사용하는 것을 추천한다.
- 로그인시에 API에 POST로 grant_type=password 라고 넘긴다.

<br>

### Client Credentials Grant

- 어플리케이션이 Confidential Client일 때 id와 secret을 가지고 인증하는 방식이다.
- 로그인시에 API에 POST로 grant_type=client_credentials 라고 넘긴다.

### Reference

[https://showerbugs.github.io/2017-11-16/OAuth-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C](https://showerbugs.github.io/2017-11-16/OAuth-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C)
