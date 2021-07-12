# `Github Action, CodeDeploy로 CI/CD 하는 법 - 1편`

이번 글에서는 CI 도구로 Github Action, CD 도구로는 CodeDeploy를 사용해서 자동화 배포를 진행해보겠습니다.(EC2, S3, CodeDeploy 생성에 대해서는 다루지 않겠습니다.) 진행하고자 하는 아키텍쳐는 아래와 같습니다. 

<img width="617" alt="스크린샷 2021-07-12 오후 1 44 58" src="https://user-images.githubusercontent.com/45676906/125232329-5aad6c80-e317-11eb-945c-7ec58c011a80.png">

- Github에 push를 하면 Github Action이 자동으로 실행됩니다.(즉, 프로젝트를 빌드 해서 jar 파일을 생성합니다.)

- 이어서 jar와 Shell Script 파일을 압축해서 S3에 업로드 합니다.

- EC2에 설치 되어 있는 CodeDeploy Agent가 S3에 업로드 된 Zip 파일을 EC2로 가져와서 배포를 진행합니다.

- 작성한 Shell Script 파일을 통해서 jar를 실행시켜 자동화 배포가 진행되는 것입니다. 

<br>

간단하게 Spring Boot Gradle 기반으로 프로젝트가 존재한다고 가정하고 글을 시작하겠습니다. 

<br> <br>

## `Github Actions 설정하기`

참고로 Github Actions은 public Repository에서 무료로 제공합니다. Github Action을 어떻게 세팅하는지 알아보겠습니다.

<img width="881" alt="스크린샷 2021-07-12 오후 1 52 59" src="https://user-images.githubusercontent.com/45676906/125232987-b0364900-e318-11eb-9036-4b805d652dcd.png">

그리고 사용할 Repository를 들어가서 위와 같이 `Actions` -> `set up workflow yourself`를 누르겠습니다.

<br> 

![스크린샷 2021-07-12 오후 1 57 30](https://user-images.githubusercontent.com/45676906/125233387-64d06a80-e319-11eb-8e67-96ad2631e87d.png)

그러면 위와 같이 자동으로 `yml` 파일을 어떤식으로 작성해야 하는지 예시로 만들어주는 것을 볼 수 있습니다.

<br>

```yaml
name: gyunny-action

on:
  push:
    branches:
      - master
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Set up JDK 11
        uses: actions/setup-java@v1
        with:
          java-version: 11

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew
        shell: bash

      - name: Build with Gradle
        run: ./gradlew build
        shell: bash
```

위에 yml을 위의 사진에서 yml 코드를 넣는 곳에 바꿔서 넣은 후에 `Start Commit`을 누르겠습니다. 그리고 위의 yml 파일 의미가 어떤 것인지 먼저 알아보겠습니다.

- ### name
    - workflow의 이름을 지정합니다.
    
- ### on
    - 해당 workflow가 언제 실행될건지 트리거를 지정할 수 있습니다. 
    - 저는 master 브랜치가 변경사항이 생기면 자동으로 빌드 되도록 설정해놓았습니다. 
    - `workflow_dispatch`는 수동으로 해당 workflow를 실행시키겠다는 의미입니다. 
    
- ### job, steps
    - workflow는 하나 혹은 그 이상의 job을 가질 수 있고 각 job은 여러 step에 따라 단계를 나눌 수 있습니다.

- ### run-on
    - 해당 workflow를 어떤 OS 환경에서 실행할 것인지 지정할 수 있습니다.
  
<br>


<br>

![스크린샷 2021-07-12 오후 2 17 02](https://user-images.githubusercontent.com/45676906/125234520-e1fcdf00-e31b-11eb-9064-8425cea4519e.png)

그러면 위와 같이 `.github/workflows`가 자동으로 생긴 것을 볼 수 있습니다. 그리고 들어가보면 방금 생성한 yml 파일도 존재하는 것을 볼 수 있습니다. 그리고 Actions를 들어가서 다시 확인해보면 아래와 같이 빌드가 성공된 것도 확인할 수 있습니다.

<img width="659" alt="스크린샷 2021-07-12 오후 2 35 27" src="https://user-images.githubusercontent.com/45676906/125236158-75371400-e31e-11eb-8d25-4029f19c9399.png">

이제 CI 세팅은 끝났습니다. 다음에 Github Actions을 통해서 CI 된 빌드 파일을 압축해서 S3로 업로드 하는 과정에 대해서 알아보겠습니다.

<br> <br>

## `S3에 빌드한 jar 업로드 하기`

S3 버킷을 생성하는 방법에 대해서는 다루지 않겠습니다. 버킷 생성하는 과정이 궁금하다면 [여기](https://wbluke.tistory.com/39) 를 참고하시면 좋을 거 같습니다.

위에서 작성한 yml 파일에 좀 더 추가를 해야 하는데요. 어떤 것을 추가해야 하는지 좀 더 알아보겠습니다.

```yaml
name: gyunny-action

on:
  push:
    branches:
      - master
  workflow_dispatch:
    
env:
  S3_BUCKET_NAME: aws-gyun-s3

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Set up JDK 11
        uses: actions/setup-java@v1
        with:
          java-version: 11

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew
        shell: bash

      - name: Build with Gradle
        run: ./gradlew build
        shell: bash
        
      # 새로 추가 된 부분 
      - name: Make zip file
        run: zip -r ./$GITHUB_SHA.zip .
        shell: bash

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ secrets.AWS_REGION }}

      - name: Upload to S3
        run: aws s3 cp --region ap-northeast-2 ./$GITHUB_SHA.zip s3://$S3_BUCKET_NAME/$GITHUB_SHA.zip
```

위와 같이 yml을 수정하겠습니다. 추가된 코드의 의미는 아래와 같습니다.

- ### env
  - 현재 스크립트에서 사용할 환경변수를 정의하여 사용할 수 있습니다. 
  - 여러 곳에서 공통으로 사용되는 문자열이나, 명확하게 의미를 부여해야 하는 키워드에 사용하면 좋습니다.
  
- ### $GITHUB_SHA
  - Github Actions에서 제공하는 여러 기본 환경변수 중 하나입니다. 현재 workflow를 실행시키는 커밋의 해쉬값입니다.
  
- ### $ {{ secrets.이름 }} 
  - IAM 엑세스 키, 비밀 키 같은 것들을 노출시킬 수 없으니까 따로 등록해놓고 참조해서 가져오는 식으로 사용할 수 있습니다. 
  
- ### aws s3 cp
  - aws cli 명령어 중 하나입니다. copy 명령어로 현재 위치의 파일을 S3로 업로드 하거나, 반대로 S3 파일을 현재 위치로 다운로드 할 수 있습니다.
   
<br> <br>

## `Secret Key 등록하기`

<img width="993" alt="스크린샷 2021-07-12 오후 3 06 13" src="https://user-images.githubusercontent.com/45676906/125238824-c47f4380-e322-11eb-8279-9745153992b7.png">

<img width="954" alt="스크린샷 2021-07-12 오후 3 09 59" src="https://user-images.githubusercontent.com/45676906/125239186-440d1280-e323-11eb-9d5e-52577ab6e49c.png">

여기서 `New Repository secret`을 누르겠습니다. 

<br>

<img width="981" alt="스크린샷 2021-07-12 오후 3 11 43" src="https://user-images.githubusercontent.com/45676906/125239429-9c441480-e323-11eb-8395-5fb2c9de0f00.png">

위와 같이 `IAM 엑세스 키`, `IAM 비밀 엑세스 키`, `AWS Region` 3가지 정보를 등록하겠습니다. 

```
- AWS_ACCESS_KEY:ID: 엑세스 키 ID
- AWS_SECRET_ACCESS_KEY: 비밀 엑세스 키 
- AWS_REGION: ap-northeast-2
```

<img width="1510" alt="스크린샷 2021-07-12 오후 3 15 37" src="https://user-images.githubusercontent.com/45676906/125239740-107eb800-e324-11eb-82df-84b9bcf87289.png">

즉, 위와 같이 3가지의 값을 등록하였습니다. 이렇게 등록을 해놓으면 yml 파일에서 위에서 등록한 키 값들을 읽어서 사용할 수 있게 됩니다. 

이렇게 yml을 수정한 후에 Github에 push를 해보겠습니다. 

![스크린샷 2021-07-12 오후 3 24 45](https://user-images.githubusercontent.com/45676906/125240660-5a1bd280-e325-11eb-9da8-88ecd2a9beb1.png)

그러면 yml 파일에 정의된 단계대로 하나씩 수행이 되어 성공한 것을 볼 수 있습니다. 그리고 S3에 잘 업로드가 되었는지 확인을 해보겠습니다. 

<br>

![스크린샷 2021-07-12 오후 3 26 50](https://user-images.githubusercontent.com/45676906/125240864-a49d4f00-e325-11eb-9aaf-34960384650c.png)

위처럼 S3에 zip 파일이 잘 업로드 된 것을 확인할 수 있습니다. 

<br>
