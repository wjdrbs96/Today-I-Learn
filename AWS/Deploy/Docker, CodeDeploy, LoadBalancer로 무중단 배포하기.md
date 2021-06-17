# `Docker, CodeDeploy, Load-Balancer로 무중단 배포하기`

이번 글에서는 현재 진행하고 있는 [프로젝트](https://github.com/YAPP-18th/iOS1_Backend) 에서 `무중단 자동화 배포`를 하는 과정에 대해서 정리를 해보겠습니다. 
대신 EC2 생성(jar 배포) 등등 세세하게 다루지는 않고 큰 부분들만 다루어보겠습니다. 

![YAPP 아키텍쳐](https://user-images.githubusercontent.com/45676906/122323176-a6136b80-cf61-11eb-96ad-f94f34c2fd79.png)

프로젝트 전체의 아키텍쳐는 위와 같습니다. 이 중에서 `Docker`, `Load-Balancer`, `CodeDeploy`로 어떻게 무중단 배포를 하였는지에 대해서만 알아보겠습니다. (Jenkins 관련 설정을 다루지 않습니다.)
간단하게 프로젝트 배포 흐름을 정리하면 아래와 같습니다.

<br>

## `배포 순서 정리`

1. Github에 push를 한다. 
2. Jenkins가 Github Repository를 Clone 받는다.
3. Jenkins가 Build 후에 jar, dockerfile, script file, yml 파일 등등 zip 으로 압축 후에 S3로 전달
4. CodeDeploy가 S3에 업로드 된 파일로 EC2에 현재 위치 배포를 진행합니다.
5. 배포를 하는 도중에 서버가 중단되면 안되기 때문에 `Load-Balancer`와 연결해서 서버 한대씩 배포하도록 설계하였습니다. 
6. 즉, A 서버에 배포를 하면 B 서버가 트래픽을 받고, A 서버 배포가 되면 A 서버가 트래픽을 받고 B 서버에 배포를 진행합니다. 

<br>

이제 `CodeDeploy`, `Load-Balancer` 설정은 어떻게 했고, `Docker` 배포 Shell Script는 어떻게 작성했는지에 대해서 알아보겠습니다. 

<br>

## `로드 밸런서 설정`

![스크린샷 2021-04-14 오후 4 33 05](https://user-images.githubusercontent.com/45676906/114671510-24c01280-9d3f-11eb-9735-92ee684763c8.png)

`Application Load Balancer`를 선택하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 4 34 35](https://user-images.githubusercontent.com/45676906/114671819-75d00680-9d3f-11eb-90e7-3be27da1e05a.png)

실제로는 HTTPS가 적용을 하였지만 이번 글에서는 HTTP로만 할 것이기 때문에 위와 같이 체크하겠습니다.

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

위와 같이 값을 작게 설정 해주어야 CodeDeploy 배포 과정인 AllowTraffic 구간에서 빠르게 진행될 수 있습니다. 이렇게 Load-Balancer도 설정을 다 마쳤습니다. 



<br>

## `CodeDeploy 설정`

![스크린샷 2021-06-17 오후 12 00 14](https://user-images.githubusercontent.com/45676906/122329363-4078ac80-cf6c-11eb-8074-7c0de1055ecf.png)

CodeDeploy 배포 그룹에서 `역할` 설정이 중요한데요. 

<br>

![스크린샷 2021-06-17 오후 1 03 49](https://user-images.githubusercontent.com/45676906/122329512-83d31b00-cf6c-11eb-9c11-bda91e3b03ef.png)

위의 정책이 들어간 역할을 CodeDeploy 배포 그룹에 연결하겠습니다. (아마 로드밸런서 정책은 없어도 될 거 같습니다.)

<br>

![스크린샷 2021-06-17 오후 1 07 08](https://user-images.githubusercontent.com/45676906/122329815-06f47100-cf6d-11eb-8dc7-502ad70c03c6.png)

배포는 `현재 위치` 방식을 사용할 것인데 현재 위치 배포 방식을 잘 모르겠다면 [여기](https://devlog-wjdrbs96.tistory.com/304?category=885022) 를 참고하시면 좋을 거 같습니다. 
그리고 API Server는 EC2 2대를 사용하고 있는데, EC2 태그(`Key-Value` 형태)를 태그 그룹에다 위와 같이 설정해주면 됩니다. 그러면 CodeDeploy가 태그를 찾아서 해당 EC2에 배포를 진행하게 됩니다. 

<br>

![스크린샷 2021-06-17 오후 1 13 33](https://user-images.githubusercontent.com/45676906/122330370-03adb500-cf6e-11eb-983a-03cadca41ea9.png)

그리고 배포는 `CodeDeploydafult.HalfAtTime`을 사용했는데 이것은 절반씩 배포하겠다는 뜻입니다. 즉, EC2 2대를 사용하니까 1대씩 배포를 진행하겠다는 뜻입니다. 

<br>

