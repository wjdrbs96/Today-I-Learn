# `Properties 클래스란?`

```java
public class Properties extends Hashtable<Object,Object> {}
```

Properties 클래스는 구버전인 Hashtable을 확장하고 있습니다. Hashtable은 키와 값을 <Object, Object> 형태로 저장하는 반면에 Properties는 <String, String>의 형태로 저장합니다. 

`주로 애플리케이션의 환경설정과 관련된 속성(Property)을 저정하는데 사용되면 데이터를 파일로부터 읽고 쓰는 편리한 기능을 제공합니다.`

<br>

## `예제 코드`

```java
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesEx {
    public static void main(String[] args) {
        Properties prop = new Properties();

        prop.setProperty("timeout", "30");
        prop.setProperty("language", "kr");
        prop.setProperty("size", "10");
        prop.setProperty("capacity", "10");

        Enumeration<?> enumeration = prop.propertyNames();

        while (enumeration.hasMoreElements()) {
            String element = (String)enumeration.nextElement();
            System.out.println(element + " = " + prop.getProperty(element));
        }
    }
}
```

`setProperty()`, `getProperty()`를 통해서 key-value 형태로 저장하고 값을 꺼내오는 것을 할 수 있습니다.

그리고 Properties는 컬렉션 프레임웍이 생기기 전에 만들어진 구버전이라 Iterator가 아닌 Enumeration을 사용합니다. 

<br>

## `시스템 속성 가져오기`

```java
import java.util.Properties;

public class PropertiesEx {
    public static void main(String[] args) {
        Properties prop = System.getProperties();
        System.out.println("java version : " + prop.getProperty("java.version"));
        System.out.println("user language : " + prop.getProperty("user.language"));
        prop.list(System.out);
    }
}
```

위와 같이 `System` 클래스의 static 메소드를 활용하여 Properties 객체를 얻을 수 있습니다. 그리고 위와 같이 `시스템 속성`의 정보들을 출력할 수 있습니다. 

출력할 때 Properties의 `list` 메소드를 사용하면 쉽게 출력할 수 있습니다.
