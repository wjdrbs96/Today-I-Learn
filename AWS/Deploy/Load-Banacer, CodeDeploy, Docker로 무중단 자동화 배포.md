# `Load Balancer, CodeDeploy, Docker로 무중단 자동화 배포하는 법`

현재 진행하고 있는 프로젝트에서 `무중단 자동화 배포`를 진행하고 있는데요. 그 중에서 배포 부분에 대해서 어떻게 해결했는지를 정리해보겠습니다.

<br>

<img width="1094" alt="스크린샷 2021-06-12 오후 3 36 18" src="https://user-images.githubusercontent.com/45676906/121767582-f5d5e980-cb93-11eb-89c6-7b255c8609b7.png">

프로젝트 중 일부 아키텍쳐의 그림은 위와 같습니다. 사용한 도구를 정리하면 아래와 같은데요. 

- `AWS EC2, S3`
- `Docker, Jenkins`
- `AWS Load-Balancer, CodeDeploy`

<br>

