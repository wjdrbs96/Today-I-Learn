## `Spring Cloud Gateway 알아보기`

### `Spring Cloud Gateway Webflux`

Spring Cloud Gateway는 Spring Boot, Spring WebFlux, and Project Reactor 기반으로 구성되어 있음

<br>

### `How to Works`

![image](https://github.com/user-attachments/assets/81b2cb5e-5b96-42c4-849a-7f5efd670147)




<br>

### `Spring Cloud Gateway 사용하기`

```groovy
ext {
    set('springCloudVersion', "2020.0.3")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

<br>

## `Reference`

- [https://spring.io/guides/gs/gateway](https://spring.io/guides/gs/gateway)
- [https://docs.spring.io/spring-cloud-gateway/reference/index.html](https://docs.spring.io/spring-cloud-gateway/reference/index.html)