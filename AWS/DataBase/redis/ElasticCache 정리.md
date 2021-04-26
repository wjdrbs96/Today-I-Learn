# `들어가기 전에`

이번 글에서는 Redis가 무엇인지 아주 간단하게 알아보고 `AWS ElasticCache`를 사용해서 간단한 실습을 진행해보겠습니다. 

<br>

## `키-값 모델 NoSQL`

키-값 모델 NoSQL은 가장 기본적인 형태의 NoSQL이며, 키 하나로 데이터 하나를 저장하고 조회할 수 있는 단일 키-값 구조를 가집니다. 키-값 모델 NoSQL로는 대표적으로 `Redis`가 있습니다. 즉, 레디스를 한마디로 표현하면 아래와 같습니다. 

> Redis는 다양한 자료구조를 저장하고 조회할 수 있으며 다양한 종류의 조회 방법을 지원하는 인메모리 NoSQL 입니다. 

<br>

## `Redis란 무엇일까?`

Redis는 Memcached와 비슷한 `캐시 시스템`으로서 동일한 기능을 제공하면서 영속성, 다양한 데이터 구조와 같은 부가적인 기능을 지원하고 있습니다. 

`레디스는 모든 데이터를 메모리에 저장하고 조회`합니다. 즉, `인메모리 데이터베이스` 입니다. 이 말만 들으면 Redis를 모든 데이터를 메모리에 저장하는 빠른 DB가 다라고 생각할지도 모릅니다. 하지만 빠른 성능은 레디스의 특징 중 일부분 입니다. 다른 인메모리 디비들과의 가장 큰 차이점은 `레디스의 다양한 자료구조` 입니다. 

NoSQL로서 Key-Value 타입의 저장소인 `레디스(Redis, Remote Dictionary Server)`의 주요 특징은 아래와 같습니다. 

- `영속성을 지원하는 인메모리 데이터 저장소`
- `읽기 성능 증대를 위한 서버 측 복제를 지원`
- `쓰기 성능 증대를 위한 클라이언트 측 샤딩(Sharding) 지원`
- `다양한 서비스에서 사용되며 검증된 기술`
- `문자열, 리스트, 해시, 셋, 정렬된 셋과 같은 다양한 데이터형을 지원. 메모리 저장소임에도 불구하고 많은 데이터형을 지원하므로 다양한 기능을 구현`

최종적으로 Redis를 한 문장으로 정의하면 아래와 같습니다. 

> 레디스는 고성능 키-값 저장소로서 문자열, 리스트, 해시, 셋, 정렬된 셋 형식의 데이터를 지원하는 NoSQL이다.

<br>

간단하게 이론에 대해서 정리하면 위와 같습니다. 그런데 RDBMS, NoSQL을 보다보면 클러스터, 노드, [샤딩](https://cla9.tistory.com/102) 등등 약간 어려운 단어들이 등장합니다. 이러한 단어들을 자세히 알지 못하다 보니 전체적으로 이해하는데 한계가 있어서 한번 정리를 하고 가겠습니단. 

<br>

## `Cluster란?`

> 레디스 클러스터는 여러 레디스 노드상에서 데이터를 자동으로 분할하는 방법을 제공한다.  
> 특징으로는 여러대의 서버에 데이터가 분산되어 저장되므로 트래픽이 분산되는 효과를 얻을 수 있습니다. <br> 
> 가장 중요한 점은 특정 서버에 장애가 일어나더라도 백업 서버의 보완을 통해 데이터의 유실없이 서비스를 계속 이어나갈 수 있다는 점입니다.

<img width="727" alt="스크린샷 2021-04-08 오후 3 54 16" src="https://user-images.githubusercontent.com/45676906/113981468-ad900780-9882-11eb-87d1-ec2e55eb4e4c.png">

즉, 위와 같이 클러스터 안에 여러 개의 노드들이 존재하는 구조입니다. 그런데 위와 같이 하나의 Master 클러스터만 만들고 사용하는 것은 좋은 방법이 아닙니다. 

<br>

## `Master 클러스터만 사용하면 어떤 문제점이 있을까요?`

위와 같이 클러스터를 Master로만 구성하면 노드 중 하나에만 장애가 발생해도 해당 노드의 데이터에 데이터 유실이 발생합니다. 즉, Master-Slave 관계를 만들어야 합니다.  예를들어, MySQL 같은 경우는 아래와 같이 Master-Slave를 구성합니다. 

MySQL은 확장성을 위한 다양한 기술을 제공하는데 그중에서 가장 일반적인 방법이 `복제(Replication)` 입니다. MySQL의 복제는 `레플리케이션(Replication)` 이라고도 하는데, `복제는 2대 이상의 MySQL 서버가 동일한 데이터를 담도록 실시간으로 동기화하는 기술`입니다. 일반적으로 MySQL 복제에는 INSERT나 UPDATE, DELETE와 같은 쿼리를 이용해 `변경할 수 있는 MySQL 서버`와 SELECT 쿼리로 데이터를 `읽기만 할 수 있는 MySQL 서버`로 나뉩니다. 

MySQL에서는 쓰기와 읽기의 역할로 구분해 전자를 `마스터(Master)`라고 하고 후자를 `슬레이브(Slave)`라고 합니다.

즉, 이와 같이 구성을 하면 Master에 장애가 발생해도 Slave를 Master 노드로 승격시키면 됩니다. 

<img width="723" alt="스크린샷 2021-04-08 오후 4 00 21" src="https://user-images.githubusercontent.com/45676906/113982273-871e9c00-9883-11eb-9790-c53e99a0c159.png">

<br> <br>


# `AWS ElasticCache란?`

AWS 서비스에서 Redis를 사용하기 위해서는 `ElasticCache`를 사용할 수 있습니다. ElasticCache는 `Memcached`, `Redis`를 지원합니다. 이번 글에서는 Redis로 실습을 해보겠습니다. 

여기서도 위에서 보았던 것처럼 `클러스터`, `복제(Replication) 노드`, `샤드` 설정들을 해주어야 합니다. 어떠한 옵션들이 있는지 알아보겠습니다. 

<img width="796" alt="스크린샷 2021-04-08 오후 4 13 09" src="https://user-images.githubusercontent.com/45676906/113983803-50e21c00-9885-11eb-9384-f71871f26a59.png">

1. 싱글 클러스터 노드(No Replication)
2. 클러스터 모드 없이 복제(Replication)만 지원(클러스터 모드 X)
3. 클러스터 모드와 Replication 모두 지원(클러스터 모드 O)

당연히 제일 좋은 것은 3번입니다. 트래픽도 여러 샤드로 분산시킬 수 있고 데이터를 Master 노드 뿐만 아니라 복제해놓기 때문에 Master에 장애가 발생해도 복제본을 사용할 수 있습니다. 각각의 특징을 정리하면 아래와 같습니다. 

<img width="716" alt="스크린샷 2021-04-08 오후 4 16 56" src="https://user-images.githubusercontent.com/45676906/113984252-d796f900-9885-11eb-89a6-d040b6506fbb.png">

여기서 잠시 `수직적 확장(Scale Up)`과 `수평적 확장(Scale Out)`이라는 용어가 나옵니다. 간단하게 어떤 것인지 알아보고 가겠습니다. 

<br> 

### `스케일 업과 스케일 아웃이란 무엇일까?`

- 각 단일 서버(하드웨어)의 성능을 증가시켜서 더 많은 요청을 처리하는 방법을 `스케일 업(Scale up)`이라고 합니다. 

![1](https://user-images.githubusercontent.com/45676906/113954860-a0105880-9855-11eb-8589-7a42bd4ac5cb.png)

즉, 스케일 업은 단일 하드웨어의 성능을 높이기 위하여 CPU, 메모리, 하드디스크를 업그레이드 하거나 추가하는 것을 말합니다. 

<br>

![1](https://user-images.githubusercontent.com/45676906/113954962-ce8e3380-9855-11eb-9001-24f0531d2a02.png)


- 동일한 사양의 새로운 서버(하드웨어)를 추가하는 방법을 `스케일 아웃(Scale Out)`이라고 합니다.  

대부분의 NoSQL은 처음부터 스케일 아웃을 염두에 두고 설계되었기 때문에 데이터의 증가나 요청량이 증가하더라도 동일하거나 비슷한 사양의 새로운 하드웨어를 추가하면 대응이 가능합니다. 

즉, 위의 `클러스터 모드`에서는 `Scale Out`을 한다는 큰 장점이 있습니다. (AWS ElasticCache의 엄청난 장점이라고 합니다.)

<br>

### `데이터 샤딩(Data Shading)`

Redis는 모든 캐시 키가 해시 슬롯에 매핑하는 방식으로 샤딩을 활용합니다. 1개의 클러스터는 16,384개의 해시 슬롯을 사용할 수 있고 일반적으로 클러스터 내 총 샤드에 균등하게 배포합니다. 

<img width="696" alt="스크린샷 2021-04-08 오후 4 21 59" src="https://user-images.githubusercontent.com/45676906/113984893-8c311a80-9886-11eb-89f8-3bc5d65461ac.png">

<br> <br>

# `Reference`

- [https://m.blog.naver.com/sehyunfa/222114663016](https://m.blog.naver.com/sehyunfa/222114663016)
- [https://aws.amazon.com/ko/blogs/database/work-with-cluster-mode-on-amazon-elasticache-for-redis/](https://aws.amazon.com/ko/blogs/database/work-with-cluster-mode-on-amazon-elasticache-for-redis/)
- [https://daddyprogrammer.org/post/1601/redis-cluster/](https://daddyprogrammer.org/post/1601/redis-cluster/)
