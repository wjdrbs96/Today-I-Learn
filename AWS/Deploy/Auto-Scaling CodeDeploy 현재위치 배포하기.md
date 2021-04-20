# `Auto-Scaling CodeDeploy 현재 위치 자동화 배포하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/305) 에서 `Blue/Green` 배포가 무엇인지를 배웠습니다. 이번 글에서는 CodeDeploy 배포 방식 중에 하나인 `현재 위치 배포` 방식으로 실습을 진행해보겠습니다. 
현재 위치 배포가 무엇인지 잘 모르겠다면 [여기](https://devlog-wjdrbs96.tistory.com/304) 를 읽고 오시면 됩니다.

이번 글은 [저번 글](https://devlog-wjdrbs96.tistory.com/305) 에서 다루고 있는 내용과 비슷한 부분이 있기 때문에 저번 글을 먼저 읽고 오시는 걸 추천드립니다.

<br>

## `이번 글에서 사용할 기술`

- `Spring Boot`
- `Travis CI`
- `CodeDeploy`
- `S3`
- `Auto-Scaling`
- `Load-Balancer`
- `Github`

<br>

## `이번 글에서 사용할 아키텍쳐`

![스크린샷 2021-04-19 오후 10 56 24](https://user-images.githubusercontent.com/45676906/115248229-782cc900-a162-11eb-8e41-0ace35929b7c.png)

이번 글에서 해보려는 아키텍쳐는 위와 같습니다. 즉, `Auto-Scaling-Group`을 2개를 만들고 각각의 그룹 안에 EC2 인스턴스 2개를 생성할 것입니다. 그리고 각각의 Auto-Scaling-Group을 로드 밸런서의 Target-Group 이랑 연결할 것입니다. 

이제 바로 실습을 진행해보겠습니다. 

<br>

## `EC2 AMI 만들기`

EC2 초기 세팅을 한 후에 AMI를 만들겠습니다.(기본적으로 CodeAgent, Java 11은 설치되었다고 가정하겠습니다.)

![스크린샷 2021-04-19 오후 11 04 02](https://user-images.githubusercontent.com/45676906/115249412-892a0a00-a163-11eb-9948-1a62b15e6c79.png)

Spring Boot 프로젝트에서 위와 같이 간단한 Controller를 세팅한 후에 jar 파일을 만들겠습니다. 

```
./gradlew clean build
```

![스크린샷 2021-04-19 오후 11 07 12](https://user-images.githubusercontent.com/45676906/115249916-0e152380-a164-11eb-9e51-adcb439c5621.png)

그러면 위와 같이 jar 파일이 만들어집니다. 이것을 AMI를 만들 때 쓰일 EC2 인스턴스로 옮기겠습니다. (저는 Filezila를 사용해서 옮기겠습니다.)

<img width="332" alt="스크린샷 2021-04-19 오후 11 11 48" src="https://user-images.githubusercontent.com/45676906/115250579-ab705780-a164-11eb-898a-a2926e50a122.png">

EC2에 위와 같이 jar 파일을 옮겼습니다. 그리고 app 디렉토리가 보입니다. 이것은 S3에 존재하는 파일들을 CodeAgent가 EC2로 가져올 때 목적지를 정하기 위해서 임시로 만든 디렉토리입니다. 

<img width="305" alt="스크린샷 2021-04-19 오후 11 13 11" src="https://user-images.githubusercontent.com/45676906/115250761-d9559c00-a164-11eb-99dd-904877b94496.png">
 
위와 같이 `/home/ec2-user/app/step3`의 경로로 디렉토리를 만들겠습니다. 

<br>

## `Linux 명령어 세팅`

EC2 인스턴스가 재부팅 될 때마다 jar를 실행하는 명령어를 수행하도록 설정을 하겠습니다.   

```
sudo vi /etc/rc.d/rc.local
```

<img width="595" alt="스크린샷 2021-04-19 오후 4 00 53" src="https://user-images.githubusercontent.com/45676906/115194513-86f68a00-a128-11eb-91f0-88d0d814e87b.png">


들어가면 위와 같이 뜰 것입니다. 여기는 EC2 인스턴스가 부팅될 때 실행되는 명령어들을 설정할 수 있습니다. 위의 영어를 읽어보면 알 수 있지만 다 적은 후에 마지막에 `chmod +x /etc/rc.d/rc.local`을 꼭 해주어야 한다고 나와있습니다.  

<br>

일단 여기서 jar를 실행시키는 명령어를 적겠습니다.  

![스크린샷 2021-04-19 오후 4 03 20](https://user-images.githubusercontent.com/45676906/115194799-e8b6f400-a128-11eb-8d69-6c3d2a82cbc8.png)

```
cd /home/ec2-user/
nohup java -jar *.jar &
```

그리고 `rc.local` 파일에 권한 부여를 하겠습니다. (필수 !!)

```
chmod +x /etc/rc.d/rc.local
```

![스크린샷 2021-04-19 오후 4 06 12](https://user-images.githubusercontent.com/45676906/115195237-798dcf80-a129-11eb-8947-9397f5d8272d.png)

위와 같이 초록색으로 바뀌었으면 권한 부여가 잘 된 것입니다. 

<br> <br>

## `EC2 AMI 만들기`

기본적으로 CodeAgent, Java, Linux 명령어 세팅, jar 파일 존재와 같은 설정들이 되어 있는 상태로 AMI를 만들어야 합니다. 

<img width="502" alt="스크린샷 2021-04-14 오후 4 56 29" src="https://user-images.githubusercontent.com/45676906/114674674-71f1b380-9d42-11eb-9284-df6b26f01888.png">

이미지로 만들고자 하는 인스턴스를 선택하고 오른쪽 마우스 클릭 후에 위와 같이 `이미지 생성`을 하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 5 01 33](https://user-images.githubusercontent.com/45676906/114675443-31df0080-9d43-11eb-9ac7-30e3138d4a07.png)

시간이 조금 걸리기는 하지만 위와 같이 AMI 이미지가 잘 만들어진 것을 볼 수 있습니다.

<br>

## `EC2 시작 템플릿 만들기`

EC2 탭에 들어가면 왼쪽에 `시작 템플릿`이 존재합니다. Auto-Scaling 그룹이 인스턴스를 만들 때 시작 템플릿 기반으로 설정된 것을 기반으로 EC2 인스턴스를 만들게 됩니다. 

![스크린샷 2021-04-14 오후 5 04 20](https://user-images.githubusercontent.com/45676906/114675872-b2056600-9d43-11eb-9484-0b643e08e379.png)

위에서 만든 이미지 선택, 프리티어 용도인 t2.miro, 본인이 원하는 키 페어를 선택하겠습니다.

<br>

![스크린샷 2021-04-14 오후 5 06 40](https://user-images.githubusercontent.com/45676906/114676083-e2e59b00-9d43-11eb-887d-3e0c814296fa.png)

해당 보안 그룹은 꼭! 최소 8080은 열려있는 보안 그룹이어야 합니다.

<br>

![스크린샷 2021-04-14 오후 5 08 03](https://user-images.githubusercontent.com/45676906/114676271-188a8400-9d44-11eb-9262-46bad00c6972.png)

그리고 위의 `인스턴스 프로파일` 설정은 중요합니다. 위와 같이 설정을 해주어야 Auto-Scaling을 통해 만들어진 인스턴스도 해당 역할을 가지고 만들어집니다. (자세한 내용은 [참고하기](https://devlog-wjdrbs96.tistory.com/303) 에서 확인하시면 됩니다.)

<br> <br>

## `로드 밸런서 생성하기`

![스크린샷 2021-04-14 오후 4 33 05](https://user-images.githubusercontent.com/45676906/114671510-24c01280-9d3f-11eb-9735-92ee684763c8.png)

`Application Load Balancer`를 선택하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 4 34 35](https://user-images.githubusercontent.com/45676906/114671819-75d00680-9d3f-11eb-90e7-3be27da1e05a.png)

현재 테스트에서는 HTTPS는 사용하지 않고 HTTP로만 할 것이기 때문에 위와 같이 체크하겠습니다. 

<br> 

![스크린샷 2021-04-14 오후 4 36 34](https://user-images.githubusercontent.com/45676906/114672122-bd569280-9d3f-11eb-86ae-4ee5517e9dc7.png)

위와 같이 가용 영역은 모두 체크를 하고 다음으로 넘어가겠습니다. 그리고 보안그룹은 80, 8080이 열려 있도록 만들겠습니다. (원하는 형태의 보안그룹을 만들어서 사용하면 됩니다.)

<br>

![스크린샷 2021-04-14 오후 4 39 58](https://user-images.githubusercontent.com/45676906/114672641-52598b80-9d40-11eb-8c50-86d97401ff2b.png)

- `대상그룹의 포트는 대상 그룹 내 인스턴스로 로드밸런싱을 할 때 해당 포트로 트래픽을 보낸다는 뜻입니다.`
- `상태 검사는 주기적으로 로드밸런스가 대상그룹 내 인스턴스들에게 요청을 보내 확인하는 경로입니다.`

<br>

![스크린샷 2021-04-14 오후 4 48 18](https://user-images.githubusercontent.com/45676906/114673537-43270d80-9d41-11eb-8956-845950e076d0.png)

- `정상 임계값`: 연속으로 몇 번 정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `비정상 임계값`: 연속으로 몇 번 비정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `제한 시간`: 타임아웃 시간으로 응답이 몇 초 이내로 오지 않을 경우 비정상 응답으로 판단할지 지정하는 항목
- `간격`: 몇 초 간격으로 인스턴스의 상태를 물어볼지 지정하는 항목

<br>

위와 같이 값을 작게 설정 해주어야 CodeDeploy 배포 과정인 AllowTraffic 구간에서 빠르게 진행될 수 있습니다.
그리고 다음 대상 등록에서는 인스턴스를 지금 등록하지 않고 Auto-Scaling 그룹을 만들면서 자동으로 등록되게 할 것이니 일단 넘어가겠습니다.

위에서 `새 대상 그룹`을 지정해서 만들기 때문에 로드밸런서를 만들 때 대상 그룹이 하나 같이 만들어집니다.(Target-Group의 Healthy Check는 8080 포트에 root 경로로 지정하겠습니다.)

<br> <br>

## `두 번째 대상 그룹 만들기`

<img width="923" alt="스크린샷 2021-04-19 오후 11 35 16" src="https://user-images.githubusercontent.com/45676906/115254277-25eea680-a168-11eb-9c03-003324cc4807.png">

두 번째 대상 그룹도 8080 포트 인스턴스들에게 보내기 위해서 위와 같이 설정하겠습니다. 

<br>

![스크린샷 2021-04-19 오후 11 34 40](https://user-images.githubusercontent.com/45676906/115254421-4880bf80-a168-11eb-82b1-4b0abb862555.png)

그리고 두 번째 대상 그룹은 root 경로가 아닌 /test로 Health Check 경로를 설정하겠습니다. 그리고 다음에 인스턴스는 등록하지 않고 대상 그룹 생성을 하겠습니다. 

<br>

<img width="1239" alt="스크린샷 2021-04-19 오후 11 46 25" src="https://user-images.githubusercontent.com/45676906/115255875-b083d580-a169-11eb-9bbf-bf8dbf07252f.png">

그러면 위와 같이 `Target-Group-B`는 로드 밸런서와 연결이 되어 있지 않습니다. 로드 밸런서와 연결하는 작업을 하겠습니다. 

![스크린샷 2021-04-19 오후 11 49 23](https://user-images.githubusercontent.com/45676906/115256321-14a69980-a16a-11eb-98a2-79c4f28919e1.png)

로드 밸런서 탭에 들어온 후에 위의 순서대로 클릭을 하겠습니다. 

![스크린샷 2021-04-19 오후 11 52 07](https://user-images.githubusercontent.com/45676906/115256559-4ae41900-a16a-11eb-8ea7-d3ee5e0e8a87.png)

![스크린샷 2021-04-19 오후 11 53 02](https://user-images.githubusercontent.com/45676906/115256769-7d8e1180-a16a-11eb-8277-577b353ac315.png)

위와 같이 트래픽은 50% : 50%로 분배될 수 있도록 설정하고 업데이트를 클릭하겠습니다. 


<br> <br>

## `Auto-Scaling Group 만들기`

Auto-Scaling Group 2개를 만들겠습니다. 

![스크린샷 2021-04-14 오후 5 11 03](https://user-images.githubusercontent.com/45676906/114676673-7ae38480-9d44-11eb-806f-ae8e0a903927.png)

그 다음 뷰에서는 `시작 템플릿 준수`, `모든 서브넷 체크` 후에 다음으로 넘어가겠습니다. 

<br>

![스크린샷 2021-04-14 오후 5 12 59](https://user-images.githubusercontent.com/45676906/114677042-e0d00c00-9d44-11eb-994c-f9205a1f2efc.png)

위에서 만든 `로드밸런싱 타겟 그룹`을 지정하고 다음으로 넘어가겠습니다. 첫번 째 Auto-Scaling Group은 Target-Group에다 연결하겠습니다. 

<br> <br>

<img width="895" alt="스크린샷 2021-04-14 오후 5 15 02" src="https://user-images.githubusercontent.com/45676906/114677207-0bba6000-9d45-11eb-9d2a-4800ac04b4ae.png">

저는 인스턴스 2개로 유지할 것이기 때문에 용량을 모두 2로 설정하고 게속 다음을 누른 후에 Auto-Scaling-Group을 만들겠습니다. 

<br>

<img width="1075" alt="스크린샷 2021-04-14 오후 5 17 21" src="https://user-images.githubusercontent.com/45676906/114677559-63f16200-9d45-11eb-846b-554e6c98b4ef.png">

그러면 위에서 `적정 용량`, `최소 용량`, `최대 용량`을 모두 2로 지정했기 때문에 인스턴스가 2개가 시작 템플릿 설정에 맞춰서 자동으로 생성되고 있는 것을 볼 수 있습니다. 그리고 로드밸런서 타겟그룹에 가서 연결이 잘 되었는지 확인 해보겠습니다.

![스크린샷 2021-04-15 오후 2 07 45](https://user-images.githubusercontent.com/45676906/114816925-108a1d00-9df4-11eb-97f8-9fe860ffcc5a.png)

위와 같이 8080 포트가 `Healthy` 상태인 것도 확인할 수 있습니다. 이제 자동화 배포를 하기 위해서 CodeDeploy를 설정해보겠습니다.

<br>

## `두 번째 Auto-Scaling 그룹 만들기`

![스크린샷 2021-04-19 오후 11 57 30](https://user-images.githubusercontent.com/45676906/115257499-1ae94580-a16b-11eb-9989-dbfd9c48ddd3.png)

위와 같이 두 번째 Auto-Scaling Group에는 `두 번째 대상 그룹`을 지정하고 나머지는 첫 번째 그룹과 다 똑같이 만들겠습니다.

<br> <br>

## `대상 그룹 확인해보기`

![스크린샷 2021-04-20 오전 12 01 13](https://user-images.githubusercontent.com/45676906/115258457-f8a3f780-a16b-11eb-9165-678c9a5b1727.png)

![스크린샷 2021-04-20 오전 12 01 02](https://user-images.githubusercontent.com/45676906/115258256-c85c5900-a16b-11eb-917f-d13ac7d1088e.png)

위와 같이 4개의 인스턴스가 모두 Healthy 상태인 것을 확인할 수 있습니다. 

<br> <br>

## `CodeDeploy 만들기`

![스크린샷 2021-04-20 오전 12 07 00](https://user-images.githubusercontent.com/45676906/115259048-7cf67a80-a16c-11eb-93bd-ababf01d3863.png)

여기서 서비스 역할을 정하는데 이것은 [참고하기](https://devlog-wjdrbs96.tistory.com/303) 을 참고하시길 바랍니다.(역할 매우 중요합니다!! 설정을 제대로 하지 않으면 작동하지 않습니다 ㅠㅠ)

<br>

![스크린샷 2021-04-20 오전 12 09 33](https://user-images.githubusercontent.com/45676906/115259435-da8ac700-a16c-11eb-97b9-81a01390c07b.png)

<br>

![스크린샷 2021-04-20 오전 11 12 11](https://user-images.githubusercontent.com/45676906/115329933-07bb9180-a1ce-11eb-8499-4325d3372382.png)

일단은 체크하지 않고 만들어보겠습니다. (체크 하고 안하고의 차이가 좀 애매한데 좀 더 공부해봐야 겠습니다.)

<br> <br>

## `자동화 배포 해보기`

자동화 배포는 위에서 말했던 것처럼 `Spring Boot`, `Travis CI`, `S3`, `CodeDeploy`, `Load-Balancer`, `Auto-Scaling-Group`을 사용할 것입니다. 또한 자동화 배포를 하려면 `appspec.yml`, `deploy.sh`, `travis.yml` 파일들을 작성해야 하는데 그러한 내용은 [Blue/Greeen 자동화 배포]() 에서 다뤘기 때문에 해당 링크에서 참고하시면 됩니다. (똑같이 진행하겠습니다.) 

![스크린샷 2021-04-20 오전 12 15 00](https://user-images.githubusercontent.com/45676906/115260147-73214700-a16d-11eb-9097-dd167eae731e.png)

위와 같이 간단하게 수정을 한 후에 Github Push를 해보겠습니다. 그러면 Blue/Green 배포 방식과는 다르게 인스턴스를 줄이거나 생성하는 과정이 없기 때문에 상당히 빠른 시간에 배포가 진행되는 것을 볼 수 있습니다.

![스크린샷 2021-04-20 오전 11 41 16](https://user-images.githubusercontent.com/45676906/115329541-692f3080-a1cd-11eb-85b3-aec2634232e5.png)

![스크린샷 2021-04-20 오전 11 44 02](https://user-images.githubusercontent.com/45676906/115329785-cb883100-a1cd-11eb-81f1-04af9e4768f4.png)

![스크린샷 2021-04-20 오전 11 44 12](https://user-images.githubusercontent.com/45676906/115329869-e9559600-a1cd-11eb-97fe-41944198e9c5.png)

그러면 위와 같이 배포에 성공한 것을 볼 수 있습니다. 