# `Nginx 기본 명령어 정리`

현재 `EC2 Linux2 환경에서 진행하고 있습니다.`

<br>

## `Nginx 설치하기`

```
sudo yum install nginx
```

<br>

## `Nginx 명령어 정리`

```
sudo service nginx start     // 시작
sudo service nginx stop      // 중지
sudo service nginx restart   // Nginx 서비스를 중지했다가 시작합니다.
sudo service nginx reload    // Nginx 서비스를 정상적으로 다시 시작합니다. 다시로드 할 때 기본 Nginx 프로세스는 자식 프로세스를 종료하고 새 구성을로드하며 새 자식 프로세스를 시작합니다.
sudo service nginx status    // 상태 확인
```
