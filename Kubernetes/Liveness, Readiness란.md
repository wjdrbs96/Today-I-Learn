## `Liveness, Readiness란?`

### `Liveness probe`

애플리케이션의 상태를 체크해서 서버가 제대로 응답하는지 혹은 컨테이너가 제대로 동작중인지를 검사한다. 

Pod은 정상적인 Running 상태이지만, 애플리케이션에 문제가 생겨서 접속이 안되는 경우를 감지한다. (메모리 오버플로우 등) 문제를 감지하면 Pod을 죽이고 재실행하여 애플리케이션의 문제를 해결한다.

<br>

### `Readiness probe`

컨테이너가 요청을 처리할 준비가 되었는지 확인하는 probe이다. Pod이 새로 배포되고 Running 상태여도 처음에 로딩하는 시간이 있기 때문에 이 시간 동안은 애플리케이션에 접속하려고 하면 오류가 발생한다. 

Readiness probe는 어플리케이션이 구동되기 전까지 서비스와 연결되지 않게 해준다. (Readiness Probe가 실패할 때 엔드포인트 컨트롤러가 파드에 연관된 모든 서비스들의 엔드포인트에서 파드의 IP주소를 제거한다. ) 

Liveness Probe와 비교했을 때 어떤 차이가 있냐면, Liveness Probe는 probe 핸들러 조건 아래 fail이 나면 pod을 재실행 시키지만 Readiness Probe는 pod을 서비스로부터 제외시킨다.