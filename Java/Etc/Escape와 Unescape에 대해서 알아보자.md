## `Escape와 Unescape에 대해서 알아보자`

```java
public class EscapeExample {
    public static final String[] dirties = new String[] {"<", ">", "&"};
    public static final String[] escapes = new String[] {"&lt;", "&gt;", "&amp;"};
    public static String unescape(String clean) {
        return StringUtils.replaceEach(clean, escapes, dirties);
    }
}
```

- `dirties` -> `escapes` 로 바꾸는 것은 escape 처리한다고 한다.
- `escapes` -> `dirties` 로 바꾸는 것은 unescape 처리한다고 한다.