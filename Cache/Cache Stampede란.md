## `캐시 성능 관련 정리`

- [NHN Meetup](https://meetup.nhncloud.com/posts/251)을 정리한 글이다.

<br>

### `Cache Stampede`

![image](https://user-images.githubusercontent.com/45676906/210738109-5dfdb293-e7f8-4c62-a7f3-1f8675cab658.png)

이 구조에서 레디스는 캐시로, DB 앞단에서 분산된 서버들의 요청을 받고 있다. 

캐시 키가 만료되는 시점에 read-through 구조에서 레디스에 데이터가 없을 때 서버들은 직접 DB에 가서 데이터를 읽어와 레디스에 저장한다. 

캐시 키가 만료되는 순간 많은 서버에서 이 키를 참조하는 시점이 겹치게 된다. 모든 서버들이 DB에 가서 데이터를 질의하는 duplicate read와 그 값을 반복적으로 레디스에 write하는 duplicate write가 발생하게 된다.

<br>

### `Hot Keys`

하나의 키에 대한 접근이 너무 많을 때에도 문제가 발생하며, 이 현상 또한 캐시 성능을 저하시킬 수 있다.

![image](https://user-images.githubusercontent.com/45676906/210735809-00be4aa6-c90e-47dd-9b63-db09a9d10c04.png)

Hot Key 문제가 발생하면, 가장 쉽게 생각 할 수 있는 대안은 읽기 분산이다. 

하나의 마스터에 여러개의 슬레이브를 추가하고, 어플리케이션에서는 여러대의 서버에서 데이터를 읽어오는 방식이다. 하지만 이런 구성에서 장애가 발생해서 페일오버가 발생하게 된다면 상황이 복잡해진다. 생각지도 못한 장애와 병목이 발생할 가능성이 존재한다.

<br>

![image](https://user-images.githubusercontent.com/45676906/210738830-877804ae-3d20-433e-8cd3-1268c114eb08.png)

- `L1 Cache`: EHCache
- `L2 Cache`: Redis
- `DB`: MySQL

위와 같은 구조로 캐시를 많이 구축할 것이다. 레디스와 DB 사이의 Stampede 이슈는 L1와 L2사이에서도 반복될 수 있다는 것을 기억해두자.

<br>

### `Referenece`

- [https://meetup.nhncloud.com/posts/251](https://meetup.nhncloud.com/posts/251)