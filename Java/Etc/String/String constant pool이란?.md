# `들어가기 전에`

어느정도의 사전 지식은 있다고 생각하고 정리 하려고 합니다. 
기본적인 내용을 잘 모르겠다면 [String vs StringBuilder vs StringBuffer 차이](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Java_lang/String%20vs%20StringBuffer%20vs%20StringBuilder.md) 를 보고 오시면 됩니다.

<br>

# `String Constant pool이란?`

```java
public class Test {
    public static void main(String[] args) {
        String test = "Gyunny";
    }
}
```
 
`new 연산자`를 쓰지 않고 위와 같이 String 클래스를 사용한다면 `constant pool(상수 풀)`을 이용한다는 것을 알고 있을 것입니다. 

<br>

### `그러면 상수풀은 어디에 있는 것일까요?`

`상수 풀`에 대해서 알려면 JVM에 대해서 어느정도 알아야 합니다. 하지만 JVM 글이 아니기 때문에 JVM에 대해서 자세히 정리하지는 않겠습니다.

![jvm](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxKh6U%2FbtqCPzYJhpS%2FoKDKiaPoWqwqU86rf7IVVk%2Fimg.png)

간단하게 말하면 현재 JVM에서는 `상수 풀`은 `Runtime Data Area` 안에 존재합니다. 하지만 자바 버전이 바뀔 때마다 `상수 풀`의 위치와 특징들이 조금씩 바뀌었는데 어떻게 바뀌었고 왜 바뀌었는지에 대해서 알아보겠습니다.

<br>

### `Java Heap 구조`

![runtime](https://mirinae312.github.io/img/jvm_memory/JVMHeap.png)

`Runtime Data Area` 안에 `Heap`의 구조를 보면 위와 같이 되어 있습니다.  

여기서 `permanent 영역`이 Java 7, Java 8에서 변화가 있었습니다. 어떤 변화인지 자세히 알아보겠습니다. 

<br>

### `Java7: permanent 영역`

자바 8에서 Java Heap과 Perm 영역은 아래와 같은 역할을 수행합니다.

- `Java Heap`
    - PerGen에 있는 클래스의 인스턴스 저장
    - `-Xms(min)`, `-Xmx(max)`로 사이즈 조정
- `PermGen`
    - 클래스와 메소드의 메타데이터 저장
    - 상수 풀 정보
    - JVM, JIT 관련 데이터
    - static 변수 저장
    - `-XX: PermSize(min)`, `-XX: MaxPermSize(max)`로 사이즈 조정
    
그래서 Perm에 대해서 아래와 같이 설명합니다.

> Perm 영역은 보통 Class의 Meta 정보나 Method의 Meta 정보, Static 변수와 상수 정보들이 저장되는 공간으로 흔히 메타데이터 저장 영역이라고 합니다.

Perm 영역은 메모리 사이즈도 고정이기 때문에 String 상수 풀이 많이 생기거나 많은 클래스 정보들이 등록되었을 때 `OutOfMemory Error`가 발생할 가능성이 존재해서 위험하다는 특징을 가지고 있습니다. 

그래서 자바 8에서는 아래와 같은 변경사항이 생겼습니다. 

<br>

### `Java8: Metaspace 영역`

> 자바 8부터는 자바 7에 있는 Perm 영역이 삭제되고 Native 영역으로 이동하여 Metaspace 영역으로 변경되었습니다. 
> 다만 기존 Perm에 존재하는 Static Object는 Heap 영역으로 옮겨져서 GC의 대상이 최대한 될 수 있도록 하였습니다.

![java8](https://t1.daumcdn.net/cfile/tistory/993ADD3E5C7681222D)

<br>

### `왜 Perm이 제거되고 Metaspace 영역이 추가된 것일까요?`

> 최근 Java 8에서 JVM 메모리 구조적인 개선 사항으로 Perm 영역이 Metaspace 영역으로 전환되고 기존 Perm 영역은 사라지게 되었다. Metaspace 영역은 Heap이 아닌 Native 메모리 영역으로 취급하게 된다. 
> (Heap 영역은 JVM에 의해 관리된 영역이며, Native 메모리는 OS 레벨에서 관리하는 영역으로 구분된다) 
> Metaspace가 Native 메모리를 이용함으로서 개발자는 영역 확보의 상한을 크게 의식할 필요가 없어지게 되었다.

위와 같은 이유라고 합니다. 정리하면, 각종 메타 정보를 OS가 관리하는 영역으로 옮겨 Perm 영역의 사이즈 제한을 없앤 것이라고 할 수 있습니다.

![스크린샷 2021-01-13 오전 3 59 33](https://user-images.githubusercontent.com/45676906/104360004-c161f100-5553-11eb-8feb-113324656a2b.png)

<br>

> 자바 7에서 `MaxPermSize`는  85,983,232 byte, 즉 82 MB 정도 나왔다고 합니다.
>
> 반면 자바 8에서 `MaxMetaspaceSize`는  18,446,744,073,709,547,520 byte, 즉 17,592,186,044,415 MB 정도 된다고 합니다.

<br>

따라서 현재는 아래와 같은 JVM 구조를 가지게 됩니다. 

![runtime](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbDyjp6%2FbtqCO0WAzvo%2FXkJYTnuUOHD5Wy1Rn2mK60%2Fimg.png)

`Runtime Data Area`를 자세히 보면 위와 같이 되어 있습니다. 흔히 우리가 new를 이용해서 객체를 생성할 때 저장된다고 하는 `Heap`이 존재하는 곳입니다.

지금 위의 보이는 구조가 최신 구조입니다. 현재는 `Method Area`라는 공간에 `상수 풀`이 존재하는 것을 볼 수 있습니다. 

<br>

### `Method Area란?`

클래스 정보를 처음 메모리 공간에 올릴 때 초기화되는 대상을 저장하기 위한 메모리 공간입니다. 
프로그램 실행 중 어떤 클래스가 사용되면, JVM은 해당 클래스의 클래스파일(*.class)을 읽어서 분석하여 클래스에 대한 정보(클래스 데이터)를 이곳에 저장합니다.
이 때, 그 클래스의 `클래스변수(static)(class variable)`도 Method Area(메서드 영역)에 함께 생성됩니다.
                         
또한 `Runtime constant pool` 은 Method area 내부에 존재하는 영역으로, 이는 상수 자료형을 저장하여 참조하고 중복을 막는 역할을 수행합니다.

따라서 상수풀은 JVM 내에 `Runtime Data Area` -> `Method Area` 안에 존재하는 것을 알 수 있습니다. 

<br>

## `참고하기`

```
현재 Hotspot의 클래스 메타 데이터, interned String, class static 변수들은 Java heap의 permanent generation에 저장됩니다. 
permanent generation은 Hotspot이 관리하며, 앞에서 말한 것들을 모두 저장하므로 반드시 충분한 공간을 갖고 있어야 합니다. 
클래스 메타 데이터와 static 변수들은 클래스가 로드될 때 permanent generation에 할당되고, 클래스가 언로드될 때 gc 처리됩니다. 
interned String은 permanent generation의 gc가 발생할 때 같이 수집됩니다.
```
```
구현 제안서에 따르면 클래스 메타 데이터는 네이티브 메모리에 할당하고, interned String와 클래스 statics는 Java heap으로 이동합니다. 
Hotspot은 클래스 메타 데이터에 대한 네이티브 메모리를 명시적으로 할당하고 해제할 것입니다. 
새 클래스 메타 데이터의 할당은 네이티브 메모리의 사용 가능한 양에 의해 제한되며, 커맨드 라인을 통해 설정된 -XX:MaxPermSize 값으로 고정되지 않습니다.
```

<br>

# Reference

- [https://johngrib.github.io/wiki/java8-why-permgen-removed/](https://johngrib.github.io/wiki/java8-why-permgen-removed/) <br>
- [https://www.baeldung.com/java-string-pool](https://www.baeldung.com/java-string-pool) <br> 
- [https://coding-start.tistory.com/205](https://coding-start.tistory.com/205) <br> 
- [https://swiftymind.tistory.com/112](https://swiftymind.tistory.com/112) <br>
- [https://stackoverflow.com/questions/4918399/where-does-javas-string-constant-pool-live-the-heap-or-the-stack](https://stackoverflow.com/questions/4918399/where-does-javas-string-constant-pool-live-the-heap-or-the-stack)



