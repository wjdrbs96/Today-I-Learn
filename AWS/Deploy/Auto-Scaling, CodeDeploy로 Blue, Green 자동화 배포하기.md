# `Auto Scaling, CodeDeploy로 Blue/Green 자동화 배포하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/303) 에서는 아래와 같은 아키텍쳐로 진행되었습니다.(저번 글을 한번 가볍게 보고 오는 것을 추천드립니다. AWS 서비스를 생성하는 과정에서 일부 생략되는 것이 있을 수 있습니다.) 

![1121](https://user-images.githubusercontent.com/45676906/114645130-c893c900-9d13-11eb-8da8-1a0b3f8498e8.png)

간단하게 `로드 밸런서`, `Auto-Scaling`에 해당하는 아키텍쳐만 보면 위와 같습니다. 즉, ELB에는 하나의 타켓 그룹(Auto-Scaling 그룹)만이 존재하는 상황입니다. 

그래서 이번 글에서는 [Blue/Green 배포 방식](https://devlog-wjdrbs96.tistory.com/300) 으로 타켓 그룹(Auto-Scaling 그룹)을 2개 만들어서 배포를 진행해보겠습니다. 

<br>

![1](https://user-images.githubusercontent.com/45676906/114645381-540d5a00-9d14-11eb-8ec5-c41d415c9781.png)

이번 글에서 진행할 아키텍쳐는 위와 같습니다. 즉, `Blue/Green 그룹으로 나눠서 무중단으로 자동 배포가 진행되게 할 것` 입니다.

저번 글에서 실습을 했다면 현재 `Auto Scaling 그룹`, `로드 밸런서 1개`가 있을 것입니다. 

<br>

## `로드 밸런서 생성하기`

![스크린샷 2021-04-14 오후 4 33 05](https://user-images.githubusercontent.com/45676906/114671510-24c01280-9d3f-11eb-9735-92ee684763c8.png)

`Application Load Balancer`를 선택하겠습니다. 

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
                                      
<br> <br>

## `EC2 인스턴스 AMI 만들기`

<img width="502" alt="스크린샷 2021-04-14 오후 4 56 29" src="https://user-images.githubusercontent.com/45676906/114674674-71f1b380-9d42-11eb-9284-df6b26f01888.png">

이미지로 만들고자 하는 인스턴스를 선택하고 오른쪽 마우스 클릭 후에 위와 같이 `이미지 생성`을 하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 5 01 33](https://user-images.githubusercontent.com/45676906/114675443-31df0080-9d43-11eb-9ac7-30e3138d4a07.png)

시간이 조금 걸리기는 하지만 위와 같이 AMI 이미지가 잘 만들어진 것을 볼 수 있습니다. 참고로 위의 이미지를 만들 때는 8080 포트로 접속했을 때 존재하는 페이지여야 합니다.(다른 포트를 사용해도 상관 없습니다. 저는 Spring Boot Tomcat을 이용할 예정입니다.)

<br> <br>

## `EC2 시작 템플릿 만들기`

EC2 시작 템플릿에 설정된 것을 기반으로 Auto-Scaling이 인스턴스를 만들게 됩니다. 

<br>

![스크린샷 2021-04-14 오후 5 04 20](https://user-images.githubusercontent.com/45676906/114675872-b2056600-9d43-11eb-9484-0b643e08e379.png)

간단하게 이름을 정한 후에 아래에 위와 같이 설정을 하겠습니다. 

![스크린샷 2021-04-14 오후 5 06 40](https://user-images.githubusercontent.com/45676906/114676083-e2e59b00-9d43-11eb-887d-3e0c814296fa.png)

<br>

![스크린샷 2021-04-14 오후 5 08 03](https://user-images.githubusercontent.com/45676906/114676271-188a8400-9d44-11eb-9262-46bad00c6972.png)

그리고 위의 `인스턴스 프로파일` 설정은 중요합니다. 위와 같이 설정을 해주어야 Auto-Scaling을 통해 만들어진 인스턴스도 해당 역할을 가지고 만들어집니다. (자세한 내용은 [저번 글](https://devlog-wjdrbs96.tistory.com/303) 에서 확인하시면 됩니다.)

<br>

![스크린샷 2021-04-15 오후 1 50 26](https://user-images.githubusercontent.com/45676906/114815912-272f7480-9df2-11eb-960f-3676d9242922.png)

그리고 엄청 중요한!! 좀 더 아래에 보면 `사용자 데이터`가 있습니다. 이것은 EC2 인스턴스가 처음 생성될 때 실행될 명령어 입니다. 저는 AMI를 만들 때 원본 EC2에 Spring Boot jar가 실행 중이었기 때문에 Auto-Scaling으로 만들어진 EC2에도 jar를 실행시키기 위해서 위와 같이 명령어를 적었습니다. 

```
#!/bin/bash
cd /home/ec2-user/
nohup java -jar *.jar &
```

- `#!/bin/bash`: 주석이 아닙니다! 중요하니 꼭 적어주어야 합니다.
- `cd/home/ec2-user`: 현재 저는 EC2 Linux2 버전을 사용하고 있습니다.
- `nohup java -jar *.jar &`: jar 파일을 백그라운드로 실행시키는 명령어 입니다. 
- root 권한으로 실행되기 때문에 sudo를 적지 않아도 됩니다.

<br>

<img width="655" alt="스크린샷 2021-04-15 오후 2 12 36" src="https://user-images.githubusercontent.com/45676906/114817307-b6d62280-9df4-11eb-9d79-f180927aca68.png">

그리고 위와 같이 AMI를 만들 때 사용한 원본 EC2에 java 11이 설치되어 있었기 때문에 `사용자 데이터`에는 Java 설치하는 명령어는 적지 않았습니다. 

<br>

<img width="509" alt="스크린샷 2021-04-15 오후 2 00 52" src="https://user-images.githubusercontent.com/45676906/114816430-19c6ba00-9df3-11eb-9ca9-c6ecd21bc53d.png">

AMI를 만들 때의 원본 EC2의 상태는 위와 같습니다. 원본 EC2에 jar가 실행되고 있어도 AMI를 만드는 과정에서 실행되고 있는 jar가 죽기 때문에 `사용자 데이터`에 위와 같이 명령어를 꼭 입력해야 합니다! (이것때매 하루를 삽질했습니다..ㅠㅠ)


<br> <br> 

## `Auto-Scaling 그룹 만들기`

![스크린샷 2021-04-14 오후 5 11 03](https://user-images.githubusercontent.com/45676906/114676673-7ae38480-9d44-11eb-806f-ae8e0a903927.png)

그 다음 뷰에서는 `시작 템플릿 준수`, `모든 서브넷 체크` 후에 다음으로 넘어가겠습니다. 

![스크린샷 2021-04-14 오후 5 12 59](https://user-images.githubusercontent.com/45676906/114677042-e0d00c00-9d44-11eb-994c-f9205a1f2efc.png)

위에서 만든 `로드밸런싱 타겟 그룹`을 지정하고 다음으로 넘어가겠습니다. 

<br>

<img width="895" alt="스크린샷 2021-04-14 오후 5 15 02" src="https://user-images.githubusercontent.com/45676906/114677207-0bba6000-9d45-11eb-9d2a-4800ac04b4ae.png">

그리고 게속 다음을 누르고 Auto-Scaling-Group을 만들겠습니다. 

<br>

<img width="1075" alt="스크린샷 2021-04-14 오후 5 17 21" src="https://user-images.githubusercontent.com/45676906/114677559-63f16200-9d45-11eb-846b-554e6c98b4ef.png">

그러면 위에서 `적정 용량`, `최소 용량`, `최대 용량`을 모두 2로 지정했기 때문에 인스턴스가 2개 생성되고 있는 것을 볼 수 있습니다. 그리고 로드밸런서 타겟그룹에 가서 확인 잘 연결이 되었는지 해보겠습니다.

<br>

![스크린샷 2021-04-15 오후 2 07 45](https://user-images.githubusercontent.com/45676906/114816925-108a1d00-9df4-11eb-97f8-9fe860ffcc5a.png)

위와 같이 8080 포트가 `Healthy` 상태인 것도 확인할 수 있습니다. 이제 자동화 배포를 하기 위해서 CodeDeploy를 설정해보겠습니다.

<br> <br>

## `CodeDeploy 설정하기`

이제 `Blue/Green` 방식으로 배포하기 위해서 설정하겠습니다. 

![스크린샷 2021-04-14 오후 2 54 14](https://user-images.githubusercontent.com/45676906/114661157-811c3580-9d31-11eb-9378-e8d61b582557.png)

CodeDeploy 애플리케이션을 만든 후에 CodeDeploy 애플리케이션 그룹을 만들겠습니다. 여기서 서비스 역할을 정하는데 이것은 [저번 글](https://devlog-wjdrbs96.tistory.com/303) 을 참고하시길 바랍니다.
추가로 `블루/그린 배포` 방식은 위의 역할에다 정책을 하나 더 추가해주어야 합니다. 

<br>

![스크린샷 2021-04-15 오후 2 31 43](https://user-images.githubusercontent.com/45676906/114818812-601e1800-9df7-11eb-9acd-099646a6cdbe.png)

<br>

![스크린샷 2021-04-15 오후 2 33 42](https://user-images.githubusercontent.com/45676906/114818969-a2475980-9df7-11eb-915b-a8c5cb4568d1.png)


```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "iam:PassRole",
                "ec2:CreateTags",
                "ec2:RunInstances"
            ],
            "Resource": "*"
        }
    ]
}
```

위와 같이 입력한 후에 `정책`을 하나 만들겠습니다. 

![스크린샷 2021-04-15 오후 2 35 14](https://user-images.githubusercontent.com/45676906/114819093-dd498d00-9df7-11eb-864b-0276d6cd4b18.png)

<br>

![스크린샷 2021-04-15 오후 2 36 34](https://user-images.githubusercontent.com/45676906/114819205-108c1c00-9df8-11eb-88fc-d7d801ee9c0b.png)

위에서 만든 정책을 CodeDeploy에 부여할 역할에다가 위와 같이 추가하겠습니다. 이제 다시 CodeDeploy로 돌아오겠습니다.

<br>


![스크린샷 2021-04-14 오후 2 56 24](https://user-images.githubusercontent.com/45676906/114661280-b6c11e80-9d31-11eb-8916-09e27d02d5d3.png)

위에서 `인스턴스 수동 프로비저닝`을 선택하면 직접 새로운 Auto-Scaling 그룹을 만들어야 하지만 `그룹 자동 복사`를 선택하면 자동으로 Auto-Scaling 그룹(Green 그룹)이 생성되고 마지막에 Blue Group 삭제까지 자동으로 진행됩니다.

<br>

![스크린샷 2021-04-14 오후 2 59 54](https://user-images.githubusercontent.com/45676906/114661574-33ec9380-9d32-11eb-9340-3ce0b66b0395.png)

배포 성공한 후에 원본 인스턴스를 종료하는 시간을 테스트를 빨리 하기 위해서 5분으로 설정했습니다.(실제 환경에서는 30분 ~ 1시간 등등 상황에 맞게 설정하면 될 거 같습니다.)
그리고 배포는 절반씩 진행하려고 `CodeDeployDefault.HalfAtTime`을 선택했습니다.

<br>

![스크린샷 2021-04-14 오후 3 02 37](https://user-images.githubusercontent.com/45676906/114661963-da389900-9d32-11eb-8061-514725b3a7f2.png)

현재는 타겟 그룹이 하나 라서 그 타겟 그룹을 지정했습니다. 타겟 그룹 내부에서 `Blue/Green` 방식으로 배포가 진행될 것입니다.


<br>

## `Spring Boot, Travis CI 사용하기`

![스크린샷 2021-04-19 오전 11 15 56](https://user-images.githubusercontent.com/45676906/115172622-9d88eb00-a100-11eb-8d75-7e7d84835b2b.png)

위와 같이 Controller를 수정한 후에 Github에 push 하겠습니다. 


![스크린샷 2021-04-19 오전 11 24 06](https://user-images.githubusercontent.com/45676906/115173314-015fe380-a102-11eb-8105-08aa7c526d97.png)

위의 1단계를 진행하면서 Green 그룹과 EC2 인스턴스가 생성됩니다.

![스크린샷 2021-04-19 오전 11 24 06](https://user-images.githubusercontent.com/45676906/115173314-015fe380-a102-11eb-8105-08aa7c526d97.png)

![스크린샷 2021-04-19 오전 11 27 49](https://user-images.githubusercontent.com/45676906/115173470-569bf500-a102-11eb-86df-c6389cec3496.png)

Auto-Scaling Group도 잘 만들어진 것을 볼 수 있습니다.

![스크린샷 2021-04-19 오전 11 30 02](https://user-images.githubusercontent.com/45676906/115173663-bf836d00-a102-11eb-9467-f859d4df9a9d.png)

여기서 `시작 템플릿`에 설정해놓은 사용자 데이터를 통해서 인스턴스 생성될 때 해당 명령어를 실행하게 됩니다. 근데 Blue Group의 인스턴스들은 문제없이 잘 되는데.. Green Group으로 만들어진 인스턴스들만 실행이 먹통이 되는 문제가 있었습니다.

<img width="755" alt="스크린샷 2021-04-19 오전 11 48 51" src="https://user-images.githubusercontent.com/45676906/115175010-3f123b80-a105-11eb-95bd-079edd5ff843.png">

예를들어, 위와 같이 Green Group에서 만들어진 EC2 인스턴스에서 8080 포트가 떠있지만 접속은 안됩니다. 도대체 왜그런지.. 아신다면 댓글 부탁드립니다 ㅠㅠ 또 가끔은 접속이 되기도 했는데.. 접속이 안되면 포트 kill 해준 후에 jar 재실행 하고 수동으로 했습니다 ..

![스크린샷 2021-04-19 오전 11 31 34](https://user-images.githubusercontent.com/45676906/115173750-ea6dc100-a102-11eb-99bf-2d51dee98286.png)
 
![스크린샷 2021-04-19 오전 11 31 51](https://user-images.githubusercontent.com/45676906/115173793-fc4f6400-a102-11eb-9b72-f7f0b93a8496.png)

![스크린샷 2021-04-19 오전 11 36 01](https://user-images.githubusercontent.com/45676906/115174063-826baa80-a103-11eb-9664-b3dd4baf10d6.png)

위와 같이 3단계가 끝난 후에 Auto-Scaling 그룹의 대상그룹을 확인해보겠습니다. 

![스크린샷 2021-04-19 오전 11 35 56](https://user-images.githubusercontent.com/45676906/115174044-7a136f80-a103-11eb-8ae1-e7026bca0443.png)

그러면 위와 같이 자동으로 Green Group이 로드 밸런서의 대상그룹에 연결된 것을 볼 수 있습니다.

![스크린샷 2021-04-19 오전 11 39 42](https://user-images.githubusercontent.com/45676906/115174315-002fb600-a104-11eb-98d7-b3ff684e00b6.png)

Blue Group의 Auto-Scaling 그룹과 인스턴들이 위에서 설정한 시간 5분이 지나면 삭제될 것입니다. 

![스크린샷 2021-04-19 오전 11 40 56](https://user-images.githubusercontent.com/45676906/115174406-281f1980-a104-11eb-8add-29a8cc0f80bc.png)

위와 같이 Blue Group은 자동으로 삭제되고 있는 것을 볼 수 있습니다.

![스크린샷 2021-04-19 오전 11 42 17](https://user-images.githubusercontent.com/45676906/115174563-6f0d0f00-a104-11eb-9e76-eb55741969be.png)

그리고 Blue Group의 EC2 인스턴스들이 자동으로 종료되는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-19 오전 11 44 19](https://user-images.githubusercontent.com/45676906/115174682-a67bbb80-a104-11eb-987f-2a3725b65177.png)

그리고 로드 밸런서 DNS로 접속해보면 새로운 배포 버전의 인스턴스인 Green Group으로 접속이 잘 되는 것을 볼 수 있습니다. 

![스크린샷 2021-04-19 오전 11 45 30](https://user-images.githubusercontent.com/45676906/115174725-beebd600-a104-11eb-86b0-e94ce96973fc.png)

이렇게 최종적으로 배포에 성공한 것을 볼 수 있습니다. 이번 실습은 진짜 엄청나게 일주일 동안 삽질하면서 겨우겨우 했습니다.. 그럼에도 아직도 의문점이 있는 부분이 있지만.. 일단은 이정도로 마무리 해보려 합니다.  