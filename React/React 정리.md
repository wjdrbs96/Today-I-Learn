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

