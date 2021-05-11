# `AWS Lambda로 Thumbnail 자동 생성하기`

특정 앱 또는 웹을 사용하다 보면 아래와 같이 `썸네일 이미지`를 본 적이 있을 것입니다. 아래와 같은 작은 화면을 제공하는 이미지가 리스트로 나열되는데 고품질의 이미지 파일이라던지 큰 사이즈의 파일이 제공된다면 로딩 속도에서 차이가 발생할 것입니다.

<img width="274" alt="스크린샷 2021-05-11 오후 1 09 01" src="https://user-images.githubusercontent.com/45676906/117756947-0f64d800-b25a-11eb-949e-0b5e48a5a008.png">

그래서 원본 이미지를 썸네일 이미지로 제작해서 사용합니다. 이 때 썸네일 이미지를 직접 제작해서 업로드 하고 사용할 수도 있지만, 매 번 그럴 수는 없기 때문에 이를 자동화해서 사용하는 것이 좋습니다. 이러한 자동화를 아래와 같은 Flow로 진행해보려 합니다.

<br>

![스크린샷 2021-05-10 오후 2 51 53](https://user-images.githubusercontent.com/45676906/117611908-432ef780-b19f-11eb-8d5b-08e2f4c7c0e9.png)

위의 그림 처럼 원본 이미지를 S3에 업로드하면 `Lambda`에 Trigger를 걸고, `Labmda`가 S3에 썸네일 이미지를 만들어서(`이미지 리사이징`) 업로드 하는 식입니다.

바로 한번 어떻게 `Lambda를 통해서 썸네일 이미지를 만드는지`에 대해서 알아보겠습니다.

<br>

## `IAM 역할 생성`

![스크린샷 2021-05-10 오후 2 54 05](https://user-images.githubusercontent.com/45676906/117612134-aa4cac00-b19f-11eb-9601-126549f4f7ae.png)

![스크린샷 2021-05-10 오후 3 00 06](https://user-images.githubusercontent.com/45676906/117612627-7de55f80-b1a0-11eb-8215-e4abc6ff1697.png)

![스크린샷 2021-05-10 오후 3 00 15](https://user-images.githubusercontent.com/45676906/117612655-889ff480-b1a0-11eb-9d7f-d29e1f09d38c.png)

`S3FullAccess`와 `lambdaFullAccess`를 체크하고 아래의 것도 같이 체크하겠습니다.

<br>

![스크린샷 2021-05-11 오전 10 58 40](https://user-images.githubusercontent.com/45676906/117746986-56e26880-b248-11eb-957e-7612ee80d629.png)

처음엔 안만들었는데 `Lambda`가 CloudWatch에 Log를 쓰기 위해서는 위의 권한이 필요합니다. 그래서 총 3개의 권한을 역할에 추가해야합니다.

<br>

![스크린샷 2021-05-10 오후 3 02 12](https://user-images.githubusercontent.com/45676906/117612798-c43abe80-b1a0-11eb-9b92-e6c1a06d38f0.png)

그리고 이름을 적은 후에 역할을 만들겠습니다. (역할을 만들 때는 2개의 권한이었고, 나중에 CloudWatch에 Log를 쓰기 위해서 위의 권한이 필요하다는 것을 알고 다시 권한을 추가해서 총 3개의 권한을 가지고 있습니다.)


<br>

## `Lambda 생성하기`

![스크린샷 2021-05-10 오후 3 08 32](https://user-images.githubusercontent.com/45676906/117613327-b20d5000-b1a1-11eb-949f-627c7c3cd161.png)

![스크린샷 2021-05-10 오후 3 08 39](https://user-images.githubusercontent.com/45676906/117613392-ccdfc480-b1a1-11eb-811b-8654b2475bc7.png)

<br>

## `S3 Bucket 2개 생성`

![스크린샷 2021-05-10 오후 3 16 29](https://user-images.githubusercontent.com/45676906/117614103-c7cf4500-b1a2-11eb-9fb2-20f9b7c88f65.png)

![스크린샷 2021-05-10 오후 3 17 40](https://user-images.githubusercontent.com/45676906/117614194-ee8d7b80-b1a2-11eb-82ac-c2605d19b2dc.png)

![스크린샷 2021-05-10 오후 3 22 27](https://user-images.githubusercontent.com/45676906/117614766-c2262f00-b1a3-11eb-8611-cab052424add.png)

![스크린샷 2021-05-10 오후 3 23 30](https://user-images.githubusercontent.com/45676906/117614898-f1d53700-b1a3-11eb-96f2-5fa2dbcde85f.png)

![스크린샷 2021-05-10 오후 3 23 23](https://user-images.githubusercontent.com/45676906/117614859-df5afd80-b1a3-11eb-9c8a-c6d89a8c2474.png)

![스크린샷 2021-05-10 오후 3 28 17](https://user-images.githubusercontent.com/45676906/117615264-6dcf7f00-b1a4-11eb-8fd7-e498212d20e4.png)

<br>

## `S3에 사진 업로드 하기`

![스크린샷 2021-05-10 오후 3 32 31](https://user-images.githubusercontent.com/45676906/117615673-01a14b00-b1a5-11eb-870a-3830dfd426f6.png)

위와 같이 S3에 사진 업로드를 하면 `CloudWatch`에도 log가 출력되는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-05-11 오전 11 05 50](https://user-images.githubusercontent.com/45676906/117747309-e720ad80-b248-11eb-9616-3f7c33f8bec2.png)

그리고 `Python`으로 `Image Resize` 하는 코드를 작성해보겠습니다.([소스코드](https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/with-s3-tutorial.html))

<br>

![스크린샷 2021-05-11 오전 11 22 14](https://user-images.githubusercontent.com/45676906/117748545-27812b00-b24b-11eb-8521-f876282190e7.png)


```py
import boto3
import os
import sys
import uuid
from urllib.parse import unquote_plus
from PIL import Image
import PIL.Image

s3_client = boto3.client('s3')

def resize_image(image_path, resized_path):
  with Image.open(image_path) as image:
      image.thumbnail(tuple(x / 2 for x in image.size))
      image.save(resized_path)

def lambda_handler(event, context):
  for record in event['Records']:
      bucket = record['s3']['bucket']['name']
      key = unquote_plus(record['s3']['object']['key'])
      tmpkey = key.replace('/', '')
      download_path = '/tmp/{}{}'.format(uuid.uuid4(), tmpkey)
      upload_path = '/tmp/resized-{}'.format(tmpkey)
      s3_client.download_file(bucket, key, download_path)
      resize_image(download_path, upload_path)
      s3_client.upload_file(upload_path, '{}-resized'.format(bucket), key)
```

그리고 한번 더 사진 업로드를 해보겠습니다. 그러면 아래와 같은 오류를 만날 수 있는데요.

<br>

![스크린샷 2021-05-11 오전 11 08 04](https://user-images.githubusercontent.com/45676906/117747472-34048400-b249-11eb-8148-e95e09cb202d.png)

에러를 보면 `PIL` module을 찾을 수 없다고 나옵니다. 그래서 해당 모듈을 Docker를 이용해서 설치해보겠습니다.

<br>

## `PIL Module 설치`

```
docker run --name lambda-img -it lambci/lambda:build-python3.8 bash
```

![스크린샷 2021-05-10 오후 4 04 20](https://user-images.githubusercontent.com/45676906/117618877-868e6380-b1a9-11eb-8c01-f2ef337b1bd9.png)

```
mkdir -p opt/python
pip install pillow -t opt/python
pip install boto3 -t opt/python
cd opt
ls
exit
```

![스크린샷 2021-05-10 오후 4 07 08](https://user-images.githubusercontent.com/45676906/117619139-d9681b00-b1a9-11eb-99bc-43e55e52a680.png)

```
docker container cp lambda-img:/var/task/opt/python ./dockerfiledown
```

위와 같이 Docker를 사용해서 필요한 모듈을 설치하고 해당 컨테이너의 디렉토리를 로컬로 복사하겠습니다. 그리고 복사한 디렉토리를 `zip`으로 압축하겠습니다.

<br>

## `Lambda 계층 만들기`

![스크린샷 2021-05-10 오후 4 24 15](https://user-images.githubusercontent.com/45676906/117621147-3fee3880-b1ac-11eb-9800-5aef3c07f434.png)

![스크린샷 2021-05-10 오후 4 25 57](https://user-images.githubusercontent.com/45676906/117621329-77f57b80-b1ac-11eb-9068-17a76d2713f0.png)


그리고 `Lambda`에서 `계층 생성`을 누르고 zip 파일을 위와 같이 업로드하겠습니다.

<br>

![스크린샷 2021-05-11 오전 11 17 04](https://user-images.githubusercontent.com/45676906/117748184-85f9d980-b24a-11eb-9e01-da624e1718d8.png)

그러면 위와 같이 계층이 잘 생성된 것을 확인할 수 있습니다.

<br>

![스크린샷 2021-05-11 오전 11 18 53](https://user-images.githubusercontent.com/45676906/117748339-bfcae000-b24a-11eb-9399-5c7c097ebc0d.png)

그리고 Lambda 함수 아래에 보면 `Add Layer`라고 있는데 그것을 누르고 `Layer 추가`를 위와 같이 하겠습니다.

<br>

![스크린샷 2021-05-11 오전 11 20 45](https://user-images.githubusercontent.com/45676906/117748456-06203f00-b24b-11eb-93f5-d7e9bd58aa83.png)

![스크린샷 2021-05-11 오전 11 24 13](https://user-images.githubusercontent.com/45676906/117748714-8181f080-b24b-11eb-8e12-da30c7cf5aa4.png)

그러면 위와 같이 계층이 잘 추가된 것을 확인할 수 있습니다.

<br>

## `S3 파일 업로드 리사이징 확인`

모든 세팅이 끝났기 때문에 다시 S3 파일 업로드를 했을 때 람다 함수가 작동해서 S3 버킷에 리사이징 된 파일이 잘 저장되는지 확인해보겠습니다.

![스크린샷 2021-05-11 오전 11 58 16](https://user-images.githubusercontent.com/45676906/117751552-40400f80-b250-11eb-8289-5c1d7153ea2b.png)

위와 같이 `429.4KB` 크기의 사진 업로드를 하였습니다.

<br>

![스크린샷 2021-05-11 오전 11 59 33](https://user-images.githubusercontent.com/45676906/117751642-6cf42700-b250-11eb-9ae8-3e34f336bf50.png)

그러면 위와 같이 이미지 사이즈가 `129.6KB`로 줄어든 것을 확인할 수 있습니다.

<br>

## `에러 정리`

```
[ERROR] ClientError: An error occurred (404) when calling the HeadObject operation: Not Found Traceback (most recent call last):  
```

중간에 진행하다가 위와 같은 에러를 만났는데요. 원인은.. 버킷 이름이 잘못되어 있었습니다! 그래서 혹시나 진행하다가 위와 같은 에러를 만난다면 버킷 이름이 잘 되어 있는지 확인해보시면 될 것 같습니다!