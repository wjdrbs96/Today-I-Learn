## `Thymeleaf common import 하는 법`

```yaml
spring:
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    cache: false
```

`yml`에 위의 설정을 합니다. 

<br>

![스크린샷 2022-02-13 오전 2 17 25](https://user-images.githubusercontent.com/45676906/153721216-238d63ef-59c2-4651-88c6-7b9305e9a435.png)

`Thymeleaf`는 `templates` 아래에 위치합니다. 

<br>

## `common`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<th:block th:fragment="index">
    <!-- Boostrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</th:block>
</html>
```

공통으로 사용되는 로직의 예시는 위와 같습니다. 

<br>

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <style>
        .document-body-custom {
            padding: 30px;
        }
        .document-pagination-custom {
            text-align: center;
        }
    </style>
    <meta charset="UTF-8">
</head>
<body>

<th:block th:replace="common/index :: index"></th:block> <!-- common import 하기 -->

<div class="document-body-custom">
    <table class="table table-bordered">
    </table>
</div>
</body>
</html>
```

위처럼 공통으로 사용되는 로직은 `th:block th:replace`를 사용해서 하면 됩니다.
