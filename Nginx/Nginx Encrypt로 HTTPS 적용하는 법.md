# `Nginx로 HTTPS 적용하는 법`

이번엔 `AWS EC2 Linux2`에서 `Nginx, Let's Encrypt`를 사용하여 `HTTPS`를 적용하는 법에 대해서 알아보겠습니다. 

<br> <br>

## `AWS EC2 Linux2에 Nginx 설치하기`

```
sudo yum install nginx       // 설치
sudo service nginx start     // 시작
sudo service nginx stop      // 중지
sudo service nginx restart   // Nginx 서비스를 중지했다가 시작합니다.
sudo service nginx reload    // Nginx 서비스를 정상적으로 다시 시작합니다. 다시로드 할 때 기본 Nginx 프로세스는 자식 프로세스를 종료하고 새 구성을로드하며 새 자식 프로세스를 시작합니다.
sudo service nginx status    // 상태 확인
```

`Nginx`를 설치하고 `Reverse Proxy`를 `/etc/nginx/conf.d/*.conf` 형태의 파일을 만들어 설정할 수 있습니다.

<br> <br>

## `AWS EC2 Linux2에 Let's Encrypt 설치 및 설정`

```
sudo wget -r --no-parent -A 'epel-release-*.rpm' http://dl.fedoraproject.org/pub/epel/7/x86_64/Packages/e/
sudo rpm -Uvh dl.fedoraproject.org/pub/epel/7/x86_64/Packages/e/epel-release-*.rpm
sudo yum-config-manager --enable epel*
sudo yum repolist
sudo yum install -y certbot
sudo yum install certbot-nginx
sudo service nginx stop
sudo certbot --nginx
```

위의 명령어로 `Let's Encrypt`를 설치하겠습니다.


<br>

그리고 `Domain`을 구입한 후에 위의 `/etc/nginx/conf.d/*.conf 파일 안에 server_name`에 도메인 이름을 적어 준 후에 아래 명령어를 치겠습니다. 

```
sudo certbot --nginx -d 도메인이름
```

![스크린샷 2021-12-09 오후 5 55 50](https://user-images.githubusercontent.com/45676906/145366902-f44dd2cd-ec70-4df1-b6b3-e5f731d6ddeb.png)

그러면 자동으로 `/etc/nginx/conf.d/*.conf` 파일에 `#managed by Certbot`이라고 하면서 자동으로 설정들을 해줍니다.

그리고 `https://도메인`으로 들어가면 `https` 적용이 됩니다~