# `MySQL Upsert 사용법`

이번 글에서는 `MySQL`에서 `Upsert` 쿼리를 사용하는 법에 대해서 정리해보겠습니다. Upsert는 이름에서 어느정도 유추할 수 있듯이 `Update` + `Insert`를 합친 말입니다. 

즉, `Upsert는 중복되는 값이 있다면 Update를 하고 중복되는 값이 없다면 Insert`를 하는 쿼리입니다. 좀 더 정확히 말하면 `Unique Key`의 값이 중복된다면 `Update`를 하고, `Unique 컬럼의 값이 존재하지 않는다면 INSERT`를 하는 것입니다. 실제로 프로젝트를 하다 보니 이러한 쿼리를 사용해야 할 상황이 와서 이렇게 정리를 하게 되었습니다. 

Upsert 쿼리를 보면 아래와 같습니다. 

```sql
INSERT INTO user_refresh_token (user_id, refresh_token)
VALUES (#{userId}, #{refreshToken}) ON DUPLICATE KEY
UPDATE refresh_token = #{refreshToken}
```

쿼리의 생김새도 `INSERT` 쿼리와 `UPDATE` 쿼리를 섞어놓은 것 같습니다. Upsert 쿼리는 `user_refresh_token` 테이블에다 적용을 할 것인데 `user_refresh_token` 스키마는 아래와 같습니다. 

<br>

<img width="479" alt="스크린샷 2021-07-22 오후 4 10 24" src="https://user-images.githubusercontent.com/45676906/126601830-13537e34-b97f-4774-b065-063f42f0c7e3.png">

현재 `user_id`를 `Unique Key`로 설정하였습니다.(참고로 만약 컬럼에 `Unique Key`를 설정하지 않았다면 값은 계속 중복해서 INSERT가 됩니다. 그래서 어떤 값을 기준으로 중복 체크를 해서 Update or INSERT를 할 지 선택해서 그 값에 `Unique Key`를 걸어주어야 합니다.) 

그리고 정말 Upsert 쿼리가 잘 실행되는지 확인해보기 위해서 테이블에 `INSERT` 쿼리를 두 번 날려보겠습니다.

<br>

![스크린샷 2021-07-22 오후 4 15 19](https://user-images.githubusercontent.com/45676906/126602256-22d9dc15-ca3c-4215-af62-412d957983f2.png)

현재 위와 같이 데이터가 존재하는 상황에서 한번 더 위의 `Upsert` 쿼리를 실행해보면 `refresh_token`의 값만 업데이트 되는 것을 확인할 수 있습니다.
