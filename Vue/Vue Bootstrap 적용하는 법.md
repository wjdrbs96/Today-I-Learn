## `Vue Bootstrap 적용하는 법`

```
npm install vue bootstrap-vue bootstrap
```

위의 명령어로 `Bootstrap` 관련된 것들을 설치한다.

<br>

```
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
```

사용하고자 하는 js 파일에 위의 내용을 추가해주면 된다. (ex: main.js)