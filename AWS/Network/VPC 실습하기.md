# `VPC 실습하기`

VPC를 직접 만들어 보면서 실습을 진행해보겠습니다.

![스크린샷 2021-04-02 오후 3 51 37](https://user-images.githubusercontent.com/45676906/113389682-665dce80-93cb-11eb-8be2-6e53d9c9eb19.png)

![스크린샷 2021-04-02 오후 3 53 49](https://user-images.githubusercontent.com/45676906/113389897-c0f72a80-93cb-11eb-9bbf-6edeb5230ee3.png)

`IPv4 CIDR 블록`에서 위와 같이 `공인 IP`로 사용되지 않는 `사설 IP`를 등록하겠습니다. VPC의 개념과 CIDR의 개념을 잘 모르겠다면 [여기](https://devlog-wjdrbs96.tistory.com/290) 를 먼저 읽고 오시는 걸 추천드립니다.

![스크린샷 2021-04-02 오후 3 56 41](https://user-images.githubusercontent.com/45676906/113390147-1d5a4a00-93cc-11eb-8c46-ea5bd514a37b.png)

위와 같이 VPC가 잘 생성되었고 이번에는 `서브넷`을 설정해보겠습니다. 

<br>

## `서브넷 설정하기`

![스크린샷 2021-04-02 오후 3 58 36](https://user-images.githubusercontent.com/45676906/113390289-601c2200-93cc-11eb-9567-465d33ac27a0.png)

![스크린샷 2021-04-02 오후 3 59 59](https://user-images.githubusercontent.com/45676906/113390397-9659a180-93cc-11eb-9f1e-eedecd075fc6.png)

방금 위에서 만들었던 VPC 안에서 `서브넷`을 구성할 것이기 때문에 위에서 만든 VPC를 선택하겠습니다.

![스크린샷 2021-04-02 오후 4 02 37](https://user-images.githubusercontent.com/45676906/113390685-2bf53100-93cd-11eb-942c-2e0fc4c9b910.png)

보통 서브넷마다 가용영역을 다르게 주어 만드는데 지금은 VPC 안에 하나의 서브넷을 만드는 예시이기 때문에 가용영역 중 아무거나 선택하겠습니다. 그리고 `IPv4 CIDR`은 VPC의 /숫자 보다는 큰 값이어야 합니다. 왜냐하면 VCP 내부에서 서브넷을 만드는 것인데 서브넷이 VPC 보다 더 큰 범주가 되는 것은 말이 안됩니다.(50cm 박스에 70cm 박스를 넣을려는 것이라 생각하면 됩니다.)

그러면 서브넷도 생성이 되었습니다.  

![스크린샷 2021-04-02 오후 4 18 40](https://user-images.githubusercontent.com/45676906/113391936-48926880-93cf-11eb-8733-8d64cecebe02.png)

그리고 `라우팅 테이블`을 보겠습니다. 위와 같이 VPC를 만들면서 자동으로 라우팅 테이블이 만들어진 것을 볼 수 있습니다. 

<br>

## `EC2 생성하기`

![스크린샷 2021-04-02 오후 4 26 17](https://user-images.githubusercontent.com/45676906/113392804-a4a9bc80-93d0-11eb-9487-e078a4e2cde2.png)

기존에 만들었던 `VPC`, `서브넷`을 선택하겠습니다.

![스크린샷 2021-04-02 오후 4 27 24](https://user-images.githubusercontent.com/45676906/113392810-a6738000-93d0-11eb-8529-8962c2823970.png)

인스턴스의 보안그룹은 `80`, `433`, `22`번만 열어놓겠습니다. 이렇게 VPC 내부 서브넷 안에 인스턴스를 만들었습니다. 그 후에 외부와 소통을 하기 위해서는 인터넷 게이트웨이를 만들어주어야 합니다.

![스크린샷 2021-04-02 오후 4 33 44](https://user-images.githubusercontent.com/45676906/113393176-3f0a0000-93d1-11eb-984f-04ab9a12fc50.png)

![스크린샷 2021-04-02 오후 4 35 33](https://user-images.githubusercontent.com/45676906/113393345-809aab00-93d1-11eb-93a4-363640ea0ee5.png)

![스크린샷 2021-04-02 오후 4 36 26](https://user-images.githubusercontent.com/45676906/113393492-b50e6700-93d1-11eb-8b09-13310f7be55d.png)

새로 만든 인터넷 게이트웨이를 VPC와 연결하겠습니다. 그리고 이제 EC2의 EIP 까지 할당해주겠습니다.

<br>

## `네트워크 ACL 설정`

![스크린샷 2021-04-02 오후 4 40 28](https://user-images.githubusercontent.com/45676906/113393819-336b0900-93d2-11eb-8972-e1268690d752.png)

위와 같이 `서브넷`의 방화벽 역할을 하는 네트워크 ACL은 서브넷을 만들 때 자동으로 생성되어 있을 것입니다.

<img width="1416" alt="스크린샷 2021-04-02 오후 4 45 17" src="https://user-images.githubusercontent.com/45676906/113394225-d02da680-93d2-11eb-8336-ec95ae0b2dee.png">

인바운드, 아웃바운드 규칙을 보면 위와 같이 default로 설정이 되어 있을 것입니다.

