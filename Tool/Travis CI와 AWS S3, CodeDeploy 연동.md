# `Travis CI와 AWS S3, CodeDeploy 연동하기`

AWS의 배포 시스템인 CodeDeploy를 이용하기 전에 배포 대상인 EC2가 CodeDeploy를 연동 받을 수 있게 IAM 역할을 하나 생성하겠습니다. 

<br>

## `EC2에 IAM 역할 추가하기`

![스크린샷 2021-03-07 오전 2 51 16](https://user-images.githubusercontent.com/45676906/110216002-040ace80-7ef0-11eb-8340-5db63ba9555d.png)

![스크린샷 2021-03-07 오전 2 51 49](https://user-images.githubusercontent.com/45676906/110216029-2bfa3200-7ef0-11eb-973d-9e6e7d8c72e4.png)

- `역할`
    - AWS 서비스에만 할당할 수 있는 권한
    - EC2, CodeDeploy
    
- `사용자`
    - AWS 서비스 외에 사용할 수 있는 권한
    - 로컬 PC, IDC 서버 등

    
![스크린샷 2021-03-07 오전 2 54 03](https://user-images.githubusercontent.com/45676906/110216072-724f9100-7ef0-11eb-85de-ef40324a843b.png)

![스크린샷 2021-03-07 오전 2 55 19](https://user-images.githubusercontent.com/45676906/110216108-a7f47a00-7ef0-11eb-9150-f1f8c1f45ad3.png)

![스크린샷 2021-03-07 오전 2 56 35](https://user-images.githubusercontent.com/45676906/110216131-c35f8500-7ef0-11eb-9cc7-6aaba890d5ea.png)

태그는 원하는 이름으로 지으면 됩니다.

![스크린샷 2021-03-07 오전 2 57 46](https://user-images.githubusercontent.com/45676906/110216184-fa359b00-7ef0-11eb-8feb-b3e67446b119.png)

이렇게 만든 역할을 EC2 서비스에 등록하겠습니다. 다시 EC2 인스턴스 목록으로 가겠습니다. 

![스크린샷 2021-03-07 오전 3 10 43](https://user-images.githubusercontent.com/45676906/110216479-c6f40b80-7ef2-11eb-9908-f49d78707e84.png)

![스크린샷 2021-03-07 오전 3 12 12](https://user-images.githubusercontent.com/45676906/110216531-facf3100-7ef2-11eb-92bb-b0ddfd8cc933.png)

![스크린샷 2021-03-07 오전 3 13 13](https://user-images.githubusercontent.com/45676906/110216568-19352c80-7ef3-11eb-9846-f6f11fdcf156.png)

그리고 위와 같이 EC2를 재부팅 하겠습니다. 

![스크린샷 2021-03-07 오전 3 16 20](https://user-images.githubusercontent.com/45676906/110216642-85179500-7ef3-11eb-8567-b83a2a6bff0e.png)

```
sudo apt install awscli
aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2
```

위와 같이 입력하면 download가 잘 되는 것을 알 수 있습니다.

```
install 파일에 실행 권한이 없으니 실행 권한을 추가한다.
chmod +x ./install

install 파일로 설치를 진행한다.
sudo ./install auto

위의 명령어가 /usr/bin/env: ruby: No such file or directory 이게 뜨면서 안된다면
sudo apt-get install ruby  설치를 진행하고 해보자.

설치가 끝났으면 Agent가 정상적으로 실행되고 있는지 상태 검사를 한다.
sudo service codedeploy-agent status

다음과 같이 running 메세지가 출력되면 정상이다.
The AWS CodeDeploy agent is running as PID xxx

// 참고: https://stackoverflow.com/questions/62286857/aws-codedeploy-agenten-on-ubuntu-20-0lts-ruby-errors
```

<br>

## `CodeDeploy 권한 생성`

![스크린샷 2021-03-07 오전 3 42 46](https://user-images.githubusercontent.com/45676906/110217411-3966ea80-7ef7-11eb-9160-256678fabbfc.png)

CodeDeploydptj EC2에 접근하려면 마찬가지로 권한이 필요합니다. AWS의 서비스이니 IAM 역할을 생성합니다. 

![스크린샷 2021-03-07 오전 3 43 19](https://user-images.githubusercontent.com/45676906/110217420-4be12400-7ef7-11eb-9b83-68bcd67787cc.png)

![스크린샷 2021-03-07 오전 3 44 44](https://user-images.githubusercontent.com/45676906/110217478-80ed7680-7ef7-11eb-8da9-b72cd58ebd46.png)

![스크린샷 2021-03-07 오전 3 45 37](https://user-images.githubusercontent.com/45676906/110217507-aa0e0700-7ef7-11eb-9886-c61b5ee8bfe9.png)

![스크린샷 2021-03-07 오전 3 46 51](https://user-images.githubusercontent.com/45676906/110217522-c1e58b00-7ef7-11eb-920d-4bb5887d46ce.png)

위와 같이 하나 밖에 없기 때문에 `다음`을 누르고 넘어갑니다.

![스크린샷 2021-03-07 오전 3 50 16](https://user-images.githubusercontent.com/45676906/110217597-3e786980-7ef8-11eb-80dc-62e0f648e8a8.png)

![스크린샷 2021-03-07 오전 3 50 59](https://user-images.githubusercontent.com/45676906/110217613-551ec080-7ef8-11eb-8dc9-52685e8445b6.png)

<br>

## `CodeDeploy 생성`

![스크린샷 2021-03-07 오전 3 51 55](https://user-images.githubusercontent.com/45676906/110217634-7e3f5100-7ef8-11eb-895c-8750caf8a0c9.png)

![스크린샷 2021-03-07 오전 3 53 13](https://user-images.githubusercontent.com/45676906/110217679-b8a8ee00-7ef8-11eb-8632-2aec19dda769.png)

![스크린샷 2021-03-07 오전 3 54 47](https://user-images.githubusercontent.com/45676906/110217708-dd9d6100-7ef8-11eb-84f3-6db93084f2a0.png)

![스크린샷 2021-03-07 오전 3 55 16](https://user-images.githubusercontent.com/45676906/110217720-fefe4d00-7ef8-11eb-9332-d2681a0258c1.png)

![스크린샷 2021-03-07 오전 3 57 26](https://user-images.githubusercontent.com/45676906/110217813-8481fd00-7ef9-11eb-8c50-49101b162063.png)

![스크린샷 2021-03-07 오전 3 58 54](https://user-images.githubusercontent.com/45676906/110217827-a2e7f880-7ef9-11eb-82f8-d27bbf022bb5.png)

![스크린샷 2021-03-07 오전 4 00 57](https://user-images.githubusercontent.com/45676906/110217855-ca3ec580-7ef9-11eb-8ddf-b61e70a6a17f.png)

배포 구성이란 한번 배포할 떄 몇 대의 서버에 배포할지를 결정합니다.

<br>

## `Travis CI, S3, CodeDeploy 연동`

EC2에 접속해서 디렉토리를 만들어 줍니다. 

```
mkdir app
cd app
mkdir step2
cd step2
mkdir zip
```

Travis CI의 Build가 끝나면 S3에 zip 파일이 전송되고, 이 zip 파일은 /home/ubuntu/app/step2/zip 으로 복사되어 압축을 풀 예정입니다.

Travis CI의 설정은 `.traivis.yml`로 진행했습니다. `AWS CodeDeploy`의 설정은 `appspec.yml`로 진행합니다. 

```yaml
version: 0.0
os: ubuntu
files:
  - source:
      destination: /home/ubuntu/app/step2/zip
      overwrite: yes
```

`appspec.yml`에 위의 코드를 적어줍니다. 
