## `Kubernetes 실습하기`

```yaml
version: "3"

services:
  wordpress:
    image: wordpress:5.9.1-php8.1-apache
    environment:
      WORDPRESS_DB_HOST: mysql
      WORDPRESS_DB_NAME: wordpress
      WORDPRESS_DB_USER: root
      WORDPRESS_DB_PASSWORD: password
    ports:
      - "30000:80"

  mysql:
    image: mariadb:10.7
    environment:
      MYSQL_DATABASE: wordpress
      MYSQL_ROOT_PASSWORD: password
```

<img width="935" alt="스크린샷 2022-08-15 오후 11 23 25" src="https://user-images.githubusercontent.com/45676906/184653692-803ce152-7a95-4d9c-99ef-e8343f657049.png">

docker-compose를 사용해서 `WordPress-MySQL`을 띄워서 접속할 수 있습니다.

<br>

## `Kubernetes 배포`

<img width="838" alt="스크린샷 2022-08-15 오후 11 24 05" src="https://user-images.githubusercontent.com/45676906/184653797-b4fc6a58-e9a8-4c5f-b2e3-f70a4968d655.png">

이번에는 `docker-compose`가 아니라 `Kubernetes`를 사용해서 배포를 진행해보겠습니다.

<br>

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: wordpress-mysql
  labels:
    app: wordpress
spec:
  selector:
    matchLabels:
      app: wordpress
      tier: mysql
  template:
    metadata:
      labels:
        app: wordpress
        tier: mysql
    spec:
      containers:
        - image: mariadb:10.7
          name: mysql
          env:
            - name: MYSQL_DATABASE
              value: wordpress
            - name: MYSQL_ROOT_PASSWORD
              value: password
          ports:
            - containerPort: 3306
              name: mysql

---
apiVersion: v1
kind: Service
metadata:
  name: wordpress-mysql
  labels:
    app: wordpress
spec:
  ports:
    - port: 3306
  selector:
    app: wordpress
    tier: mysql

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: wordpress
  labels:
    app: wordpress
spec:
  selector:
    matchLabels:
      app: wordpress
      tier: frontend
  template:
    metadata:
      labels:
        app: wordpress
        tier: frontend
    spec:
      containers:
        - image: wordpress:5.9.1-php8.1-apache
          name: wordpress
          env:
            - name: WORDPRESS_DB_HOST
              value: wordpress-mysql
            - name: WORDPRESS_DB_NAME
              value: wordpress
            - name: WORDPRESS_DB_USER
              value: root
            - name: WORDPRESS_DB_PASSWORD
              value: password
          ports:
            - containerPort: 80
              name: wordpress

---
apiVersion: v1
kind: Service
metadata:
  name: wordpress
  labels:
    app: wordpress
spec:
  type: NodePort
  ports:
    - port: 80
  selector:
    app: wordpress
    tier: frontend
```

위의 yml을 배포하겠습니다.

```
kubectl apply -f wordpress-k8s.yml
kubectl get all
```

<br>

![스크린샷 2022-08-15 오후 11 27 33](https://user-images.githubusercontent.com/45676906/184654402-57b1a94b-4d18-4f92-9473-1a2267fb4ab5.png)

```
minikube ip
```

위의 명령어를 통해서 IP를 알아낼 수 있습니다.

<br>

### `Kubernetes Pod 삭제했을 때`

<img width="873" alt="스크린샷 2022-08-15 오후 11 31 04" src="https://user-images.githubusercontent.com/45676906/184654977-07c1083b-2893-4d86-8095-2ba67950e2bf.png">

위에 실행되어 있는 `Kubernetes Pod` 하나를 삭제해보겠습니다.

<br>

![스크린샷 2022-08-15 오후 11 33 28](https://user-images.githubusercontent.com/45676906/184655435-f9ba9272-27eb-45bf-87fd-9df9c8028111.png)

```
kubectl delete PodName
``` 

하지만 위처럼 삭제해도 `Kubernetes`에서 자동으로 Pod를 새로 생성합니다.

<br>

![스크린샷 2022-08-15 오후 11 36 08](https://user-images.githubusercontent.com/45676906/184655864-d512c406-c61b-44f9-b456-6eefc6d1f9fe.png)

`Replicas`를 2로 변경하겠습니다.

<br>

<img width="896" alt="스크린샷 2022-08-15 오후 11 37 22" src="https://user-images.githubusercontent.com/45676906/184656060-4e671ee6-0f7f-47d9-a551-d4bafcf236b3.png">

그러면 손쉽게 WordPress Pod 하나가 더 생긴 것을 볼 수 있습니다. 

이거를 `Docker`로 구현한다면 쉽지 않을 수 있습니다. `Docker Container` 2개를 띄운 후에 앞에 프록시 역할을 하는 컨테이너 하나 더 띄워서 작업을 해야 하는데, `Kubernetes`를 사용하면 손쉽게 로드밸런서 역할을 할 수 있게 여러 개의 파드를 띄우는 것이 가능합니다.

<br>

## `Reference`

- [https://subicura.com/k8s/guide/#%E1%84%8B%E1%85%AF%E1%84%83%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%89%E1%85%B3-%E1%84%87%E1%85%A2%E1%84%91%E1%85%A9](https://subicura.com/k8s/guide/#%E1%84%8B%E1%85%AF%E1%84%83%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%89%E1%85%B3-%E1%84%87%E1%85%A2%E1%84%91%E1%85%A9)