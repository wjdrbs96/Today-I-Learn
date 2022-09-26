## `axios 설치 및 사용법`

```
npm install axios
```

```vue
<template>
  <div>
    <h1>Hi</h1>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "axios",
  components: {
  },
  data: () => ({
  }),

  async mounted() {
  },

  methods: {
    async test(page) {
      try {
        const response = await axios.get(`/api/v1/test`, {
          params: {
            page: page - 1,
            size: this.page.size
          }
        });

        // PathVariable 사용 법
        // const response = await axios.get(`/api/v1/test/${testId}`)

        // Request Body 데이터 전송하는 법
        // const response = await axios.post('/api/v1/test', {
        //   "testNo": 1,
        //   "testName": "이발"
        // })

        if (response.status === 200) {
          const { contents, totalPages } = response.data.result;
        }
      } catch (error) {
        // 예외 처리
      }
    },
  }
}
</script>
<style scoped>
</style>
```

- params: 속성을 사용하면 `Query String`을 사용할 수 있습니다.