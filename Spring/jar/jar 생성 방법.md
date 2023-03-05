## `jar 생성 방법`

```
./gradlew clean :admin:bootJar -Pprofile=dev
java -jar -Dspring.profiles.active=dev ./admin/build/libs/*.jar
```

위처럼 jar를 생성하고 jar를 실행시킬 수 있다.