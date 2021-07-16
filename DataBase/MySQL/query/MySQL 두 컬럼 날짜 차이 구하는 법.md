# `MySQL에서 두 컬럼 날짜 차이 구하는 법`

프로젝트를 진행하다가 어떤 테이블에 저장되어 있는 컬럼(created_at, expired_at)의 날짜 차이를 구해야 하는 상황이 왔습니다. 

이거를 어떻게 구할까 하다가 MySQL에서 지원하는 함수가 있어서 간단하게 정리하려 합니다.

```sql
SELECT DATEDIFF(expired_at, created_at) 
FROM discussion_group
WHERE id = 42
```

위와 같이 `DATEDIFF` 라는 함수를 사용하면 두 컬럼의 날짜 차이를 구할 수 있습니다. 아주 쉽쥬?