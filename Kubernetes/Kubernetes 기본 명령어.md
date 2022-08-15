## `Kubernetes 기본 명령어`

<img width="607" alt="스크린샷 2022-08-15 오후 11 41 35" src="https://user-images.githubusercontent.com/45676906/184656727-6a3516fd-0633-4326-a98e-435a1992d2e7.png">

<br>

### `alias 설정하기`

```
alias k='kubectl'
k version

# shell 설정 추가
echo "alias k='kubectl'" >> ~/.bashrc
source ~/.bashrc
```

위처럼 터미널에 작성하면 편하게 작업할 수 있습니다.

<br>

## `상태 설정하기 (apply)`

```
kubectl apply -f https://subicura.com/k8s/code/guide/index/wordpress-k8s.yml
```

`apply`를 사용하면 `yml` 파일을 실행할 수 있습니다.

<br>

## `리소스 목록보기 (get)`

```
# Pod 조회
kubectl get pod

# 줄임말(Shortname)과 복수형 사용가능
kubectl get pods
kubectl get po

# 여러 TYPE 입력
kubectl get pod,service
#
kubectl get po,svc

# Pod, ReplicaSet, Deployment, Service, Job 조회 => all
kubectl get all

# 결과 포멧 변경
kubectl get pod -o wide
kubectl get pod -o yaml
kubectl get pod -o json

# Label 조회
kubectl get pod --show-labels
```

<br>

## `리소스 상세 상태보기 (describe)`

```
kubectl describe po/{podName}
ex) kubectl describe po/wordpress-bd4df79df-9xnqs
```

<img width="996" alt="스크린샷 2022-08-15 오후 11 59 20" src="https://user-images.githubusercontent.com/45676906/184660049-b2f59b41-6045-4481-b2a2-c56ed3851056.png">

<br>

## `리소스 제거 (delete)`

```
kubectl get pod
kubectl delete pod/{podName}
ex) kubectl delete pod/wordpress-bd4df79df-9xnqs
```

<br>

## `컨테이너 로그 조회 (logs)`

```
# Pod 조회로 이름 검색
kubectl get pod

# 조회한 Pod 로그조회
kubectl logs wordpress-5f59577d4d-8t2dg

# 실시간 로그 보기
kubectl logs -f wordpress-5f59577d4d-8t2dg
```

<br>

## `컨테이너 명령어 전달 (exec)`

컨테이너 접속하는 명령어는 `exec` 입니다.

```
# Pod 조회로 이름 검색
kubectl get pod

# 조회한 Pod의 컨테이너에 접속
kubectl exec -it wordpress-5f59577d4d-8t2dg -- bash
```

<br>

## `Reference`

- [https://subicura.com/k8s/guide/kubectl.html#kubectl-%E1%84%86%E1%85%A7%E1%86%BC%E1%84%85%E1%85%A7%E1%86%BC%E1%84%8B%E1%85%A5](https://subicura.com/k8s/guide/kubectl.html#kubectl-%E1%84%86%E1%85%A7%E1%86%BC%E1%84%85%E1%85%A7%E1%86%BC%E1%84%8B%E1%85%A5)