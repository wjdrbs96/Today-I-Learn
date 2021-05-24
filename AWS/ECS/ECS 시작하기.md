# `ECS 시작하기`

![스크린샷 2021-05-24 오후 2 26 34](https://user-images.githubusercontent.com/45676906/119300674-21a03680-bc9c-11eb-8128-a2db625b87ac.png)

처음에 ECS를 들어간 후에 `클러스터 생성`을 누르겠습니다. 

<br>

![스크린샷 2021-05-24 오후 2 28 27](https://user-images.githubusercontent.com/45676906/119300870-75128480-bc9c-11eb-98f1-0a0151a9d363.png)

그리고 `Fargate`를 사용할 것이기 때문에 `네트워크 전용`을 누르겠습니다. 

<br>

![스크린샷 2021-05-24 오후 2 30 35](https://user-images.githubusercontent.com/45676906/119300992-b0ad4e80-bc9c-11eb-9009-79ceb8062eb7.png)

그리고 위와 같이 `클러스터 이름`을 적고, `VPC는 만들어져 있는 것`을 사용하기 위해서 체크하지 않고 생성을 누르겠습니다.

<br>

![스크린샷 2021-05-24 오후 2 33 00](https://user-images.githubusercontent.com/45676906/119301177-071a8d00-bc9d-11eb-86d3-b842a9607b73.png)

그러면 위와 같이 잘 생성이 되었을 것이고 `클러스터 보기`를 누르겠습니다. 그리고 `ECR`을 Repository를 생성하겠습니다. ECR은 Private Docker Hub라고 생각하면 될 것 같습니다.

<br>

## `ECR Repository 만들기`

![스크린샷 2021-05-24 오후 2 35 59](https://user-images.githubusercontent.com/45676906/119301449-7abc9a00-bc9d-11eb-8e57-5d38e4691940.png)

위와 같이 이름을 설정하고 나머지는 Default로 놓은 후에 레포지토리를 생성하겠습니다.

<br>

![스크린샷 2021-05-24 오후 2 38 10](https://user-images.githubusercontent.com/45676906/119301653-c4a58000-bc9d-11eb-81e3-fd1503952eb9.png)

그리고 레포지토리가 생성되면 위와 같이 `푸시 명령`을 확인할 수 있습니다.

<br>

![스크린샷 2021-05-24 오후 2 39 34](https://user-images.githubusercontent.com/45676906/119301813-06cec180-bc9e-11eb-9bbc-404ee32b097a.png)

위와 같이 `푸시 명령`에 대해서 설명이 잘 나와 있는 것을 볼 수 있습니다. 푸시 명령을 통해서 ECR로 푸시 해보겠습니다. 

<br>

## `Spring 프로젝트`

저는 [여기](https://github.com/wjdrbs96/Spring-ECS) 에 Spring gradle Project를 생성해놓았습니다! 자신이 원하시는 프로젝트, Dockerfile을 작성 후에 그것을 ECR에 올리면 될 거 같습니다.

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /root

COPY ./build/libs/*.jar .

CMD java -jar *.jar
```

저는 위와 같이 Dockerfile을 작성했습니다. 이것을 ECR로 한번 올려보겠습니다.

<br>

```
./gradlew clean build
```

위의 명령어를 통해서 먼저 jar 파일을 만들겠습니다.

<br>

![스크린샷 2021-05-24 오후 2 57 34](https://user-images.githubusercontent.com/45676906/119303314-7c3b9180-bca0-11eb-901b-d27e67b118cb.png)

그리고 ECR에서 제공해주는 푸시 명령어를 순서대로 입력하면 위와 같습니다. 이미지가 ECR로 잘 올라갔는지 확인해보겠습니다. 

<br>

![스크린샷 2021-05-24 오후 2 59 22](https://user-images.githubusercontent.com/45676906/119303428-a8571280-bca0-11eb-89e0-013422e628bd.png)

위와 같이 이미지가 잘 올라간 것을 확인할 수 있습니다. (혹시나 안된다면 AWS Configure 설정을 해야 합니다.) 그리고 ELB도 사용할 것이기 때문에 ELB를 생성하겠습니다. 

<br>

## `ELB 만들기`

![스크린샷 2021-05-24 오후 3 02 43](https://user-images.githubusercontent.com/45676906/119303687-23b8c400-bca1-11eb-89f6-4683fb3155ea.png)

![스크린샷 2021-05-24 오후 3 04 39](https://user-images.githubusercontent.com/45676906/119303907-6ed2d700-bca1-11eb-9e63-20cc5ed84381.png)

![스크린샷 2021-05-24 오후 3 04 49](https://user-images.githubusercontent.com/45676906/119303944-7a260280-bca1-11eb-928c-5c5cb5ceb899.png)

위와 같이 `가용 영역`을 모두 활성화 시키고 다음을 누르겠습니다.

<br>

그리고 보안그룹은 80번, 443번 정도? 열려있는 보안그룹을 선택하겠습니다. (기존에 있는거 써도 되고, 새로 만들어도 됩니다!)

<br>


![스크린샷 2021-05-24 오후 3 08 00](https://user-images.githubusercontent.com/45676906/119304210-e3a61100-bca1-11eb-8f86-fa7e53497074.png)

그리고 `타겟 그룹`은 사용하지 않을 것이기 때문에 대충 만들겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 09 51](https://user-images.githubusercontent.com/45676906/119304399-2831ac80-bca2-11eb-9fbe-df8ddaa3d9d3.png)

그리고 인스턴스를 추가하지 않고 생성하겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 11 17](https://user-images.githubusercontent.com/45676906/119304644-85c5f900-bca2-11eb-9525-b3a6dd05780c.png)

위와 같이 리스너는 사용하지 않고 임시로 만들었기 때문에 삭제하겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 13 39](https://user-images.githubusercontent.com/45676906/119304796-be65d280-bca2-11eb-9079-7f12ba145afd.png)

타겟 그룹도 사용하지 않을 것이고 임시로 만든 것이라 똑같이 삭제하겠습니다. 비어있는 ALB를 만든 것입니다.

<br>

## `Task Definition 정의`

Task Definition은 말 그대로 어떤 일을 해야 하는지 정의만 해놓는 것입니다. 

![스크린샷 2021-05-24 오후 3 16 29](https://user-images.githubusercontent.com/45676906/119305026-17356b00-bca3-11eb-937a-6cd55d1ff82f.png)

![스크린샷 2021-05-24 오후 3 17 26](https://user-images.githubusercontent.com/45676906/119305078-303e1c00-bca3-11eb-813c-3dc5366fe9e3.png)

그리고 `FARGATE`를 만들 것이기 때문에 위와 같이 선택하고 `다음`을 누르겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 19 08](https://user-images.githubusercontent.com/45676906/119305253-72fff400-bca3-11eb-96b8-7d3b3b18fa1a.png)

원하는 이름을 입력하겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 20 42](https://user-images.githubusercontent.com/45676906/119305419-ad699100-bca3-11eb-9f23-f77746cea12d.png)

`작업 메모리`, `작업 CPU`를 자신의 사용량에 맞게 설정하고 `컨테이너 추가`를 누르겠습니다. 그러면 어떤 화면이 뜨고 `이미지`라는 칸이 존재합니다. 거기에 ECR에 올렸던 이미지의 URI를 넣어야 합니다.

<br>


![스크린샷 2021-05-24 오후 3 23 28](https://user-images.githubusercontent.com/45676906/119305732-181acc80-bca4-11eb-9bb0-ddded803f836.png)

아래와 같이 ECR로 가서 URI를 복사한 후에 넣어주겠습니다.

![스크린샷 2021-05-24 오후 3 24 53](https://user-images.githubusercontent.com/45676906/119306025-82337180-bca4-11eb-9996-95c0cb1175c8.png)

그리고 `추가`를 누르겠습니다.

<br>

![스크린샷 2021-05-24 오후 3 32 26](https://user-images.githubusercontent.com/45676906/119306567-51077100-bca5-11eb-92e3-20014d7c3e2f.png)

![스크린샷 2021-05-24 오후 3 34 31](https://user-images.githubusercontent.com/45676906/119306754-9af05700-bca5-11eb-88ce-b2c417e8e67e.png)

![스크린샷 2021-05-24 오후 3 37 31](https://user-images.githubusercontent.com/45676906/119307039-0508fc00-bca6-11eb-81ad-373872d96388.png)

![스크린샷 2021-05-24 오후 3 40 39](https://user-images.githubusercontent.com/45676906/119307398-9b3d2200-bca6-11eb-9c8b-dedef08202c9.png)

![스크린샷 2021-05-24 오후 3 42 45](https://user-images.githubusercontent.com/45676906/119307542-ce7fb100-bca6-11eb-831b-48aeae64a602.png)

![스크린샷 2021-05-24 오후 3 44 51](https://user-images.githubusercontent.com/45676906/119307815-359d6580-bca7-11eb-8569-4448ebea6077.png)

![스크린샷 2021-05-24 오후 3 48 00](https://user-images.githubusercontent.com/45676906/119308013-79906a80-bca7-11eb-9af3-1f0c4e569c96.png)

![스크린샷 2021-05-24 오후 3 49 45](https://user-images.githubusercontent.com/45676906/119308187-b65c6180-bca7-11eb-9e45-59cc748c34e9.png)

![스크린샷 2021-05-24 오후 3 51 38](https://user-images.githubusercontent.com/45676906/119308480-0f2bfa00-bca8-11eb-8110-deb3ea9b8000.png)

<img width="822" alt="스크린샷 2021-05-24 오후 3 56 17" src="https://user-images.githubusercontent.com/45676906/119308902-94171380-bca8-11eb-90cb-059ef0a530de.png">

![스크린샷 2021-05-24 오후 3 58 03](https://user-images.githubusercontent.com/45676906/119309116-d3456480-bca8-11eb-9642-3f868a8bbc1c.png)

![스크린샷 2021-05-24 오후 3 58 52](https://user-images.githubusercontent.com/45676906/119309279-0e479800-bca9-11eb-9cc1-24bd9bf8b7dd.png)

ECS가 잘 생성이 되었습니다. ELB로 가서 접속을 해보겠습니다.

<br>



