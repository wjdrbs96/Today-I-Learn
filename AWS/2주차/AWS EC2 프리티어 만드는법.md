# `AWS EC2 프리티어 만드는 법`

![스크린샷 2021-03-05 오후 2 14 28](https://user-images.githubusercontent.com/45676906/110070572-a1b1b100-7dbd-11eb-85c9-a7785a6a1c8a.png)

AWS 계정을 로그인 화면 위와 같은 화면을 볼 수 있습니다. 

![스크린샷 2021-03-05 오후 2 17 28](https://user-images.githubusercontent.com/45676906/110070665-ce65c880-7dbd-11eb-8a71-888f32cc839a.png)

그리고 여기서 `인스턴스 시작` 버튼을 누르겠습니다. 

![스크린샷 2021-03-05 오후 2 20 20](https://user-images.githubusercontent.com/45676906/110070745-fa814980-7dbd-11eb-84e7-b87bd57a243f.png)

위의 보이는 `Ubuntu`를 선택하겠습니다. 

![스크린샷 2021-03-05 오후 2 21 48](https://user-images.githubusercontent.com/45676906/110070875-4af8a700-7dbe-11eb-845f-860b9aa7e25f.png)

위와 같이 기본으로 선택에서 있는 것을 선택하고 `보안 그룹`으로 들어가겠습니다. 

![스크린샷 2021-03-05 오후 2 23 55](https://user-images.githubusercontent.com/45676906/110070953-77acbe80-7dbe-11eb-9af3-93e2237810a5.png)

`EC2`의 `port`를 열어주기 위해서 `보안 그룹`의 설정을 합니다. (`규칙 추가`를 누르겠습니다.)

![스크린샷 2021-03-05 오후 2 27 35](https://user-images.githubusercontent.com/45676906/110071334-323cc100-7dbf-11eb-9777-5b59011f01a0.png)

저는 `HTTP`, `HTTPS`, `Spring`, `NodeJS`, `MySQL`을 주로 사용할 예정이라서 위와 같이 포트번호를 열겠습니다. 그리고 `검토 및 시작`을 누르겠습니다. 

![스크린샷 2021-03-05 오후 2 33 28](https://user-images.githubusercontent.com/45676906/110071674-cc9d0480-7dbf-11eb-9081-387762266c33.png)

그리고 `시작하기`를 누르겠습니다. 

![스크린샷 2021-03-05 오후 2 36 20](https://user-images.githubusercontent.com/45676906/110071951-5056f100-7dc0-11eb-9aa9-48fe6fc72b50.png)

그러면 위와 같은 화면을 볼 수 있습니다. (EC2에 접속하기 위해서는 `pem`키가 필요하기 때문에 pem 파일의 이름을 설정한 후에 `다운로드 하고 잃어버리지 않게 잘 보관해야 합니다! !`)
`또한 보안이 취약한 곳에 올리면 안됩니다! 따라서 잘 보관하기..`

그리고 `키 페어를 다운로드` 했으면 `인스턴스 시작` 버튼이 활성화가 됩니다. `인스턴스 시작`을 누르겠습니다.

![스크린샷 2021-03-05 오후 2 41 55](https://user-images.githubusercontent.com/45676906/110072359-fd316e00-7dc0-11eb-9d08-3a9702a2015d.png)

다음에도 `인스턴스 시작`을 누르겠습니다.

![스크린샷 2021-03-05 오후 2 43 21](https://user-images.githubusercontent.com/45676906/110072509-3ec21900-7dc1-11eb-8357-71e72b832218.png)

그러면 위와 같이 `인스턴스`가 정상적으로 만들어진 것을 볼 수 있습니다.

<br>

## `Elastic IP`

- EC2 서버를 껐다 키게 되면 Public IP가 변경되기 때문에 `고정 IP`를 할당하겠습니다. 
- `탄력적 IP가 혼자 돌아가면 과금이 되기 때문에 서버 중지/정지/삭제 할 경우 EIP 먼저 relase 하고 인스턴스 중단해야 합니다.`

![스크린샷 2021-03-05 오후 2 52 51](https://user-images.githubusercontent.com/45676906/110073316-872e0680-7dc2-11eb-8894-7272d3db9d2d.png)

위의 `탄력적 IP`로 들어가겠습니다.

![스크린샷 2021-03-05 오후 2 53 46](https://user-images.githubusercontent.com/45676906/110073382-a462d500-7dc2-11eb-8acd-f6d1edf8752b.png)

그리고 `탄력적 IP 생성`을 누르겠습니다. 

![스크린샷 2021-03-05 오후 2 55 52](https://user-images.githubusercontent.com/45676906/110073575-f0157e80-7dc2-11eb-9cb8-8362d4ae241e.png)
 
`할당`을 누르겠습니다. 

![스크린샷 2021-03-05 오후 2 57 46](https://user-images.githubusercontent.com/45676906/110073725-35d24700-7dc3-11eb-9285-6360ed67c415.png)

![스크린샷 2021-03-05 오후 2 58 44](https://user-images.githubusercontent.com/45676906/110073788-5bf7e700-7dc3-11eb-9ba2-758e7ca7f03c.png)

![스크린샷 2021-03-05 오후 2 59 59](https://user-images.githubusercontent.com/45676906/110073866-82b61d80-7dc3-11eb-91d4-be90e27b0365.png)

이렇게 `탄력적 IP`가 할당되었습니다. 

![스크린샷 2021-03-05 오후 3 03 16](https://user-images.githubusercontent.com/45676906/110074115-03751980-7dc4-11eb-8df8-9a2fd2adc09d.png)

<br>

## `SSH로 EC2 접속하기`

```
1. AWS에서 발급 받은 pem 파일이 있는 위치로 이동합니다. 

2. chmod 400 파일명.pem

3. sudo ssh -i '파일명.pem' ubuntu@아이피주소
ex) sudo ssh -i 'AWS.pem' ubuntu@52.79.90.119
```

![title](https://user-images.githubusercontent.com/45676906/93416524-ecf40800-f8e0-11ea-8764-eba8cbaaafd2.png)

그러면 위와 같이 `EC2`에 접속할 수 있습니다.

<br>

## `EC2 접속 커스텀 하기`

매번 위와 같이 입력을 한 후에 접속하기는 매우 번거롭기 때문에 커스텀을 해보겠습니다. (해당 작업은 로컬 터미널에서 진행해야 합니다.)

```
cd ~/.ssh/
그리고 ls로 확인해보면 config 파일이 존재할 것입니다.

vi config 

Host [ssh 접속을 할때 IP 대신 사용할 호스트명]
  HostName [EC2의 EIP 주소]
  User ec2-user
  IdentityFile ~/.ssh/{pem 키 이름}   // 본인 pem 파일이 있는 경로를 설정해야 합니다.

예를들면 아래와 같이 수정하면 된다. 

Host sopt
  HostName 52.79.90.119
  User ubuntu
  IdentityFile ~/Desktop/AWS/SOPT_Server_AWS.pem       

그리고 여기서 Host를 sopt로 했기 때문에 이제 ssh sopt로 접속이 가능합니다.
```
