## `Vue Components`

```vue
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
</head>
<body>
  <div id="app">
    <app-header></app-header>
    <app-footer></app-footer>
  </div>

  <div id="app2">
    <app-header></app-header>
    <app-footer></app-footer>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
  <script>
    // 전역 컴포넌트
    // Vue.component('컴포넌트 이름', 컴포넌트 내용);
    Vue.component('app-header', {
      template: '<h1>Header</h1>'
    });  

    new Vue({
      el: '#app',
      // 지역 컴포넌트 등록 방식
      components: {
        // '컴포넌트 이름': 컴포넌트 내용,
        'app-footer': {
          template: '<footer>footer</footer>'
        }
      },
    });

    new Vue({
      el: '#app2',
      components: {
        'app-footer': {
          template: '<footer>footer</footer>'
        }
      }
    })
  </script>
</body>
</html>
```

- 전역 컴포넌트: `Vue.component` 말 그대로 전역으로 사용해야 하는 경우 
- 지역 컴포넌트: 일반적으로는 지역 컴포넌트를 사용함