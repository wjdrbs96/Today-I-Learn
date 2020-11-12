# finalizer와 cleaner 사용을 피하라

자바는 두 가지 객체 소멸자를 제공한다. 그중 `finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.`
finalizer 대안으로 cleaner를 그 대인으로 소개했지만, `cleaner는 finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불필요하다.`

<br>

finalizer와 cleaner는 즉시 수행된다는 보장이 없다. 객체에 접근할 수 없게 된 후 finalizer나 cleaner가 실행되기까지 얼마나 걸릴지 알 수 없다. 
`즉, finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.`

<br>

예를들어, 파일 닫기를 finalizer와 cleaner에 맡기면 중대한 오류를 일으킬 수 있다. 시스템이 동시에 열 수 있는 파일 개수에 한계가 있기 때문이다. 
시스템이 finalizer와 cleaner 실행을 게을리해서 파일을 계속 열어 둔다면 새로운 파일을 열지 못해 프로그램이 실패할 수 있다.
그리고 클래스에 finalizer를 달아두면 그 인스턴스의 자원 회수가 제멋대로 지연될 수 있다. 

<br>

자바 언어 명세는 finalizer나 cleaner의 수행 시점뿐 아니라 수행 여부조차 보장하지 않는다. 따라서 프로그램 생애주기와 상관없는, 
`상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안된다.`
예를 들어 데이터베이스 같은 공유 자원의 영구 락(lock) 해제를 finalizer나 cleaner에 맡겨 놓으면 분산 시스템 전체가 서서히 멈출 것이다.

<br>

## finalizer와 cleaner는 심각한 성능 문제도 동반한다. 

예를 들어, 시간 측정을 해보았을 때 AutoCloseable 객체를 생성하고 가비지 컬렉터가 수거하기까지 12ns가 걸린 반면(try~with~resources로 닫음), finalizer를 사용하면 550ns가 걸렸다. 
finalizer가 가비지 컬렉터의 효율을 떨어뜨린다. 

<br>

## 이쯤이면 cleaner와 finalizer는 대체 어디에 쓰는 물건인지 궁금해진다.

적절한 쓰임새가 두 가지정도 있다. 하나는 자원의 소유자가 close 메소드를 호출하지 않는 것에 대비한 안전망 역할이다. cleaner나 finalier가 즉시 호출되리라는
보장은 없지만, 클라이언트가 하지 않은 자원 회수를 늦게라도 해주는 것이 아예 안하는 것보다는 낫다.

<br>

> PS 무슨 소리인지 잘 모르겠어서 일단 여기까지만 정리 ,, 