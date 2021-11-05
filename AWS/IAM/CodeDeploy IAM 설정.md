# `자동화 배포에 필요한 IAM 설정`

![스크린샷 2021-05-07 오전 8 56 28](https://user-images.githubusercontent.com/45676906/117379360-43ff2980-af12-11eb-8520-19ed29cd2418.png)

처음에 `IAM` 탭에 들어가서 `사용자`를 하나 만들겠습니다. 그리고 `프로그래밍 방식 엑세스`를 체크하면 `엑세스 키`, `비밀 엑세스 키`를 발급 받아 나중에 사용하게 됩니다. 

<br>

![스크린샷 2021-05-07 오전 9 00 27](https://user-images.githubusercontent.com/45676906/117379672-ec14f280-af12-11eb-8c60-c381238a533c.png)

![스크린샷 2021-05-07 오전 9 04 38](https://user-images.githubusercontent.com/45676906/117379874-6cd3ee80-af13-11eb-830c-617618536884.png)

그리고 `사용자`에게 `S3 접근 권한`, `CodeDeploy 접근 권한`을 추가해야 합니다. (자동 배포할 때 S3, CodeDeploy를 사용할 것이기 때문입니다.)

<br>

![스크린샷 2021-05-07 오전 9 06 20](https://user-images.githubusercontent.com/45676906/117379976-aad11280-af13-11eb-90ac-676d47cecea4.png)

그리고 `다음`을 누르다 보면 위와 같은 화면을 볼 수 있습니다. `엑세스 키 ID`, `비밀 엑세스 키`는 위의 화면에서만 볼 수 있기 때문에 꼭 `.csv 다운로드`를 해서 로컬에 저장하고 있어야 합니다. 

<br>

## `EC2 역할 만들기`

EC2가 CodeDeploy를 연동받을 수 있게 EC2에게 IAM 역할 하나를 만들겠습니다. 

![스크린샷 2021-05-07 오전 9 14 10](https://user-images.githubusercontent.com/45676906/117380479-b83acc80-af14-11eb-87ab-c8439ff583ac.png)

EC2를 체크하고 다음을 누르겠습니다. 

<br>

![스크린샷 2021-05-07 오전 9 16 44](https://user-images.githubusercontent.com/45676906/117380593-0354df80-af15-11eb-93c9-837e061245ba.png)

`AmazonEC2RoleAWSCodeDeploy`를 체크하고 다음을 누르겠습니다. 

<br>

![스크린샷 2021-05-07 오전 9 19 06](https://user-images.githubusercontent.com/45676906/117380723-562e9700-af15-11eb-99bc-bf5dbbacfcf2.png)

원하는 이름을 적고 `정책`이 올바르게 생성되었는지 확인한 후에 생성 버튼을 누르겠습니다. 

<br>

## `EC2 IAM 역할 변경`

![스크린샷 2021-05-07 오전 9 20 12](https://user-images.githubusercontent.com/45676906/117380800-883ff900-af15-11eb-89fe-39a23f7fb30a.png)

그리고 위에서 만든 역할을 EC2에 적용해보겠습니다. 

<br>

![스크린샷 2021-05-07 오전 9 20 27](https://user-images.githubusercontent.com/45676906/117380973-f5ec2500-af15-11eb-9a7b-7d7150feadff.png)

<br>

## `EC2 태그 설정`

![스크린샷 2021-05-07 오전 9 24 30](https://user-images.githubusercontent.com/45676906/117381059-1ddb8880-af16-11eb-84f4-0078381c28b6.png)

![스크린샷 2021-05-07 오전 9 24 48](https://user-images.githubusercontent.com/45676906/117381115-43689200-af16-11eb-88cf-8a24121ca1d3.png)

기억할 수 있는 이름의 `키, 값`을 설정하고 저장을 누르겠습니다. (이것은 CodeDeploy로 배포할 때 사용되기 때문에 기억하기 쉬운 이름으로 하는 것이 좋습니다.)

<br>

## `CodeDeploy를 위한 권한 생성`

![스크린샷 2021-05-07 오전 9 28 07](https://user-images.githubusercontent.com/45676906/117381245-980c0d00-af16-11eb-8d38-99eddc13118f.png)

![스크린샷 2021-05-07 오전 9 28 12](https://user-images.githubusercontent.com/45676906/117381271-ad813700-af16-11eb-8935-02723752877d.png)

`IAM 역할`을 하나 만들고 거기에서 `CodeDeploy`를 체크하겠습니다. 

<br>

![스크린샷 2021-05-07 오전 9 30 05](https://user-images.githubusercontent.com/45676906/117381377-e7523d80-af16-11eb-81e9-89e03dc97ae7.png)

![스크린샷 2021-05-07 오전 9 30 25](https://user-images.githubusercontent.com/45676906/117381411-f802b380-af16-11eb-94f1-224096f1efb8.png)

위와 같이 이름을 정하고 계속 다음을 눌러서 `CodeDeploy Role`을 만들겠습니다. 
