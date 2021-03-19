# `Codepipeline 사용하는 법`

![스크린샷 2021-03-19 오후 5 34 39](https://user-images.githubusercontent.com/45676906/111752752-7066d400-88d9-11eb-88fc-84f97b1abd29.png)

위와 같이 `파이프라인 생성` 버튼을 누르겠습니다. 

![스크린샷 2021-03-19 오후 5 35 56](https://user-images.githubusercontent.com/45676906/111752963-a6a45380-88d9-11eb-9233-e120682cdd36.png)

위와 같이 `파이프라인 이름`을 설정하고 `고급 설정`을 누르겠습니다. 

![스크린샷 2021-03-19 오후 5 37 20](https://user-images.githubusercontent.com/45676906/111753110-cf2c4d80-88d9-11eb-9554-227f64a6e0ac.png)

[고급 설정]에서는 아티팩트 저장소를 지정하는데,

- [기본 위치]를 선택할 경우 새로운 S3 버킷을 자동으로 생성해줍니다.
- [사용자 지정 위치]를 선택할 경우 미리 만들어 놓은 버킷을 선택할 수 있습니다. 

현재는 S3 버킷을 이미 만들어 놨기 때문에 [사용자 지정 위치]를 선택한 후 버킷 명을 선택하겠습니다. [암호화 키]는 [기본 AWS 관리형 키]를 선택한 후 다음으로 넘어갑니다.

![스크린샷 2021-03-19 오후 5 39 10](https://user-images.githubusercontent.com/45676906/111753314-11ee2580-88da-11eb-8120-5336cb0f3c00.png)

위와 같이 `CodeCommit`에 있는 Repository와 연결한 후에 `master` 브랜치로 선택을 하겠습니다. 

![스크린샷 2021-03-19 오후 5 40 33](https://user-images.githubusercontent.com/45676906/111753551-54176700-88da-11eb-87ae-937d340a0d9f.png)

<br>

# `Reference`

- [https://docs.aws.amazon.com/ko_kr/codepipeline/latest/userguide/tutorials-simple-codecommit.html](https://docs.aws.amazon.com/ko_kr/codepipeline/latest/userguide/tutorials-simple-codecommit.html)