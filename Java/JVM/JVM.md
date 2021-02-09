# `들어가기 전에`

JVM을 얘기하기 전에 `JDK vs JRE`와 기본 배경지식에 대해서 간단하게 정리하고자 합니다.

![jvm](https://camo.githubusercontent.com/240d4f951ae9a918200059ed9bffb4c5877a6cbf0bd87d5e8be1146796b6c252/68747470733a2f2f692e737461636b2e696d6775722e636f6d2f416176654e2e706e67)

- `JDK(Java Development Kit): JRE를 포함하여 자바 개발도구와 자바 컴파일러(javac)를 제공합니다.`
- `JRE(Java Runtime Environment): JDK에 포함된 JRE의 JVM과 java.lang, java.util 같은 Java API를 제공합니다.`

![스크린샷 2021-02-07 오후 9 08 54](https://user-images.githubusercontent.com/45676906/107145995-b189d100-6988-11eb-90d9-af2fe588b431.png)

즉, 자바 개발도구인 JDK를 이용해 개발된 프로그램은 JER에 의해 가상의 컴퓨터인 JVM 상에서 구동됩니다. 

<br>

간단히 배경지식을 알아보았으니 `JVM`에 대해서 알아보겠습니다. 

<br>

# `JVM(Java Virtual Machine)이란?` 

![jvm1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxKh6U%2FbtqCPzYJhpS%2FoKDKiaPoWqwqU86rf7IVVk%2Fimg.png)

JVM의 구조는 위와 같습니다. 크게 보면 `Java Compiler`, `Byte Code`, `Class Loader`, `Execution`, `Runtime Data Area,`, `Garbase Collector` 등등 존재하는 것을 볼 수 있습니다. 

그러면 `Java Source code`를 실행하면 어떤 과정이 일어날까요? 

- `자바 소스 파일을 자바 컴파일러(javac)가 해석하여 바이트코드(.class)로 변환시킵니다.`
- `바이트코드로 변환된 파일을 Class Loader가 JVM 내로 적재 시킵니다.(이 과정에서 Runtime Data Area에 올리는 거 같습니다)`
- `로딩된 class 파일들을 Execution engine을 통해 해석합니다.`
- `해석된 바이트코드는 Runtime Data Area로 옮겨져 실질적으로 수행이 이루어지게 됩니다.`

간단하게 말하면 `클래스 로더(Class Loader)가 컴파일된 자바 바이트코드를 런타임 데이터 영역(Runtime Data Areas)에 로드`하고, `실행 엔진(Execution Engine)이 자바 바이트코드를 실행`한다. 

<br>

## `Class Loader(클래스 로더)`

클래스로더는 간단하게 말하면 컴파일 타임이 아니라 런타임에 클래스를 처음으로 참조할 때 해당 클래스를 JVM 내로 클래스(.class) 파일을 `로딩` 시키고, `링크` 과정을 통해서 관련된 클래스들을 연결시키고 `초기화`하는 작업을 하는 곳입니다. 
특징을 정리해보면 아래와 같습니다. 

- `컴파일 타임에 JVM 내로 로딩을 하는 것이 아니라 클래스가 처음 사용될 때 로딩을 합니다. 즉, 동적 로딩을 하게 됩니다.`
- `클래스로더는 JVM 내의 Runtime Data Area에 바이트 코드를 배치시킵니다.`
 
이정도는 `클래스 로더`의 기본적인 특징이라서 `조금만~` 더 자세히 특징에 대해서 알아보겠습니다.

- ### `계층 구조`
    - 클래스 로더끼리 부모-자식 관계를 이루어 계층 구조로 생성됩니다. 아래 그림에서 볼 수 있듯이 최상위 클래스 로더는 `부트스트랩 클래스 로더`입니다.
    
- ### `위임 모델`
    - 계층 구조를 바탕으로 클래스 로더끼리 로드를 위임하는 구조로 동작합니다. 클래스를 로드할 때 먼저 상위 클래스 로더를 확인하여 상위 클래스 로더에 있다면 해당 클래스를 사용하고, 없다면 로드를 요청받은 클래스 로더가 클래스를 로드합니다.
  
- ### `가시성(visibility) 제한`
    - 하위 클래스 로더는 상위 클래스 로더의 클래스를 찾을 수 있지만, 상위 클래스 로더는 하위 클래스 로더의 클래스를 찾을 수 없습니다.
    
- ### `언로드 불가`
    - 클래스 로더는 클래스를 로드할 수는 있지만 언로드할 수는 없습니다. 언로드 대신, 현재 클래스 로더를 삭제하고 아예 새로운 클래스 로더를 생성하는 방법을 사용할 수 있습니다.

![loader](https://d2.naver.com/content/images/2015/06/helloworld-1230-2.png)

각 계층마다 존재하는 `클래스 로더`는 어떤 역할을 하는지 알아보겠습니다. 

- ### `부트스트랩 클래스 로더(Bootstrap Class Loader)`
    -  JVM을 기동할 때 생성되며, Object 클래스들을 비롯하여 자바 API들을 로드합니다. 다른 클래스 로더와 달리 자바가 아니라 네이티브 코드로 구현되어 있습니다.

- ### `익스텐션 클래스 로더(Extension Class Loader)`
    - 기본 자바 API를 제외한 확장 클래스들을 로드한다. 다양한 보안 확장 기능 등을 여기에서 로드하게 됩니다.
    
- ### `시스템 클래스 로더(System Class Loader)`
    - 부트스트랩 클래스 로더와 익스텐션 클래스 로더가 JVM 자체의 구성 요소들을 로드하는 것이라 한다면, 시스템 클래스 로더는 애플리케이션의 클래스들을 로드한다고 할 수 있습니다. 사용자가 지정한 $CLASSPATH 내의 클래스들을 로드합니다. 

- ### `사용자 정의 클래스 로더(User-Defined Class Loader)`
    - 애플리케이션 사용자가 직접 코드 상에서 생성해서 사용하는 클래스 로더입니다. 
    
<br>

그리고 클래스 로더가 아직 로드되지 않은 클래스를 찾으면, 다음 그림과 같은 과정을 거쳐 클래스를 `로드`하고 `링크`하고 `초기화`합니다. 

![class](https://d2.naver.com/content/images/2015/06/helloworld-1230-3.png)

- `로드`: 클래스를 파일에서 가져와서 JVM의 메모리에 로드합니다.
- `검증(Verifying)`: 읽어 들인 클래스가 자바 언어 명세(Java Language Specification) 및 JVM 명세에 명시된 대로 잘 구성되어 있는지 검사합니다. 클래스 로드의 전 과정 중에서 가장 까다로운 검사를 수행하는 과정으로서 가장 복잡하고 시간이 많이 걸립니다. JVM TCK의 테스트 케이스 중에서 가장 많은 부분이 잘못된 클래스를 로드하여 정상적으로 검증 오류를 발생시키는지 테스트하는 부분입니다.
- `준비(Preparing)`: 클래스가 필요로 하는 메모리를 할당하고, 클래스에서 정의된 필드, 메서드, 인터페이스들을 나타내는 데이터 구조를 준비합니다.
- `분석(Resolving)`: 클래스의 상수 풀 내 모든 심볼릭 레퍼런스(참조하는 대상의 이름을 지칭)를 다이렉트 레퍼런스(물리적 주소)로 변경합니다.
- `초기화`: 클래스 변수들을 적절한 값으로 초기화 합니다. 즉, `static initializer들을 수행하고, static 필드들을 설정된 값으로 초기화`합니다.

지금은 하나하나 특징을 자세히 적지는 않았기에, 더 자세한 것은 [여기](https://d2.naver.com/helloworld/1230) 에서 확인하면 좋습니다.
         
<br>

## `Runtime Data Area`

이번 글에서 제일 중점적으로 다룰 부분이 바로 `Runtime Data Area` 입니다. JVM이 Java ByteCode를 실행하기 위해 사용하는 메모리 공간입니다. 즉 JVM이라는 프로그램이 운영체제 위에서 실행되면서 할당받는 메모리 영역입니다.

아래 그림을 보면 여러가지 많은 것들이 있는데 하나씩 알아보면서 정리해보겠습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fci4Dqe%2FbtqCOyMIluC%2FkcfCKeWROOa7wGGKMdBy5K%2Fimg.png)

내부 구조는 위와 같습니다. 하나씩 간단하게 알아보고 `Heap`, `Runtime Constant Pool`에 대해서는 좀 더 자세히 알아보겠습니다.

일단 쓰레드는 `Main 쓰레드`, `사용자 쓰레드`와 같은 것들이 위와 같이 만들어진다고 생각하면 됩니다. 쓰레드 내에 존재하는 것을 보면 일반 `프로세스 - 쓰레드 관계에서 쓰레드 내부에 존재하는 것`들과 비슷합니다. 

- ### `PC Register`
    - Thread 마다 하나씩 갖고 있으며 현재 JVM이 수행할 명령어의 주소를 저장하는 공간입니다.

- ### `JVM Stack 영역`
    - 말 그대로 메소드 호출이나 변수의 복귀주소와 같은 것들이 쌓이는 공간입니다. 
    - Method 정보는 해당 Method의 매개변수, 지역변수, 임시변수 그리고 어드레스(메소드 호출 한 주소)등을 저장하고 Method 종료시 메모리 공간이 사라집니다.
    - `멀티 Thread 프로그램의 경우 각 Thread가 자신의 Stack을 가지고는 있지만 Heap 영역은 공유하기 때문에, 프로그래밍시에 Thread-safe 하지 않는 이슈에 주의하며 프로그래밍을 해야 한다. 결론적으로 Heap 영역 자체가 Thread-safe 하지 않는 상태입니다. Thread-safe 하게 객체를 생성하기 위해서는 Immutable한 객체를 설계하는 것이 좋습니다.`
    
- ### `Native Method stack`
    - 자바 외의 언어로 작성된 네이티브 코드를 위한 스택입니다. 즉, JNI(Java Native Interface)를 통해 호출하는 C/C++ 등의 코드를 수행하기 위한 스택으로, 언어에 맞게 C 스택이나 C++ 스택이 생성됩니다.
    
- ### `Method Area` 
    - `메서드 영역은 모든 스레드가 공유하는 영역으로 JVM이 시작될 때 생성`됩니다. `JVM이 읽어 들인 각각의 클래스와 인터페이스에 대한 런타임 상수 풀, 필드와 메서드 정보, Static 변수, 메서드의 바이트코드 등을 보관`합니다. 메서드 영역은 JVM 벤더마다 다양한 형태로 구현할 수 있으며,` 오라클 핫스팟 JVM(HotSpot JVM)에서는 흔히 Permanent Area, 혹은 Permanent Generation(PermGen)이라고 불린다.` 메서드 영역에 대한 가비지 컬렉션은 JVM 벤더의 선택 사항이다.   
    - 그러면 이거를 Java 7에서는 Perment Area라 부르고, Java 8에서 Method Area로 바뀐건가?... 궁금
   
- ### `Runtime Constant Pool`
    - 클래스 파일 포맷에서 constant_pool 테이블에 해당하는 영역입니다. 메서드 영역에 포함되는 영역이긴 하지만, `JVM 동작에서 가장 핵심적인 역할을 수행`하는 곳이기 때문에 JVM 명세에서도 따로 중요하게 기술합니다. 각 `클래스와 인터페이스의 상수`뿐만 아니라, `메서드와 필드에 대한 모든 레퍼런스까지 담고 있는 테이블`입니다. `즉, 어떤 메서드나 필드를 참조할 때 JVM은 런타임 상수 풀을 통해 해당 메서드나 필드의 실제 메모리상 주소를 찾아서 참조합니다.`

- ### `Heap` 
    - 인스턴스 또는 객체를 저장하는 공간으로 가비지 컬렉션 대상입니다. JVM 성능 등의 이슈에서 가장 많이 언급되는 공간입니다. 힙 구성 방식이나 가비지 컬렉션 방법 등은 JVM 벤더의 재량입니다.    

<br>


## `Execution Engine(실행 엔진)`       

자바 코드가 실행된 후에 `클래스 로더`를 통해서 위에서 말했던 것처럼 클래스 정보들이 JVM 내로 올라오게 됩니다. 그리고 클래스로더는 바이트코드를 `Runtime Data Area`로 배치시킨다고 했습니다. 

요약하자면 자바소스 파일을 `컴파일`하게 되면 `바이트코드`로 변환이 된 후에 다시 `실행 엔진`을 통해서 `기계어`로 변환이 되는 과정이 일어납니다. 이러한 과정 때문에 `자바는 OS에 독립적이다` 라는 특징을 가질 수 있습니다. 

`실행 엔진`에 보면 `Interpreter`, `JIT compiler`, `Garbage Collector`가 존재합니다. 

- ### `Interpreter`
    - 바이트코드 명령어를 하나씩 읽어서 해석하고 실행합니다. 하나씩 해석하고 실행하기 때문에 바이트코드 하나하나의 해석은 빠른 대신 인터프리팅 결과의 실행은 느리다는 단점을 가지고 있습니다. 흔히 얘기하는 인터프리터 언어의 단점을 그대로 가지는 것입니다. 즉, 바이트코드라는 '언어'는 기본적으로 인터프리터 방식으로 동작합니다.
    
- ### `JIT(Just-In-Time)`
    - 인터프리터의 단점을 보완하기 위해 도입된 것이 JIT 컴파일러입니다. `인터프리터 방식으로 실행하다가 적절한 시점에 바이트코드 전체를 컴파일하여 네이티브 코드로 변경하고, 이후에는 해당 메서드를 더 이상 인터프리팅하지 않고 네이티브 코드로 직접 실행하는 방식이다.` 네이티브 코드를 실행하는 것이 하나씩 인터프리팅하는 것보다 빠르고, 네이티브 코드는 캐시에 보관하기 때문에 한 번 컴파일된 코드는 계속 빠르게 수행되게 된다.
    `네이티브 코드를 실행하는 것이 하나씩 인터프리팅하는 것보다 빠르고, 네이티브 코드는 캐시에 보관하기 때문에 한 번 컴파일된 코드는 계속 빠르게 수행되게 된다.`                                                                                                                                                                           
    - > JIT 컴파일러가 컴파일하는 과정은 바이트코드를 하나씩 인터프리팅하는 것보다 훨씬 오래 걸리므로, 만약 한 번만 실행되는 코드라면 컴파일하지 않고 인터프리팅하는 것이 훨씬 유리하다. 따라서, JIT 컴파일러를 사용하는 JVM들은 내부적으로 해당 메서드가 얼마나 자주 수행되는지 체크하고, 일정 정도를 넘을 때에만 컴파일을 수행한다.

- ### `Garbage Collector`
    - GC를 수행하는 모듈이 존재합니다. 자세한 내용은 GC 글에서 정리를 하겠습니다.
    
<br>
 
그리고 Java 8 JVM에는 나름? 큰 변화가 있었습니다. 그것에 대해서도 정리를 해보려 합니다.  

![스크린샷 2021-02-09 오전 11 45 33](https://user-images.githubusercontent.com/45676906/107308638-52cd7000-6acc-11eb-9c19-e4f33a916e8f.png)

Java 7까지의 구조를 보면 `Permanent` 영역이 존재합니다. 그리고 Java 8에서는 `Permanent -> Metaspace`로 바뀌었습니다.
즉, `Heap` 영역에 속했던 것이 `Native 영역`으로 이동된 것입니다. 

<br>

### `Java 7에서 Java 8로 JVM의 변화`

먼저 java 7의 permanent 영역의 특징에 대해서 정리해보겠습니다. permanent 영역에는 다음과 같은 정보들이 저장되었습니다.

- `Class의 Meta 정보(바이트코드 포함)`
- `Method의 Meta 정보`
- `static Object, static 상수`
- `상수화된 String Object`
- `Class와 관련된 배열 객체 Meta 정보`
- `JVM 내부적인 객체들과 최적화 컴파일러(JIT)의 최적화 정보`

이러한 많은 정보들을 Permanent에 저장하다 보니,  `String 상수 풀 정보`, `static Object`, `Class Meta 정보`들이 쌓여 `Out Of Memory`가 발생하는 문제가 생겼습니다. 

그래서 Java 8에서는 `Permanent 영역을 삭제하고 Metaspace 영역을 추가하고 Native 영역으로 이동시켰습니다.`

![coding](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Febv9pZ%2Fbtqw6oJ0fvp%2FFq1JlAb8YlF2C5qg0rrirk%2Fimg.png)

위와 같은 그림의 형태로 바뀌게 되었습니다. 그러면 Permanent에 저장되었던 정보들은 어디에 저장이 될까요?

- `Class의 Meta 정보(바이트코드 포함) -> Native 영역으로 이동`
- `Method의 Meta 정보 -> Native 영역으로 이동`
- `static Object, static 상수 -> Heap 영역으로 이동`
- `상수화된 String Object -> Method Area로 이동`
- `Class와 관련된 배열 객체 Meta 정보`
- `JVM 내부적인 객체들과 최적화 컴파일러(JIT)의 최적화 정보`

<br>

> Perm 영역은 보통 Class의 Meta 정보나 Method의 Meta 정보, Static 변수와 상수 정보들이 저장되는 공간으로 흔히 메타데이터 저장 영역이라고도 한다. 이 영역은 Java 8 부터는 Native 영역으로 이동하여 Metaspace 영역으로 변경되었다. <br>
> (다만, 기존 Perm 영역에 존재하던 Static Object는 Heap 영역으로 옮겨져서 GC의 대상이 최대한 될 수 있도록 하였다)

![스크린샷 2021-02-08 오후 12 44 13](https://user-images.githubusercontent.com/45676906/107173545-5b11a680-6a0b-11eb-8740-3b57c9b4672e.png)

Native 영역의 가장 큰 특징 중의 하나는 Native 영역은 JVM에 의해서 크기가 강제되지 않고, 프로세스가 이용할 수 있는 메모리 자원을 최대로 활용 할 수 있습니다 

정리하면 아래와 같습니다. 

- `Perm은 JVM에 의해 크기가 강제되던 영역입니다.`
- `Metaspace는 Native 영역으로 OS가 자동으로 크기를 조절합니다.`
- `그 결과 기존과 비교해 큰 메모리 영역을 사용할 수 있게 되었습니다.`
    - `Java 8부터는 Perm 영역 크기로 인한 java.lang.OutOfMemoryError를 보기 힘들어졌습니다.`

<br>

### `왜 Perm이 제거됐고 Metaspace 영역이 추가된 것일까?`

> 최근 Java 8에서 JVM 메모리 구조적인 개선 사항으로 Perm 영역이 Metaspace 영역으로 전환되고 기존 Perm 영역은 사라지게 되었다. Metaspace 영역은 Heap이 아닌 Native 메모리 영역으로 취급하게 된다. (Heap 영역은 JVM에 의해 관리된 영역이며, Native 메모리는 OS 레벨에서 관리하는 영역으로 구분된다) Metaspace가 Native 메모리를 이용함으로서 개발자는 영역 확보의 상한을 크게 의식할 필요가 없어지게 되었다.

<br>

### `SDK로 사이즈 알아보기`

Java 7  버전으로 `Perm` 사이즈를 알아보겠습니다. 

```
sdk use java 7.0.282-zulu (Java Version 7로 변경)
sdk current (Java 현재 버전 확인)
java -XX:+PrintFlagsFinal -version -server | grep PermSize 
```

![스크린샷 2021-02-09 오전 11 51 46](https://user-images.githubusercontent.com/45676906/107309188-51507780-6acd-11eb-9cae-a87e2d130af3.png)

<br>

이번에는 Java 11 버전으로 `Metapsace` 사이즈를 알아보겠습니다. 

```
sdk use java 11.0.10.hs-adpt (Java Version 11로 변경)
java -XX:+PrintFlagsFinal -version -server | grep MetaspaceSize
```

![스크린샷 2021-02-09 오전 11 57 44](https://user-images.githubusercontent.com/45676906/107309599-13a01e80-6ace-11eb-8caf-72c20d02736e.png)

Java 8의 `MaxMetaspaceSize`는 `18446744073709547520`인 것을 볼 수 있습니다. 이는 `약 16ExaBye, 64bit 프로세서 최고 메모리 상한치`라고 합니다. 

즉, Metaspace 영역은 `Native 영역`이기 때문에 개발자가 크게 신경을 쓰지 않아도 되는 영역으로 바뀐 것 같습니다.

<br>

## `Heap 이란?`

Heap은 `new` 연산을 통해서 객체를 만들면 인스턴스가 Heap 영역의 메모리에 할당이 됩니다. 프로그램이 시작될 때 미리 Heap 영역을 할당해 놓으며 `인스턴스와 인스턴스 변수가 저장됩니다.` 레퍼런스 변수의 경우 Heap에 인스턴스가 저장되는 것이 아니라 포인터가 저장됩니다.   

> Heap 영역은 Garbage Collection의 대상이 되는 영역

Heap은 위의 그림에서 보았듯이 `Runtime Method Area` 안에 속해있습니다. Heap 내부에는 `Eden`, `Survivor`, `Old generation`이 있습니다. 

![coding](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Febv9pZ%2Fbtqw6oJ0fvp%2FFq1JlAb8YlF2C5qg0rrirk%2Fimg.png)

- `Young 영역(Yong Generation 영역)`: 새롭게 생성한 객체의 대부분이 여기에 위치합니다. 대부분의 객체가 금방 접근 불가능 상태가 되기 때문에 매우 많은 객체가 Young 영역에 생성되었다가 사라집니다. 이 영역에서 객체가 사라질때 `Minor GC`가 발생한다고 말합니다.
- `Old 영역(Old Generation 영역)`: 접근 불가능 상태로 되지 않아 Young 영역에서 살아남은 객체가 여기로 복사됩니다. 대부분 Young 영역보다 크게 할당하며, 크기가 큰 만큼 Young 영역보다 GC는 적게 발생합니다. 이 영역에서 객체가 사라질 때 `Major GC(혹은 Full GC)`가 발생한다고 말합니다.


<br> <br>

# `Reference`

- [https://jins-dev.tistory.com/entry/Java-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94ClassLoader%EC%97%90-%EB%8C%80%ED%95%9C-%EC%9D%B4%ED%95%B4](https://jins-dev.tistory.com/entry/Java-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94ClassLoader%EC%97%90-%EB%8C%80%ED%95%9C-%EC%9D%B4%ED%95%B4)
- [https://asfirstalways.tistory.com/158](https://asfirstalways.tistory.com/158)
- [https://iann.tistory.com/17](https://iann.tistory.com/17)
- [https://swiftymind.tistory.com/112](https://swiftymind.tistory.com/112)
- [https://yckwon2nd.blogspot.com/2015/03/java8-permanent.html](https://yckwon2nd.blogspot.com/2015/03/java8-permanent.html)
- [https://coding-start.tistory.com/205](https://coding-start.tistory.com/205)
- [https://johngrib.github.io/wiki/java8-why-permgen-removed/](https://johngrib.github.io/wiki/java8-why-permgen-removed/)
- [https://www.holaxprogramming.com/2013/07/16/java-jvm-runtime-data-area/](https://www.holaxprogramming.com/2013/07/16/java-jvm-runtime-data-area/)
- [https://blog.voidmainvoid.net/184](https://blog.voidmainvoid.net/184)
- [https://javaslave.tistory.com/23](https://javaslave.tistory.com/23)
- [https://d2.naver.com/helloworld/1230](https://d2.naver.com/helloworld/1230)
- [https://d2.naver.com/helloworld/1329](https://d2.naver.com/helloworld/1329)