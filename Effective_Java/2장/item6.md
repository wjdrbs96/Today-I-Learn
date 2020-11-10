# 불필요한 객체 생성을 피하라

똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 특히 `불변 객체`는 언제든 재사용할 수 있다. 

<br>

다음 코드는 하지 말아야 할 극단적인 예이니 유의해서 보자. 

```
String s = new String("Gyunny");
```

이 문장은 실행될 때마다 String 인스턴스를 새로 만든다. 매우 비효율적이다. 생성자에 넘겨진 `Gyunny`자체가 이 생성자로 만들어내려는 String과 기능적으로 완전히 똑같다. 

<br>

이 문장이 반복문이나 빈번히 호출되는 메소드 안에 있다면 쓸데없는 String 인스턴스가 수백만 개 만들어질 수도 있다. 
이번에는 개선된 버전을 봐보자. 

```
String s = "Gyunny";
```

이 코드는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다. 나아가 이 방식을 사용한다면 같은 가상 머신 안에서 이와 똑같은 문자열 리터럴을 사용하는
모든 코드가 같은 객체를 재사용함이 보장된다. (`상수풀을 참조`)

<br>

생성자 대신 정적 팩터리 메소드를 제공하는 불변 클래스에서는 정적 팩토리 메소드를 사용해 불필요한 객체 생성을 피할 수 있다. 

```java
public class Test {
    public static void main(String[] args) {
        Boolean true1 = Boolean.valueOf("true");
        Boolean true2 = Boolean.valueOf("true");

        System.out.println(true1 == true2);  // true
    }
}
```

위의 true1과 true1는 같은 객체이다. 

<br>

생성자는 호출할 때마다 새로운 객체를 만들지만, 팩토리 메소드는 그렇지 않다. 불변 객체만이 아니라 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용할 수 있다. 

<br>

## 객체가 불변이라면 재사용해도 안점함이 명백하다. 

하지만 훨씬 덜 명백하거나, 심지어 직관에 반대되는 상황도 있다. `어탭터`를 생각해보자.
`어댑터`는 실제 작업은 뒷단 객체에 위임하고, 자신은 제2의 인터페이스 역할을 해주는 객체다. 
즉, 뒷단 객체 외에는 관리할 상태가 없으므로 뒷단 객체 하나당 어댑터 하나씩만 만들어지면 충분하다. 

<br>

예컨대 `Map` 인터페이스의 `keySet` 메소드는 Map 객체 안의 키 전부를 담은 `Set`뷰를 반환한다. keySet을 호출할 때마다 새로운 Set 인스턴스가 만들어지리라고 생각할 수 도 있지만, 사실은 매번 같은 Set 인스턴스를 반환할지도 모른다. 

<br>

반환된 Set 인스턴스가 일반적으로 가변이더라도 반환된 인스턴스들은 기능적으로 모두 똑같다. 즉, 반환한 객체 중 하나를 수정하면 다른 모든 객체가 따라서 바뀐다. 
모두가 똑같은 Map 인스턴스를 대변하기 때문이다. 따라서 keySet이 뷰 객체를 여러 개 만들어도 상관은 없지만, 그럴 필요도 없고 이점도 없다. 

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Map<String, Integer> menu = new HashMap<>();
        menu.put("Burger", 8);
        menu.put("Pizza", 9);

        Set<String> names1 = menu.keySet();
        Set<String> names2 = menu.keySet();

        names1.remove("Burger");
        System.out.println(names2.size());   // 1
        System.out.println(menu.size());     // 1

    }
}
```

위와 같이 keySet()으로 반환되는 Set은 같은 객체이기 때문에 names1에서 remove 했어도 name2, munu에도 영향을 미치는 것을 알 수 있다. 

<br>

## 불필요한 객체를 만들어내는 또 다른 예 `오토박싱(auto boxing)`

`오토박싱`은 프로그래머가 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술이다. 
`오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.`

```java
public class Test {
    private static long sum() {
        Long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i;
        }
        
        return sum;
    }
}
```

위의 메소드의 코드는 제대로 정답은 나오긴 하지만, 깔끔하게 구현했을 때보다 훨씬 느리다. sum 변수는 long이 아닌 Long으로 선언해서 불필요한 Long 인스턴스가
약 2^31개가 만들어진 것이다. 단순히 sum 타입을 long으로만 바꿔주면 6.3초에서 0.59초로 빨라질 정도이다.  

<br>

교훈은 명확하다. `박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.`

<br>

이번 주제를 `객체 생성은 비싸니 피해야 한다.`로 오해하면 안된다. 특히나 요즘의 JVM에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다. 

<br>

