# `AWS Auto-Scaling 시작하기 - 1부`

![스크린샷 2021-04-09 오후 4 08 01](https://user-images.githubusercontent.com/45676906/114142317-c5808d80-994d-11eb-8f61-4ca778994269.png)

`AWS Auto Scaling`을 들어가보면 `시작 구성`, `Auto Scaling 그룹`이 있습니다. 

- `시작 구성`: Auto Scaling을 할 때 인스턴스를 만들 때 어떤 이미지에 기반해서 만들 것인가를 설정합니다.(성능, 용량, 운영체제 등등) 
- `Auto Scaling 그룹`: 언제, 어떤 조건에서 Auto Scaling으로 인스턴스를 생성하고, 줄일지를 정하는 것입니다. 

<br>

## `시작 구성 만들기`

![스크린샷 2021-04-09 오후 4 30 28](https://user-images.githubusercontent.com/45676906/114145102-0928c680-9951-11eb-8263-7d2516dec22d.png)

나머지는 전부 Default로 설정하겠습니다. (보안그룹, 용량, 키페어는 본인의 상황에 맞게 설정하시면 됩니다.) 그러면 쉽게 생성할 수 있습니다. 

<br>

## `Auto Scaling Group 만들기`

![스크린샷 2021-04-09 오후 4 37 52](https://user-images.githubusercontent.com/45676906/114146025-0e3a4580-9952-11eb-8680-5eb70f4a7efd.png)

![스크린샷 2021-04-09 오후 4 39 56](https://user-images.githubusercontent.com/45676906/114146186-42ae0180-9952-11eb-974c-e8162bc8e461.png)

![스크린샷 2021-04-09 오후 4 40 44](https://user-images.githubusercontent.com/45676906/114146344-70934600-9952-11eb-866b-d5d2eab1e8bc.png)

![스크린샷 2021-04-09 오후 4 42 14](https://user-images.githubusercontent.com/45676906/114146596-c0720d00-9952-11eb-9f55-deb50376e734.png)

![스크린샷 2021-04-09 오후 4 45 21](https://user-images.githubusercontent.com/45676906/114147027-3d04eb80-9953-11eb-89e3-f550f5e491f3.png)

그리고 위와 같이 만든 `시작 템플릿`을 등록하겠습니다. 

![스크린샷 2021-04-09 오후 4 48 17](https://user-images.githubusercontent.com/45676906/114147358-9836de00-9953-11eb-8a9e-9721da2a738c.png)

그리고 위와 같이 `가용영역` 두 개를 지정하겠습니다. 이렇게 두개를 지정하면 Auto Scaling을 통해 인스턴스를 만들 때 가용영역을 번갈아 가면서 만들기 때문에 하나의 가용영역에 문제가 생겨도 상대적으로 영향을 덜 받을 수 있게 됩니다. 

![스크린샷 2021-04-09 오후 4 53 01](https://user-images.githubusercontent.com/45676906/114147927-2d39d700-9954-11eb-9b49-efbb6e5c82a9.png)

기존에 만들었던 `로드 밸런서`를 연결하겠습니다. 

![스크린샷 2021-04-12 오전 11 09 15](https://user-images.githubusercontent.com/45676906/114331711-95203580-9b7f-11eb-8432-f3fc444d4d2d.png)

그 외에 설정은 Default로 두고 다음을 누르겠습니다. 

![스크린샷 2021-04-09 오후 5 03 06](https://user-images.githubusercontent.com/45676906/114149222-a38b0900-9955-11eb-96b4-427f402d7754.png)

Auto Scaling 그룹의 크기를 구성하려면` 최소, 최대 및 원하는 용량`을 설정합니다. 원하는 용량은 그룹의 최소 크기보다 크거나 같고 그룹의 최대 크기보다 작거나 같아야 합니다.

`Auto Scaling 그룹은 원하는 용량에 지정된 만큼 인스턴스를 시작하여 시작합니다.` Auto Scaling 그룹에 연결된 조정 정책 또는 예약된 작업이 없는 경우 는 원하는 인스턴스 양을 Amazon EC2 Auto Scaling 유지하여 그룹의 인스턴스에 대한 주기적인 상태 확인을 수행합니다. 비정상 인스턴스는 종료되고 새 인스턴스로 대체됩니다.

Auto Scaling 그룹은 최소 용량과 최대 용량에 서로 다른 값을 갖는 한 탄력적입니다. Auto Scaling 그룹의 원하는 용량을 변경하라는 모든 요청(수동 조정 또는 Auto Scaling)은 이러한 제한 내에 있어야 합니다.

그룹을 자동으로 확장하도록 선택한 경우 최대 제한을 사용하면 에서 수요 증가를 처리하기 위해 필요에 따라 인스턴스 수를 Amazon EC2 Auto Scaling 확장할 수 있습니다. 최소 제한은 항상 일정 수의 인스턴스가 실행되도록 하는 데 도움이 됩니다.

![스크린샷 2021-04-09 오후 5 06 11](https://user-images.githubusercontent.com/45676906/114149666-1dbb8d80-9956-11eb-8119-60a03ebd88ea.png)

![스크린샷 2021-04-09 오후 5 08 27](https://user-images.githubusercontent.com/45676906/114149863-55c2d080-9956-11eb-967a-f21cf7eb0347.png)

![스크린샷 2021-04-09 오후 5 10 56](https://user-images.githubusercontent.com/45676906/114150053-8d317d00-9956-11eb-83dd-eccdb857deb6.png)

그러면 위와 같이 `Auto Scaling` 그룹이 잘 만들어진 것을 볼 수 있습니다. 

