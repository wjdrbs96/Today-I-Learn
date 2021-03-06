# `IAM 유저 및 MFA 생성하는 법`

![스크린샷 2021-03-08 오후 1 46 58](https://user-images.githubusercontent.com/45676906/110275662-d7040c00-8014-11eb-83ad-42ab009c8b86.png)

AWS 홈페이지를 들어간 후에 IAM 접속을 하겠습니다. 

![스크린샷 2021-03-08 오후 1 49 02](https://user-images.githubusercontent.com/45676906/110275795-1af71100-8015-11eb-969e-5b7697d9f3af.png)

`사용자 추가` 버튼을 누르겠습니다. 

![스크린샷 2021-03-08 오후 1 49 59](https://user-images.githubusercontent.com/45676906/110275860-3d892a00-8015-11eb-898e-1dabe65b89d7.png)

`사용자 이름`에 원하는 이름을 정한 후에 `Console 액세스` 방식을 체크하겠습니다. 

![스크린샷 2021-03-08 오후 1 51 41](https://user-images.githubusercontent.com/45676906/110275985-8640e300-8015-11eb-961b-ba0dba972dac.png)

위와 같이 체크를 한 후에 `원하는 비밀번호`를 설정해줍니다. (나중에 이것으로 로그인 할 것입니다.)

![test](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/2_%EA%B7%B8%EB%A3%B9%EC%83%9D%EC%84%B1.png)

권한 설정에서 그룹을 생성할 수 있습니다. 그룹에 권한을 설정하고 유저를 추가하면, 일괄적으로 관리할 수 있어 편리합니다. 유저에게서 권한을 제거하고 싶을 때는 그룹에서 사용자를 제거하면 됩니다. [그룹 생성]을 클릭합니다.

![test1](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/2_%EA%B7%B8%EB%A3%B9%EC%83%9D%EC%84%B12.png)

![test2](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/2_%EC%82%AC%EC%9A%A9%EC%9E%90%EC%B6%94%EA%B0%80.png)

계속 `다음`을 누르겠습니다. 

![Test](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/2_%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A7%8C%EB%93%A4%EA%B8%B0.png)

![cvc](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/1-2-8.png)

이제 `사용자 추가`가 되었습니다. 밑줄 친 위치에는 사용자의 콘솔 액세스 주소를 알려줍니다. 해당 주소를 통해 IAM 유저로 로그인 할 수 있습니다. 사용자의 액세스 키(액세스 키 ID와 보안 액세스 키)를 보려면 보고 싶은 각 암호와 액세스 키 옆에 있는 표시를 선택합니다. 액세스 키를 저장하려면 [.csv 다운로드]를 선택한 후 안전한 위치에 파일을 저장합니다.

<br>
                    
### 중요함! ! 

```
보안 액세스 키는 이 때만 확인 및 다운로드가 가능하기 때문에 사용자에게 AWS API를 사용하도록 하려면 이 정보를 제공해야 합니다. 사용자의 새 액세스 키 ID와 보안 액세스 키를 안전한 장소에 보관해야 합니다! <br>
이 단계가 지난 후에는 보안 키에 다시 액세스할 수 없습니다.
```

<br>

## `루트 계정에 MFA 활성화 하기`

AWS Multi-Factor Authentication(MFA)은 사용자 이름과 암호 외에 보안을 한층 더 강화할 수 있는 간단하며 효과적인 수단입니다. 
AWS 계정 및 해당 계정에 속하는 개별 IAM 사용자에 대해 AWS MFA를 활성화할 수 있습니다. 루트 계정에는 MFA를 구성하여 AWS 리소스를 보호하는 것이 좋습니다. 사용자가 AWS 웹사이트나 서비스에 액세스할 때 승인된 인증 디바이스 또는 SMS(문자 서비스) 문자 메시지의 고유한 인증 코드를 입력해야 하기 때문에 MFA는 보안을 더 강화합니다.

MFA는 보안 토큰 기반과 SMS 문자 메시지 기반 방식이 있으나, AWS는 2019년 2월 1일부터 SMS 기반 MFA 지원을 중단합니다. 이 글에서는 보안 토큰 기반의 가상 MFA 어플리케이션(Google Authenticator)을 사용하겠습니다

![tt](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/1-5-1.png)

![dd](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/1-5-2.png)

![dds](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/1-5-3.png)

![dddd](https://tech.cloud.nongshim.co.kr/wp-content/uploads/2018/10/1-5-4.png)

그리고 `구글 OTP 앱`을 다운한 후에 QR 코드 스캔 해서 MFA 코드를 얻은 후에 입력하면 됩니다. 

그러면 이제 앞으로 루트 계정으로 로그인 할 때 마다 MFA 코드가 필요하게 됩니다. 

