# `AWS CodeCommit 실습`

![스크린샷 2021-03-19 오전 9 57 43](https://user-images.githubusercontent.com/45676906/111716559-9f5d5580-8899-11eb-9ec6-c40c6996a388.png)

AWS에서 위와 같이 `IAM 대시보드`에 들어갑니다. 

![스크린샷 2021-03-19 오전 10 00 43](https://user-images.githubusercontent.com/45676906/111716741-04b14680-889a-11eb-9c44-e29b13d80fb3.png)

저는 기존에 만들어놨던 `AWS_Gyun 사용자`를 사용하겠습니다. 이 사용자에게 `CodeCommit` 접근 권한을 주겠습니다. 

<img width="1792" alt="스크린샷 2021-03-19 오전 10 02 29" src="https://user-images.githubusercontent.com/45676906/111716865-43df9780-889a-11eb-87c7-0047dd2f3841.png">

위와 같이 `권한 추가` 버튼을 누르겠습니다. 

<img width="1792" alt="스크린샷 2021-03-19 오전 10 10 13" src="https://user-images.githubusercontent.com/45676906/111717586-a7b69000-889b-11eb-8560-0b872df7368b.png">

위와 같이 `AWSCodeCommitPowerUser` 권한을 주기 위해서 체크한 후에 `다음`을 누르겠습니다. 이렇게 권한이 추가 되었으니 `CodeCommit`으로 가서 `Local PC`에다 clone을 받는 실습을 해보겠습니다. 

<br>

## `IAM 엑세스 키 만들기`

![스크린샷 2021-03-19 오전 10 32 22](https://user-images.githubusercontent.com/45676906/111719079-7c817000-889e-11eb-9c9c-3b7a6232e795.png)

`IAM`에서 `보안 자격 증명`을 들어간 후에 `AWS CodeCommit에 대한 HTTPS Git 자격 증명`에서 `자격 증명 생성`을 누르겠습니다. 

![스크린샷 2021-03-19 오전 10 35 00](https://user-images.githubusercontent.com/45676906/111719265-df730700-889e-11eb-888b-890fd08f7205.png)

위와 같이 `자격 증명`이 생성되는 것을 볼 수 있습나다. 이것은 나중에 Repository clone에서 꼭 필요하기 때문에 지금 `다운로드`를 해놓겠습니다. 

<br>

## `CodeCommit으로 Repository clone 받기`

![스크린샷 2021-03-19 오전 10 26 17](https://user-images.githubusercontent.com/45676906/111718606-97071980-889d-11eb-9a69-04c53f2ad229.png)

위와 같이 `CodeCommit`에 만들어놨던 레포지토리에 들어가면 위와 같은 화면을 볼 수 있습니다. 여기서 레포지토리 URL을 복사하겠습니다. 

![스크린샷 2021-03-19 오전 10 28 51](https://user-images.githubusercontent.com/45676906/111718827-fe24ce00-889d-11eb-8a5b-1db48c975945.png)

그리고 Local Terminal에서 복사한 URL을 적고 엔터를 치면 위와 같이 `유저 이름`을 적으라고 나옵니다. 여기서 위에서 발근 받은 것들을 적어주면 됩니다.
 
<img width="961" alt="스크린샷 2021-03-19 오전 10 39 11" src="https://user-images.githubusercontent.com/45676906/111719526-658f4d80-889f-11eb-9ecb-a3171051df20.png">

그러면 위와 같이 `CodeCommit`에서 만든 레포지토리가 Local PC에 clone 된 것을 볼 수 있습니다. 

> apache-php 로 하려 했으나 설치가 계속 안되서 임시로 Spring Boot를 CodeCommit에다 push 

clone 받은 폴더 안에 Spring Boot 프로젝트를 만든 후에 아래와 같은 Git 명령어를 치겠습니다.

```
git add .    ( 파일 추가 )
git status   ( 파일 상태 확인 )
git commit -m "커밋 메세지"
git push origin master (원격 저장소 master 브랜치에 push)
```

<img width="1411" alt="스크린샷 2021-03-19 오전 11 19 53" src="https://user-images.githubusercontent.com/45676906/111722366-13512b00-88a5-11eb-93e4-d96f1eb3b370.png">

그리고 `AWS CodeCommit`을 들어가서 확인해보면 로컬에서 만들었던 프로젝트가 그대로 반영된 것을 볼 수 있습니다. 

<br>

# `Reference`

- [https://docs.aws.amazon.com/ko_kr/codecommit/latest/userguide/setting-up-gc.html?icmpid=docs_acc_console_connect_np](https://docs.aws.amazon.com/ko_kr/codecommit/latest/userguide/setting-up-gc.html?icmpid=docs_acc_console_connect_np)