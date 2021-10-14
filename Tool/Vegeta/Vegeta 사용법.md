## `Vegeta 사용법 정리`

```
brew update && brew install vegeta // 설치

echo "GET http://url" | vegeta attack -rate=100 -duration=10s | vegeta report 
```

두 번째 명령어는 해당 주소로 10초 동안 TPS 100에 해당하는 트래픽을 보내겠다는 뜻입니다. TPS 100 이란 `Transaction per Second의 약자로 1초당 처리할 수 있는 트랜잭션의 개수를 의미합니다.`
