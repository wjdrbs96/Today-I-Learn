## `Kubernetes Deployment`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: echo-deploy
spec:
  replicas: 4
  selector:
    matchLabels:
      app: echo
      tier: app
  template:
    metadata:
      labels:
        app: echo
        tier: app
    spec:
      containers:
        - name: echo
          image: ghcr.io/subicura/echo:v1
```

```
kubectl apply -f echo-deployment.yml
```

![스크린샷 2022-08-22 오후 11 20 11](https://user-images.githubusercontent.com/45676906/185944318-3a8693e5-3aea-4d20-9c5c-9881bba38593.png)

replicas를 4로 설정해서 pod 4개가 실행된 것을 확인할 수 있습니다. 

<br>

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: echo-deploy
spec:
  replicas: 4
  selector:
    matchLabels:
      app: echo
      tier: app
  template:
    metadata:
      labels:
        app: echo
        tier: app
    spec:
      containers:
        - name: echo
          image: ghcr.io/subicura/echo:v2
```

이번에는 image 마지막에 태그를 v1 -> v2로만 바꾼 후에 다시 실행해보겠습니다.

```
kubectl apply -f echo-deployment.yml
```

![스크린샷 2022-08-22 오후 11 23 33](https://user-images.githubusercontent.com/45676906/185944991-69cb91dc-6b63-4a07-a41a-42d93c42d50a.png)

이번에는 replicaset은 2개가 되었고 pod는 4개로 유지하고 있습니다.

<br>

![image](https://user-images.githubusercontent.com/45676906/185945188-02ac86b7-53a5-42ab-909f-52b2828d291d.png)

Deployment는 새로운 이미지로 업데이트하기 위해 ReplicaSet을 이용합니다. 버전을 업데이트하면 새로운 ReplicaSet을 생성하고 해당 ReplicaSet이 새로운 버전의 Pod을 생성합니다.

새로운 ReplicaSet을 0 -> 1개로 조정하고 정상적으로 Pod이 동작하면 기존 ReplicaSet을 4 -> 3개로 조정합니다.

<br>

![image](https://user-images.githubusercontent.com/45676906/185945351-dfc1be01-e093-46ff-9a4c-42596d8ab3c6.png)

새로운 ReplicaSet을 1 -> 2개로 조정하고 정상적으로 Pod이 동작하면 기존 ReplicaSet을 3 -> 2개로 조정합니다.

<br>

![image](https://user-images.githubusercontent.com/45676906/185945429-937dfac7-282c-4e95-a4a2-30cbbc147103.png)

<br>

```
kubectl describe deploy/echo-deploy
```

![스크린샷 2022-08-22 오후 11 26 43](https://user-images.githubusercontent.com/45676906/185945735-8ceb52f4-50b5-414a-9b35-e94280504567.png)

<br>

