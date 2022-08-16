## `Kubernetes Pod 만들기`

```
kubectl run echo --image ghcr.io/subicura/echo:v1
```

```
kubectl get po
```

위의 명령어를 치면 이미지를 가진 컨테이너 하나를 띄워 줌

<br>

```
kubectl describe pod/echo
```

Pod의 상태를 상세하게 확인하는 `describe` 명령어를 사용할 수 있습니다.

<br>

<img width="1326" alt="스크린샷 2022-08-17 오전 12 35 13" src="https://user-images.githubusercontent.com/45676906/184920144-5ffbb550-7419-4b91-8037-1925cb52bdb1.png">

<br>

## `Pod 생성 분석`

<img width="300" alt="asdsad" src="https://user-images.githubusercontent.com/45676906/184920273-46abf42d-f4ac-4a33-9335-60cae229dc9e.png">

minikube 클러스터 안에 Pod가 있고, Pod 안에 컨테이너가 존재하는 상황입니다.

<br>

<img width="858" alt="스크린샷 2022-08-17 오전 12 37 34" src="https://user-images.githubusercontent.com/45676906/184920673-49131791-9f36-4b59-81d1-732365f18dee.png">

1. Scheduler는 API서버를 감시하면서 할당되지 않은 Pod이 있는지 체크
2. Scheduler는 할당되지 않은 Pod을 감지하고 적절한 node에 할당 (minikube는 단일 노드)
3. 노드에 설치된 kubelet은 자신의 노드에 할당된 Pod이 있는지 체크
4. kubelet은 Scheduler에 의해 자신에게 할당된 Pod의 정보를 확인하고 컨테이너 생성
5. kubelet은 자신에게 할당된 Pod의 상태를 API 서버에 전달

<br>

## `Pod 제거`

```
kubectl delete pod/echo
```

위의 명령어로 pod를 삭제할 수 있습니다.

<br>

## `yml로 설정 파일 작성하기`

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: echo
  labels:
    app: echo
spec:
  containers:
    - name: app
      image: ghcr.io/subicura/echo:v1
```

```
kubectl apply -f echo-pod.yml
kubectl get po
```

<img width="601" alt="스크린샷 2022-08-17 오전 12 42 12" src="https://user-images.githubusercontent.com/45676906/184921603-44e3bb7c-16fa-4498-9879-56fb0c8d8884.png">

<br>

```
# Pod 생성
kubectl apply -f echo-pod.yml

# Pod 목록 조회
kubectl get pod

# Pod 로그 확인
kubectl logs echo
kubectl logs -f echo

# Pod 컨테이너 접속
kubectl exec -it echo -- sh
# ls
# ps
# exit

# Pod 제거
kubectl delete -f echo-pod.yml
```

<br>

## `컨테이너 상태 모니터링`

![image](https://user-images.githubusercontent.com/45676906/184922155-4e542e4c-ff01-4d71-a128-e66fe02c1558.png)

> 컨테이너 생성과 실제 서비스 준비는 약간의 차이가 있습니다. 서버를 실행하면 바로 접속할 수 없고 짧게는 수초, 길게는 수분Java ㅂㄷㅂㄷ의 초기화 시간이 필요한데 실제로 접속이 가능할 때 서비스가 준비되었다고 말할 수 있습니다.

<br>

### `livenessProbe`

컨테이너가 정상적으로 동작하는지 체크하고 정상적으로 동작하지 않는다면 컨테이너를 재시작하여 문제를 해결합니다.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: echo-lp
  labels:
    app: echo
spec:
  containers:
    - name: app
      image: ghcr.io/subicura/echo:v1
      livenessProbe:
        httpGet:
          path: /not/exist
          port: 8080
        initialDelaySeconds: 5
        timeoutSeconds: 2     # Default 1
        periodSeconds: 5      # Defaults 10
        failureThreshold: 1   # Defaults 3
```

일부러 존재하지 않는 경로인 `/not/exist`로 설정하였습니다. 

![스크린샷 2022-08-17 오전 12 47 48](https://user-images.githubusercontent.com/45676906/184922898-a8ae445f-f392-4f12-a762-642f3da4f13b.png)

그러면 위와 같이 `RESTARTS` 수가 2번이 되고, `STATUS`도 `CrashLoopBackOff`인 것을 볼 수 있습니다.

<br>

### `readinessProbe`

> 컨테이너가 준비되었는지 체크하고 정상적으로 준비되지 않았다면 Pod으로 들어오는 요청을 제외합니다. <br> <br>
> livenessProbe와 차이점은 문제가 있어도 Pod을 재시작하지 않고 요청만 제외한다는 점입니다.

<img width="725" alt="스크린샷 2022-08-17 오전 12 49 47" src="https://user-images.githubusercontent.com/45676906/184923286-3d86c24d-a46b-4494-a8e7-f9ed4fbf1940.png">

이번에는 Pod가 뜨긴 하는데 Ready에 보면 0개가 준비된 것을 볼 수 있습니다. 즉, 재시작하지는 않고 접근만 막는 것을 볼 수 있습니다.

<br>

### `livenessProbe + readinessProbe`

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: echo-health
  labels:
    app: echo
spec:
  containers:
    - name: app
      image: ghcr.io/subicura/echo:v1
      livenessProbe:
        httpGet:
          path: /
          port: 3000
      readinessProbe:
        httpGet:
          path: /
          port: 3000
```

![스크린샷 2022-08-17 오전 12 52 46](https://user-images.githubusercontent.com/45676906/184923990-240d1c2b-6bcb-47fb-b8cd-c27fdf6713ed.png)

이번에는 정상적인 경로를 입력한 후에 `livenessProbe + readinessProbe`를 띄웠더니 정상적으로 Pod가 실행되었고 로그를 보아도 해당 경로에 요청을 보내고 있는 것을 볼 수 있습니다.

<br>

## `다중 컨테이너`

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: counter
  labels:
    app: counter
spec:
  containers:
    - name: app
      image: ghcr.io/subicura/counter:latest
      env:
        - name: REDIS_HOST
          value: "localhost"
    - name: db
      image: redis
```

> 하나의 Pod에 속한 컨테이너는 서로 네트워크를 localhost로 공유하고 동일한 디렉토리를 공유할 수 있습니다.

<br>

<img width="301" alt="스크린샷 2022-08-17 오전 12 42 12" src="https://user-images.githubusercontent.com/45676906/184924396-3ebd97cb-bff9-4f40-a748-2322b8b8f720.png">

같은 Pod에 컨테이너가 생성되었기 때문에 counter앱은 redis를 localhost로 접근할 수 있습니다.

<br>

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: counter
  labels:
    app: counter
spec:
  containers:
    - name: app
      image: ghcr.io/subicura/counter:latest
      env:
        - name: REDIS_HOST
          value: "localhost"
    - name: db
      image: redis
```

![스크린샷 2022-08-17 오전 12 57 11](https://user-images.githubusercontent.com/45676906/184924906-bc87f2b0-56a5-48c8-babc-c3527f4d59b9.png)

위의 yml로 파드를 띄웠을 때 파드가 2개가 생긴 것을 볼 수 있습니다.

<br>

```
# Pod 생성
kubectl apply -f counter-pod-redis.yml

# Pod 목록 조회
kubectl get pod

# Pod 로그 확인
kubectl logs counter # 오류 발생 (컨테이너 지정 필요)
kubectl logs counter app
kubectl logs counter db

# Pod의 app컨테이너 접속
kubectl exec -it counter -c app -- sh
# curl localhost:3000
# curl localhost:3000
# telnet localhost 6379
  dbsize
  KEYS *
  GET count
  quit

# Pod 제거
kubectl delete -f counter-pod-redis.yml
```

<img width="861" alt="스크린샷 2022-08-17 오전 12 59 46" src="https://user-images.githubusercontent.com/45676906/184925445-d9613133-8674-4c6b-9e67-93cff449c182.png">

컨테이너에 접속해서 요청을 보내보면 위와 같이 카운터가 1씩 증가하는 것을 볼 수 있습니다.

<br>



<br>

## `Reference`

- [https://subicura.com/k8s/guide/pod.html#%E1%84%88%E1%85%A1%E1%84%85%E1%85%B3%E1%84%80%E1%85%A6-pod-%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5](https://subicura.com/k8s/guide/pod.html#%E1%84%88%E1%85%A1%E1%84%85%E1%85%B3%E1%84%80%E1%85%A6-pod-%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5)