# `Amazon Linux 2에 LAMP 웹 서버 설치`

EC2 linux2 버전에서 웹 서버를 설치하는 명령어를 정리해보겠습니다. (아래의 명령어는 EC2에 접속한 후에 설치를 해야 합니다.)

```
sudo yum update -y
sudo amazon-linux-extras install -y php7.2
sudo yum install -y httpd  (Apache 웹 서버 시작(d는 daemon 임))
sudo systemctl start httpd
sudo systemctl enable httpd (Apache 웹 서버가 매번 시스템이 부팅할 때마다 시작되도록 함)
sudo systemctl is-enabled httpd (httpd 가 실행되고 있는지 확인하는 명령어)
```

그리고 본인의 EC2 IP 주소로 접속하면 아래와 같이 웹 서버가 잘 설치된 것을 볼 수 있습니다. 

![스크린샷 2021-03-22 오후 2 59 14](https://user-images.githubusercontent.com/45676906/111946647-34bd4b80-8b1f-11eb-9aa5-62d74e955208.png)


<br>

# `Reference`

- [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ec2-lamp-amazon-linux-2.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ec2-lamp-amazon-linux-2.html)
