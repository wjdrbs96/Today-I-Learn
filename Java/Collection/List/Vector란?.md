# `Vector 클래스란?`

```java
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {}
```

Vector 클래스를 보면 `List` 인터페이스를 구현하고 있는 것을 볼 수 있습니다. 그래서 `저장 순서를 유지`하고, `중복을 허용`한다는 특징을 가지고 있습니다. 

