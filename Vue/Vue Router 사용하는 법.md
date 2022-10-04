## `Vue Router 사용하는 법`

```
npm install vue-router@3 --save
```

npm 버전 7 이상 부터는 `vue-router` 설치할 때 에러가 발생해서 위의 명령어로 했음

<br>

```js
import Vue from 'vue'
import VueRouter from 'vue-router'
import PostList from '../components/PostList.vue'

Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/posts',
            component: PostList
        },
    ]
})

export default router
```

- path: URI 경로
- component: URI를 입력했을 때 이동할 컴포넌트

<br>

```
<router-link to="/login">Login</router-link>
<router-link to="/main">Main</router-link>
```

위처럼 `a tag`와 같은 기능을 하도록 사용할 수 있습니다.