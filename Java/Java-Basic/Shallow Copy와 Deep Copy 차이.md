## `Shallow Copy와 Deep Copy 차이는?`

```java
List<String> list = new ArrayList<>();

...

List<String> temp = list; // shallow copy
```

- `Shallow Copy`

<br>

```java
CopyObject original = new CopyObject("Test", 20);
CopyObject copyConstructor = new CopyObject(original);
CopyObject copyFactory = CopyObject.copy(original);
```

- Deep Copy

추가 정리 예정