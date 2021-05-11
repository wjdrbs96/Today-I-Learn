# `ECS CodeDeploy 자동화 배포`

가장 먼저 서비스 역할에서 Amazon ECS에 액세스할 수 있는 권한을 CodeDeploy에 부여하는 서비스 역할을 만들겠습니다. 

![스크린샷 2021-05-07 오전 9 51 47](https://user-images.githubusercontent.com/45676906/117382624-e8d13500-af19-11eb-800b-0865b06f9a2e.png)

![스크린샷 2021-05-07 오전 9 51 54](https://user-images.githubusercontent.com/45676906/117382657-fab2d800-af19-11eb-9429-93519d008e06.png)

위에는 `CodeDeploy`를 선택하고 아래의 `사용 사례 선택`에서는 `CodeDeploy-ECS`를 체크하고 다음을 누르겠습니다. 

<br>

![스크린샷 2021-05-07 오전 9 53 47](https://user-images.githubusercontent.com/45676906/117382753-2a61e000-af1a-11eb-8898-6c46bc7bc002.png)

![스크린샷 2021-05-07 오전 9 54 35](https://user-images.githubusercontent.com/45676906/117382817-4f565300-af1a-11eb-9dbf-7daddab416d8.png)

<br>

## `S3 버킷 생성`

![스크린샷 2021-05-07 오전 10 26 16](https://user-images.githubusercontent.com/45676906/117384788-b8d86080-af1e-11eb-83a7-680067096cf4.png)

버킷의 이름만 설정하고 나머지는 그대로 두고 S3 버킷을 만들겠습니다. 
