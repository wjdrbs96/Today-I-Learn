# `HTTP 헤더 용도`

- HTTP 전송에 필요한 모든 부가정보를 담습니다.
    - ex) 메세지 바디의 내용, 메세지 바디의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등등

    
![스크린샷 2021-05-25 오후 3 15 03](https://user-images.githubusercontent.com/45676906/119448265-04d33400-bd6c-11eb-815f-aa4da62ded25.png)

<br>

## `HTTP 헤더 분류`

- General 헤더: 메세지 전체에 적용되는 정보
- Request 헤더: 요청 정보 
- Response 헤더: 응답 정보
- `Entity 헤더: 엔티티 바디 정보 ex) Content-Type: text/html, Content-Length: 3423`

<br>

## `HTTP 바디`

<img width="627" alt="스크린샷 2021-05-25 오후 3 18 32" src="https://user-images.githubusercontent.com/45676906/119448546-790dd780-bd6c-11eb-8f82-ff00ec36fe3d.png">

- 메세지 본문은 엔티디 본문을 전달하는데 사용
- 엔티티 본문은 요청이나 응답에서 전달할 실제 데이터
- 엔티티 헤더는 엔티티 본문의 데이터를 해석할 수 있는 정보 제공
    - 데이터 유형(html, json), 데이터 길이, 압축 정보 등등
    
<br>

## `RFC723x 변화`

- 엔티티(Entity) -> 표현(Representation)
- Representation = representation Metadata + Representation Data
- 표현 = 표현 메타데이터 + 표현 데이터

<br>

## `HTTP 바디(RFC7230(최신)`

<img width="904" alt="스크린샷 2021-05-25 오후 3 22 02" src="https://user-images.githubusercontent.com/45676906/119448961-f6394c80-bd6c-11eb-878f-e85e3d23b586.png">

- 메세지 본문을 통해 표현 데이터 전달
- 메세지 본문 = 페이로드
- 표현은 요청이나 응답에서 전달할 실제 데이터
- 표현 헤더는 표현 데이터를 해석할 수 있는 정보 제공
    - 데이터 유형(html, json), 데이터 길이, 압축 정보 등등
    
<br>

## `표현`

- Content-Type: 표현 데이터의 형식
- Content-Encoding: 표현 데이터의 압축 방식
- Content-Language: 표현 데이터의 자연 언어
- Content-Length: 표현 데이터의 길이

<br>

## `Content-Type`

- 표현 데이터의 형식 설명
- `text/html`, `charset=utf-8`
- application/json
- image/png

<img width="669" alt="스크린샷 2021-05-25 오후 3 26 04" src="https://user-images.githubusercontent.com/45676906/119449358-85defb00-bd6d-11eb-9b34-2a879043ca32.png">

