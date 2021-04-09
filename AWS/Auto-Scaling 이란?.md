# `AWS Auto Scaling 이란 무엇일까?`

이번 글에서는 AWS의 꽃이라고 할 수 있는 `Auto Scaling`에 대해서 알아보겠습니다. Amazon EC2 Auto Scaling은 동적으로 EC2의 인스턴스의 수를 늘려주고 줄여주는 역할을 합니다. 

이렇게 동적으로 늘리고 줄이는 것이 왜 필요할까요? 

예를들어, 어떤 사이트에서 17시에 치킨 쿠폰 선착순 100명한테 무료로 주겠다는 이벤트를 했다고 가정하겠습니다. 기존 서비스에서는 EC2 인스턴스 5대를 사용하고 있었고 이것으로는 이벤트 때의 트래픽을 처리하기가 힘들 거 같아서 5대의 EC2 인스턴스를 더 늘렸다고 가정하겠습니다. 

이렇게 EC2 10대로 무사히 치킨 이벤트를 마쳤습니다. 이제 이벤트가 끝났으니 트래픽도 조금 줄어들 것이고 다시 EC2 인스턴스를 5대의 맞춰주어야 합니다. 이렇게 매번 이벤트 전에 인스턴스를 수동으로 만들었다 생성했다 하는 것은 매우 번거롭고 쉽지 않은 작업일 것입니다. 
또한 더 중요한 것은 10대의 인스턴스로 트래픽을 감당 가능하다는 것도 확실치는 않습니다. 

즉, `Auto Scaling`은 이러한 번거로운 작업들을 자동으로 인스턴스를 생성했다 없앴다 하는 것들을 해줍니다. (ex: CPU 사용률에 따라) 

그리고 `Auto-Scaling`을 언제, 어떻게 인스턴스를 생성하고 삭제할 지를 정하는 설정들이 필요한데 이것을 `Auto-Scaling Group`에서 합니다. 

`Auto Scaling 그룹`에서는 아래와 같은 것들을 지정할 수 있습니다. 

- `최소 인스턴스 수`를 지정할 수 있으며, Amazon EC2 Auto Scaling에서는 그룹의 크기가 이 값 아래로 내려가지 않습니다. 
- 또한 각 Auto Scaling 그룹의 `최대 인스턴스 수`를 지정할 수 있으며, Amazon EC2 Auto Scaling에서는 그룹의 크기가 이 값을 넘지 않습니다. 
- `원하는 용량`을 지정한 경우 그룹을 생성한 `다음에는 언제든지 Amazon EC2 Auto Scaling에서 해당 그룹에서 이만큼의 인스턴스를 보유할 수 있습니다.`

<img width="436" alt="스크린샷 2021-04-09 오후 5 41 12" src="https://user-images.githubusercontent.com/45676906/114153883-c835af80-995a-11eb-97de-ad35f14200df.png">

예를 들어, 위의 그림에서 보면 Auto Scaling 그룹의 경우 `최소 인스턴스 수 1개`, `희망 인스턴스 용량 2개`, `최대 인스턴스 수 4개`가 됩니다. 

<br>

## `Auto Scaling 구성 요소`

- ### `Group`
    -  `Auto-Scaling`을 언제, 어떻게 인스턴스를 생성하고 삭제할 지를 정하는 설정합니다. 그룹을 생성할 때 EC2 인스턴스의 최소 및 최대 인스턴스 수와 원하는 인스턴스 수를 지정할 수 있습니다. ([참고하기](https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/AutoScalingGroup.html))
        
- ### `구성 템플릿`
    - 그룹은 EC2 인스턴스에 대한 구성 템플릿으로 [시작 템플릿](https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/LaunchTemplates.html) 을 사용합니다. 인스턴스의 AMI ID, 인스턴스 유형, 키 페어, 보안 그룹, 블록 디바이스 매핑 등의 정보를 지정할 수 있습니다.
    
- ### `조정 옵션`
    - Amazon EC2 Auto Scaling은 Auto Scaling 그룹을 조정하는 다양한 방법을 제공합니다. 예를 들어, 지정한 조건의 발생(동적 확장) 또는 일정에 따라 조정하도록 그룹을 구성할 수 있습니다.([참고하기](https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/scaling_plan.html#scaling_typesof))
    
    
<br>

# `Reference`

- [https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/what-is-amazon-ec2-auto-scaling.html](https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/what-is-amazon-ec2-auto-scaling.html)    