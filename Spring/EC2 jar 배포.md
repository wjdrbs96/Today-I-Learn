# `EC2에서 jar 배포하기`

```
1. nohup java -jar 파일이름.jar &
ex) nohup java -jar demo-0.0.1-SNAPSHOT.jar &
```

<br>

## `jar 파일 재배포`

```
sudo netstat -tnlp
sudo kill -9 pid
ex) sudo kill -9 15212
sudo nohup java -jar server.jar &
```

![스크린샷 2021-02-06 오후 9 26 59](https://user-images.githubusercontent.com/45676906/107118111-4bd31180-68c2-11eb-8800-aa6cf2a6d322.png)

<br>

## `Spring log 확인`

NodeJS pm2 처럼 API 호출 로그가 남는 것이 아니라 코드 상에서 logger를 이용해서 로그를 남긴 것이 출력 됨

![스크린샷 2021-02-06 오후 9 30 19](https://user-images.githubusercontent.com/45676906/107118158-8d63bc80-68c2-11eb-8035-b40c5336df2d.png)

로그는 `nohup.out` 파일에 저장이 됩니다.

```
tail -f nohup.out (실시간 로그 확인 가능)
```
