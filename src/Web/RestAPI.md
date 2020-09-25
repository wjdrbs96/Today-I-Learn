## `REST API(Representational State Tranfer API)`

- 웹의 장점을 최대한 활용할 수 있는 아키텍처로써 발표되었다. 

<br>

### REST의 구성

- `Resource(자원)` - URI는 정보의 자원을 표현해야 한다.
- `Verb(행위)` = HTTP Method(GET, POST, PUT, DELETE 등)
- `Representations(표현)`

<br>

### API

- `Application Programming Interface`의 약자임
- API는 서버 사용 설명서


<br>

## REST API 중심 규칙 

### 1. URI는 정보의 자원을 표현해야 한다. (리소스명은 동사보다는 명사를 사용)

- `GET /members/show/1`  (x)
- `GET /members/1`       (o)

<br>

### 2.자원에 대한 행위는 HTTP Method로 표현

- `GET /members/insert/1`  (x)
- `GET /members/1`         (o) 

<br>

### 3. 주의 사항

- 슬래시 구분자(/)는 계층 관계를 나타내는데 사용 

```
http://test.com/sopt/server
http://test.com/animals/mammals/whales
```

- URI 마지막 문자로 슬래시(/)를 포함하지 않는다.

```
http://test.com/sopt/server/ (x)
http://test.com/sopt/server  (o)
```

- 하이픈(-)은 URI 가독성을 높이는데 사용

```
http://test.com/soptserverteam    (👎)
http://test.com/sopt-server-team  (👍)
```

- 밑줄(_)은 URI에 사용하지 않는다.

```
http://test.com/sopt_server_team   (x)
http://test.com/sopt-server-tema   (o)
```

- URI 경로에는 소문자가 적합하다.

```
http://test.com/SoptServerTeam    (x)
http://test.com/sopt-server-team  (o)
```

- 파일 확장자는 URI에 포함시키지 않는다. 

```
http://test.com/sopt/appjam.jpeg
```


<br>

## REST API


### Uniform Interface

- URI로 지정한 리소스에 대한 조작을 통일하고 한정적인 인터페이스로 수행하는 아키텍처 스타일

<br>

### Stateless

- HTTP Session과 같은 컨텍스트 저장소에 상태 정보를 저장하지 않음. 
- API 서버는 들어오는 요청 만을 들어오는 메세지로만 처리하면 되며 세션과 같은 정보를 신경 쓸 필요 없음

<br>

### Layer System

- REST 서버는 다중 계층으로 구성될 수 있으며 보안, 로드밸런싱, 암호화 게층을 추가해 구조상의 유연성을 둘 수 있다. 
- PROXY, 게이트웨이 같은 네트워크 기반의 중간 매체를 사용할 수 있음

<br>

### Self-descriptiveness

- REST API 메세지만 보고도 API를 쉽게 이해할 수 있음 (자체 표현 구조)

<br>

### Client-Server

- REST 서버는 API를 제공하고, 제공된 API를 이용해서 비즈니스 로직 처리 및 저장을 책임진다.
- 각각의 역할이 확실하게 구분되기 때문에 클라이언트와 서버에서 개발해야 할 내용이 명확해지고 서로의 의존성이 줄어든다. 

<br>

### Cacheable

- HTTP라는 기존의 웹 표준을 그대로 사용하기 때문에 캐싱 기능 적용 가능
