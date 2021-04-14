# `Auto Scaling, CodeDeploy로 Blue/Green 자동화 배포하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/303) 에서는 아래와 같은 아키텍쳐로 진행되었습니다.(이것을 기반으로 할 것이라 아래의 생략되는 부분들이 존재하기 때문에 먼저 위의 실습을 진행하고 오는 것을 추천합니다.) 

![1121](https://user-images.githubusercontent.com/45676906/114645130-c893c900-9d13-11eb-8da8-1a0b3f8498e8.png)

간단하게 `로드 밸런서`, `Auto-Scaling`에 해당하는 아키텍쳐만 보면 위와 같습니다. 즉, ELB에는 하나의 타켓 그룹(Auto-Scaling 그룹)만이 존재하는 상황입니다. 

그래서 이번 글에서는 [Blue/Green 배포 방식](https://devlog-wjdrbs96.tistory.com/300) 으로 타켓 그룹(Auto-Scaling 그룹)을 2개 만들어서 배포를 진행해보겠습니다. 

![1](https://user-images.githubusercontent.com/45676906/114645381-540d5a00-9d14-11eb-8ec5-c41d415c9781.png)

이번 글에서 진행할 아키텍쳐는 위와 같습니다. 즉, `Blue/Green 그룹으로 나눠서 무중단으로 자동 배포가 진행되게 할 것` 입니다.

<br>

## `Load-Balancer 대상 그룹 만들기`

![스크린샷 2021-04-14 오전 11 14 39](https://user-images.githubusercontent.com/45676906/114645627-ca11c100-9d14-11eb-9cb9-6d4574a322f8.png)

![스크린샷 2021-04-14 오전 11 28 33](https://user-images.githubusercontent.com/45676906/114645559-ae0e1f80-9d14-11eb-954b-18b9264a21b2.png)

![스크린샷 2021-04-14 오전 11 32 14](https://user-images.githubusercontent.com/45676906/114645824-1b21b500-9d15-11eb-8eb5-7e01272542ed.png)

위와 같이 선택을 한 후에 로드 밸런서 타켓 그룹을 하나 더 만들었습니다. 그리고 Auto-Scaling을 통해서 만들어진 인스턴스를 등록할 것이기 때문에 대상 등록은 지금 하지 않고 생성하겠습니다.

<br>

## `로드 밸런서에 타겟 그룹 등록하기`

새로 만든 타겟 그룹을 로드 밸런서에 등록을 해야 합니다. 

![스크린샷 2021-04-14 오후 1 35 02](https://user-images.githubusercontent.com/45676906/114655098-760fd800-9d26-11eb-8b4b-3c464b7fe4f2.png)

![스크린샷 2021-04-14 오후 1 37 40](https://user-images.githubusercontent.com/45676906/114655158-a0619580-9d26-11eb-9fb6-89cef1ee3376.png)

![스크린샷 2021-04-14 오후 1 38 22](https://user-images.githubusercontent.com/45676906/114655260-d141ca80-9d26-11eb-9ace-e1771f37a48b.png)

위와 같이 새로운 타겟그룹을 리스너에 추가한 후에 업데이트를 누르겠습니다. 

<br>

## `Auto-Scaling 그룹 만들기`

![스크린샷 2021-04-14 오전 11 34 36](https://user-images.githubusercontent.com/45676906/114646034-7a7fc500-9d15-11eb-843b-42b9c430c26a.png)

`시작 템플릿` 만드는 과정은 [이전 글](https://devlog-wjdrbs96.tistory.com/303) 을 참고하시면 됩니다.

![스크린샷 2021-04-14 오후 1 41 20](https://user-images.githubusercontent.com/45676906/114655460-34336180-9d27-11eb-86c1-08e8305abd99.png)

![스크린샷 2021-04-14 오전 11 39 41](https://user-images.githubusercontent.com/45676906/114646401-22958e00-9d16-11eb-84c1-3c0bde7b928f.png)

나머지 설정은 다 저번 글에서 만든 Auto-Scaling 그룹 설정과 동일하게 하고 만들겠습니다. 그리고 잠시 기다리면 EC2 인스턴스 2개가 생성될 것입니다.

![스크린샷 2021-04-14 오후 1 44 43](https://user-images.githubusercontent.com/45676906/114655698-b15ed680-9d27-11eb-9a23-e82b871db378.png)

그리고 `로드밸런서의 타겟 그룹 탭`에 들어가서 위에서 만든 타겟 그룹을 들어가보면 위와 같이 `Healthy`한 상태인 것을 볼 수 있습니다. (즉, 잘 연결이 되었습니다.)


<br> <br>

## `CodeDeploy 설정하기`

이제 `Blue/Green` 방식으로 배포하기 위해서 설정하겠습니다. 

 


