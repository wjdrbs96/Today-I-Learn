## `ThreadLocal Tip 정리`

```java
public class UserThreadLocal {
    
    private static final ThreadLocal<String> threadLocal;
    
    static {
        threadLocal = new ThreadLocal<>();
    }
    
    public static String get() {
        return threadLocal.get();
    }
    
    public static void set(String userId) {
        threadLocal.set(userId);
    } 
    
    public static void remove() {
        threadLocal.remove();
    }
}
```

위와 같이 `ThreadLocal`을 사용할 수 있다. 

<br>

## `Interceptor ThreadLocal`

```java
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserThreadLocal.set(); // set 하기
    }
    
    @Override
    public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        UserThreadLocal.remove(); // 사용했던 ThreadLocal 제거해주기
    }
}
```

인터셉터를 사용하면서 `ThreadLocal` 팁 정리