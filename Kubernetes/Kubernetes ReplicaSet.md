## `Kubernetes ReplicaSet`

> Pod을 단독으로 만들면 Pod에 어떤 문제(서버가 죽어서 Pod이 사라졌다던가)가 생겼을 때 자동으로 복구되지 않습니다. 이러한 Pod을 정해진 수만큼 복제하고 관리하는 것이 ReplicaSet입니다.

<br>

### `ReplicaSet 만들기`

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: echo-rs
spec:
  replicas: 1
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
# ReplicaSet 생성
kubectl apply -f echo-rs.yml

# 리소스 확인
kubectl get po,rs
```

![스크린샷 2022-08-17 오후 11 33 25](https://user-images.githubusercontent.com/45676906/185162456-fc9fbf8e-a734-4dd9-b492-08ac2362e26d.png)

ReplicaSet과 Pod이 같이 생성된 것을 볼 수 있습니다.

<br>

```
kubectl get pod --show-labels
```

![스크린샷 2022-08-17 오후 11 39 53](https://user-images.githubusercontent.com/45676906/185166274-55821bee-fd84-4e6c-bee1-0a4f2cce6192.png)

실행되고 있는 Pod의 Label을 확인할 수 있습니다.

<br>

```
kubectl label pod/echo-rs-tcdwj app-
```

![스크린샷 2022-08-17 오후 11 42 24](https://user-images.githubusercontent.com/45676906/185167822-9cea2704-6364-460a-b328-0a8245740562.png)

위처럼 Pod의 Label을 제거 했더니 쿠버네티스가 새로운 파드를 하나 더 띄운 것을 확인할 수 있습니다.

<br>

![스크린샷 2022-08-17 오후 11 44 50](https://user-images.githubusercontent.com/45676906/185169074-797d1a23-062a-4df5-b9c8-7a2dd8c5c479.png)

이번에는 다시 Pod의 Label을 동일하게 설정했더니 ReplicaSet이 1로 설정 되어 있어서 파드 하나가 Terminating 되고 있는 것을 확인할 수 있습니다.

<br>

<img width="890" alt="스크린샷 2022-08-17 오후 11 46 26" src="https://user-images.githubusercontent.com/45676906/185169596-7681837a-3bf4-42c8-b9b9-b4ba1b39daad.png">

1. ReplicaSet Controller는 ReplicaSet조건을 감시하면서 현재 상태와 원하는 상태가 다른 것을 체크
2. ReplicaSet Controller가 원하는 상태가 되도록 Pod을 생성하거나 제거
3. Scheduler는 API서버를 감시하면서 할당되지 않은unassigned Pod이 있는지 체크
4. Scheduler는 할당되지 않은 새로운 Pod을 감지하고 적절한 노드node에 배치
5. 이후 노드는 기존대로 동작

<br>

ReplicaSet은 ReplicaSet Controller가 관리하고 Pod의 할당은 여전히 Scheduler가 관리합니다. 각자 맡은 역할을 충실히 수행하는 모습이 보기 좋습니다.

<br>

### `스케일 아웃`

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: echo-rs
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

<br>

## `마무리`

ReplicaSet은 원하는 개수의 Pod을 유지하는 역할을 담당합니다. label을 이용하여 Pod을 체크하기 때문에 label이 겹치지 않게 신경써서 정의해야 합니다.

실전에서 ReplicaSet을 단독으로 쓰는 경우는 거의 없습니다. 바로 다음에서 배울 Deployment가 ReplicaSet을 이용하고 주로 Deployment를 사용합니다.

<br>

## `Reference`

- [https://subicura.com/k8s/guide/replicaset.html#replicaset-%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5](https://subicura.com/k8s/guide/replicaset.html#replicaset-%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5)