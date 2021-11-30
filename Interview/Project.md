# `Project`

## WebSocket 이 무엇인지 설명해주세요. 

<br>

## Chat System 어떻게 구현했는지 설명해주세요.

<br>

## Redis 를 사용한 이유에 대해서 설명해주세요.

<br>

## CI/CD 무중단 배포 어떻게 구현했는지 설명해주세요. 

1. Github develop branch push 
2. Github 에서 Jenkins 로 WebHook 을 날린 후에 Jenkins 가 Project Build 
3. Build 결과물 (jar, appspec.yml, script 등등) 파일들을 AWS S3 에 zip 형태로 올림
4. EC2 에서 zip 파일 가져와서 압축 해제 후에 Shell Script 대로 실행하기 

<br>

## JWT 가 무엇인가요? 

JWT는 `JSON WEB TOKEN`의 줄임말입니다. 내부는 `HEADER`, `PAYLOAD`, `SIGNATURE`를 담고 있습니다. 

> JWT는 두 개체에서 JSON 객체를 사용하여 가볍고 자가수용적인 (self-contained) 방식으로 정보를 안전성 있게 전달해줍니다.

- HEADER : 토큰의 타입과 해시 암호화 알고리즘으로 구성
- PAYLOAD : 직접 넣은 값이 존재
- SIGNATURE : Secret Key 를 포함하여 암호화 되어 있습니다.

<br>



<br>

## Session 도 있는 JWT 를 사용한 이유가 무엇인가요? 

<br>

## Oauth 가 무엇인가요? 

<br>

## JWT Token 이 탈취 당하면 어떻게 하실건가요? 

<br>

## 비밀번호 암호화 과정에 대해서 설명해주세요. 