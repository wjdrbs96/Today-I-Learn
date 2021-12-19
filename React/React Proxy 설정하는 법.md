## `React Proxy 설정하는 법`

```
yarn add http-proxy-middleware
```

먼저 위의 모듈을 설치해주어야 한다. 

<br>

```js
const onSubmit = async (event) => {
    event.preventDefault();
    
    if (password !== confirmPassword) {
      return alert('비밀번호와 비밀번호 확인이 다릅니다.')
    }

    const result = await axios.post('/api/v1/user/signup', {
      loginId: loginId,
      password: password,
      nickname: nickname
    }, {
      Headers: { "Content-Type": `application/json` }
    })
  
    localStorage.setItem("accessToken", JSON.stringify(result.data));  
}
```

위처럼 `axios`를 통해서 서버 통신할 때 프록시 설정이 필요했다. `React는 3000 포트`, `Spring은 8080` 이다. 

<br> <br>

## `Proxy 설정하기`

`/src/setProxy.js` 파일을 만들자. 

```js
const proxy = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    proxy('/api/v1', {
      target: 'http://localhost:8080/',
      changeOrigin: true
    })
  );
};
```

그리고 위와 같이 설정하면 된다. `/api/v1`으로 시작하면 `{{base_url}}`을 `target`에 설정한 것으로 바꿔주는 역할을 하는 것이 프록시이다. `CORS` 처리를 할 때 유용하다.