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
- `바이트코드로 변환된 파일을 Class Loader가 JVM 내로 적재 시킵니다.`
- `로딩된 class 파일들을 Execution engine을 통해 해석합니다.`
- `해석된 바이트코드는 Runtime Data Area로 옮겨져 실질적으로 수행이 이루어지게 됩니다.`

간단하게 말하면 이러한 과정을 통해서 자바 프로그램이 실행됩니다. 하나씩 세부적으로 어떤 의미를 갖고 있는지 좀 더 자세히 알아보겠습니다. 

<br>

## `Class Loader(클래스 로더)`

클래스로더는 간단하게 말하면 JVM 내로 클래스(.class) 파일을 로딩 시키고, 링크 과정을 통해서 관련된 클래스들을 연결시키고 초기화하는 작업을 하는 곳입니다. 
특징을 정리해보면 아래와 같습니다. 

- `컴파일 타임에 JVM 내로 로딩을 하는 것이 아니라 클래스가 처음 사용될 때 로딩을 합니다. 즉, 동적 로딩을 하게 됩니다.`
- `사용하지 않는 클래스는 메모리에서 삭제하고, 처음으로 사용된 클래스만 로딩, 링크, 초기화하는 작업이 이루어지게 됩니다.`
- `클래스로더는 JVM 내의 Runtime Data Area에 바이트 코드를 배치시킵니다.`

- ### `Loading`
    - 클래스 파일에서 클래스 이름, 상속 관계, 클래스의 타입(class, interface, enum), 메소드 & 생성자 & 멤버변수 정보, 상수 등에 대한 정보를 Binary 데이터로 변경합니다.
- ### `Linking`
    - Verification 과 Preparation, Resolution 단계를 거치면서 바이트코드를 검증하고 필요한 만큼의 메모리를 할당합니다.
- ### `initialization`
    - static block의 초기화 및 static 데이터들을 할당합니다.
                
<br>

## `Execution Engine(실행 엔진)`       

자바 코드가 실행된 후에 `클래스 로더`를 통해서 위에서 말했던 것처럼 클래스 정보들이 JVM 내로 올라오게 됩니다. 그리고 클래스로더는 바이트코드를 `Runtime Data Area`로 배치시킨다고 했습니다. 

요약하자면 자바소스 파일을 `컴파일`하게 되면 `바이트코드`로 변환이 된 후에 다시 `실행 엔진`을 통해서 `기계어`로 변환이 되는 과정이 일어납니다. 이러한 과정 때문에 `자바는 OS에 독립적이다` 라는 특징을 가질 수 있습니다. 

`실행 엔진`에 보면 `Interpreter`, `JIT compiler`, `Garbage Collector`가 존재합니다. 

- ### `Interpreter`
    - 인터프리터는 런타임시에 한줄 한줄 읽어가며 변환하는 것입니다. 하지만 이것은 한줄씩 수행하기 때문에 느리다는 단점을 가지고 있습니다.(파이썬이 C언어보다 느린 이유와 같을 거? 같습니다.)
    
- ### `JIT(Just-In-Time)`
    - JIT Compiler는 인터프리터 언어의 단점(느림)을 보완하기 위해 나왔습니다. 간단히 말하면 바이트코드를 컴파일 하여 `네이티브 코드(기계어)`로 변경하고 이후에는 더 이상 인터프리팅 하지 않고 네이티브 코드로 직접 실행하는 방식입니다. 
    > 네이티브 코드는 캐시에 보관하기 때문에 한 번 컴파일된 코드는 빠르게 수행하게 됩니다. 하지만 JIT 컴파일러가 컴파일하는 과정은 바이트코드를 인터프리팅하는 것보다 훨씬 오래걸리기 때문에 한 번만 실행되는 코드라면 컴파일 하지 않고 인터프리팅 하는 것도 중요합니다. <br> <br>
      출처: [https://asfirstalways.tistory.com/158](https://asfirstalways.tistory.com/158)
                                                                                                                                                                           
- ### `Garbage Collector`
    - GC를 수행하는 모듈이 존재합니다. 자세한 내용은 GC 글에서 정리를 하겠습니다.
    
<br>

## `Runtime Data Area`

이번 글에서 제일 중점적으로 다룰 부분이 바로 `Runtime Data Area` 입니다. 정말 중요한 `Heap` 영역이 속해 있고, 여러가지 많은 것들을 알아보면서 정리해보겠습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fci4Dqe%2FbtqCOyMIluC%2FkcfCKeWROOa7wGGKMdBy5K%2Fimg.png)

내부 구조는 위와 같습니다. 하나씩 간단하게 알아보고 `Heap`, `Runtime Constant Pool`에 대해서는 좀 더 자세히 알아보겠습니다.

일단 쓰레드는 `Main 쓰레드`, `사용자 쓰레드`와 같은 것들이 위와 같이 만들어진다고 생각하면 됩니다. 쓰레드 내에 존재하는 것을 보면 일반 `프로세스 - 쓰레드 관계에서 쓰레드 내부에 존재하는 것`들과 비슷합니다. 

<br>

- ### `PC Register`
    - Thread 마다 하나씩 갖고 있으며 현재 JVM이 수행할 명령어의 주소를 저장하는 공간입니다.

- ### `JVM 스택 영역`
    - 말 그대로 메소드 호출이나 변수의 복귀주소와 같은 것들이 쌓이는 공간입니다. 
    
- ### `Native Method stack`
    - 실제 Object 클래스의 hashCode()와 같이 특정 클래스들의 어떤 메소드를 보면 `native`가 붙어 있는 것을 볼 수 있습니다. 이러한 메소드들이 저장되어 있는 공간입니다.
    네이티브 메소드는 OS의 시스템 정보, 리소스를 사용하거나 접근하기 위한 코드로 C, C++로 작성되어 있습니다.     
        
- ### `Method Area` 
    - Method Area는 중요하게 정리하고 넘어갈 부분 중에 하나입니다. 클래스 로더를 통해서 클래스의 `클래스 변수`, `static 블록`, `static 변수`, `상수` 등이 초기화 되고 저장됩니다. 
       

<br>

그리고 중요하게 봐야할 부분은 `Heap` 영역과 `Constant Pool` 입니다. 

일단 Java 8에 JVM에는 나름? 큰 변화가 있었습니다. (위에 보이는 `Runtime Data Area`는 Java 8 이후의 구조도 라고 생각하면 됩니다.) 

![HotSpot](https://t1.daumcdn.net/cfile/tistory/993ADD3E5C7681222D)

Java 7까지의 구조를 보면 `Permanet` 영역이 존재합니다. 그리고 Java 8에서는 `Permanent -> Metaspace`로 바뀌었습니다. 

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

> Perm 영역은 보통 Class의 Meta 정보나 Method의 Meta 정보, Static 변수와 상수 정보들이 저장되는 공간으로 흔히 메타데이터 저장 영역이라고도 한다. 이 영역은 Java 8 부터는 Native 영역으로 이동하여 Metaspace 영역으로 변경되었다. <br>
> (다만, 기존 Perm 영역에 존재하던 Static Object는 Heap 영역으로 옮겨져서 GC의 대상이 최대한 될 수 있도록 하였다)

정리하면 아래와 같습니다. 

![스크린샷 2021-02-08 오후 12 44 13](https://user-images.githubusercontent.com/45676906/107173545-5b11a680-6a0b-11eb-8740-3b57c9b4672e.png)

<br>

### `왜 Perm이 제거됐고 Metaspace 영역이 추가된 것일까?`

> 최근 Java 8에서 JVM 메모리 구조적인 개선 사항으로 Perm 영역이 Metaspace 영역으로 전환되고 기존 Perm 영역은 사라지게 되었다. Metaspace 영역은 Heap이 아닌 Native 메모리 영역으로 취급하게 된다. (Heap 영역은 JVM에 의해 관리된 영역이며, Native 메모리는 OS 레벨에서 관리하는 영역으로 구분된다) Metaspace가 Native 메모리를 이용함으로서 개발자는 영역 확보의 상한을 크게 의식할 필요가 없어지게 되었다.

<br>

# `Reference`

- [https://jins-dev.tistory.com/entry/Java-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94ClassLoader%EC%97%90-%EB%8C%80%ED%95%9C-%EC%9D%B4%ED%95%B4](https://jins-dev.tistory.com/entry/Java-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94ClassLoader%EC%97%90-%EB%8C%80%ED%95%9C-%EC%9D%B4%ED%95%B4)
- [https://asfirstalways.tistory.com/158](https://asfirstalways.tistory.com/158)
- [https://iann.tistory.com/17](https://iann.tistory.com/17)
- [https://swiftymind.tistory.com/112](https://swiftymind.tistory.com/112)
- [https://yckwon2nd.blogspot.com/2015/03/java8-permanent.html](https://yckwon2nd.blogspot.com/2015/03/java8-permanent.html)
- [https://coding-start.tistory.com/205](https://coding-start.tistory.com/205)
- [https://johngrib.github.io/wiki/java8-why-permgen-removed/](https://johngrib.github.io/wiki/java8-why-permgen-removed/)