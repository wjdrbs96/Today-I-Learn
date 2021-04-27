```yaml
#spring:
#  profiles:
#    active: real1
#server:
#  port: 8081
#
#---
#spring:
#  profiles:
#    active: real2
#server:
#  port: 8082

---
spring:
  profiles:
    group:
      "real1": "real1_port"

---
spring:
  config:
    activate:
      on-profile: "real1_port"

server:
  port: 8081

---
spring:
  profiles:
    group:
      "real2": "real2_port"


---
spring:
  config:
    activate:
      on-profile: "real2_port"

server:
  port: 8082
---
```