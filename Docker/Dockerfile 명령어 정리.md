# `Dockerfile 명령어 정리하기`

- ### `FROM`
    - 생성할 이미지의 베이스가 될 이미지를 뜻합니다. FROM 명령어는 Dockerfile을 작성할 때 반드시 한 번 이상 입력해야 합니다.
   
- ### `MAINTAINER`
    - 이미지를 생성한 개발자 정보를 나타냅니다. 
    
- ### `RUN`
    - 이미지를 만들기 위해 컨테이너 내부에서 명령어를 실행합니다.
    - RUN 명령어에 ["/bin/bash", "echo hello" >> test.html"] 같이 입력하면 /bin/bash 셸을 이용해 'echo hello >> test2.html'을 실행한다는 뜻입니다. 
    
- ### `ADD`
    - 파일을 이미지에 추가합니다.   
    - Dockerfile이 위치한 디렉터리에서 파일을 가져옵니다. 
    - ex) ADD test.html /var/www/html 이라면 Dockerfile과 같은 디렉토리에 있는 test.html 파일을 이미지 내부의 /var/www/html에 추가한다는 뜻입니다.
    
- ### `WORKDIR`
    - 명령어를 실행할 디렉터리를 나타냅니다. 배시 쉘에서 cd 명령어를 입력하는 것과 같은 기능을 합니다. 즉, 컨테이너 내부에서 작동을 합니다. 
    
- ### `EXPOSE`
    - Dockerfile의 빌드로 생성된 이미지에서 노출할 포트를 설정합니다. 그러나 EXPOSE를 설정한 이미지로 컨테이너를 생성했다고 해서 반드시 이 포트가 호스트의 포트와 바인딩되는 것은 아니며, 단지 컨테이너의 80번 포트를 사용할 것임을 나타내는 것뿐입니다. 
    
- ### `CMD`
    - CMD는 컨테이너가 시작될 때마다 실행할 명령어를 설정하며, Dockerfile에서 한 번만 사용할 수 있습니다. 
    
- ### `ADD, COPY`
    - ADD와 COPY는 큰 차이점이 없는 것처럼 보입니다. 하지만 차이점이 존재합니다. 
    - COPY는 로컬 디렉터리에서 읽어 들인 컨텍스트로부터 이미지에 파일을 복사하는 역할을 합니다.
    - ADD는 외부 URL 및 tar 파일에서도 파일을 추가할 수 있다는 점이 다릅니다. 
    - ADD를 사용하는 것은 그다지 권장하지 않습니다. 
    
- ### `ENTRYPOINT, CMD`
    - ENTRYPOINT, CMD는 역할 자체는 비슷하지만 서로 다른 역할을 담당하는 명령어입니다. 
    - ![스크린샷 2021-04-06 오전 11 14 19](https://user-images.githubusercontent.com/45676906/113649633-bdb0b700-96c9-11eb-938a-5f75c0ab0bbe.png)
    - 위의는 entrypoint를 사용하지 않은 경우입니다. 이 때는 /bin/bash에 접속한 것을 볼 수 있습니다. 
    - <img width="792" alt="스크린샷 2021-04-06 오전 11 15 48" src="https://user-images.githubusercontent.com/45676906/113649640-c0aba780-96c9-11eb-8d3c-90900e9355bc.png">
    - 이번에는 entrypoint를 사용하였습니다. 이 때는 /bin/bash를 출력한 것을 볼 수 있습니다. 
    - 즉 둘 다 컨테이너가 시작될 때 수행할 명령을 지정한다는 점에서 같지만, entrypoint는 커맨드를 인자로 받아 사용할 수 있는 스크립트의 역할을 할 수 있다는 점에서 다릅니다. 
    
<br>

## `Dockerfile 예시`

```dockerfile
FROM ubuntu:14.04
MAINTAINER wjdrbs96
RUN apt-get update
RUN apt-get install apache2 -y
ADD test.html /var/www/html
WORKDIR /var/www/html
RUN ["/bin/bash", "-c", "echo hello >> test2.html"]
EXPOSE 80
CMD apachectl -DFOREGROUND
```