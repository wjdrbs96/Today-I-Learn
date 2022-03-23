## `JavaScript Ajax, fetch, axios 사용법 간단하게 정리하기`

## `fetch`

```js
const save = (requestDTO) => {
    return fetch('/post', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(requestDTO)
    })
}
```

`POST` 요청이라면 위와 같이 `Request Body`에 데이터를 담아서 요청을 보낼 수 있다.

<br>

```js
const getPost = () => {
    return fetch('/post', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json'
        },
    }).then(response => {
        return response.json();
    }).then(data => {
        return data;
    })
}
```

응답 데이터가 `json()`으로 날라온다면 위와 같이 `첫 번째 then()`에서 `json`을 꺼낸 후에 `두 번째 then()`에서 데이터를 반환해야 한다.

<br>

```js
const getPost = () => {
    return fetch('/post', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json'
        },
    }).then(response => {
        return response.text();
    }).then(data => {
        return data;
    })
}
```

만약에 응답 데이터가 `JSON()`이 아니라 `text()` 형태로 온다면 `text()`를 사용해서 데이터를 꺼내야 한다.

<br>

## `axios`

```js
const axiosTest = () => {
    let value = arrayQueue.shift();
    const fileChunkFromStart = file.slice(value[0], value[1]);
    return axios({
        method: 'post',
        url: '/test',
        headers: {
            'Content-type': "application/json",
        },
        data: fileChunkFromStart,
        onUploadProgress: function (progressEvent) {
            const uploadPercentage =  progressEvent.loaded / progressEvent.total * 100;
        },
    });
}
```

위처럼 axios를 사용할 수 있고, 프로그래스바도 사용할 수 있다.

<br>

