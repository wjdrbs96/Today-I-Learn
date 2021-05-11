# `ECR 이란 무엇일까?`

> Amazon Elastic Container Registry (Amazon ECR)는 안전하고 확장 가능하고 신뢰할 수 있는 AWS 관리형 컨테이너 이미지 레지스트리 서비스입니다. 
> Amazon ECR는 AWS IAM를 사용하여 리소스 기반 권한으로 프라이빗 컨테이너 이미지 리포지토리를 지원합니다. 이렇게 하면 지정된 사용자 또는 Amazon EC2 인스턴스가 컨테이너 리포지토리 및 이미지에 액세스할 수 있습니다. 
> 선호하는 CLI를 사용하여 도커 이미지, Open Container Initiative(OCI) 이미지 및 OCI 호환 아티팩트를 푸시, 풀 및 관리할 수 있습니다.

AWS 공식문서에서 ECR은 위와 같이 정의하고 있습니다. 간단하게 요약하면..? `Private Docker Hub`와 비슷하다고 생각이 듭니다. 

<br>

## `ECR 요금`

> Amazon ECR 신규 고객은 프라이빗 리포지토리에 대해 AWS 프리 티어의 일부로서 1년 동안 월 500MB의 스토리지를 받습니다.
> 신규 또는 기존의 Amazon ECR 고객은 퍼블릭 리포지토리에 대해 월 50GB의 상시 무료 스토리지를 제공합니다. 매월 퍼블릭 리포지토리에서 (AWS 계정을 사용하지 않고) 익명으로 500GB의 데이터를 인터넷으로 전송할 수 있습니다. 
> AWS 계정에 가입하거나 기존 AWS 계정으로 ECR에 인증하는 경우에는 매달 무료로 5TB의 데이터를 퍼블릭 리포지토리에서 인터넷으로 전송할 수 있으며, 모든 AWS 리전의 퍼블릭 리포지토리에서부터 AWS 컴퓨팅 리소스로 데이터를 전송하는 경우에 무제한 대역폭을 무료로 얻을 수 있습니다.
> 무료 사용량은 모든 리전에서 매월 계산되어 청구서에 자동으로 적용됩니다. 무료 사용량은 누적되지 않습니다.

어느 정도 용량의 제한이 있지만 그 용량만 넘어가지 않으면 프리티어 기간 동안은 무료로 사용할 수 있는 거 같습니다. 자세한 내용은 [여기](https://aws.amazon.com/ko/ecr/pricing/) 를 참고해주세요. 

<br>


## `ECR IAM 권한 추가`

![스크린샷 2021-05-03 오후 5 15 18](https://user-images.githubusercontent.com/45676906/116854865-5556e600-ac33-11eb-8ffc-0c277c53c602.png)

ECR 서비스를 이용하려면 `IAM` 사용자에게 ECR 접근 권한을 주어야 합니다. 그래서 IAM 사용자에게 `AmazonEC2ContainerRegistryFullAccess`의 권한을 주겠습니다.

<br>

## `ECR Repository 생성하기`

![스크린샷 2021-05-06 오전 10 13 52](https://user-images.githubusercontent.com/45676906/117230682-90862e80-ae58-11eb-8e08-ea2049d0c3c5.png)

AWS에서 ECR 서비스에 들어가서 `Repository 생성`을 누르겠습니다. 그러면 위의 화면 같이 선택을 한 후에 나머지 설명은 `Default`로 놓고 레포지토리를 만들겠습니다. 

<br>

![스크린샷 2021-05-06 오전 10 20 05](https://user-images.githubusercontent.com/45676906/117231110-80bb1a00-ae59-11eb-8a42-8570a329eb13.png)

그리고 오른쪽 위를 보면 `레포지토리이름 푸시 명령`이라는 것이 있습니다. 이것을 누르면 위와 같은 화면을 볼 수 있는데요. 이것을 통해서 `Docker Image`를 ECR에다 push 할 수 있습니다. 

`EC2`에서 위의 명령을 통해서 `ECR`에 push 하는 간단한 실습을 해보겠습니다. 

<br>

## `EC2 Docker 설치`

```
sudo yum update -y
sudo amazon-linux-extras install -y docker
sudo service docker start
```


참고로 저는 `AWS EC2 Linux2` 버전을 사용하고 있습니다. 설치와 관련해서 자세히 내용은 [여기](https://docs.aws.amazon.com/ko_kr/AmazonECR/latest/userguide/getting-started-cli.html) 를 참고하시면 됩니다. 

그리고 위의 `푸시 명령`에 있는 것들을 하나씩 EC2에서 입력하면 됩니다. 

![스크린샷 2021-05-06 오전 11 05 29](https://user-images.githubusercontent.com/45676906/117231907-24f19080-ae5b-11eb-9af0-465a9d4b5d46.png)

첫 번째 명령을 그대로 복사해서 EC2에 입력하면 위와 같이 `로그인 성공`을 볼 수 있습니다. 

<br>

![스크린샷 2021-05-06 오전 11 08 51](https://user-images.githubusercontent.com/45676906/117232169-9c272480-ae5b-11eb-8d56-d00080c98201.png)

그리고 `Dockerfile`이 위치해있는 곳에서 `두 번째 명령`을 치겠습니다. 지금은 이미지를 만들기 위함이기 때문에 `Dockerfile`은 어떤 것이든 상관 없습니다. 예제의 Dockerfile이 필요하다면 [여기](https://docs.aws.amazon.com/ko_kr/AmazonECR/latest/userguide/getting-started-cli.html) 를 참고하면 좋을 거 같습니다. 

<br>

```
docker tag gyunny:latest {number}.dkr.ecr.ap-northeast-2.amazonaws.com/gyunny:latest
docker push {number}.dkr.ecr.ap-northeast-2.amazonaws.com/gyunny:latest
```

그리고 3, 4번째에 존재하는 명령어도 복사해도 그대로 EC2에 입력하겠습니다. 그러면 이미지의 태그가 정해지고 해당 이미지가 `AWS ECR`로 push가 됩니다. 

![스크린샷 2021-05-06 오전 10 22 44](https://user-images.githubusercontent.com/45676906/117232439-15bf1280-ae5c-11eb-9920-9fcedb9b9acb.png)

그러면 위와 같이 `ECR`에 Docker Image가 push된 것을 볼 수 있습니다. ECR에 존재하는 이미지를 pull 받는 것도 push -> pull로 명령어 하나만 바꿔주면 됩니다. 

<br>

```
docker pull {number}.dkr.ecr.ap-northeast-2.amazonaws.com/gyunny:latest
```

<img width="878" alt="스크린샷 2021-05-06 오전 11 18 33" src="https://user-images.githubusercontent.com/45676906/117232940-2754ea00-ae5d-11eb-96d3-609907d614ce.png">

그러면 위와 같이 Image가 잘 다운되는 것도 확인할 수 있습니다.