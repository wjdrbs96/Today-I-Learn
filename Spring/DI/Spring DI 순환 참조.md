## `Spring DI 순환 참조`

### `Autowired 필드 주입`

A -> B -> C ->A 순환참조가 진행되고 있을 때 `생성자 주입`을 하면 서버가 실행될 때 순환 참조를 잡아줍니다. 하지만 `필드 주입`을 사용한다면 서버가 실행될 때 순환참조를 잡아내지 못한다는 단점이 있는데요.

[Spring Boot 2.6 Release](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.6-Release-Notes#circular-references-prohibited-by-default)를 보면 Spring Boot 2.6 부터 `필드 주입(Autowired)`도 서버가 실행될 때 순환 참조를 알게된다는 것을 알 수 있습니다.

<img width="931" alt="스크린샷 2022-06-17 오후 2 56 08" src="https://user-images.githubusercontent.com/45676906/174234432-3a157405-c7c6-4666-b159-6427a5979085.png">
