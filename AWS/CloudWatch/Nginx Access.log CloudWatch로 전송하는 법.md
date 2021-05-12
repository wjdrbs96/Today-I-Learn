# `Nginx Access Log CloudWatch로 전송하는 법`

![스크린샷 2021-05-12 오전 11 47 46](https://user-images.githubusercontent.com/45676906/117910959-df7c0a00-b317-11eb-89ed-2a6f1655e99e.png)

이번 글에서는 위의 그림과 같이 `EC2에 설치된 Nginx Access Log`를 CloudWatch로 전송하는 법에 대해서 정리해보겠습니다.

<br>

## `IAM 역할 생성`

![스크린샷 2021-05-12 오전 9 05 52](https://user-images.githubusercontent.com/45676906/117899107-6b366c00-b301-11eb-9339-59ce82679102.png)

![스크린샷 2021-05-12 오전 9 09 25](https://user-images.githubusercontent.com/45676906/117899314-cd8f6c80-b301-11eb-8d88-63d9df4dfca0.png)

`역할`에다 `CloudWatchAgentServerPolicy` 정책을 추가하겠습니다.

<br>

![스크린샷 2021-05-12 오전 9 10 48](https://user-images.githubusercontent.com/45676906/117899384-fca5de00-b301-11eb-85f2-70ef082dc26a.png)

그리고 원하는 `역할 이름`을 정하고 정책이 잘 선택되었는지 확인 후에 역할을 생성하겠습니다. 

<br>

![스크린샷 2021-05-12 오전 9 13 05](https://user-images.githubusercontent.com/45676906/117899586-63c39280-b302-11eb-85bf-82d15feb1db8.png)

저는 새로 `EC2 Linux2` 버전으로 만들 것이기 때문이 위와 같이 `IAM 역할`을 추가하겠습니다. (만약 기존에 존재하는 EC2를 사용할 것이라면 위에서 만든 역할을 해당 EC2에게 적용해주면 됩니다.)

EC2가 생성이 되었으면 접속을 한 후에 `CloudWatch Agent 설치`를 해보겠습니다. 

<br>

## `Apache 웹 서버 설치`

그 전에 먼저 `LAMP 웹 서버`를 설치하겠습니다. 

```
sudo yum update -y
sudo amazon-linux-extras install -y php7.2
sudo yum install -y httpd  
sudo systemctl start httpd (Apache 웹 서버 시작(d는 daemon 임))
sudo systemctl enable httpd (Apache 웹 서버가 매번 시스템이 부팅할 때마다 시작되도록 함)
sudo systemctl is-enabled httpd (httpd 가 실행되고 있는지 확인하는 명령어)
```

위의 명령어를 통해서 설치하면 `/var/www` 경로가 생겼을 것입니다. 거기로 이동해서 `CloudWatch Agent`를 설치하겠습니다. (EC2 Linux2를 만들면 /var/www가 없기 때문에 Apache 웹서버는 필요 없지만.. 설치는 하겠습니다.)

<br>

## `EC2 인스턴스에 CloudWatch Agent 설치`

```
cd /var/www
sudo wget https://s3.amazonaws.com/amazoncloudwatch-agent/linux/amd64/latest/AmazonCloudWatchAgent.zip
```

![스크린샷 2021-05-12 오전 9 25 38](https://user-images.githubusercontent.com/45676906/117900428-1c3e0600-b304-11eb-98a6-d2a129a108ad.png)

그러면 위와 같이 `zip` 파일 하나가 생깁니다. 스크립트 압축 파일을 해제하겠습니다. 

<br>

```
sudo unzip AmazonCloudWatchAgent.zip -d AmazonCloudWatchAgent
```

![스크린샷 2021-05-12 오전 9 27 32](https://user-images.githubusercontent.com/45676906/117900553-5e674780-b304-11eb-8b7c-dcb90f54532a.png)

그리고 해당 디렉토리로 이동 후에 설치 스크립트를 이용해 에이전트를 설치하겠습니다. 

<br>

```
sudo rm AmazonCloudWatchAgent.zip
cd AmazonCloudWatchAgent
sudo ./install.sh
```

![스크린샷 2021-05-12 오전 9 29 30](https://user-images.githubusercontent.com/45676906/117900675-a4bca680-b304-11eb-98b4-1a19569c3936.png)

<br>

CloudWatch Agents는 손쉽게 설치할 수 있도록 설치 마법사를 제공합니다. 설치 마법사를 실행하겠습니다. 

```
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-config-wizard
```

![스크린샷 2021-05-12 오전 9 32 16](https://user-images.githubusercontent.com/45676906/117900921-33c9be80-b305-11eb-9579-2a90bade2aaf.png)

위의 질문에 모두 `1`을 치고 엔터 누르겠습니다. 그러면 계속 다음 설정에 대한 질문들이 나옵니다. 

<br>

![스크린샷 2021-05-12 오전 9 36 58](https://user-images.githubusercontent.com/45676906/117901156-bd798c00-b305-11eb-891c-e3b5a0a07478.png)

위에서 보이는 것 같이 질문에 대한 답을 하고 엔터를 누르겠습니다. 

<br>

![스크린샷 2021-05-12 오전 9 38 53](https://user-images.githubusercontent.com/45676906/117901544-8657aa80-b306-11eb-93d8-bb9d949f124b.png)

![스크린샷 2021-05-12 오전 9 43 18](https://user-images.githubusercontent.com/45676906/117901616-a8e9c380-b306-11eb-92e1-b1e95ee2cc64.png)

위의 보이는 질문과 답에 맞게 입력하고 엔터를 치고 설정을 하겠습니다. 

<br>

```
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json -s (설치된 CloudAgent 실행)
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a status (올바르게 실행되었는지 확인)
```

![스크린샷 2021-05-12 오전 9 47 43](https://user-images.githubusercontent.com/45676906/117901945-65dc2000-b307-11eb-92c8-3264b4192438.png)

그러면 위와 같이 `running`이 잘 뜨고 있는 것을 확인할 수 있습니다. 

<br>

### `Apache 웹서버 Kill 하기`

```
sudo netstat -tnlp
sudo kill -9 PID (80번 포트의 PID)
ex) sudo kill -9 5142   
```

그리고 이번 실습에서는 Nginx를 사용할 것이기 때문에 위에서 웹 서버를 설치했던 웹서버를 위의 명령어를 통해서 80번 포트를 kill 하겠습니다.

<br>

## `EC2 Linux2 Nginx 설치하기`

```
sudo amazon-linux-extras install -y nginx1 (nginx 설치)
nginx -v (설치확인)
sudo service nginx start (nginx 서비스 시작)
```


<br>

## `AWS CloudWatch 로그 그룹 생성`

![스크린샷 2021-05-12 오전 9 52 13](https://user-images.githubusercontent.com/45676906/117902135-cc613e00-b307-11eb-860b-370756c4c8c3.png)

그리고 EC2 인스턴스에서 `CloudWatch` 에이전트가 로그 파일도 모니터링해서 CloudWatch Logs로 보낼 수 있게 설정 파일을 편집하겠습니다. 

```
sudo vi /opt/aws/amazon-cloudwatch-agent/bin/config.json
```

<img width="775" alt="스크린샷 2021-05-12 오전 11 31 15" src="https://user-images.githubusercontent.com/45676906/117909705-b3f82000-b315-11eb-88cd-89c9bd9bc36f.png">

```js
"logs": {
    "logs_collected": {
        "files": {
            "collect_list": [
                {
                    "file_path": "/var/log/nginx/access.log",  // EC2 Nginx log가 저장되는 위치
                    "log_group_name": "Spring-log",   // CloudWatch 로그 그룹 이름
                    "log_stream_name": "{instance_id}"
                }
            ]
        }
    }
}
```

위와 같이 입력하고 `:wq`를 통해서 저장하겠습니다. 

<br>

### `CloudAgent 재시작`

```
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a stop
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json -s
```

그리고 아래와 같이 EC2 IP로 접속하면 Nginx default 화면이 보일텐데요. 아래와 같이 접속을 하면 `/var/log/nginx/access.log`에 로그가 생기고 그 로그는 CloudWatchAgent를 통해서 CloudWatch 로그 그룹으로 이동할 것입니다. 

<img width="1284" alt="스크린샷 2021-05-12 오전 11 32 50" src="https://user-images.githubusercontent.com/45676906/117909748-c96d4a00-b315-11eb-936e-5b7037d2b6f5.png">

<br>

### `CloudWatch 로그 그룹 확인`

![스크린샷 2021-05-12 오전 11 33 01](https://user-images.githubusercontent.com/45676906/117909803-e43fbe80-b315-11eb-833d-8311fc874f42.png)

![스크린샷 2021-05-12 오전 11 33 06](https://user-images.githubusercontent.com/45676906/117909829-f02b8080-b315-11eb-8044-f9e097271866.png)

그러면 위와 같이 `Nginx` 관련 로그가 `CloudWatch`에도 저장이 되는 것을 확인할 수 있습니다. 