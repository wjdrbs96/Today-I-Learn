# `EC2 Linux2 환경변수 설정하기`

- `/etc` 디렉토리 아래에 `environment` 파일을 만들기

```
account_id=12314
```

위와 같이 `Key-Value` 형태의 환경 변수를 설정하였음 

<br>

```shell
ACCOUNT_ID=$(echo $account_id)
docker push ${ACCOUNT_ID}.dkr.ecr.ap-northeast-2.amazonaws.com/yapp:latest
```

그리고 셸 스크립트에서 위와 같이 환경변수를 꺼낼 수 있음 


<br>

# `Reference`

- [https://linuxize.com/post/how-to-set-and-list-environment-variables-in-linux/](https://linuxize.com/post/how-to-set-and-list-environment-variables-in-linux/)