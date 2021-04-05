## `EC2 인스턴스 생성하기`

jenkins를 사용할 때 프리티어로 사용하면 EC2가 맛이가는 문제가 발생해서 성능 좋은 EC2로 생성해보겠습니다. 

![1](https://user-images.githubusercontent.com/45676906/113543993-702d3f00-9622-11eb-82bc-5c7096c6a89a.png)

![2](https://user-images.githubusercontent.com/45676906/113544080-95ba4880-9622-11eb-92d8-da084cd219c5.png)

![3](https://user-images.githubusercontent.com/45676906/113544083-96eb7580-9622-11eb-8ec5-d46c4757fc37.png)

<br>

## `EC2 Linux2에 Docker 설치하기`

```
sudo yum update -y
sudo amazon-linux-extras install -y docker
sudo service docker start
```

<br>

## `docker로 jenkins 설치하기`

```
sudo docker run -d --name jenkins -p 32789:8080 jenkins/jenkins:jdk11
```

![스크린샷 2021-04-05 오전 12 09 37](https://user-images.githubusercontent.com/45676906/113513182-44b73f80-95a3-11eb-9d67-3e71bb445725.png)

위와 같이 컨테이너가 잘 실행되고 있다면 `http://EC2-IP:32789`로 접속하겠습니다. 

![스크린샷 2021-04-05 오전 12 08 58](https://user-images.githubusercontent.com/45676906/113513215-79c39200-95a3-11eb-8461-70555aad41b6.png)

![스크린샷 2021-04-05 오전 12 13 44](https://user-images.githubusercontent.com/45676906/113513288-f8b8ca80-95a3-11eb-8757-356f9504657b.png)

```
sudo docker exec -it jenkins bash  // jenkins bash 쉘 접속
cat /var/jenkins_home/secrets/initialAdminpassword
```

![스크린샷 2021-04-05 오전 12 16 43](https://user-images.githubusercontent.com/45676906/113513346-42091a00-95a4-11eb-9727-f40e456886eb.png)

![스크린샷 2021-04-05 오전 12 20 15](https://user-images.githubusercontent.com/45676906/113513440-b5ab2700-95a4-11eb-9f86-1c9a56fe86f0.png)

그리고 원하는 계정으로 가입을 하겠습니다. 

![스크린샷 2021-04-05 오전 12 21 26](https://user-images.githubusercontent.com/45676906/113513469-e723f280-95a4-11eb-9523-47ce65bb7151.png)

![스크린샷 2021-04-05 오전 12 21 32](https://user-images.githubusercontent.com/45676906/113513499-04f15780-95a5-11eb-8632-1d263fe49489.png)

![스크린샷 2021-04-05 오전 12 24 55](https://user-images.githubusercontent.com/45676906/113513652-b85a4c00-95a5-11eb-8b13-7a89af63db96.png)

AWS CodeDeploy 플러그인을 설치하겠습니다.

![스크린샷 2021-04-05 오전 12 30 05](https://user-images.githubusercontent.com/45676906/113513742-1a1ab600-95a6-11eb-80b8-779bd1365f0d.png)

![스크린샷 2021-04-05 오전 12 31 03](https://user-images.githubusercontent.com/45676906/113513803-51896280-95a6-11eb-8583-51aa56666929.png)

![스크린샷 2021-04-05 오전 12 32 48](https://user-images.githubusercontent.com/45676906/113513857-944b3a80-95a6-11eb-9cd3-7507d9c53103.png)

![스크린샷 2021-04-05 오후 5 35 27](https://user-images.githubusercontent.com/45676906/113555406-d7a0ba00-9635-11eb-83b3-3db07ad1488a.png)

![스크린샷 2021-04-05 오전 12 38 41](https://user-images.githubusercontent.com/45676906/113514024-50a50080-95a7-11eb-82e7-e5d5a2099082.png)

![스크린샷 2021-04-05 오전 12 41 36](https://user-images.githubusercontent.com/45676906/113514102-b85b4b80-95a7-11eb-914d-cd1dfabc267a.png)

![스크린샷 2021-04-05 오후 5 33 38](https://user-images.githubusercontent.com/45676906/113555527-0880ef00-9636-11eb-9e22-6648fbd36bb0.png)

![스크린샷 2021-04-05 오전 12 48 20](https://user-images.githubusercontent.com/45676906/113514291-b776e980-95a8-11eb-935e-c14d74efbe96.png)

<br>

## `Github WebHook 추가`

<img width="818" alt="스크린샷 2021-04-05 오전 12 50 38" src="https://user-images.githubusercontent.com/45676906/113514345-f147f000-95a8-11eb-9f77-3831ed271010.png">

![스크린샷 2021-04-05 오전 12 51 02](https://user-images.githubusercontent.com/45676906/113514398-3835e580-95a9-11eb-9851-61f6a8d0aba5.png)

![스크린샷 2021-04-05 오전 12 53 12](https://user-images.githubusercontent.com/45676906/113514405-4dab0f80-95a9-11eb-9554-68f72141330b.png)

<br>

## `EC2 Linux2 CodeAgent 설치하기`

```
sudo yum install -y aws-cli
cd /home/ec2-user/ 
sudo aws configure 
wget https://aws-codedeploy-ap-northeast-2.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent status
```

![test](https://t1.daumcdn.net/cfile/tistory/992FB64D5AC566E018)

<br>

## `Jenkins CI 성공`

![스크린샷 2021-04-05 오후 5 31 36](https://user-images.githubusercontent.com/45676906/113555779-6b728600-9636-11eb-8840-70370695f2ff.png)


