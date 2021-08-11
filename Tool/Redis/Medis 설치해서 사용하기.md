# `Medis 설치해서 사용하기!`

MySQL 같은 경우는 `WorkBench`가 있어서 손쉽게 데이터를 보고 관리할 수 있는데요. 하지만 Redis는 매번 `redis-cli`를 통해서 데이터를 확인하고 하는 것이 번거로웠습니다.
그러던 중에 Medis라는 Redis 데이터를 UI로 볼 수 있는 아주 좋은 것이 있어서 정리해보려고 합니다. 그래서 Mac에서 `Medis`를 설치해서 사용하는 법에 대해서 알아보겠습니다. 

![스크린샷 2021-08-10 오후 11 04 03](https://user-images.githubusercontent.com/45676906/128881271-75f41a2f-ec45-4ded-a6cc-0601cb7d362f.png)

먼저 `AppStore`에서 `Medis`를 검색하면 `Medis2 - GUI for Redis`가 나오는데요. 이것을 다운하겠습니다. 

<br> 

![스크린샷 2021-08-10 오후 11 07 13](https://user-images.githubusercontent.com/45676906/128881826-5015e222-9a0d-4bca-806a-ef63251b5b05.png)

그러면 위와 같은 화면이 뜨는데요. 위의 화면에서 다른 `ElastiCache`와 같은 것을 사용한다면 해당 URL을 적은 후에 `Connect`를 누르면 되고 local Redis를 사용할 것이라면 바로 `Connect`를 누르면 됩니다.

<br>

![스크린샷 2021-08-10 오후 11 10 41](https://user-images.githubusercontent.com/45676906/128882267-6b708cc3-de3b-48cb-acd2-334c04c350b9.png)

그러면 위와 같이 현재 Redis에 저장되어 있는 `Key`-`Value`를 `GUI`로 확인할 수 있습니다. (너무 편리한 거 같습니다!) 

<br>

![스크린샷 2021-08-10 오후 11 40 18](https://user-images.githubusercontent.com/45676906/128887272-2fbd4a81-5d62-4b4e-95c8-4bcc339f4669.png)

그리고 위와 같이 `Comman Query`를 눌러서 직접 Redis Query를 실행해볼 수 도 있습니다.