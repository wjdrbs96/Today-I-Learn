## EC2 (Elastic Compute Cloud) 접속하는 법

```
1. AWS에서 발급 받은 pem 파일이 있는 위치로 이동한다. 

2. chmod 400 파일명.pem

3. sudo ssh -i '파일명.pem' ubuntu@아이피주소
ex) sudo ssh -i 'AWS.pem' ubuntu@52.79.90.119
```

![title](https://user-images.githubusercontent.com/45676906/93416524-ecf40800-f8e0-11ea-8764-eba8cbaaafd2.png)

그러면 위와 같이 `EC2`에 접속할 수 있다. 

<br>

## EC2 접속 커스텀 하기 

매번 위와 같이 입력을 한 후에 접속하기는 매우 번거롭기 때문에 커스텀을 해보자. 

```
cd ~/.ssh/
그리고 ls로 확인해보면 config 파일이 존재할 것이다. 

vi config 

Host [ssh 접속을 할때 IP 대신 사용할 호스트명]
  HostName [EC2의 EIP 주소]
  User ec2-user
  IdentityFile ~/.ssh/{pem 키 이름}

예를들면 아래와 같이 수정하면 된다. 

Host sopt
  HostName 52.79.90.119
  User ubuntu
  IdentityFile ~/Desktop/AWS/SOPT_Server_AWS.pem

그리고 나는 Host를 sopt로 했기 때문에 이제 ssh sopt로 접속이 가능하다. 
```

