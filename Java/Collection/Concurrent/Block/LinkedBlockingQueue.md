# `LinkedBlockingQueue 란?`

`LinkedBlockingQueue`는 이름에서 알 수 있듯이 `BlockingQueue`와 `LinkedList`의 특징을 가진 클래스라고 할 수 있습니다. 

![스크린샷 2021-02-08 오후 8 36 10](https://user-images.githubusercontent.com/45676906/107214450-481fc600-6a4d-11eb-805e-df3e31f3f7f6.png)

생성자를 보면 위와 같이 `기본 생성자`, `용량을 지정할 수 있는 생성자`가 존재합니다. 기본 생성자로 만들었을 때는 `Integer.MAX_VALUE`의 용량을 가지는 것을 볼 수 있습니다. 

그리고 `LinkedBlockingQueue는 put() 및 take() 작업에 고유한 lock을 사용합니다. 그래서 put(), take() 모두 병렬로 수행되어 처리 속도를 개선할 수 있습니다.`

![스크린샷 2021-02-08 오후 8 42 53](https://user-images.githubusercontent.com/45676906/107215112-3854b180-6a4e-11eb-8c4c-57b1d9f2d0be.png)

그리고 LinkedBlockingQueue 내부를 보면 위와 같이 `Node`를 이용하여 `LinkedList`의 형태를 유지합니다. 

즉, 노드 객체를 만들어서 `삽입` or `삭제`를 해야하기 때문에 `ArrayBlockingQueue` 보다는 비용이 비싸다는 특징이 있습니다. 

용량이 빠르게 커지거나, 빠르게 축소되는 경우에는 `LinkedBlocingQueue` 보다는 `ArrayBlockingQueue`가 더 낫습니다. 

마지막으로 `LinkedBlockingQueue`의 성능은 예측하기가 힘들기 때문에, 매번 성능 체크를 해야합니다!

