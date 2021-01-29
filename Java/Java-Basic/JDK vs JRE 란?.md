# `들어가기 전에`

![java](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcmMU26%2FbtqAU5Fdd6B%2FriL6B4PWnNh9B4jereguL0%2Fimg.png)

자바는 위와 같은 과정을 통해서 코드가 실행이 됩니다. 즉, 자바 소스를 컴파일 하면 바이트 코드가 생성되고 바이트 코드를 JVM에서 실행을 시키는 것입니다. 

간단하게 말하면 위와 같은데 이 과정을 좀 더 자세히 이해하려면 `JDK(Java Development Kit) `와 `JRE(Java Runtime Environment)`가 어떤 것인지를 이해해야 합니다.

<br>

## `JDK vs JRE 란?`

![jdk vs jre](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FL2JVv%2FbtqAU6c3LWW%2FCDMSryWI5LedYjoUmSZkD0%2Fimg.png)

사진으로 보면 JDK와 JRE의 차이는 위와 같습니다. 

- JRE가 아닌 JDK 부분을 보면 주로 Tool 관련된 것임을 알 수 있습니다. 대표적인 예시로 `컴파일러`, `디버깅 도구`들이 속해 있습니다. 
- JRE를 보면 `java.lang`, `java.util`, `Math`와 같은 패키지들을 가지고 있고, 자바 실행 환경을 담당하고 있습니다. 

![dk](https://i.stack.imgur.com/AaveN.png)

즉, 요약하면 위와 같은 사진으로도 표현할 수 있습니다. (배포되는 JDK, JRE, JVM은 편의를 위해 JDK가 JRE를 포함하고 다시 JRE는 JVM을 포함하는 형태로 배포됩니다.)

JVM(Java Virtual Machine)의 존재와 역할을 아는 것이 자바 개발 환경을 이해하는데 필수적 입니다. JVM은 이름 그대로 가상기계 입니다. 현실 세계에서 컴퓨터를 구동하기 위해서는 물리적 컴퓨터인 하드웨어와 운영체제, 그리고 그 위에서 구동될 소프트웨어가 필요합니다. 

거기에 더해 소프트웨어를 개발할 수 있는 개발 도구가 필요합니다. 자바에서도 이러한 특징을 볼 수 있습니다. 

![ss](https://mblogthumb-phinf.pstatic.net/20150625_171/topjlim_1435208135455MYhmo_PNG/%C0%DA%B9%D9%B8%DE%B8%F0%B8%AE%B1%B8%C1%B6_2_1.png?type=w2)

즉, `자바 개발 도구인 JDK를 이용해 개발 프로그램은 JRE에 의해 가상의 컴퓨터인 JVM 상에서 구동됩니다.`

<br>

### `자바 프로그램의 개발과 구동`


![실행환경](https://mblogthumb-phinf.pstatic.net/20150625_14/topjlim_1435208135737zTexa_PNG/%C0%DA%B9%D9%B8%DE%B8%F0%B8%AE%B1%B8%C1%B6_2_2.png?type=w2)

JDK는 자바 소스 컴파일러인 `javac.exe`를 포함하고 있고, JRE는 자바 프로그램 실행기인 `java.exe`를 포함하고 있습니다. 
자바가 이런 구조를 택한 이유는 기존 언어로 작성한 프로그램은 윈도우 95용, 윈도우 XP용, 리눅스용, 애플 맥 OS 등등 여러 플랫폼에서 각각 따로 배포 파일을 준비해야 하는 불편함을 없애주었습니다. 

