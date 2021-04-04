# `ELB(Elastic Load Balancer) 사용해보기`

<img width="481" alt="스크린샷 2021-04-04 오전 2 26 40" src="https://user-images.githubusercontent.com/45676906/113486350-31499d00-94ed-11eb-962e-ac020931ca08.png">

`Elastic Load Balancer`를 이용하면 적절히 부하를 EC2 인스턴스에 부하를 분산할 수 있습니다. 한번 실습을 해보는 시간을 가져보겠습니다. (EC2 인스턴스는 이미 존재한다고 가정하고 시작하겠습니다.)

<br>

## `ELB 대상 그룹 만들기`

![스크린샷 2021-04-04 오전 2 32 04](https://user-images.githubusercontent.com/45676906/113486475-06137d80-94ee-11eb-9d75-6fd519914b48.png)

EC2 인스턴스를 들어간 후에 왼쪽 아래에 `대상 그룹`으로 들어가보겠습니다. 



![스크린샷 2021-04-04 오전 2 28 36](https://user-images.githubusercontent.com/45676906/113486414-82f22780-94ed-11eb-91e2-ec8a9915d9f4.png)

![스크린샷 2021-04-04 오전 2 29 17](https://user-images.githubusercontent.com/45676906/113486426-98675180-94ed-11eb-87d3-e9c7deeeae4c.png)

Load Balancer 유형 중에서 `Apllication Load Balancer`를 사용해서 진행하겠습니다. 