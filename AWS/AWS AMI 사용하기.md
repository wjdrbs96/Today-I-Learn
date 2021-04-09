# `AWS AMI 사용하기`

AWS에는 AMI라는 것이 존재합니다. `Amazon Machine Image`의 약자입니다. 어떤 역할을 할까요? 이름에서 알 수 있듯이 Image 라는 것의 개념인데요. Docker Image와 느낌이 비슷한 거 같습니다. 

예를들어, 이번 글에서 실습해볼 것인 EC2 인스턴스를 이미지로 만들어 놓고, 이미지로 여러 개의 EC2 인스턴스를 만들 수 있는 것입니다. (Docker와 같이 아주 편리합니다. 나중에 `Auto Scaling` 할 때 사용됩니다.) 
따라서 이번 글에서 EC2 인스턴스를 AMI를 사용해서 이미지화 하고 인스턴스를 복제하는 간단한 실습을 진행해보겠습니다. 

<br>

## `EC2 AMI 이미지 만들기`

![스크린샷 2021-04-09 오후 3 05 09](https://user-images.githubusercontent.com/45676906/114136050-15a72200-9945-11eb-96b1-19cf1aab10d5.png)

AWS에서 EC2로 들어간 후에 이미지로 만들 인스턴스를 체크한 후에 작업을 누르겠습니다. 

![스크린샷 2021-04-09 오후 3 07 20](https://user-images.githubusercontent.com/45676906/114136220-51da8280-9945-11eb-9a40-d33b5d4ab3ad.png)

그리고 `이미지` -> `이미지 생성`을 누르겠습니다. 

![스크린샷 2021-04-09 오후 3 08 28](https://user-images.githubusercontent.com/45676906/114136378-8d754c80-9945-11eb-9cb8-b912eefd01f7.png)

위와 같이 `이미지 이름`을 정한 후에 바로 생성을 누르겠습니다. (저장용량 크기는 필요에 따라 설정해서 정하면 됩니다.) 그리고 이미지가 생성되는데 시간이 걸리는데 이 때 본 EC2 인스턴스가 잠시 중단이 됩니다..

그리고 AMI 탭에 들어가서 이미지를 확인해보면 만들어진 것을 볼 수 있습니다. (저는 5분 정도 기다리니 생성되었습니다.)

![스크린샷 2021-04-09 오후 3 17 03](https://user-images.githubusercontent.com/45676906/114137153-b9dd9880-9946-11eb-96b5-81a0f30c2ab8.png)

![스크린샷 2021-04-09 오후 3 18 54](https://user-images.githubusercontent.com/45676906/114137338-01642480-9947-11eb-85d3-214e3ee01353.png)

그리고 이미지를 가지고 기존에 EC2 인스턴스와 똑같은 인스턴스를 하나 만들어보겠습니다. AMI에서 `시작하기`를 누르겠습니다.

![스크린샷 2021-04-09 오후 3 20 46](https://user-images.githubusercontent.com/45676906/114137480-3f614880-9947-11eb-8ceb-9427e868b8a7.png)

기존 EC2 사양과 다르게 생성할 수 있지만 저는 `프리티어` 성능으로 선택하고 넘어가겠습니다. 

![스크린샷 2021-04-09 오후 3 22 45](https://user-images.githubusercontent.com/45676906/114137690-83544d80-9947-11eb-8614-6aa2ba840df9.png)

실제 EC2 인스턴스를 만드는 것처럼 다 선택할 수 있습니다. 저는 전부 Default로 놓고 만들겠습니다. 

![스크린샷 2021-04-09 오후 3 24 50](https://user-images.githubusercontent.com/45676906/114137842-bac2fa00-9947-11eb-81c6-92dc37b60482.png)

그리고 `보안 그룹`은 위와 같이 포트를 열어놓겠습니다. (EC2 인스턴스 만들 때와 과정이 전부 똑같아서 일부 설명은 제외하겠습니다.)

![스크린샷 2021-04-09 오후 3 27 23](https://user-images.githubusercontent.com/45676906/114138131-260ccc00-9948-11eb-912c-273c91515143.png)

EC2 인스턴스에서 확인을 해보면 위와 같이 인스턴스가 하나 더 생긴 것을 볼 수 있습니다. 그러면 원래 인스턴스와 이미지로 만든 인스턴스가 똑같은지 브라우저에서 접속을 해서 테스트를 해보겠습니다. 

`그 전에 만약 EC2에 웹 서버가 설치되어 있지 않다면 아래와 같이 설치를 하고 테스트를 하면 됩니다.`

<br>

## `Amazon Linux 2에 LAMP 웹 서버 설치`

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

```
sudo vi /var/www/html/index.php
```

![스크린샷 2021-04-09 오후 3 36 07](https://user-images.githubusercontent.com/45676906/114138929-4e48fa80-9949-11eb-9106-5be74ac61cc1.png)

그리고 위와 같이 간단하게 볼 수 있게 적겠습니다. 그러면 http://{EC2-IP} 로 접속하면 해당 파일의 내용이 뜨게 됩니다. (80번 포트) 

<br>

![스크린샷 2021-04-09 오후 3 29 18](https://user-images.githubusercontent.com/45676906/114138350-771cc000-9948-11eb-8b56-24510e47e6d8.png)

![스크린샷 2021-04-09 오후 3 29 49](https://user-images.githubusercontent.com/45676906/114138355-784ded00-9948-11eb-87a6-0a5b5ed71737.png)  

그래서 기존 EC2 인스턴스와 이미지로 만든 인스턴스의 IP로 위와 같이 80번 포트로 접속해보면 결과가 똑같은 것을 확인할 수 있습니다.