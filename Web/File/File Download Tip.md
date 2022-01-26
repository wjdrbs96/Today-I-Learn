## `File Download Tip`

웹 페이지에서 파일을 다운로드할 때 `html a 태그의 download 속성`을 이용하면 쉽게 다운로드할 수 있습니다. 

```html
<a href="파일서버URL" download="test.png"></a>
```

만약 위처럼 `a 태그`가 존재하는데, 웹 사이트의 `Origin`과 `파일 서버 Origin`이 다르다면 `download`에 지정한 이름대로 파일 다운로드가 진행되지 않습니다. 

<br> <br>

## `HTTP Header Content Disposition`

`HTTP Header` 속성에 보면 `Content-Disposition` 이라는 것이 존재합니다. `Content-Disposition`는 `Response Header`에 넣어주면 됩니다. 

`Response Header`에 `Content-Disposition` 속성이 존재한다면 이 속성에 존재하는 이름으로 파일 다운로드를 시도합니다. 

```
HttpHeader: Content-Disposition
HeaderValue: attachment; fileName = test.jpg
```

위와 같이 지정해주면 됩니다. 

<br> <br>

## `Content-Length`

파일 다운로드, Zip 파일 다운로드 할 때는 `Response Header에 존재하는 Content-Length` 값이 중요합니다. 이 값을 알아야 파일의 크기를 알 수 있고, 웹 브라우저에서 파일 다운로드 시간을 측정할 수 있기 때문입니다. 

<br> <br>

## `CRC32`

