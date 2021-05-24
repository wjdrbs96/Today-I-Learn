# `EC2`

- EC2 AMI 이미지로 EC2가 생성될 때는 Docker AMI 기반으로 EC2 인스턴스 생성하고 그 안에서 Docker run 을 하게 된다.

<br>

# `Fargate`

- 바로 Docker run 을 하기 때문에 속도 차이가 크다.

<br>

## `ECS 장점`

- 바로 Docker run을 할 수 있기에 속도가 빠름(Fargate)
- 리소스(CPU) 설정을 세세하게 할 수 있음