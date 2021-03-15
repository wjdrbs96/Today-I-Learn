# `AWS EC2 프리티어 만드는 법`

![스크린샷 2021-03-15 오후 2 24 30](https://user-images.githubusercontent.com/45676906/111107507-468c7500-859a-11eb-81b3-3a6d5d94cfd0.png)

RDS를 세팅하기 전에 보안 그룹을 만들어야 합니다. `보안 그룹`에 들어간 다음 `보안 그룹` 생성을 누르겠습니다. 

![스크린샷 2021-03-15 오후 2 28 07](https://user-images.githubusercontent.com/45676906/111107748-c4508080-859a-11eb-9724-118cbd04e33e.png)

인바운드 규칙에서 `MySQL`은 3306을 쓰기 때문에 위와 같이 설정을 해주겠습니다.

![스크린샷 2021-03-15 오후 2 30 02](https://user-images.githubusercontent.com/45676906/111107847-feba1d80-859a-11eb-8d72-6b1e27e02825.png) 

아웃바운드 규칙에서도 위와 같이 설정을 해주면 됩니다. 

![스크린샷 2021-03-15 오후 2 31 35](https://user-images.githubusercontent.com/45676906/111107938-30cb7f80-859b-11eb-8d84-4c6385388e2a.png)

이제는 RDS 세팅을 해보겠습니다.

![스크린샷 2021-03-15 오후 2 40 02](https://user-images.githubusercontent.com/45676906/111108513-60c75280-859c-11eb-85aa-aea82ac91288.png)

![스크린샷 2021-03-15 오후 2 41 02](https://user-images.githubusercontent.com/45676906/111108588-87858900-859c-11eb-9f32-eab0ccb3ca8d.png)

![스크린샷 2021-03-15 오후 2 42 10](https://user-images.githubusercontent.com/45676906/111108683-b439a080-859c-11eb-96e8-43d55539b1d8.png)

![스크린샷 2021-03-15 오후 2 44 17](https://user-images.githubusercontent.com/45676906/111108904-185c6480-859d-11eb-91eb-2e5f0f62f7ea.png)

- `마스터 사용자 이름, 비밀번호`는 꼭 기억해야 합니다!

![스크린샷 2021-03-15 오후 2 47 06](https://user-images.githubusercontent.com/45676906/111109064-5ce80000-859d-11eb-845f-f0da47be2469.png)

![스크린샷 2021-03-15 오후 2 48 42](https://user-images.githubusercontent.com/45676906/111109211-a6d0e600-859d-11eb-8666-b599ca439ab6.png)

![스크린샷 2021-03-15 오후 2 50 19](https://user-images.githubusercontent.com/45676906/111109299-d5e75780-859d-11eb-839c-bb948923052d.png)

그리고 `데이터베이스 생성` 버튼을 누르면 `RDS`가 생성이 됩니다. 

