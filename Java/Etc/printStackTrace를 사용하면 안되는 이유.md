# `printStackTrace()를 사용하면 안되는 이유는?`

```java
try {
    // some code
} catch (Exception e) {
    e.printStackTrace();
}
```

우리는 위와 같이 `printStackTrace` 메소드를 자주 사용해왔을 것입니다. 그런데 `printStackTrace()`는 사용을 지양해야 하는 메소드입니다. 그 이유에 대해서 간단하게 알아보겠습니다. 

![스크린샷 2021-03-28 오전 12 54 25](https://user-images.githubusercontent.com/45676906/112726337-355f4300-8f60-11eb-9162-98002cd1333f.png)

내부 메소드를 보면 `System.err`를 사용하고 있습니다. [System.out.println()을 사용하면 안되는 이유](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Etc/System.out.println%20%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%A7%80%20%EC%95%8A%EC%95%84%EC%95%BC%20%ED%95%98%EB%8A%94%20%EC%9D%B4%EC%9C%A0.md) 에서 간단하게 정리를 한 적이 있는데요.
`System.err`보다는 `logger`를 이용하는 것을 지향합니다. 그 이유는 위의 글을 읽고오면 됩니다. 즉, 내부적으로 `System.err`를 사용하고 있기 때문에 지양해야 하는 메소드입니다. 

```java
try {
    // some code
} catch (Exception e) {
    logger.log("Context message", e);
}
```

따라서 위와 같이 사용하는 것이 좋습니다. 

<br>

## `또 다른 단점`

- printStackTrace를 사용하면 예외 발생 위치가 정확하게 드러나기 때문에 보안에 위협이 될 수 있습니다.

<br>

# `Reference`

- [https://stackoverflow.com/questions/7469316/why-is-exception-printstacktrace-considered-bad-practice/7469490](https://stackoverflow.com/questions/7469316/why-is-exception-printstacktrace-considered-bad-practice/7469490)