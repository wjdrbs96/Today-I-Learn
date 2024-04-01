# `문자열 인코딩, 디코딩 이란?`


## `인코딩(Encoding)이란?`

`인코딩(Encoding)`이란 문자를 컴퓨터가 이해하기 쉽게 변환하는 과정이라고 생각하면 쉬울 것 같습니다. 

<br>

## `디코딩(Decoding)이란?`

`디코딩(Decoding)`이란 일반적으로 암호화되어 있거나 컴퓨터가 이해할 수 있는 값들을 알아보기 쉽게 변환하는 것을 말합니다.

<br>

## `인코딩(Encoding)이 필요한 이유는?`

> 컴퓨터는 모든 정보를 0과 1인 숫자로만 저장합니다. 
> 사용자가 문서 작업을 하면서 작성한 한글, 영문, 특수기호 등은 컴퓨터가 이해할 수 없습니다.
> 컴퓨터가 이해할 수 있는 신호로 변환해주는 과정을 인코딩이라고 합니다.
> 만약 인코딩이 제대로 이루어지지 않는다면, 컴퓨터는 우리가 작업한 문서에 문자들을 이해할 수 없기 떄문에 문자깨짐 현상이 나타나게 됩니다.

그러면 흔히 많이 들어본 `UTF-8`, `UTF-16`, `EUC-KR` 등등은 무엇일까요? 이것이 바로 `인코딩 방식`입니다.

위의 인코딩 방식에 대해서 알기 위해서는 먼저 `아스키 코드`, `유니 코드`가 무엇인지 알아야 합니다. 많이 들어봤지만 막상 설명하려고 하면 되게 헷갈리는데 정리를 한번 해보겠습니다. 

<br>

## `아스키 코드(ACII CODE)란?`

![ascci](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FqZZUy%2FbtqBdUY0S42%2FrLKvu8vNdrkiWmJo2MK6sK%2Fimg.jpg)

아스키 코드는 영어 알파벳과 특정 문자에 대해서 숫자와 1:1 매핑을 시켜놓은 것이고, 7비트의 이진수 조합으로 만들어져 총 128개의 문자를 표현합니다.

그런데 위의 표를 보면 알파벳과 일부 특수 기호들만 존재합니다. 그러면 `한글`, `일본어`, `아랍어` 등등 언어는 어떻게 표현할 수 있을까요?

아쉽게도 `아스키 코드`로는 표현하는 것이 불가능합니다. 이러한 이유로 `국제 표준화 기구(ISO, International Standards Organization)`은 `ISO-646`과 `ISO-8859`를 도입했습니다. 

<br>

## `유니 코드 (Unicode)`

그래서 세계의 모든 언어들을 표현하기 위해 나온 것이 바로 `유니 코드`입니다. 유니코드는 언어와 상관없이 모든 문자를 16비트로 표현하므로 최대 65,536자까지 표현할 수 있습니다. 

(참고로 `자바`는 모든 문서를 `유니코드`로 처리합니다)

그러면 대략 `인코딩 방식`에 대해 알아보기 전에 알아야 할 사전 지식을 어느정도 공부한 것 같습니다. 이제 `인코딩 방식`에 대해서 알아보겠습니다.

아스키 코드, ISO, 유니코드에 대해서 좀 더 자세히 싶다면 [여기](https://velog.io/@viiviii/1-ASCII-ISO-8859-Unicode%EC%9D%98-%ED%83%84%EC%83%9D) 글을 읽어보면 좋을 거 같습니다.

<br>

## `인코딩 방식이란?`

위에서 보았듯이 대표적으로 `EUC-KR`, `UTF-8`, `UTF-16`이 있습니다. 하나씩 어떤 의미를 갖고 있는지 알아보겠습니다. 

- ### `EUC-KR: 대표적인 한글 인코딩 방식 중에 하나입니다.`
    - 영어는 그대로 아스키코드 1바이트로 표현할 수 있고, 한글은 2바이트로 표현할 수 있습니다. 
    - 하지만 모든 현대 한글 음절을 표현할 수 없습니다. 국제적으로 교환할 수 있는 문서 및 여러 나라 언어를 처리하는 시스템에서는 곤란합니다. 
     
- ### `UTF-8: 대표적인 유니코드 인코딩 방식`
    - 영미권 사람들은 1 byte만 있어도 영어를 표현할 수 있어 굳이 2 byte를 사용하지 않아도 되기에 이럴 때 사용하는 방법이 UTF-8 입니다.
    - UTF-8은 가변 인코딩 방식입니다. 다시 말하면 `a`는 1 byte, `가`는 3 byte 입니다. 즉 영어, 숫자는 1 byte, 한글은 3 byte 입니다.
    - 문자들 마다 크기가 달라서 다루기 어렵다는 단점이 있습니다.
    - 8비트를 기준으로 인코딩 하기 때문에 8이 붙었습니다.
    
- ### `UTF-16: 대표적인 유니코드 인코딩 방식`
    - UTF-8으로 사전적인 모든 문자들은 표시할 수 있었지만, 한자나 고어, 이모티콘 등 다양한 문자를 표현하기 위해 2 byte 단위의 UTF-16이 등장했습니다.
    - 1 byte로 표현되는 영어, 숫자가 2 byte로 표현되어서 용량이 커지는 단점이 있습니다.
    - 16비트를 기준으로 인코딩 하기 때문에 16이 붙었습니다. 

<br>

## `Java에서 문자 인코딩`

자바에서는 String 클래스를 만들 때 인코딩 값을 넣을 수 있습니다. 

```java
public final class String {

    public String(byte bytes[], String charsetName)
            throws UnsupportedEncodingException {
        this(bytes, 0, bytes.length, charsetName);
    }

    public String(byte bytes[]) {
        this(bytes, 0, bytes.length);
    }
}
```

String 클래스에 내부에 보면 많은 생성자 중에 위와 같은 생성자가 존재합니다. 그리고 위의 매개변수는 보면 `charsetName`이 나오는데 이것을 무엇을 의미할까요?

위에서 정리했던 `인코딩 방식`을 넘겨주는 곳입니다.(ex: UTF-8, UTF-16 등등)

<br>

### `String 문자열 byte로 변환하기`

```java
public final class String {
    public byte[] getBytes(String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null) throw new NullPointerException();
        return StringCoding.encode(charsetName, value, 0, value.length);
    }

    public byte[] getBytes(Charset charset) {
        if (charset == null) throw new NullPointerException();
        return StringCoding.encode(charset, value, 0, value.length);
    }

    public byte[] getBytes() {
        return StringCoding.encode(value, 0, value.length);
    }
}
```

String 클래스 내부에 `getBytes()` 메소드가 존재합니다. 이것은 문자열은 인코딩 할 때 사용하는 메소드 입니다. 위와 같이 charsetName을 지정해줄 수도 있고 안해줄 수도 있습니다.

<br>

## `캐릭터 셋 이름`

| 캐릭터 셋 이름   | 설명                                                      |
|------------|---------------------------------------------------------|
| US-ASCII   | 7비트 아스키                                                 |
| ISO-8859-1 | ISO 라틴 알파벳                                              |
| UTF-8      | 8비트 UCS 변환 포맷                                           |
| UTF-16BE   | 16비트 UCS 변환 포맷. big-endian 바이트 순서를 가진다.                 |
| UTF-16LE   | 16비트 UCS 변환 포맷. little-endian 바이트 순서를 가진다.              |
| UTF-16     | 16비트 UCS 변환 포맷. 바이트의 순서는 byte-order mark라는 것에 의해서 정해진다. |
| EUC-KR     | 8비트 문자 인코딩으로, EUC의 일종이며 대표적인 "한글 완성형" 인코딩               |
| MS949      | Microsoft에서 만든 "한글 확장 완성형" 인코딩                          |

<br>

이제 예제 코드를 보면서 알아보겠습니다. 

```java
public class Test {
    public static void main(String[] args) {
        String korean = "한글";
        byte[] bytes = korean.getBytes();
        String string = new String(bytes);
        System.out.println(string);
    }
}
```
```
한글
```

위와 같이 `korean`을 `getBytes()` 의해서 인코딩 할 때 매개변수에 아무 것도 넘겨주지 않으면 `defaultCharset`이 적용되어 UTF-8로 적용됩니다.

그리고 String 클래스를 이용해서 다시 디코딩 할 때도 따로 디코딩 방식을 지정해주지 않으면 UTF-8로 디코딩하기 때문에 문제 없이 한글이 출력되는 것을 볼 수 있습니다.

```java
import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "한글";
        byte[] bytes = korean.getBytes("UTF-16");
        String string = new String(bytes);
        System.out.println(string);
    }
}
```


하지만 위와 같이 직접 `UTF-16`으로 인코딩 방식을 지정했는데 디코딩 방식은 지정해주지 않으면 어떻게 될까요? 결과는 `한글이 깨져서 출력됩니다.`

즉, 인코딩은 UTF-16 으로 했지만, 디코딩 할 때는 UTF-8로 했다고 할 수 있습니다.

따라서 이러한 경우에는 아래와 같이 `인코딩과 디코딩 방식을 맞춰줘야` 합니다.

```java
import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "한글";
        byte[] bytes = korean.getBytes("UTF-16");
        String string = new String(bytes, "UTF-16");
        System.out.println(string);
    }
}
```
```
한글
```

그러면 한글이 잘 출력되는 것을 알 수 있습니다.

<br>

## `Referenece`

- [자바의 신 - 1]()
- [https://velog.io/@viiviii/1-ASCII-ISO-8859-Unicode%EC%9D%98-%ED%83%84%EC%83%9D](https://velog.io/@viiviii/1-ASCII-ISO-8859-Unicode%EC%9D%98-%ED%83%84%EC%83%9D)
- [https://stackoverflow.com/questions/5943152/string-decode-utf-8](https://stackoverflow.com/questions/5943152/string-decode-utf-8)
- [https://stackoverflow.com/questions/7048745/what-is-the-difference-between-utf-8-and-iso-8859-1](https://stackoverflow.com/questions/7048745/what-is-the-difference-between-utf-8-and-iso-8859-1)