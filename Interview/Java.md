# `Java`

## `1) 추상 클래스와 인터페이스의 차이에 대해서 설명해주세요.`

```
추상 클래스는 말 그대로 클래스에 가깝고, extends 키워드를 사용합니다. 
즉, extends 키워드에 맞게 하위 클래스에게 자신의 기능을 확장해주는 것에 가깝습니다. 
대표적으로 동물 → 강아지, 고양이 등등 이러한 경우는 추상 클래스가 더 적절하다. 
지금의 예시처럼 어떤 비슷한 느낌의 계열끼리 있을 때 추상 클래스를 사용합니다

인터페이스는 클래스가 무엇을 할 수 있다라고 하는 기능을 구현하도록 강제하는 특징을 가지고 있습니다. 
그래서 인터페이스는 extends 키워드가 아니라 implements 키워드를 사용하는 것을 알 수 있습니다.

또한 인터페이스의 멤버 변수는 public static final 이어야 하며, 이를 생략할 수 있습니다. 
그리고 인터페이스의 모든 메소드는 public abstract 이어야 하며, 이를 생략할 수 있습니다. (단, static 메소드와 default 메소드는 예외) 

인터페이스는 implements 라는 키워드처럼 인터페이스에 정의된 메소드를 각 클래스의 목적에 맞게 기능을 구현하는 느낌이고, 
추상 클래스는 extends 키워드를 사용해서 자신의 기능들을 하위 클래스로 확장 시키는 느낌이라고 생각합니다.
```

<br>

## `2) SOLID 에 대해서 설명해주세요.`

> S → SRP → 단일 책임 원칙 : 어떤 클래스를 변경해야 하는 이유는 하나여야 한다.

> O → OCP → 개방 폐쇄 원칙 : 확장에는 열려있고, 변경에는 닫혀있어야 합니다. 대표적인 예시는 JDBC가 있음

> L → LSP → 리스코프 치환 원칙 : 서브 타입은 언제나 자신의 기반 타입으로 교체할 수 있어야 한다. 즉, 부모 클래스의 인스턴스를 사용하는 위치에 자식 클래스의 인스턴스를 대신 사용했을 때 코드가 원래 의도대로 작동해야 합니다. 아버지 - 딸 (리스코프 치환 원칙 위배), 동물 - 강아지 (리스토프 치환 원칙 적합)

> I → ISP → 인터페이스 분리 원칙 : SRP와 상당히 유사한데, 인터페이스는 자신이 사용하지 않는 메소드를 가져서는 안된다.

> D → DIP → 의존 역전 원칙 : 추상 적인 것은 구체적인 것에 의존하면 안된다. 구체적인 것이 추상적인 것에 의존해야 한다.

<br>

## `3) final 키워드가 클래스, 필드 메소드에 붙었을 때를 설명해주세요.`

> 메소드 final : 오버라이딩이 불가능한 메소드가 됩니다.

> 클래스 final : 다른 클래스의 조상이 될 수 없습니다.

> 필드 final : 초기화가 한번만 가능합니다.

<br>

## `4) equals로 재정의 한다면 hashCode도 재정의 해야 하는 이유는?`

```
Object 규약에 equals가 true 라면 hashCode 값도 같아야 한다는 규약이 있습니다. 이러한 이유는 만약에 A 클래스에 equals만 오버라이딩 해서 필드의 값들이 같다면 true 라고 했다고 가정하겠습니다. 
그러면 그 때 HashMap을 사용한다면 Key(class), Value(값) 으로 저장한 후에 다시 get 해온다면 null을 출력하게 될 것입니다. 
왜냐하면 HashMap은 해시 코드 기반으로 하기에 다른 버킷에 존재하기 때문입니다.
```

<br>

### `5) HashMap 의 충돌 과정과 Java 8에서 어떻게 충돌을 해결하고 있는지 설명해주세요.`

```
Hash 충돌에는 '개방 주소법', '분리 연결법'이 존재합니다.

Open Addressing은 데이터를 삽입하려는 해시 버킷이 이미 사용 중인 경우 다른 해시 버킷에 해당 데이터를 삽입하는 방식입니다. 
Open Addressing은 연속된 공간에 데이터를 저장하기 때문에 Separate Chaining에 비하여 캐시 효율이 높다. 따라서 데이터 개수가 충분히 적다면 Open Addressing이 Separate Chaining보다 더 성능이 좋다
 
자바 Hash는 분리 연결법을 사용하고 있습니다. 해시 버킷에 충돌이 일어날 때마다 옆으로 LinkedList 형태로 저장하는 것을 말하는데요. 
이렇게 저장해서 길이가 8이 되면 리스트 → 트리로 변경이 됩니다. 
그리고 다시 6개 이하가 되면 트리 → 리스트 형태로 바뀝니다.
```

Reference: [https://d2.naver.com/helloworld/831311](https://d2.naver.com/helloworld/831311)

<br>

## `6) HashMap과 HashTable의 차이에 대해서 설명해주세요.`

> HashTable 은 싱크로나이즈드가 붙어 있어서 Thread-Safe 하다는 특징이 있으며 아주 예전부터 있던 클래스가 현재는 잘 관리되지 않는 클래스임 + Key에 null을 허용하지 않음

> HashMap 은 Thread-Safe 하지 않다는 특징을 가지고 있음 + null을 허용함

<br>

## `ConcurrentHashMap 이 무엇인지 설명해주세요.`

HashMap 은 멀티 스레드 환경에서 사용할 수 없는 클래스입니다. HashTable 은 멀티스레드 환경에서 사용할 수 있지만 너무 예전에 나온 클래스이고 단점에 대한 보완을 하고 있는 클래스도 아닙니다. 그래서 HashMap 의 멀티스레드에서 사용할 수 없다는 단점을 보완하는 클래스가 ConcurrentHashMap 입니다. 

ConcurrentHashMap 은 `put` 작업을 할 때 메소드 전체에 `Synchronized`가 붙어있지 않다는 특징이 있습니다. 그리고 `Lock`을 버킷 마다 가지고 있어 같은 버킷에 대해서 쓰는 것이 아니라면 여러 쓰레드에서도 동시에 쓰기 작업을 할 수 있습니다.

ConcurrentHashMap 은 읽기 작업에는 여러 쓰레드가 동시에 읽을 수 있다.

즉, ConcurrentHashMap 은 멀티 쓰레드 환경에서 읽기 작업보다 쓰기 작업이 많을 때 사용하면 좋습니다.

<br>

## `7) StringBuilder vs StringBuffer 차이를 설명해주세요.`

> 두 클래스는 완전히 동일한데 하나의 차이만 존재합니다. StringBuilder는 Thread-Safe 하지 않고, StringBuffer는 Thread-Safe 합니다.  String 클래스는 불변 클래스입니다. 즉 값이 매번 바뀌면 새로 메모리를 할당해서 얻습니다.
> String 도 불변 클래스입니다. 

<br>

## `8) ArrayList vs LinkedList 차이를 설명해주세요.`

```
ArrayList는 동적 배열과 비슷합니다. 크기를 지정하지 않고 ArrayList를 만들면 크기 10의 배열로 만들게 됩니다. 개수가 초과되면 1.5 배씩 크기가 늘어납니다. 늘어나는 과정에서 오버헤드가 발생합니다. 
배열의 특징이다 보니 검색을 할 때 O(1)로 가져올 수 있고 끝에서 추가하고 삭제하는 것도 O(1)로 할 수 있습니다. 
하지만 앞이나 중간에 삽입 삭제를 했을 때는 나머지 원소들을 다 땡겨야 한다는 큰 단점을 가지고 있습니다.  

반면에 LinkedList는 불연속적으로 있는 데이터들을 연결한 형태입니다. 즉, 데이터를 삽입, 삭제 하는 것은 쉽습니다. 그리고 양방향 연결리스트 형태로 구현되어 있습니다. 
하지만 검색에는 ArrayList 보다 상대적으로 느리다는 것을 알 수 있습니다.
```

<br>

## `ArrayList, LinkedList 삽입, 삭제, 검색 시간 복잡도 얘기해주세요.`

|컬렉션|읽기(접근시간)|추가/삭제| 비 고 |
|--------|-------|-------|---------------|
| ArrayList | 빠르다 | 느리다 | 순차적인 추가삭제는 더 빠름. <br> 비효율적인 메모리 사용 |
| LinkedList | 느리다 | 빠르다 | 데이터가 많을 수록 접근성이 떨어짐 |

> 다르고자 하는 데이터의 개수가 변하지 않는 경우라면, ArrayList가 최상의 선택이겠지만, 데이터 개수의 변경이 잦다면 LinkedList를 사용하는 것이 더 나은 선택이 될 것입니다.

![time](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fc47wrr%2FbtqNG0s9sD1%2FGE9KaZbmsXUbPKVzOkon20%2Fimg.png)

<br>

## `9) 다형성이란 무엇인가요?`

> 다형성(polymorphism)이란 하나의 객체가 여러 가지 타입을 가질 수 있는 것을 의미합니다.
> 하나의 메소드나 클래스가 상황에 따라 다양한 방법으로 동작하는 것을 의미한다.

> List<Integer> list = new ArrayList<>();

> 대표적으로 위와 같이 사용할 수 있습니다.

<br>

### `10) 오버로딩과 오버라이딩 차이가 무엇인가요?`

```
> 오버로딩: 메소드의 이름은 같지만 파라미터 형태만 다름

> 오버라이딩: 메소드의 이름 파라미터 다 같지만 내부 구현체만 재정의해서 사용하는 것
```

<br>

### `11) 접근 제어자에 대해서 설명해주세요.`

```
> public: 어디에서나 접근이 가능함

> protected: 현재 클래스, 자식 클래스에서 까지만 접근 가능함 

> default: 같은 패키지에서만 접근 가능

> private: 같은 클래스 내부에서만 접근 가능함
```

<br>

## `12) 직렬화가 무엇인가요?`

```
자바 직렬화란 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도 사용할 수 있도록 
바이트(byte) 형태로 데이터 변환하는 기술과 바이트로 변환된 데이터를 다시 객체로 변환하는 기술(역직렬화)을 아울러서 이야기합니다.
```

<br>

## `직렬화가 사용되는 곳`

- Servlet Session
- Cache (EhCache, Redis, MemCached)
- 자바 RMI

<br>

## `직렬화 장단점`

- 장점: 자바 시스템에 최적화 되어 있다. 복잡한 데이터 구조여도 몇 개만 잘 설정 하면 쉽게 직렬화를 적용할 수 있다.
- 단점: 사소한 거 하나만 틀려도 직렬화가 안되기 때문에 에러를 발생시킬 위험이 크고 지뢰 시스템이 될 수 있음. 용량도 무거워서 용량이 민감하다면 JSON, XML 같은 것을 사용하는 것이 좋다.

<br>

## `13) Checked Exception vs UnChecked Exception 차이가 무엇인가요?`

```
`RuntimeException`의 하위 클래스들이 `Uncheck Exception` 이라 하고 RuntimeException의 하위 클래스가 아닌 Exception 클래스의 하위 클래스들을 `Checked Exception`이라고 합니다.

체크 예외는 RuntimeException의 하위 클래스가 아니면서 Exception 클래스의 하위 클래스들입니다. `체크 예외의 특징은 반드시 에러 처리를 해야하는 특징(try/catch or throw)`을 가지고 있습니다.

언체크 예외는 RuntimeException의 하위 클래스들을 의미합니다. 이것은 체크 예외와는 달리 에러 처리를 강제하지 않습니다.

CheckedException : 롤백 되지 않음 => ClassNotFoundException
UncheckedException : 롤백 됨 => ArrayOutOfIndexException
```

<br>

## `어떤 경우에 Checked? Unchecked를 쓸까요?`

- Checked 의 대표적으로 `ClassNotFoundException`, `FileNotFoundException` 같은 클래스들이 있음. `try-catch`를 강제해야 하는 경우
- Unchecked 는 대표적으로 `ArrayOutOfIndexException`, `NullPointerException` 등이 있다. 런타임에 발생하는 예외들

<br>

## `객체지향 장단점에 대해서 말씀해주세요.` 

- ### 장점
  - 코드를 응집력 있게 작성할 수 있음
  - 객체의 역할, 책임, 협력을 생각하면서 짜다 보면 규모가 크고 여러명에서 작업하는 프로젝트에서는 좀 더 효율적일 수 있음

- ### 단점
  - 객체지향에 대해서 잘 알기가 쉽지 않음 (러닝 커브?)
  - 오히려 객체지향적으로 짜다 보면 작은 프로젝트에서도 더 복잡해질 수도 있다고 생각함
  - 클래스가 많아지고 코드가 길어질 수 있음

<br>

## `14) Collection 인터페이스 하위 인터페이스를 말해주세요.`

- List
  - ArrayList, LinkedList
- Queue
  - LinkedList
- Set
  - HashSet, LinkedHashSet, TreeSet

<br>

### `List`

- 순서가 있는 데이터의 집합이다.
- 데이터의 중복을 허용한다.
- LinkedList : 양방향 포인터 구조로 데이터의 삽입, 삭제가 빈번할 경우 빠른 성능을 보장한다. 스택, 큐, 양방향 큐 등을 만들기 위한 용도로 사용된다.
- ArrayList : 상당히 빠르고 크기를 마음대로 조절할 수 있는 배열이다. 단방향 포인터 구조로 자료에 대한 순차적인 접근에 강점이 있다.

<br>

### `Set`

- 순서가 없는 데이터의 집합이다.
- 데이터의 중복을 허용하지 않는다.
- HashSet : 가장 빠른 임의 접근 속도를 가진다. 순서가 랜덤으로 저장된다.
- TreeSet : 정렬된 순서대로 보관하며 정렬 방법을 지정할 수 있다.
- LinkedHashSet : 추가된 순서, 또는 가장 최근에 접근한 순서대로 접근이 가능하다.

<br>

### `Map`

- 키-값 쌍으로 이루어진 데이터의 집합이다.
- 순서는 유지되지 않고, 키는 중복을 허용하지 않는다. 값은 중복을 허용한다.
- HashMap : Map 인터페이스를 구현하기 위해 HashTable을 사용한 클래스, 중복을 허용하지 않고 순서를 보장하지 않는다. 키와 값으로 null이 허용된다.
- TreeMap : 이진검색트리의 형태로 키와 값이 쌍으로 이루어진 데이터를 저장한다. 정렬된 순서로 키, 값 쌍을 저장하므로 빠른 검색이 가능하다. 저장시 정렬을 하기 때문에 저장시간이 다소 오래걸린다.
- HashTable : HashMap보다 느리지만 동기화가 지원된다. 키와 값으로 null이 허용되지 않는다.
- LinkedHashMap : 기본적으로 HashMap을 상속받아 HashMap과 매우 흡사하다. Map에 있는 엔트리들이 연결 리스트가 유지되므로 입력한 순서대로 반복이 가능하다.
  
<br>

### `Iterable vs Iterator 차이가 무엇인가요?`

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbE4TfJ%2FbtqBh1w4sLx%2FicJkqcLkLArocYCR4rHUFK%2Fimg.png)

- Iterator 인터페이스의 역할은 데이터를 순차적으로 가져올 수 있게 해주는 역할을 합니다.
- Collection 인터페이스가 Iterable 인터페이스를 extends 한 이유는 하위 클래스에서 iterator()을 반드시 구현하게 하기 위해서 입니다.
- List, Set, Queue 인터페이스들 마다 데이터를 꺼내는 방법이 표준화 되어 있지 않다면 데이터를 읽어올 때마다 방법을 제 각각 알아야 하기 때문이 쉽지 않을 것입니다.
- 그래서 Iterator 인터페이스를 통해서 방법을 표준화 시켜 코드의 일관성을 유지할 수 있습니다.

<br>

## `12) Kotlin과 Java의 차이점 느낀대로 말해주세요.`

1. 코틀린 주 생성자 사용 방식
2. NULL 가능 여부
3. Data Class를 사용하면 `Equals`, `toString` 같은 것들을 자동으로 만들어줌
4. 코틀린의 val, var
5. 코틀린은 기본이 final class
6. 코틀린 파라미터 디폴트 값 설정 가능
7. 확장 함수

<br>

## `코틀린이랑 자바가 어떻게 100% 호환되는지 설명해주세요.`


<br>

## `13. Java 8에 추가된 것들에 대해서 설명해주세요.`

- Date -> LocalDateTime, LocalDate 등장
- Lambda, Stream 생성
- Interface Default Method 추가
- JVM Permanent 영역 삭제

<br>

## `14. equals vs hashCode vs == 차이점이 무엇인가요?`

- equals: 객체가 가지는 필드들이 같은 값을 가지는지 확인하는 메소드
- hashCode: 객체가 같은 메모리 주소에 저장되어 있는지 확인하는 메소드
- == : 객체가 저장된 메모리 주소가 같은지 판단

<br> 

## `15. 프로세스 vs 쓰레드 차이점에 대해서 설명해주세요.`

- 프로세스는 운영체제로부터 자원을 할당받습니다. 즉, 프로그램이 메모리에 올라간 상태를 의미합니다.
- `스레드는 프로세스로부터 자원을 할당받고, 프로세스의 코드/데이터/힙영역을 공유`하기 때문에 좀 더 효율적으로 통신할 수 있습니다. 또한 컨텍스트 스위칭도 캐시 메모리를 비우지 않아도 되는 스레드쪽이 빠릅니다. 그리고, 스레드는 자원 공유로 인해 문제가 발생할 수 있으니 이를 염두에 둔 프로그래밍을 해야합니다.
-	프로세스를 생성하는거보다 Thread 생성이 더 시간 적게듬
-	프로세스를 종료하는거보다 Thread 종료가 더 시간 적게듬
-	프로세스를 스위칭하는거보다 같은 프로세스에 있는 두 Thread 스위칭이 더 시간 적게듬

<br>

## `16. Stream map vs FlatMap 차이점에 대해서 설명해주세요.`

- `map`: map()은 데이터를 특정 데이터로 변환하는데 사용됩니다. 스트림의 요소에 저장된 값 중에서 원하는 필드만 뽑아내거나 특정 형태로 변환해야 할 때가 있다. 
- `flatmap`: flatMap()은 Array나 Object로 감싸져 있는 모든 원소를 단일 원소 스트림으로 반환합니다.

<br>
 
## `17. JVM 메모리 구조에 대해서 설명해주세요.`

### `JVM 실행과정`

1. 프로그램이 실행되면 JVM은 OS로부터 이 프로그램이 필요로 하는 메모리를 할당 받는다. JVM은 이 메모리를 용도에 따라 여러 영역으로 나누어 관리한다.
2. 자바 컴파일러(javac)가 자바소스(.java)코드를 읽어 들여 자바 바이트코드(.class)로 변환시킨다.
3. 변경된 Class 파일들을 `Class Loader`를 통해 `JVM 메모리 영역(Runtime Data Areas)` 으로 로딩한다.
4. 로딩된 class 파일들은 `Execution engine`을 통해 해석된다.
5. 해석된 바이트 코드는 `Runtime Data Areas`에 배치되어 실질적인 수행이 이루어지게된다.
6. 이러한 실행과정속에서 JVM은 필요에 따라 Thread Synchronization과 GC 같은 관리 작업을 수행한다.
7. 추가로 Runtime Data Area에 Java 7에서 Java 8로 넘어오면서 Out of Memory 문제로 Permanent 영역이 사라지고 Metaspace 영역이 생겼습니다. 
   1. Perm 영역에서 Method Meta 정보, Static 변수, 상수, 상수 풀 들이 저장되었따. 그런데 Perm -> Metaspace 로 바뀌면서 Static Object 는 Heap 영역으로 옮겨져서 최대한 GC 대상이 될 수 있도록 했다.

<br>

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxKh6U%2FbtqCPzYJhpS%2FoKDKiaPoWqwqU86rf7IVVk%2Fimg.png)

<br>

### `Executioin Engine(실행 엔진)`

.class파일을 실행시키는 역할을 합니다. 클래스 로더가 JVM 내의 Runtime Data Area에 .class(바이트코드) 를 배치하고, 이를 `실행 엔진`에 의해 실행된다. 

- Interpreter
  - 바이트 코드를 명령어 단위로 읽어서 실행하는 인터프리터. 한 줄씩 수행하기 때문에 느리다는 단점이 있다.

- JIT compiler(Just - In - Time)
  - JIT 컴파일러는 인터프리터 방식의 단점을 보완하기 위해 도입했다. 인터프리터 방식으로 실행하다가 적절한 시점에 바이트 코드 전체를 컴파일하여 네이티브 코드로 변경하고, 이후에는 네이티브 코드를 직접 실행하는 방식이다. 단, JIT 컴파일러가 컴파일하는 과정은 바이트 코드를 인터프리팅하는 것보다 훨씬 오래 걸리므로, JIT 컴파일러를 사용하는 JVM은 내부적으로 해당 메서드가 얼마나 자주 수행되는지 확인하여, 일정 정도를 넘을 때에만 컴파일을 수행한다.
  
<br>

### `Runtime Data Areas`

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fci4Dqe%2FbtqCOyMIluC%2FkcfCKeWROOa7wGGKMdBy5K%2Fimg.png)

1) PC Register 
   1) Thread가 시작될 때 생성되며 생성될 때 마다 생성되는 공간으로 스레드마다 하나씩 존재한다. Thread가 어떤 부분을 어떤 명령으로 실행해야할 지에 대한 기록을 하는 부분으로 현재 수행 중인 JVM 명령의 주소를 갖는다. 그리고 JVM은 오직 JVM 스택에 스택 프레임을 추가하고(push) 제거하는(pop) 동작만 수행한다
   
2) JVM stack 
   1) 프로그램 실행과정에서 임시로 할당되었다가 메소드를 빠져나가면 바로 소멸되는 특성의 데이터를 저장하기 위한 영역이다. 예를들어 호출된 메서드의 파라미터, 지역 변수, 리턴 값 및 연산 값 등이 임시로 저장되는 영역이다.
   
3) Native Method stack 
   1) 자바 프로그램이 컴파일되어 생성되는 바이트 코드가 아닌 실제 실행할 수 있는 기계어로 작성된 프로그램을 실행시키는 영역이다. JAVA Native Interface를 통해 바이트 코드로 전환하여 저장한다.
   
4) Heap 
   1) 객체를 저장하는 가상 메모리 공간이다. GC의 대상이 되는 영역. 프로그램 실행 중 생성되는 인스턴스(new 연산자), 배열등은 모두 Heap 영역에 생성된다. 즉, 인스턴스변수(instance variable)들이 생성되는 공간이다.

5) Method Area 
   1) 클래스 정보를 처음 메모리 공간에 올릴 때 초기화되는 대상을 저장하기 위한 메모리 공간. 프로그램 실행 중 어떤 클래스가 사용되면, JVM은 해당 클래스의 클래스파일(*.class)을 읽어서 분석하여 클래스에 대한 정보(클래스 데이터)를 이곳에 저장한다. 이 때, 그 클래스의 클래스변수(class variable)도 Method Area(메서드 영역)에 함께 생성된다.
   2) Perm 영역이라고도 하는데 Java 8 부터 Metaspace 로 변경되어 Native 영역에서 관리하기 시작했다.
   3) Perm 영역에서 Method Meta 정보, Static 변수, 상수, 상수 풀 들이 저장되었음. 그런데 Perm -> Metaspace 로 바뀌면서 Static Object 는 Heap 영역으로 옮겨져서 최대한 GC 대상이 될 수 있도록 했다.

<br>

또한 Runtime constant pool 은 Method area 내부에 존재하는 영역으로, 이는 상수 자료형을 저장하여 참조하고 중복을 막는 역할을 수행한다.

<br>

## `JRE vs JDK 차이가 무엇인가요?`

![jdk vs jre](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FL2JVv%2FbtqAU6c3LWW%2FCDMSryWI5LedYjoUmSZkD0%2Fimg.png)

- JRE가 아닌 JDK 부분을 보면 주로 Tool 관련된 것임을 알 수 있습니다. 대표적인 예시로 `컴파일러`, `디버깅 도구`들이 속해 있습니다.
- JRE를 보면 `java.lang`, `java.util`, `Math`와 같은 패키지들을 가지고 있고, 자바 실행 환경을 담당하고 있습니다.

<br> 

## `18. GC에 대해서 설명해주세요.`

![image](https://user-images.githubusercontent.com/45676906/143909431-0e8e4bac-bd12-4d11-91b3-365c2ab3afb9.png)

- Java 8 에서 `Permanent` 영역이 사라지고 `Metaspace`가 생기고 `Native` 영역에서 관리되기 시작함

<br>

GC를 이해하기 위해서 객체가 제일 먼저 생성되는 Young 영역입니다. `Young` 영역은 3개의 영역으로 나뉩니다. 

- Eden 영역
- Survivor 영역(2개)

<br>

Survivor 영역이 2개이기 때문에 총 3개의 영역으로 나뉘는 것이다. 각 영역의 처리 절차를 순서에 따라서 기술하면 다음과 같다.

- 새로 생성한 대부분의 객체는 Eden 영역에 위치한다.
- Eden 영역에서 GC가 한 번 발생한 후 살아남은 객체는 Survivor 영역 중 하나로 이동된다.
- Eden 영역에서 GC가 발생하면 이미 살아남은 객체가 존재하는 Survivor 영역으로 객체가 계속 쌓인다.
- 하나의 Survivor 영역이 가득 차게 되면 그 중에서 살아남은 객체를 다른 Survivor 영역으로 이동한다. 그리고 가득 찬 Survivor 영역은 아무 데이터도 없는 상태로 된다. `Young 에서 일어나는 GC 를 Minor GC 라고 합니다.`
- 이 과정을 반복하다가 계속해서 살아남아 있는 객체는 Old 영역으로 이동하게 된다.
- Old Generation 영역에서 살아남았던 객체들이 일정 수준 쌓이게 되면 미사용된다고 식별된 객체들을 제거해주는 `Full GC`가 발생하게 됩니다.
  이 과정에서 `STW(Stop-The-World)`가 발생하게 됩니다. (STW란, `Old Generation`의 쌓인 많은 객체들을 효율적으로 제거해주기 위해 JVM이 잠시 멈추는 현상을 뜻합니다.)

<br> <br>

## `Old 영역에 대한 GC`

Old 영역은 기본적으로 데이터가 가득 차면 GC를 실행한다. GC 방식에 따라서 처리 절차가 달라지므로, 어떤 GC 방식이 있는지 살펴보면 이해가 쉬울 것이다. GC 방식은 JDK 7을 기준으로 5가지 방식이 있다.

- Serial GC
- Parallel GC
- Parallel Old GC(Parallel Compacting GC)
- Concurrent Mark & Sweep GC(이하 CMS)
- G1(Garbage First) GC

<br>

### `Serial GC`

- Young 영역에서의 GC는 앞 절에서 설명한 방식을 사용한다. Old 영역의 GC는 `Mark-Sweep-Compact`이라는 알고리즘을 사용한다. 
  1. 이 알고리즘의 첫 단계는 `Old 영역에 살아 있는 객체를 식별(Mark)`하는 것이다. 
  2. 그 다음에는 `힙(heap)의 앞 부분부터 확인하여 살아 있는 것만 남긴다(Sweep)`. 
  3. 마지막 단계에서는 각 객체들이 연속되게 쌓이도록 힙의 가장 앞 부분부터 채워서 객체가 존재하는 부분과 객체가 없는 부분으로 나눈다(`Compaction`).
- `Serial GC는 적은 메모리와 CPU 코어 개수가 적을 때 적합한 방식이다.`

<br>

### `Parallel GC`

- `Parallel GC는 Serial GC와 기본적인 알고리즘은 같다`.
- `Serial GC는 GC를 처리하는 스레드가 하나인 것에 비해, Parallel GC는 GC를 처리하는 쓰레드가 여러 개` >> **Serial GC보다 빠르게 객체를 처리할 수 있다**
- Parallel GC는 메모리가 충분하고 코어의 개수가 많을 때 유리하다.

<br>

### `Parallel Old GC`

- Old 영역에서 작동할때만 다름
  - `Mark-Sweep-Compaction` 알고리즘 말고, `Mark-Summary-Compaction`을 사용한다

<br>

### `CMS GC (Concurrent Mark Sweep GC)`

초기 `Initial Mark` 단계에서는 클래스 로더에서 가장 가까운 객체 중 살아 있는 객체만 찾는 것으로 끝낸다. 따라서, 멈추는 시간은 매우 짧다. 그리고 `Concurrent Mark 단계에서는 방금 살아있다고 확인한 객체에서 참조하고 있는 객체들을 따라가면서 확인한다.` 이 단계의 특징은 다른 스레드가 실행 중인 상태에서 동시에 진행된다는 것이다.

그 다음 Remark 단계에서는 `Concurrent Mark` 단계에서 새로 추가되거나 참조가 끊긴 객체를 확인한다. 마지막으로 `Concurrent Sweep` 단계에서는 쓰레기를 정리하는 작업을 실행한다. 이 작업도 다른 스레드가 실행되고 있는 상황에서 진행한다.

`이러한 단계로 진행되는 GC 방식이기 때문에 stop-the-world 시간이 매우 짧다.` 모든 애플리케이션의 응답 속도가 매우 중요할 때 CMS GC를 사용하며, Low Latency GC라고도 부른다.

그런데 `CMS GC는 stop-the-world 시간이 짧다는 장점`에 반해 다음과 같은 단점이 존재한다.

- 다른 GC 방식보다 메모리와 CPU를 더 많이 사용한다.
- Compaction 단계가 기본적으로 제공되지 않는다.

<br>

- `Initial Mark`: 클래스 로더에서 가장 가까운 객체 중 살아 있는 객체만 찾는다.
- `Concurrent Mark`: 방금 살아있다고 확인한 객체에서 참조하고 있는 객체들을 따라가면서 새로 추가되거나 참조가 끊긴 객체를 확인한다.
- `Concurrent Sweep`: GC 대상들을 정리하는 작업을 실행한다. 

따라서, CMS GC를 사용할 때에는 신중히 검토한 후에 사용해야 한다. 그리고 조각난 메모리가 많아 Compaction 작업을 실행하면 다른 GC 방식의 stop-the-world 시간보다 stop-the-world 시간이 더 길기 때문에 Compaction 작업이 얼마나 자주, 오랫동안 수행되는지 확인해야 한다.

<br>

### `G1 GC`

- G1 GC를 이해하려면 지금까지의 Young 영역과 Old 영역에 대해서는 잊는 것이 좋다.
- G1 GC는 바둑판의 각 영역에 객체를 할당하고 GC를 실행한다. 그러다가, 해당 영역이 꽉 차면 다른 영역에서 객체를 할당하고 GC를 실행한다.
- G1 GC의 가장 큰 장점은 성능이다. 지금까지 설명한 어떤 GC 방식보다도 빠르다.
- 큰 메모리를 가진 멀티 프로세서 머신을 위한 컬렉터에 적합하다.

![1](https://d2.naver.com/content/images/2015/06/helloworld-1329-6.png)

<br>

### `Stop the world 는 어느 GC 에서 일어나나요?`

<br>

## `CMS GC에서는 Comapction 은 아예 발생하지 않나요?`

<br>

### Java Version 별 GC 방식

- Java 7 : Parallel GC
- Java 8 : Parallel GC
- Java 9 : G1 GC
- Java 11 : G1 GC

<br>

## 제네릭에 대해서 설명해주세요.

- JDK 1.5에 도입 되었다. 
- 컴파일 과정에서 타입체크를 해주는 기능으로 객체의 타입을 컴파일 시에 체크하기 때문에 객체의 타입 안정성을 높이고 형변환의 번거로움을 줄여줍니다. 

<br>

## mutable vs immutable 에 대해서 설명해주세요.

- mutable: 변경 가능한 객체입니다. 최초 생성 이후에 자유롭게 변경 가능합니다. 
- immutable: 변경 불가능 객체입니다. 대표적으로 Java String 이 존재합니다. 

<br>

## Call By Reference vs Call By Value 차이가 무엇인가요?

- Call By Value : 값을 복사 해서 넘김
- Call By Reference : 값의 주소를 넘김
- `Java는 Call By Value` 이다. 

<br>

## String 상수 풀이 무엇인가요? 

```java
String str1 = "hello";
String str2 = new String("hello");
```

- 두 코드의 차이점에 대해서 설명해주세요. 
- `str1`은 `상수풀`에서 가져오고 `new String()`은 `Heap`에 객체가 저장됩니다. 

<br>

```java
String str1 = "hello";
String str2 = "hello";

str1 == str2 
```

- 위 코드의 결과는 무엇일까요? true 입니다. 둘 다 `String 상수 풀`에서 가져오기 때문에 `true`가 나옵니다.

<br>

```java
String str1 = new String("hello");
String str2 = new String("hello");

str1 == str2
```

- 위 코드의 결과는 무엇일까요? `new`를 통해 객체를 생성하면 메모리가 각각 할당 되기 때문에 `false`가 나옵니다.

<br>

## String 상수풀은 GC 영역의 대상인가요?

- 조사 필요
- GC 대상 아니라고 생각합니다. (아마두?)

<br>

## Object equals 는 어떻게 동작하나요?

![스크린샷 2021-12-02 오후 11 48 41](https://user-images.githubusercontent.com/45676906/144445001-1830a2c8-8e98-479e-aa89-b3d35e57c0b3.png)

Object equals 는 `==`을 사용해서 비교합니다. 

<br>

## 두 객체간의 equals 가 true 이면 hashCode 도 무조건 true 인가요?

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메소드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 합니다.(단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없습니다.)
- `equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 합니다.`
- `equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없습니다.` 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아집니다.

<br>

위의 문장들은 Object 명세에서 발췌한 규약입니다.

<br>

## 두 객체간의 hashCode 가 true 이면 무조건 equals 가 true 인가요?

`false` 입니다. 왜냐하면 Object 명세를 보면 equals가 다르다고 했더라도 hashCode가 무조건 다른 것이 아니기 때문입니다.(해시 버킷에 충돌날 경우가 있기 때문에) 즉, 해시코드가 같은데 equals는 false 가 나올 수 있습니다.  

<br>

## final int[] arr = {1, 2, 3, 4, ,5] 에서 arr[1] = 10 처럼 값을 바꾸는 것이 가능한가요?

- 가능합니다. final 은 `초기화`가 한번만 가능한것이라 내부 값을 바꿀 수 있습니다.
- final List<Integer> list = new ArrayList<>(); 에서 list.add(1) 도 마찬가지로 값을 변경할 수 있습니다.

<br>

## 박싱 언박싱 차이가 무엇인가요?

<br>

## Stream foreach 랑 for 문 중에 뭐가 더 빠르다고 생각하시나요? 

일반적으로 `Stream.forEach()`를 사용하면 전통적인 `for-loop`를 사용할 때보다 오버헤드가 훨씬 심각하게 발생하기 때문에, 모든 for-loop를 Stream.forEach()로 대체하면, 애플리케이션 전체에 걸쳐 누적되는 CPU 싸이클 낭비는 무시하지 못할 수준이 될 수 있다.

원시 데이터(primitive data type)를 반복문으로 처리할 때는 절대적으로 전통적인 `for-loop`를 써야한다(collections보다 배열의 경우에는 특히 더)

<br>

## int, char 은 어떻게 초기화 할까요? 

- int 는 `int a = 0` 식으로 하면 된다.
- 하지만 `char ch =''`는 이런식으로 초기화 할 수 없다.  따라서 `char ch = ' '` or `char ch = '\u0000'`

<br>

## static 이 무엇인가요? 

- static은 클래스 멤버라고 하며, 인스턴스가 생성될 때마다 독립적으로 생기는 멤버 변수와 달리 해당 클래스에 하나만 생성되고 모든 인스턴스에서 공동으로 접근할 수 있는 멤버이다
- static 키워드를 통해 생성된 정적멤버들은 PermGen 또는 Metaspace에 저장되며 저장된 메모리는 모든 객체가 공유하며 하나의 멤버를 어디서든지 참조할 수 있는 장점이 있습니다.
- 그러나, GC의 관리 영역 밖에 존재하기 때문에 프로그램 종료시까지 메모리가 할당된 채로 존재합니다. 너무 남발하게 되면 시스템 성능에 악영향을 줄 수 있습니다.

<br>

## 객체지향 4대 원칙에 대해서 설명해주실 수 있나요?

### 캡슐화(Encapsulation): 정보 은닉(information hiding)

비슷한 역할을 하는 속성과 메소드들을 하나의 클래스로 모은것을 캡슐화 라고 한다. 캡슐화에 속한 개념으로 정보 은닉이라는것이 있는데, 캡슐 내부의 로직이나 변수들을 감추고 외부에는 기능(api)만을 제공하는것을 의미한다.

<br>

### 상속(Inheritance): 재사용

상속이란 클래스를 재사용 하는것이다. 상위 클래스를 하위 클래스에서 상속 받게 되면 상위 클래스의 멤버변수나 메소드를 그대로 물려 받을 수 있다. 상속이 있기 때문에 코드를 재활용할 수 있고 그렇기 때문에 생산성이 높고 유지보수 하기가 좋다.

<br>

### 추상화(Abstraction): 모델링

객체지향에서의 추상화는 어떤 하위클래스들에 존재하는 공통적인 메소드를 인터페이스로 정의하는것을 예로 들 수 있다.

<br>

### 다형성(Polymorphism): 사용 편의

다형성은, 같은 모양의 함수가 상황에 따라 다르게 동작 하는것을 의미한다. 대표적으로 오버로딩과 오버라이딩이 있다. 또는 `List<String> list = new ArrayList<>()` 와 같은 형태가 대표적이다. 

<br>

## 아래의 코드를 보고 결과를 설명해주세요.

```java
public class Test {
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 1;
        Integer c = 99999;
        Integer d = 99999;

        System.out.println(a == b);   // 1
        System.out.println(c == d);   // 2
    }
}
```

- 1번은 `true` 입니다. 이유는 Integer 는 -127 ~ 127 까지는 캐싱을 해놓기 때문에 true 가 나옵니다.
- 2번은 `false` 입니다. 그 이후에는 실제 값을 비교해서 가져오기 때문에 `==` 으로 비교하면 `false`가 나옵니다.

<br>

## 자바에서 멀티스레드 동기화 하는 방법들에 대해서 설명해주세요.

- synchronized 
- java.util.concurrent.locks 
- java.util.concurrent.atomic
- [참고하기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Thread/java.util.concurrent.locks/ReentrantLock%EC%9D%B4%EB%9E%80%3F.md)

<br>

## List 의 remove 동작 방식을 설명해주세요.

![스크린샷 2021-12-08 오전 1 57 48](https://user-images.githubusercontent.com/45676906/145072657-8c10a96b-4472-47bb-ac9c-bf0fd9bfedff.png)

<br>

![스크린샷 2021-12-08 오전 1 58 04](https://user-images.githubusercontent.com/45676906/145072696-ff26a12b-207b-4936-8b89-e85ac5f36ca4.png)

`ArrayList`를 보면 `remove` 메소드가 두 가지 존재합니다. 여기서 볼 점은 `Object`를 `remove`하는 것인데 내부 동작원리는 객체의 `equals`를 기반으로 비교하게 됩니다. 

즉, 해당 `Object`에 `equals`, `hashCode` 기반으로 동작하기 때문에 `오버라이딩`을 해서 구현해놓아야 올바르게 작동하게 됩니다. 

<br>

## 리플렉션이란 무엇인가요?

리플렉션은 구체적인 클래스 타입을 알지 못해서 그 클래스의 메소드와 타입 그리고 변수들을 접근할 수 있도록 해주는 자바 API 입니다.

<br>

## List sort 메소드는 어떤 정렬 알고리즘을 사용하나요?



<br>

## 해시맵에 데이터 저장과 조회하는데 시간복잡도가 어떻게 되나요?



<br>