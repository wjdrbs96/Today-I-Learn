# `Kubernetes 아키텍쳐`

<img width="838" alt="스크린샷 2022-08-15 오후 9 01 49" src="https://user-images.githubusercontent.com/45676906/184631635-d80a0d5d-7373-4c8b-98ab-c09b21b0e22f.png">

쿠버네티스는 `상태 체크 -> 차이점 발견 -> 조치` 하는 과정을 반복함. 즉, 파드가 죽어도 다시 쿠버네티스에서 자동으로 파드를 새로 띄워줌

<br>

<img width="1246" alt="스크린샷 2022-08-15 오후 9 05 30" src="https://user-images.githubusercontent.com/45676906/184632113-34e15553-214d-4728-8f65-c17b36eb8dac.png">

<br>

<img width="1144" alt="스크린샷 2022-08-15 오후 9 06 49" src="https://user-images.githubusercontent.com/45676906/184632244-0799e6ab-91fb-4e31-968f-bb63e26bee93.png">

<br>

### `Master 상세 - etcd`

- 모든 상태와 데이터를 저장
- 분산 시스템으로 구성하여 안전성을 높임 (고가용성)
- 가볍고 빠르면서 정확하게 설계 (일관성)
- Key - Value 형태로 데이터 저장
- TTL, watch 같은 부가 기능 제공
- 백업 필수

<br>

### `Master 상세 - API server`

- 상태를 바꾸거나 조회
- etcd와 유일하게 통신하는 모듈
- REST API 형태로 제공
- 권한을 체크하여 적절한 권한이 없을 경우 요청을 차단
- 관리자 요청 뿐 아니라 다양한 내부 모듈과 통신
- 수평으로 확장되도록 디자인

<br>

### `Master 상세 - Scheduler`

- 새로 생성된 Pod를 감지하고 실행할 노드를 선택
- 노드의 현재 상태와 Pod의 요구사항을 체크

<br>

### `Master 상세 - Controller`

논리적으로 다양한 컨트롤러가 존재

- 북제 컨트롤러
- 노드 컨트롤러
- 엔드포인트 컨트롤러

끊임 없이 상태를 체크하고 원하는 상태를 유지

<br>

### `Node 상세 - kubelet`

- 각 노드에서 실행
- Pod를 실행/중지하고 상태를 체크

<br>

### `Node 상세 - proxy`

- 네트워크 프록시와 부하 분산 역할
 
<br>

## `Pod`

- 가장 작은 배포 단위
- 전체 클러스에서 고유한 IP를 할당
- 여러 개의 컨테이너가 하나의 Pod에 속할 수 있음

<img width="971" alt="스크린샷 2022-08-15 오후 9 22 07" src="https://user-images.githubusercontent.com/45676906/184634285-6b77ab42-3a43-419d-bf90-0b8cf442ff4a.png">

<br>

## `ReplicaSet`

- 여러 개의 Pod를 관리

<img width="710" alt="스크린샷 2022-08-15 오후 9 23 24" src="https://user-images.githubusercontent.com/45676906/184634423-83847762-3b7d-424c-bb71-a84b310542e8.png">

<br>

## `Deployment`

- 배포 버전을 관리

<img width="493" alt="스크린샷 2022-08-15 오후 9 23 52" src="https://user-images.githubusercontent.com/45676906/184634488-5a18574e-87c4-4abf-8651-e020b9de9d01.png">

<br>

<img width="627" alt="스크린샷 2022-08-15 오후 9 24 27" src="https://user-images.githubusercontent.com/45676906/184634549-c5c11ad9-de90-419e-9d07-a7b3f48dd817.png">

버전 2로 올리면 ReplicaSet을 하나 더 만들어서 배포를 진행함

<br>

## `Service - ClusterIP`

<img width="348" alt="스크린샷 2022-08-15 오후 9 26 37" src="https://user-images.githubusercontent.com/45676906/184634811-4ee08979-9786-431f-b895-21a8d4485c8a.png">

- 클러스터 내부에서 사용하는 프록시
- pod를 로드밸런서 하는 별도의 서비스 (서비스는 고정 IP이고 Pod는 IP가 자주 바뀌기 때문에 Service 사용)

<br>

<img width="899" alt="스크린샷 2022-08-15 오후 9 28 31" src="https://user-images.githubusercontent.com/45676906/184635041-0c0e2ecb-5eb4-43a1-b422-c112ff4ff60b.png">

- 클러스터 내부에서 서비스 연결은 DNS를 이용

<br>

## `Service - NodePort`

<img width="1067" alt="스크린샷 2022-08-15 오후 9 31 11" src="https://user-images.githubusercontent.com/45676906/184635409-c0929dc4-cb19-40fa-800d-633b2cb29132.png">

- 노드(Host)에 노출되어 외부에서 접근 가능한 서비스
- ClusterIP는 내부에서만 사용하고 외부에서 접근할 수 없다는 문제가 있음

<br>

## `Service - LoadBalancer`

<img width="949" alt="스크린샷 2022-08-15 오후 9 32 06" src="https://user-images.githubusercontent.com/45676906/184635537-8786daf7-f9b9-445b-9f20-eaa3b4bc6f46.png">

- 로드밸런서 하나만 외부 IP로 노출

<br>

## `Ingress`

<img width="721" alt="스크린샷 2022-08-15 오후 9 32 42" src="https://user-images.githubusercontent.com/45676906/184635613-9216fbc7-7104-46d2-ba68-0c9092d8d0f0.png">

- 도메인 또는 경로별 라우팅

<br>

# `API 호출`

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: frontend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: wed
        image: image:v1
```

- yaml을 통해서 API 호출을 한다.

<br>

