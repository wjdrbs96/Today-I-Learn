## `intellij http 파일`

```http request
### HTTP Request Example 
POST /v1/gyunny
Host: {{host}}
Content-Type: application/json
Authorization: Bearer {{Authorization}}

{
  "part": "서버",
  "name": "이름,
}
```

- `HTTTP Method URI`를 적으면 된다. (`ex: POST /v1/gyunny`)
- 밑에 들어가는 내용은 하나씩 HTTP Header Key-Value가 들어가게 된다.
- POST 라서 Request Body가 필요하다면 위처럼 `{}`에 Key-Value를 넣어주면 사용할 수 있다.