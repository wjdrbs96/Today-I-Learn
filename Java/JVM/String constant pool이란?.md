# `String constant pool이란?`

이번 글에서는 String `상수 풀`에 대해서 정리해보려 합니다. 어느정도의 사전 지식은 있다고 생각하고 정리 하려고 합니다. 
기본적인 내용을 먼저 보고 오려면 [String vs StringBuilder vs StringBuffer 차이](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Java_lang/String%20vs%20StringBuffer%20vs%20StringBuilder.md) 를 보고 오면 됩니다.

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

`상수 풀`에 대해서 알려면 JVM에 대해서 알아야 합니다. 하지만 JVM 글이 아니기 때문에 JVM에 대해서 자세히 정리하지는 않습니다.

![jvm](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxKh6U%2FbtqCPzYJhpS%2FoKDKiaPoWqwqU86rf7IVVk%2Fimg.png)

간단하게 말하면 `상수 풀`은 `Runtime Data Area` 안에 존재합니다. 하지만 그 안에서도 자바 버전이 바뀔 때마다 `상수 풀`의 위치는 조금씩 바뀌었는데 어떻게 바뀌었고 왜 바뀌었는지에 대해서 알아보겠습니다.

![runtime](https://mirinae312.github.io/img/jvm_memory/JVMHeap.png)

Runtime Data Area 안에 Heap의 구조를 보면 위와 같이 되어 있습니다. 

자바 7 이전에는 String 상수풀을 permanent 영역에서 관리를 했는데 이 영역은 메모리 사이즈가 고정이 되어 있어서 `상수 풀이 빈번하게 등록이` 일어나면 `OutOfMemery Error`를 발생시킨다는 문제가 있었습니다.
(그리고 상수풀에 있는 것들은 GC가 되지 않는다는 문제도 있었습니다.)

그래서 자바 7에서는 `String constant pool`을 GC가 될 수 있게 Heap에다 저장을 했습니다. 그래서 `OutOfMemory Error`를 줄일 수 있었습니다. 

<br>

![java8](https://t1.daumcdn.net/cfile/tistory/993ADD3E5C7681222D)

자바 8에서 부터는 메모리 구조가 위와 같이 변경되었습니다. 그리고 상수풀은 Method Area에서 관리하게 됩니다.


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

> 정확하게는 잘 모르겠다... 메모리가 올라가고 등등 구조가 어떻게 바뀌고.. 아직 완성 못한 글..

<br>

# Reference

[https://www.baeldung.com/java-string-pool](https://www.baeldung.com/java-string-pool)
[https://coding-start.tistory.com/205](https://coding-start.tistory.com/205)
[https://johngrib.github.io/wiki/java8-why-permgen-removed/](https://johngrib.github.io/wiki/java8-why-permgen-removed/)
[https://swiftymind.tistory.com/112](https://swiftymind.tistory.com/112)



