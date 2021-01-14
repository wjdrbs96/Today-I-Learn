# `Nginx를 이용해서 프록시 서버 만들기`

Nginx에 대해 자세히 알지 모른다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Web/Ngnix%EB%9E%80%3F.md) 에서 먼저 읽고 오시면 됩니다. 

어느정도 안다는 가정하게 Nginx로 Proxy_Server를 만드는 방법에 대해서 알아보겠습니다.

![server](https://user-images.githubusercontent.com/43840561/104095511-8c973500-52da-11eb-9cea-e8e77deb35f5.png)

위와 같은 구조로 앞에 `프록시 서버`를 두고 실제 서버를 감추는 아키텍쳐를 사용할때 Nginx를 이용합니다. 

바로 어떻게 Nginx로 프록시 서버를 구성하는지 알아보겠습니다. 

![스크린샷 2021-01-14 오후 2 55 52](https://user-images.githubusercontent.com/45676906/104550693-ba80ce80-5678-11eb-91c6-f6a31821b316.png)

AWS EC2에 접속한 후에 `cd /etc/nginx`로 들어간 후 `ls -l`을 쳐보면 위와 같은 디렉토리 구성을 볼 수 있습니다. 
위에 내용들을 정리하자면 아래와 같습니다. 

- ### `etc/nginx`
    - 해당 디렉토리는 Nginx를 설정하는 디렉토리(모든 설정을 이 디렉토리 안에서 함)
- ### `/etc/nginc/nginx.conf`
    - Nginx의 메인 설정 파일
    - Nginx의 글로벌 설정을 수정 할 수 있음
- ### `/etc/nginx/sites-available`
    - 프록시 설정 및 어떻게 요청을 처리해야 할지에 대해 설정
- ### `/etc/nginx/sites-enabled`
    - sites-available 디렉토리에서 연결된 파일들이 존재하는 곳
    - Nginx가 프록시 설정 적용함
- ### `/etc/nginx/snippets`
    - sites-available 디렉토리에 있는 파일들에 공통적으로 파홈될 수 있는 설정들을 정의할 수 있는 디렉토리
    

`/etc/nginx` 아래에 있는 디렉토리, 파일들을 요약하면 위와 같습니다. 

![스크린샷 2021-01-14 오후 3 01 17](https://user-images.githubusercontent.com/45676906/104551057-793cee80-5679-11eb-9447-ad369a26b799.png)

`sites-available` 디렉토리로 이동한 후에, 파일을 하나 만들어야합니다. 이름은 원하는 것으로 정해주면 되고 저는 `node-server` 라는 이름으로 하겠습니다.

![스크린샷 2021-01-14 오후 3 05 32](https://user-images.githubusercontent.com/45676906/104551281-f36d7300-5679-11eb-8e79-a65cd3bf7741.png)

위와 같이 설정을 해주면 됩니다. `{{base_url}}:80/`을 서버로 노출시키고, `{{base_url}}:3000/`을 프록시 서버로 감추겠다는 뜻입니다. 

![스크린샷 2021-01-14 오후 3 06 41](https://user-images.githubusercontent.com/45676906/104551373-1c8e0380-567a-11eb-84ed-1e92796e0e1c.png)

```
sudo ln -s /etc/nginx/sites-available/node-server /etc/nginx/sites-enabled/
```

그리고 `sites-available`과 `sites-enabled`를 연결해줘야 합니다. 

![스크린샷 2021-01-14 오후 3 09 44](https://user-images.githubusercontent.com/45676906/104551668-ac33b200-567a-11eb-9fb6-3b6ed99db727.png)

```
cd /etc/nginx/sites-enabled/  (디렉토리 이동)
la -alF (파일 링크 확인)
```

명령어를 통해서 확인해보면 위와 같이 링크가 걸린 것을 확인할 수 있습니다.

```
cd /etc/nginx/sites-available 
sudo vi default
``` 

그리고 마지막 설정만 해주면 됩니다. 그래서 기본으로 존재하는 `default` 파일을 열어보겠습니다.

<img width="786" alt="스크린샷 2021-01-14 오후 3 19 37" src="https://user-images.githubusercontent.com/45676906/104552363-0a14c980-567c-11eb-9707-ebcf163db27d.png">

파일을 열고 위와 같이 똑같이 적어주면 됩니다. 그리고 `try_files $uro $uri/ =404;`는 #을 통해서 `꼭 주석 처리를 해주어야 합니다.`

```
sudo nginx -t   (문법 체크)
sudo service nginx restart (Nginx 재시작)
```

![스크린샷 2021-01-14 오후 3 25 35](https://user-images.githubusercontent.com/45676906/104552840-e1410400-567c-11eb-8d8f-5ea5a2d04ed0.png)

`52.79.90.119`로 접속했는데 `52.79.90.119:3000`과 똑같은 결과가 나오는 것을 볼 수 있습니다. 

![스크린샷 2021-01-14 오후 3 25 46](https://user-images.githubusercontent.com/45676906/104552961-1f3e2800-567d-11eb-85c8-be3542b55c6d.png)

`52.79.90.119/home`으로 접속해도 `52.79.90.119:3000/home`과 같은 결과를 얻는 것도 볼 수 있습니다. 

이렇게 Nginx를 이용해서 프록시 서버를 만드는 방법에 대해서 알아보았습니다. 