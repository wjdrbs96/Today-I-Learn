## `Istio 설치`

```
curl -L https://istio.io/downloadIstio | sh -
cd istio-1.14.0
export PATH=$PWD/bin:$PATH
```

<br>

## `pod 생성, 삭제`

```
kubectl create -f deployment.yml
kubectl delete -f deployment.yml
```

- [https://istio.io/latest/docs/setup/getting-started/](https://istio.io/latest/docs/setup/getting-started/)
- [https://istio.io/latest/docs/setup/install/istioctl/](https://istio.io/latest/docs/setup/install/istioctl/)