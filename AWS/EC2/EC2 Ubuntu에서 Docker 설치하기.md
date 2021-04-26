# `EC2 Ubuntu에서 Docker 설치하는 법`

```
sudo apt-get update
sudo apt install docker.io
docker --version (설치확인)
```

<br>

## `EC2 Ubuntu에서 Docker-Compose 설치`

```
sudo curl \
    -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" \
    -o /usr/local/bin/docker-compose
```
```
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version
```