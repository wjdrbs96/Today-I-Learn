# `EC2에 ubuntu 20.04에 Jenkins 설치하기`

- [https://lindarex.github.io/jenkins/ubuntu-jenkins-installation/](https://lindarex.github.io/jenkins/ubuntu-jenkins-installation/) 
- 위에랑 같이 참고해서 해보기

```
wget -q -O - https://pkg.jenkins.io/debian/jenkins-ci.org.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins
```

EC2의 접속한 후에 위의 명령어들을 순서대로 입력하면 설치가 됩니다. 그런데 만약에 EC2에 Java 8이 설치되어 있지 않다면 `Error`가 발생할 것입니다. 

<br>

### `Java 설치`

```
# 원하는 버전을 설치
sudo apt install openjdk-8-jre
sudo apt install openjdk-8-jdk
sudo apt-get install openjdk-11-jdk  (Java 11 설치)
```

위의 명령어를 통해서 자바를 설치하면 에러가 사라질 것입니다. 

그리고 설치가 잘 되었는지 확인하기 위해서 아래의 명렁어로 확인해보겠습니다.

```
sudo service jenkins restart (재시작)
sudo systemctl status jenkins (젠킨스 상태 확인)
sudo service jenkins stop (실행중인 jenkins 중지하기)
```

![스크린샷 2021-03-04 오후 4 56 26](https://user-images.githubusercontent.com/45676906/109930086-92bdf680-7d0a-11eb-8a5b-951a475dd3e6.png)

그렇게 위와 같이 문제 없이 Jenkins가 시작 될 것입니다.

<br>

### `Jenkins default port 확인`

Jenkins defauflt port는 `8080`으로 설정되어 있을 것입니다.

```
sudo vi /etc/default/jenkins
```

위의 명령어를 쳐서 들어가면 `HTTP_PORT=8080`을 볼 수 있을 것입니다. 그러면 `default_port는 8080`입니다.

<br>

### `Jenkins 접속하기`

설치가 잘 완료 되었다면 `http://서버IP:젠킨스포트번호`로 접속하면 아래와 같은 화면을 볼 수 있습니다.

```
ex) http://52.12.567.987:8080  (즉, http:EC2 IP:8080)
```

![스크린샷 2021-03-04 오후 4 45 01](https://user-images.githubusercontent.com/45676906/109931165-d9602080-7d0b-11eb-8903-5a6307f8f942.png)


그러면 자동으로 위와 같은 화면으로 이동합니다. 저기에 보면 `/var/lib/jenkins/secrets/initalAdminPassword`에서 키를 찾아서 넣으라고 합니다. 

하지만 `secrets` 디렉토리 접근 권한이 없기 때문에 아래와 같이 해보겠습니다.

```
cd /var/lib/jenkins 
sudo chmod 777 secrets (잠시 권한 열기)
sudo vi initialAdminPassword (내부에서 비밀번호 복사해서 젠킨스 화면에 넣기)

sudo chmod 700 secrets (권한 돌려 놓기) 
```

![스크린샷 2021-03-04 오후 10 06 49](https://user-images.githubusercontent.com/45676906/109968790-7cc62b00-7d36-11eb-806c-4acfbe1fb45d.png)

다음 화면은 아래와 같이 Customize Jenkins 설정 화면입니다.

기본 Plug-In이 포함되어 있는 `Install suggested plugins`와 Plug-In을 선택해서 설치할 수 있는 `Select plugins to install` 중 선택할 수 있으며, Select plugins to install을 선택하여 진행하도록 하겠습니다.

![스크린샷 2021-03-05 오전 9 09 39](https://user-images.githubusercontent.com/45676906/110048166-a3b24a80-7d92-11eb-8eb0-48ca26c229e7.png)

`default`로 설정되어 있는 것과 `Git` 하나만 추가해서 체크하고 `Install`을 누르겠습니다.

![스크린샷 2021-03-04 오후 10 13 21](https://user-images.githubusercontent.com/45676906/109969231-01b14480-7d37-11eb-9d6a-8581c619e6b8.png)

그러면 위와 같이 설치가 되는 것을 볼 수 있습니다.

<img width="803" alt="스크린샷 2021-03-04 오후 10 21 33" src="https://user-images.githubusercontent.com/45676906/109970069-fad70180-7d37-11eb-9390-b792f36a1055.png">

![스크린샷 2021-03-04 오후 10 20 15](https://user-images.githubusercontent.com/45676906/109969946-d713bb80-7d37-11eb-99b5-429168fb703d.png)

위와 같은 화면이 뜬다면 본인이 원하는 것을 설정해주면 됩니다. 이제 설치가 완료 되었으니 `Start` 해보겠습니다.

 

<br>

## `예제 프로젝트 배포`

### `Git, 배포용 서버에 각각 SSH Key 연동`

```
sudo mkdir /var/lib/jenkins/.ssh
cd /var/lib/jenkins/.ssh
```

SSH Key를 생성하겠습니다. 생성할 때 발생하는 커맨드 입력은 모두 스킵합니다.(엔터 2번)

```
sudo ssh-keygen -t rsa -f /var/lib/jenkins/.ssh/키이름
ex) sudo ssh-keygen -t rsa -f /var/lib/jenkins/.ssh/test

cat /var/lib/jenkins/.ssh/test  (생성된 키 복사하기)
```

생성이 되었으면 Github으로 가보겠습니다. 

![스크린샷 2021-03-04 오후 5 23 12](https://user-images.githubusercontent.com/45676906/109934879-52fa0d80-7d10-11eb-8a2b-6640bcb8ce58.png)

원하는 프로젝트로 이동한 후에 아래의 순서대로 이동하면 됩니다.

1. `settings`
2. `Deploy keys`
3. `Add deploy key`

![스크린샷 2021-03-04 오후 5 24 10](https://user-images.githubusercontent.com/45676906/109935190-aa987900-7d10-11eb-9239-255c08f34717.png)

그러면 위와 같은 화면이 볼 수 있습니다. 여기서 `title`, `key`를 입력해야 합니다. `title`은 아무거나 입력해도 되지만 key는 아까 발급받은 `SSH Key`를 넣어주어야 합니다.(cat으로 확인한 거!)

그리고 `Add key`를 누르면 아래와 같은 화면을 볼 수 있습니다.

![스크린샷 2021-03-04 오후 5 44 26](https://user-images.githubusercontent.com/45676906/109936385-5e016d80-7d11-11eb-903f-0f6dd0c57bad.png)

이제 다시 `Jenkins`로 가보겠습니다.

<br>

## `Jenkins 삭제`

```
sudo apt remove jenkins
sudo apt-get remove --purge jenkins
sudo apt-get remove --auto-remove jenkins
```