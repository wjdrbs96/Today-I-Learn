# `Thymeleaf PathVariable, Query String 사용 법`

## PathVariable: 

```thymeleafiterateexpressions
<a th:href="@{/user/{userId}/test(userId = ${data.userId})}">
```

<br>

## QueryString 

```thymeleafiterateexpressions
<a th th:href="@{/user/details(userId=${data.userId})}"></a>
```