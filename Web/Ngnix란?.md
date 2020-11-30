# `들어가기 전에`

웹 서버와 WAS에 대해 들어보았을 것이다. `WAS의 대표적인 예는 Tomcat`이 있고, `웹 서버의 대표적인 예는 아파치, Nginx`가 있다. 
두 개의 차이가 무엇인지는 지금 정리하지는 않을 것이고, 이렇게 예시가 있다는 정도만 알아두면 될 것 같다. 

![ELB](https://miro.medium.com/max/1000/1*ycw_PI8686Y45ElrBBa8XA.png)

일반적으로 EC2 한대를 이용해서 서버를 운영하면 위와 같이 클라이언트가 직접 서버 애플리케이션에 요청을 보내는 아키텍쳐일 것이다. 

<br>

그런데 만약 동시접속자가 매우 많아진다면 서버가 하나이기 때문에 매우 과부하가 걸려서 느려지거나 여러가지가 문제가 발생할 수 있다.
이럴 때 사용할 수 있는 것이 `Nginx`가 있는데 이번 글에서는 `Nginx`가 무엇인지 알아보려 한다. 

<br>

# `Nginx(웹 서버)란 무엇인가?`

![Nginx](http://i.imgur.com/Zpw9D7x.png)

위와 같이 실제 서버 애플리케이션에 직접 접근하는 것이 아니라 중간에 `리버스 프록시`의 역할을 한다. 정리하면 아래와 같다. 

<br>

- ### `정적 파일을 처리하는 HTTP 서버로서의 역할`
    - 웹서버의 역할은 HTML, CSS, Javascript, 이미지와 같은 정보를 웹 브라우저(Chrome, Iexplore, Opera, Firefox 등)에 전송하는 역할을 한다. (HTTP 프로토콜을 준수)
      
- ### `응용프로그램 서버에 요청을 보내는 리버스 프록시로서의 역할`
    ![reverse](http://i.imgur.com/yReDKjj.png)
    - `리버스 프록시`란 클라이언트가 프록시 서버(가짜 서버)로 요청을 하면 프록시 서버가 실제 서버 애플리케이션으로부터 데이터를 가져오는 역할을 한다. 
    - 여기서 `프록시 서버가 Nginx`이고, `리버스 서버는 응용 프로그램 서버`이다.
    - 굳이 이렇게 중간에 `리버스 프록시(Nginx)`를 두는 이유는 요청(Request)에 대한 버퍼링이 있기 때문이다. 여러 클라이언트가 직접 애플리케이션 서버에 직접 요청하는 경우, 누군가는 응답 대기상태에 있을 것이다. 이럴 때 `프록시 서버`를 둠으로써 요청을 `배분`하는 것이다.
    - 프록시 서버를 사용하면 클라이언트가 실제 서버의 정보를 알지 못한다는 보안에 장점이 있다. `NodeJS Express에서 사용하는 실제 포트를 Nginx 80포트 Proxy 서버로 숨김으로써 보안을 강화할 수 있게 된다.`
    
<br>

## `Nginx 동작 방식`

![driven](http://i.imgur.com/W6JATVH.png)

- ### Nginx는 `비동기 처리 방식(Event-driven)`을 사용하고 있다.  

<br>

## `AWS에 Nginx 설치하는 법`

```
설치: sudo apt-get install nginx 
제거: sudo apt-get remote nginx
버전확인: nginx -v 
```

![스크린샷 2020-12-01 오전 12 56 12](https://user-images.githubusercontent.com/45676906/100632288-02b98b00-3370-11eb-87f9-883fda27dda6.png)

<br>

그리고 `cd /etc/nginx`로 가보면 아래와 같이 여러 디렉토리, 파일이 있을 것이다. 

<img width="663" alt="스크린샷 2020-12-01 오전 3 01 13" src="https://user-images.githubusercontent.com/45676906/100646526-7a43e600-3381-11eb-88ae-f23d80123ce0.png">

여기서 `cd sites-available`을 들어가보자. 그러면 default 라는 파일이 존재할 것인데 `sudo vi default`를 통해서 열어보자.

<img width="665" alt="스크린샷 2020-12-01 오전 3 02 46" src="https://user-images.githubusercontent.com/45676906/100646737-c3943580-3381-11eb-9062-bdef6cb58654.png">

그리고 위와 같이 `server_name`에는 본인이 사용하고 있는 EC2의 ip 주소 또는 localhost 등등 사용하고자 하는 것을 입력하면 된다. 
`proxy_pass`에는 `Nginx`가 요청을 보낼 서버의 ip주소와 포트번호를 적으면 된다.

<br>

![스크린샷 2020-12-01 오전 3 04 51](https://user-images.githubusercontent.com/45676906/100646913-0c4bee80-3382-11eb-8c6f-f6ff2170dce9.png)

그리고 위와 같이 ip 주소를 접근하게 되면 `Nginx`가 위에서 지정했던 `proxy_pass`로 요청을 보내서 위와 같은 화면이 뜨는 것을 볼 수 있다.
