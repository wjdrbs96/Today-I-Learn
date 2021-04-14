# `AWS 인증서로 HTTPS 적용해보기`

HTTPS를 적용하려면 도메인 주소가 필요합니다. 만약 도메인 주소가 없다면 [여기](https://devlog-wjdrbs96.tistory.com/292) 에서 먼저 만들고 와야 합니다. (AWS Route 53을 이용해서 도메인을 만들었습니다.)

AWS를 이용하지 않는다면 다른 인증 기관에서 SSL 인증서를 발급 받아서 등록하는 과정을 거쳐야 할 것입니다. 그런데 인스턴스가 여러 개라면 인스턴스마다 인증서를 받아 등록하는 과정이 매우 번거로울 것입니다. 

또한 각 인증서마다 만료 기간을 알고 있어야 하기 때문에 매 번 그 기간엔 맞춰서 갱신을 해줘야 합니다. 재 기간에 갱신하지 못하면 사이트가 접속이 되지 않는 큰 문제가 발생할 수 있습니다. 그래서 이번 글에서는 이러한 것들을 편리하게 해주는 `ACM(Amazon Certificate Manager)`를 이용해서 인증서를 발급받아 사용하겠습니다. 

<br>

## `ACM의 장점과 단점`

- `기존에는 인스턴스 하나하나 적용했던 SSL을 Route 53, ELB를 통해서 하나의 인증서로 여러 인스턴스에 적용할 수 있게 해줍니다.`
- `도메인 소유자에게 이메일 형식으로 인증을 받아서 빠르게 승인만 하면 인증서를 발급 받고 사용할 수 있습니다.`
- `인증서의 만료기간을 기억하고 있지 않아도 자동 갱신을 해주기 때문에 매우 편리합니다.`
- `비용이 무료입니다.`
- `하지만 ACM을 사용하려면 ELB를 꼭 사용해야 한다는 단점이 존재합니다.` 

그럼에도 ACM은 장점이 더 큰 것 같아 이번 글에서는 아래의 순서대로 정리를 해보겠습니다. 

1. AWS Route 53을 통해 도메인 구입
2. 구입한 도메인으로 AWS Route 53 연결
3. AWS ACM을 통해서 구입한 도메인의 SSL Certificate 발급
4. ELB에 SSL Listener 생성
5. 최종 SSL 적용 


<br>

## `Amazon Certificate Manager 발급 받기`

![1](https://user-images.githubusercontent.com/45676906/113504800-6ac5eb00-9575-11eb-889f-aa7829dddc27.png)

![2](https://user-images.githubusercontent.com/45676906/113506026-2fc7b580-957d-11eb-846d-994515481a3b.png)

![3](https://user-images.githubusercontent.com/45676906/113506031-39511d80-957d-11eb-9271-5c6bf178d1c7.png)

기존에 Route 53으로 만들었던 도메인 주소를 입력하겠습니다.

![4](https://user-images.githubusercontent.com/45676906/113506041-4d951a80-957d-11eb-86e8-d018965d182c.png)

저는 `DNS 검증`을 체크하고 다음으로 넘어가겠습니다.

![5](https://user-images.githubusercontent.com/45676906/113506053-6b627f80-957d-11eb-8d87-5629c77cc37e.png)

그리고 `Route 53 레코드 생성`을 누르고 생성하겠습니다.

![6](https://user-images.githubusercontent.com/45676906/113506074-8503c700-957d-11eb-8b75-8f5b4d53c182.png)

그러면 위와 같이 `Amazon 인증서`가 잘 발급된 것을 볼 수 있습니다. 

<br>

## `ELB 생성하기`

![7](https://user-images.githubusercontent.com/45676906/113506209-67832d00-957e-11eb-89b6-b5a6791726ac.png)

여기서 `리스너`는 사용자가 ELB에 접속할 포트를 지정하는 거 같습니다.(살짝 헷갈리긴 하지만,,) 그래서 HTTPS를 적용할 것이기 때문에 위와 같이 선택하고 다음으로 가겠습니다.

![8](https://user-images.githubusercontent.com/45676906/113506244-99948f00-957e-11eb-82b0-99661ba9a0d5.png)

가용 영역은 모두 활성화 시키겠습니다.

![9](https://user-images.githubusercontent.com/45676906/113506256-a87b4180-957e-11eb-84b9-86c938b81369.png)

그리고 아까 위에서 발급 받았던 `AWS 인증서`를 선택하고 다음으로 누르겠습니다. 

![10](https://user-images.githubusercontent.com/45676906/113506275-b92bb780-957e-11eb-96f6-0abc2ccca199.png)

그리고 이번에는 `ELB`의 방화벽을 설정하는 보안그룹 입니다. 위와 같이 ELB의 80, 433 포트를 열겠습니다. 

![11](https://user-images.githubusercontent.com/45676906/113506332-36572c80-957f-11eb-915a-7fba345d1a11.png)

- `대상 그룹 포트`: 대상 그룹에 연결된 EC2 인스턴스들의 80번 포트로 로드 밸런싱에 트래픽을 보내겠다는 뜻입니다.
- `상태 검사 경로`: 지정한 경로로 주기적으로 로드 밸런싱이 대상 그룹 안에 인스턴스들에게 요청을 보냅니다. 응답이 제대로 오면 health 상태이고 제대로 오지 않으면 unhealth 한 상태가 됩니다.

![12](https://user-images.githubusercontent.com/45676906/113506917-87b4eb00-9582-11eb-83d2-0ce41d531765.png)

![스크린샷 2021-04-04 오후 7 53 02](https://user-images.githubusercontent.com/45676906/113506924-93a0ad00-9582-11eb-960a-b5f09aca77c6.png)

위와 같이 사용할 인스턴스를 `대상 그룹` 안에다 추가 하겠습니다. 

<br>

## `Route 53 설정하기`

![13](https://user-images.githubusercontent.com/45676906/113506938-b6cb5c80-9582-11eb-84db-039cd53a5eb5.png)

![14](https://user-images.githubusercontent.com/45676906/113507015-f98d3480-9582-11eb-9d86-dd8a2b6fec82.png)

기존에 만들었던 도메인에 ELB를 연결시키는 작업을 하겠습니다. 

![15](https://user-images.githubusercontent.com/45676906/113507028-10338b80-9583-11eb-8a1b-e95a3ea33f29.png)

![16](https://user-images.githubusercontent.com/45676906/113507041-29d4d300-9583-11eb-9580-02033f28743d.png)

그리고 ELB의 보안그룹을 EC2 인스턴스에게도 추가 하겠습니다.

![스크린샷 2021-04-04 오후 8 21 01](https://user-images.githubusercontent.com/45676906/113507073-50930980-9583-11eb-92f5-3600e4bd576a.png)

그러면 위와 같이 `HTTPS`가 잘 적용된 것을 볼 수 있습니다. 

++) 중간 중간 설명이 부족했던 부분은 좀 더 공부하면서 보완해 나갈 에정입니다!