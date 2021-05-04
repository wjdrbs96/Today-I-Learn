# `1. GET`

- `리소스 조회할 때 사용`
- 서버에 전달하고 싶은 데이터는 `쿼리 파라미터`, `쿼리 스트링`을 통해서 전달
- `메세지 바디(Request Body)`를 사용해서 데이터를 전달할 수 있지만, 지원하지 않는 곳이 많아서 권장하지 않음

<br>

# `2. POST`

- 요청 데이터 처리
- `메세지 바디를 통해 서버로 요청 데이터 전달`
- 서버는 요청 데이터를 처리
- 메세지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행한다. 
- `주로 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용`
    - 서버가 아직 식별하지 않은 새 리소스 생성
    - `POST의 결과로 새로운 리소스가 생성되지 않을 수도 있음`
    - 단순히 데이터를 생성하거나, 변경하는 것을 넘어서 프로세스를 처리해야 하는 경우(결제 완료 후 -> ex) 배달 시작 -> 배달 완료)
    - `JSON으로 조회 데이터를 넘겨야 하는데, GET 메소드를 사용하기 어려운 경우 -> 애매하면 POST`(조회이지만 POST 사용)
    
<br>

# `3. PUT`

- `리소스를 완전히 대체`
    - 리소스가 서버에 있으면 대체
    - 리소스가 서버에 없으면 생성
- `클라이언트가 리소스를 식별`(ex: PUT /members/100 HTTP/1.1)
    - 즉, POST는 Request Body에 데이터를 넣어서 보내지만 해당 데이터가 구체적으로 어떤 번호에 적재되는지 알 수 없음
    - 반면에 PUT은 100번의 리소스에 생성 or 업데이트 요청을 할 수 있음
    
<br>

<img width="1250" alt="스크린샷 2021-05-04 오전 10 49 35" src="https://user-images.githubusercontent.com/45676906/116952760-6b61b680-acc6-11eb-96bb-08725c306346.png">

<br>

<img width="1245" alt="스크린샷 2021-05-04 오전 10 49 44" src="https://user-images.githubusercontent.com/45676906/116952769-70bf0100-acc6-11eb-8a5e-564d76543959.png">

기존꺼를 지워버리고 새로운 것으로 대체하는 것을 볼 수 있습니다. 즉, PUT은 Resource를 수정한다기 보다는 새로운 것으로 바꿔치기 하는 것에 더 가까움
리소스를 수정하기 위해서는 `Patch`를 사용함

<br>

# `4. PATCH`

<img width="1292" alt="스크린샷 2021-05-04 오전 10 52 07" src="https://user-images.githubusercontent.com/45676906/116952887-c85d6c80-acc6-11eb-97ae-7a7a20e57890.png">

<br>

<img width="1265" alt="스크린샷 2021-05-04 오전 10 52 18" src="https://user-images.githubusercontent.com/45676906/116952897-cc898a00-acc6-11eb-932b-fdaaddc52bed.png">

위와 같이 `Patch`는 리소스의 부분적인 데이터를 수정할 수 있다.(간혹 서버에서 patch를 지원하지 않는 경우도 있는데.. 이럴 때는 POST 쓰기 POST 는 무적이다!!)

<br>

# `5. DELETE`

<img width="1286" alt="스크린샷 2021-05-04 오전 10 53 40" src="https://user-images.githubusercontent.com/45676906/116952949-fd69bf00-acc6-11eb-89d5-206b840fb14a.png">

<br>

<img width="1198" alt="스크린샷 2021-05-04 오전 10 53 45" src="https://user-images.githubusercontent.com/45676906/116952959-0064af80-acc7-11eb-8e3c-62650d429b9e.png">

DELETE를 사용해서 리소스를 삭제할 수 있습니다. 

