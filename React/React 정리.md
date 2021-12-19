## React 정리

아이콘 : material Icon Theme

### components

- User, Board  도메인 별로

<br>

### pages

- 화면

<br>

### Routing => www.naver.com/login 처럼 가능하도록

패키지 설치 : yarn add styled-components => dependencies 에 들어감

ex) `yarn add -D eslint` 개발용으로 Dev dependencies 에 들어감!

```
yarn add react-router-dom
```

```
yarn add styled-components
```

<br>

## State, Props

- State : 변하는 값
  - input 태그 안에 넣음 => 트랙킹 해서 서버에게 보내 줄 때도 사용
  - onChange 변경되었을 때를 감지

```js
const [id, setId] = userState('');
const [password, setPassword] = userState('')

const [vallue, setValue] = userState({});
```

- Props: 


- 단방향 데이터 바인딩 : 부모 -> 자식 컴포넌트만 데이터 전달 가능함 (자식 -> 부모 전달 못함)

<br>

## `axios`

```
yarn add axios
import axios from 'axios'
```

```js
import axios from "axios";

function App() {
  useEffect(() => {
    (async () => {
      const { data } = await axios.get('https://api.publicapis.org/entries');
      console.log(data);
    })()
  }, [])

  return (
    <Container>
      <Login />
    </Container>
  );
}

export default App;
```

- 리액트 생명주기 공부하기 (userState, userEffect 공부하기)

<br>

```js
useEffect(() => {
    console.log("Test");
}, ([id])
```

- 렌더링 한 후에 userEffect 실행 됨

<br>

## `로컬 스토리지`

- localStorage.setItem("token", JSON.stringify(token))

- JSON.parse(localStorage.getItem("token"))

- 브라우저에서 유지됨

<br>

## `Recoil 전역 상태 관리`

```
yarn add recoil
```

- 다크모드, 밝은 모드 => 모든 컴포넌트에서 관리되는 것들 저장하면 좋음

<br>

## `Material UI Install`

```
yarn add @material-ui/core 
```

<Br>

## `Proxy 설정을 위한 패키지 설치`

```
yarn add http-proxy-middleware
```

<br>

## `Prettier 설치하는 법`

```
yarn add -D prettier  
```

<br>

## `hooks 사용하기`

재사용 되는 로직들 `hooks` 패키지에 넣어서 관리한다.

- `useXXX` 라는 이름으로 만든다.

<br>

## `Axios API 호출하는 코드 모듈로 분리하기`

```js
import axios from 'axios';

const accessToken = JSON.parse(localStorage.getItem('accessToken'));

const client = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,
  headers: { 
      "Content-Type": `application/json`,
      "Bearer " : accessToken
  }
});

export default client;
```

위처럼 따로 모듈로 빼놓으면 공통 코드를 처리할 수 있다.

<br>

## `페이지 이동 하는 법`

예를 들어 `A 페이지`에서 `B 페이지`로 이동하고 싶을 때 (ex: 로그인 후에 메인 페이지로 이동) 사용하는 것

```
import { useNavigate } from "react-router-dom";

const navigate = useNavigate();
navigate('/postlist');
```

`/postlist`로 이동한다.