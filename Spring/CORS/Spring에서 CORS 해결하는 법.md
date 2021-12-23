# `Spring에서 CORS 해결하는 법`

이번 글에서는 개인적인 프로젝트를 하면서 겪었던 SOP 문제를 `CORS`를 허용해주면서 해결했던 과정에 대해서 공유해보려 합니다. (이 글에서는 `CORS`가 무엇인지에 대해서는 자세히 다루지 않겠습니다.)

참고로 프로젝트에서 백엔드는 `Spring Boot`, 프론트엔드는 `React`를 사용하였습니다.

<br>

## `CORS란 무엇일까?`

`CORS`가 무엇인지 간단하게 알아보겠습니다. `CORS(Cross-Origin Resource Sharing)`는 `교차 출처 리소스 공유`라고 합니다. 여기서 `교차 출처`라고 하는 것은 `다른 출처`를 의미하는 것입니다. 즉, 브라우저에서 막고 있기 때문에 `CORS`를 허용해주어야 접근이 가능합니다.

<br> <br>

### `출처(Origin)은 무엇일까?`


![image](https://user-images.githubusercontent.com/45676906/128661369-002c475d-1d1f-4a60-89d3-c4b1f3e6fab3.png)

위의 보이는 것처럼 도메인에서 `Protocol` + `Host` + `Port`가 같으면 `동일한 출처`라고 얘기를 합니다. 즉, `3개 중에 하나라도 다르면 다른 출처`라고 할 수 있습니다.

| URL | 결과 | 이유 |
|-----------|-------|------|
| https://gyunny.io/test | 같은 출처 | Protocol, Host, Port 동일 |
| https://gyunny.io/test?q=work | 같은 출처 | Protocol, Host, Port 동일 |
| http://gyunny.io/test | 다른 출처 | Protocol 다름 |
| http://gyunny.com/test | 다른 출처 | Host 다름 |

<br> <br>

## `React에서 Spring을 호출한다면?`

- `React`: `http://localhost:3000`
- `Spring`: `http://localhost:8080`

<br>

`React`, `Spring`은 각각 로컬에서 실행하면 `3000`, `8080` 포트로 실행하게 됩니다. 그러면 `React`에서 `Spring API`를 호출하면 어떻게 될까요? 위에서 보았듯이 두 도메인은 `Port`가 다르기 때문에 `SOP 문제`가 발생할 것이라 예측할 수 있습니다.

<br> 

## `React에서 Spring API 호출해보기`

<img width="688" alt="스크린샷 2021-12-23 오전 11 46 45" src="https://user-images.githubusercontent.com/45676906/147180255-116fb4fd-5740-402c-a051-f2bdc29cdcda.png">

대략적인 그림을 그리면 위와 같이 `SOP`문제가 발생할 것입니다. 정말 `SOP` 문제가 발생하는지 테스트 해보면서 알아보겠습니다.

```javascript
const PostList = () => {
    useEffect(() => {
        (async () => {
            try {
                const { data } = await axios.get('http://localhost:8080/api/v1/post');
                setDataList(data.data);
            } catch (error) {
                alert(error);
            }
        })();
    }, []);
}
```

간단하게 `React`에서 `Spring API`를 호출하는 코드입니다. 이 상태로 브라우저를 실행해서 확인해보겠습니다.

<img width="558" alt="스크린샷 2021-12-23 오전 2 20 41" src="https://user-images.githubusercontent.com/45676906/147131173-d44967c4-33f3-4eba-9002-3f9f1ce41be6.png">

<br>

<img width="390" alt="스크린샷 2021-12-23 오전 2 20 52" src="https://user-images.githubusercontent.com/45676906/147131232-c205a805-61f7-4baf-9d8b-0372a6774129.png">

그러면 위와 같이 예상했던 것처럼 `SOP` 문제가 발생하는 것을 볼 수 있습니다. `SOP`를 해결하기 위해서는 `Spring Server`에서 `출처가 다른 React 자원`이 `Spring Server 자원`에 접근할 수 있도록 `권한`을 주는 작업이 필요합니다.(즉, `CORS` 작업)

`Spring에서 CORS를 해결하는 방법은 대표적으로 3가지`가 있지만 저는 그 중의 `WebMvcConfigurer` 인터페이스를 이용해서 해결하는 방법을 적용해보겠습니다.

<br>

## `WebMvcConfigurer addCorsMapiings 구현하기`

```java
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserIdArgumentResolver loginUserIdArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
```

`WebMvcConfigurer` 인터페이스가 가지고 있는 `addCorsMappings` 메소드를 오버라이딩 한 후에 위와 같이 `http://localhost:3000`에 대해서 접근할 수 있는 권한을 주면 됩니다. 위와 같이 설정만 해주면 너무나도 쉽게 `SOP`를 바로 해결할 수 있습니다.

하지만.. 위와 같이 설정을 해주었어도 `SOP` 문제가 해결되지 않았습니다. 이 때 `왜 SOP` 문제가 사라지지 않는거지? 서버에서는 분명 접근 권한을 주었는데... 라는 생각이 머리속을 지배하면서 왜 안되는지 원인을 찾기가 쉽지 않았습니다.

결국 많은 삽질과 `CORS` 동작 원리를 다시 보면서 `CORS` 이슈가 사라지지 않는 원인을 찾았는데요. 원인은 바로 제가 `Spring Security`를 사용하고 있기 때문이었습니다.

![Spring Security](https://user-images.githubusercontent.com/45676906/147133435-04ce30cc-e68f-45bc-a396-610076b1b84f.png)

시큐리티의 간단한 그림을 보면 위와 같은 구조로 되어 있습니다. 즉, `Interceptor`, `Controller` 영역에 들어오기 전에 `Filter` 영역을 먼저 거치게 된다는 것을 알 수 있습니다. 즉, `Filter`에서 걸리기 때문에 `CORS`가 해결되지 않는 것인데요. `왜 Filter에서 걸려서 CORS가 해결 되지 않았는지를 이해하려면 Preflight Request`에 대해서 알아야 합니다.

<br> <br>

## `Preflight Request`

<img width="626" alt="스크린샷 2021-12-23 오전 2 53 33" src="https://user-images.githubusercontent.com/45676906/147174009-90c0c425-219c-4391-ba08-a544497aa390.png">

`Preflight Request`는 먼저 `OPTIONS` 메소드를 통해 다른 도메인의 리소스로 `HTTP 요청`을 보내 실제 요청이 전송하기에 안전한지 확인합니다. 즉, `Preflight Request` 요청의 응답이 `200`으로 떨어져야 다음 `본 요청`을 진행할 수 있습니다.

그런데 `Spring Security Filter`에서 `Preflight Request`에 대한 응답을 `401`로 내려주기 때문에 `CORS` 문제가 해결되지 않았던 것입니다.

<br>

![스크린샷 2021-12-23 오전 3 03 09](https://user-images.githubusercontent.com/45676906/147136108-4280b672-f895-4437-8f85-3c4321ff6287.png)

```
`Response to preflight request doesn't pass access control check: 
No Access-Contrlol-Allow-Origin header is present is one the requested resource`
```

이번에도 다시 한번 요청을 보내보고 로그를 확인해보면 위와 같이 `Preflight Request`에서 문제가 생긴 것을 확인할 수 있습니다.

<br>

```java
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight Request 허용해주기
            .antMatchers("/api/v1/**").hasAnyAuthority(USER.name());
    }
}
```

`Preflight Request`가 401로 응답오는 문제를 해결하려면 `Security Config` 설정에서 하나 추가해주어야 하는데요. 위의 코드는 `Security Config` 파일의 일부분인데, 여기서 `mvcMatchers`를 사용해서 `Preflight Request OPTIONS 메소드` 요청을 허용해주면 됩니다.(현재는 위와 같이 해결했지만 추후에는 `CORS Filter`를 적용해볼 생각입니다.)

<br>

<img width="555" alt="스크린샷 2021-12-23 오전 3 13 22" src="https://user-images.githubusercontent.com/45676906/147137088-261bc222-ce8f-4097-90cd-1ffee4d9baed.png">

`Security Config`에 `OPTIONS` 메소드가 오는 경우에 허용을 해주었더니 `Preflight Request`가 `401`이 아닌 `200`으로 응답 오는 것을 확인할 수 있습니다.

<br>

<img width="558" alt="스크린샷 2021-12-23 오전 3 12 59" src="https://user-images.githubusercontent.com/45676906/147137096-ec05d8d1-d36d-444f-b5c9-2986deb133f7.png">

그리고 `Preflight Request` 이후에 본 요청도 정상적으로 응답이 오고 잘 작동하는 것까지 확인할 수 있습니다.

<br>

## `React Proxy를 사용해서 SOP 해결하기`

위에서 `WebMvcConfigurer` 인터페이스가 가지고 있는 `addCorsMappings` 메소드에서 `http://localhost:3000`에 대한 `CORS` 접근 허용 설정을 해주면 `SOP` 문제가 해결된다고 하였는데요. 이 부분은 `Spring Server`에서 할 수 있는 것이고 `React`에서도 `SOP`를 해결하기 위해 `Proxy` 라는 방법이 존재합니다.

바로 `Proxy`를 알아보기 전에 간단하게 게시글 하나를 불러오는 `JavaScript` 코드를 먼저 보겠습니다.

```javascript
export default function PostView() {
    useEffect(() => {
        (async () => {
            try {
                const { data } = await axios.get(
                    `/api/v1/post/${postId}`, {
                        headers: { 'Authorization': `Bearer ${result}` },
                    }
                );
                setData(data.data);
            } catch (error) {
                alert(error);
            }
        })();
    }, []);
}
```

코드에서 `axios`로 서버 `API`를 호출하는 곳을 보면 `절대 주소`가 아닌 `상대 주소`로 적혀있는 것을 볼 수 있습니다. 이 상태로 `서버 API`를 호출하면 어떻게 될까요?

![스크린샷 2021-12-23 오전 11 34 41](https://user-images.githubusercontent.com/45676906/147179200-d9799c3d-9cb2-4d69-885d-417e9360dd18.png)

상대 주소로 호출해도 자동으로 `http://localhost:3000`이 앞에 붙는 것을 볼 수 있고 당연하게도 `http://localhost:3000/api/v1/post/1` 은 존재하지 않기 때문에 `404 Not Found`가 응답으로 오는 것을 볼 수 있습니다.

이러한 문제를 해결하기 위해 필요한 것이 바로 `Proxy` 입니다.

<br>

## `React에서 Proxy 적용하기`

<img width="702" alt="스크린샷 2021-12-23 오전 10 46 39" src="https://user-images.githubusercontent.com/45676906/147179331-1995caf1-1ce8-47b3-95e1-70fcb8757d40.png">

브라우저에서 `React Dev Server`를 호출하고 `React Dev Server`에서 `Proxy`를 통해서 `http://localhost:8080 -> http://localhost:3000`로 대체하여 `Spring Server`를 호출하게 됩니다. 즉, `Proxy`를 통해서 `{{Base_url}}`을 대체할 수 있기에 `다른 출처`로 인식하지 않고  `같은 출처`로 인식하여 `CORS` 문제가 발생하지 않는 것입니다.

그래서 `React에서 Proxy`를 설정하는 법에 대해서 알아보겠습니다.

```
yarn add http-proxy-middleware
```

먼저 `http-proxy-middleware` 모듈을 설치하겠습니다.

<br>

```javascript
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    createProxyMiddleware('/api/v1', {
      target: 'http://localhost:8080',
      changeOrigin: true,
    })
  );
};
```

그리고 `src` 폴더 아래에 `setProxy.js` 라는 파일을 만든 후에 위와 같이 코드를 작성하면 `Proxy` 설정이 끝입니다.

그런데 문제는 위와 같이 설정해도 `Proxy`가 적용되지 않는 문제가 있었습니다. 이번에도 왜 안될까.. 하면서 좀 찾아보니 [여기](https://stackoverflow.com/questions/48291950/proxy-not-working-for-react-and-node) 에서 원인을 찾을 수 있었는데요. 추측되는 원인은 `yarn.lock`, `node_modules`에서 `Cache` 하고 있을 수 있어서 지웠다 다시 설치해보라는 내용이었습니다.

```
rm -rf yarn.lock node_modules
yarn install
```

그래서 위와 같이 다시 설치한 후에 실행하니 정상적으로 `Proxy`가 동작하였습니다.

![스크린샷 2021-12-23 오전 11 58 23](https://user-images.githubusercontent.com/45676906/147181389-3e2da4e9-ea44-4095-87eb-03b45e32a919.png)

브라우저의 `Network` 탭을 열어보면 이번에도 `http://localhost:3000`으로 요청을 보냐고 있지만 `404 Not Found`가 발생하지 않고 정상적으로 응답이 오는 것을 볼 수 있습니다. 즉, `Proxy`가 제대로 동작했기에 `http://localhost:3000`으로 요청한 것처럼 보이지만 실제로는 `http://localhost:8080`으로 요청을 보낸 것입니다.

<br>

## `글을 마무리 하며`

당연한 말이지만, 코드나 컴퓨터는 거짓말 하지 않기 때문에.. 문제가 발생하면 분명히 제가 무엇가 잘못해서 그런 것이다를 한번 더 깨닫게 된 것 같습니다. 처음에는 `CORS`는 `WebMvcConfigurer` 인터페이스의 `addCorsMappings` 메소드만 오버라이딩 한 후에 설정해주면 쉽게 해결될 것이라는 생각이 저를 우물 안으로 가두었던 것 같습니다.

지금까지 매번 `Android or iOS`와 협업을 해보다 보니 `CORS`를 만날 일이 없었는데 이번 기회에 이론을 넘어서 직접 경험해보면서 해결해보니까 좀 더 뿌듯한 것 같습니다.