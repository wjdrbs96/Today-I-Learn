# AWS EC2에서 pm2 사용법 정리

- AWS EC2에 접속하는 것은 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/AWS/EC2.md) 에서 확인하자.

<br>

## `pm2란?`

- 서버를 24시간 동안 운영해야 하는데 컴퓨터를 계속 켜놓을 수 없기 때문에 백그라운드로 실행을 시켜놓는 것
- 로그 수집 & 에러 발생시 자동으로 RESTART
- 여러 노드 프로젝트를 한 번에 돌릴 수 있음

<br>

## `pm2 설치`

```
npm install pm2 -g
pm2 version (버전 확인)
```

<br>

## `pm2 start `

```
pm2 start ./bin/www (프로젝트의 최상위 폴더에서)
```

<img width="645" alt="스크린샷 2020-11-17 오전 1 07 45" src="https://user-images.githubusercontent.com/45676906/99277710-78523100-2871-11eb-9edb-158088ab4b52.png">

- 위와 같이 `프로젝트 최상위 폴더`로 위치로 가서 `pm2 start ./bin/www`를 하면 된다.

<br>

## `pm2 list`

```
pm2 list (pm2 리스트 보기)
```

<img width="643" alt="스크린샷 2020-11-17 오전 1 10 27" src="https://user-images.githubusercontent.com/45676906/99277853-af284700-2871-11eb-95cf-0b514a41163d.png">

<br>

## `pm2 log`

```
pm2 log (Server log 보는 법)
```

<img width="650" alt="스크린샷 2020-11-17 오전 1 12 28" src="https://user-images.githubusercontent.com/45676906/99278089-f8789680-2871-11eb-98c6-265c627dbb94.png">

<br>

## `pm2 delete`

```
pm2 delete {name or id}
ex) pm2 delete 0
```

<img width="644" alt="스크린샷 2020-11-17 오전 1 14 00" src="https://user-images.githubusercontent.com/45676906/99278361-48eff400-2872-11eb-866f-b171f98e4d75.png">

- pm2 list를 통해 `name`과 `id`를 확인할 수 있다.

<br>

## `pm2 stop`

```
pm2 stop {name or id}
ex) pm2 stop 0
```

<img width="644" alt="스크린샷 2020-11-17 오전 1 14 00" src="https://user-images.githubusercontent.com/45676906/99278361-48eff400-2872-11eb-866f-b171f98e4d75.png">

- pm2 list를 통해 `name`과 `id`를 확인할 수 있다.

<br>

## `pm2 로그 저장소`

```
cd ~/.pm2/logs
``` 

<img width="655" alt="스크린샷 2020-11-17 오전 1 17 22" src="https://user-images.githubusercontent.com/45676906/99278637-a7b56d80-2872-11eb-83b0-e9314d7d9774.png">

- 로그가 저장되어 있는 곳이다. 