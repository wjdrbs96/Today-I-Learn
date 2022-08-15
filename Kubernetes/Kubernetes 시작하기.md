## `MacOS Kubernetes 시작하기`

```
# homebrew를 사용하고 있다면..
brew install minikube

# homebrew를 사용하지 않는다면, 직접 binary 다운로드
curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64 \
  && chmod +x minikube
```

```
# 버전확인
minikube version

# 가상머신 시작 (x86 추천)
minikube start --driver=hyperkit
# 가상머신 시작 (M1 추천 - 도커 데스크탑 설치 필요)
minikube start --driver=docker
# driver 에러가 발생한다면 virtual box를 사용
minikube start --driver=virtualbox
# 특정 k8s 버전 실행
minikube start --kubernetes-version=v1.23.1

# 상태확인
minikube status

# 정지
minikube stop

# 삭제
minikube delete

# ssh 접속
minikube ssh

# ip 확인
minikube ip
```

<br>

## `kubectl 설치하기`

```
brew install kubectl

curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.23.5/bin/darwin/amd64/kubectl \
  && chmod +x kubectl
```