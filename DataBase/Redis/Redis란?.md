# `Redis란 무엇인가?`

Redis는 `Remote Dictionary Server`의 약자이다. 말 그대로 `Remote 떨어져 있고`, `Dictionary 사전 형태(key: value)` 쌍으로되어 있는 서버라는 뜻으로 해석할 수 있다. 
Remote라는 말이 살짝 애매하긴 하지만 아래에서 어떤 의미인지 알아보자.

<br>

그리고 Redis는 NoSQL 데이터베이스 중에 하나이며 `In-Memory 데이터베이스이다.`, `다양한 자료구조`를 지원한다. (이것이 다른 `In-Memory 데이터베이스(ex: Memcached)와의 가장 큰 차이점이다.`)
레디스는 아래처럼 `다양한 자료구조`를 `Key-Value` 형태로 저장한다는 것이 특징이다. 

![redis](https://miro.medium.com/max/700/1*tMiZs3RCrmxLGiFZgWRP6g.png)

이렇게 다양한 자료구조를 지원하게 되면 `개발의 편의성이 좋아지고 난이도가 낮아진다`는 장점이 있다. 

<br>

예를들어, 어떤 데이터를 정렬을 해야하는 상황이 있을 때, DBMS를 이용한다면 DB에 데이터를 저장하고, 저장된 데이터를 정렬하여 다시 읽어오는 과정은 디스크에 직접 접근을 해야하기 때문에
시간이 더 걸린다는 단점이 있다. 하지만 이 때 `In-Memory` 데이터베이스인 `Redis`를 이용하고 레디스에서 제공하는 `Sorted-Set`이라는 자료구조를 사용하면 더 빠르고 간단하게 데이터를 정렬할 수 있다.

![fast](https://miro.medium.com/max/700/1*zArWVI0y5u_WVj0gktm92Q.png)

<br>

## `그러면 In-Memory 데이터베이스가 정확히 어떤 것일까?`

말 그대로 디스크에 데이터를 저장하는 것이 아니라 `메모리 위에 데이터를 저장` 한다는 것이다. 컴퓨터를 보자면 RAM은 휘발성 메모리이고, SSD는 비휘발성 메모리이다. 그래서 CPU가 메모리에 접근할 때 RAM보다 SSD가 시간이 더 걸리게 되고 이러한 개념들을 이용한 것이다.

![memory](https://aidanbae.github.io/code/devops/computer/cpucache/screenshot.png)

메모리 계층도를 보면 위와 같다. 아래로 갈수록 접근 시간이 오래걸리고 비용이 싸고, 위로 갈 수록 접근 시간이 짧지만 비용이 비싸다는 특징을 갖고 있다. 
 
<br>

`따라서 Redis도 디스크에 접근하지 않고 메모리에 접근을 해서 데이터를 가져오기 때문에 속도가 빠르다는 장점이 있는 것이다.`
(RDBMS에서 SELECT 쿼리문을 날려 특정 데이터들을 FETCH했을 때, RDBMS의 구조상 DISK에서 데이터를 꺼내오는 데 Memory에서 읽어들이는 것보다 천배 가량 더 느리다.)

<br>

### `레디스 단점`

다만 레디스의 단점은 데이터가 항상 정확하게 유지됨을 보장하지는 않는다는 것이다. 따라서 유지되면 좋긴 하지만 날아가도 서비스 자체에 큰 지장이 없는 데이터를 저장하는 게 좋다.

<br>

## 정리

이외에도 엄청나게 많고 복잡한 내용이 있지만 일단은 이정도만 정리를 한 후에 더 알게 되었을 때 내용을 추가해야겠다. 

<br>

### `Mac Redis 설치`

```
brew install redis
```

<br>

### `Redis 서비스 실행, 중지, 재시작`

```
brew services start redis
brew services stop redis
brew services restart redis
```

<br>

### `Redis 실행`

```
redis-server
```



