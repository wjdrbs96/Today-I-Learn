# `Docker Redis Tip`

- Docker-Compose 작성해서 멀티 컨테이너 사이에 접속할 때 컨테이너 이름으로 접속 가능하다.

예를들어 Spring 컨테이너, Redis 컨테이너 띄웠을 때 Redis 컨테이너의 이름을 Redis로 하면 Spring 서버에서 host 이름을 Redis 로 하면 됨