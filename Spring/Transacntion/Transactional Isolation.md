# `Transactional Isolation(트랜잭션 격리 레벨)`

| 옵션 | 설명 |
|--------|-------|
| READ_UNCOMMITTED | 트랜잭션에서 commit 되지 않은 다른 트랜잭션에서 읽는 것을 허용한다. <br> Dirty Read가 발생한다.|
| READ_COMMITTED | 트랜잭션에서 commit 되어 확정된 데이터만을 읽는 것을 허용한다. <br> Oracle 기본 격리 수준이다. <br> Non-Repeatable-Read 부정합이 발생할 수 있다. |
| REPEATABLE READ | 트랜잭션 시작 전 커밋된 내용만 조회 가능하다. <br> MySQL InnoDB 기본 격리 수준이다. <br> 한 트랜잭션 내에서 수 차례 SELECT 수행하더라도 동일한 값이 읽혀지는 것을 보장한다. <br> Phantom Read 부정합이 발생할 수 있다. |
| SERIALIZABLE | 모든 작업을 하나의 트랜잭션에 처리하는 것과 같은 높은 고립수준을 제공하는데, 이로인해 동시성 처리 효율은 매우 떨어진다. |

<br>

<img width="1047" alt="스크린샷 2021-11-25 오후 10 25 08" src="https://user-images.githubusercontent.com/45676906/143449504-f1f2b6cd-5c2a-4cf8-90fe-39f6d0f700c0.png">

<br> <br>

## `NON REPEATABLE READ vs PHANTOM READ`

### `NON REPEATABLE READ`

![image](https://user-images.githubusercontent.com/45676906/143453010-be202c18-f3ed-4e5c-b5b6-859838b2e0be.png)

`READ COMMITTED` 격리 수준에서도 `NON REPEATABLE READ`가 발생한다. 같은 트랜잭션에서 똑같은 SELECT 쿼리로 조회했을 때 항상 같은 결과를 가져와야 한다는 `REPETABLE READ` 정합성에 어긋나는 것이다.  

<br> 

### `PHANTOM READ`

![image](https://user-images.githubusercontent.com/45676906/143453192-d0ab6042-94b8-4924-bed3-e0936d82c24a.png)

InnoDB 스토리지 엔진은 트랜잭션이 ROLLBACK 될 가능성에 대비해 변경되기 전 레드코를 언두(Undo) 공간에 백업해두고 실제 레코드 값을 변경한다. 이러한 변경 방식을 `MVCC` 라고 한다. `REPEATABLE READ` 격리 수준은 이 MVCC를 위해 언두 영역에 백업된 데이터를 이용해 동일 트랜잭션 내에서는 동일한 결과를 보여줄 수 있도록 보장한다. 

`READ COMMITTED`도 `MVCC`를 이용해 COMMIT 되기 전의 데이터를 보여준다. `REPEATABLE READ`, `READ COMMITTED`의 차이는 언두 영역에 백업된 레코드의 여러 버전 가운데 몇 번째 이전 버전까지 찾아 들어가야 하는지에 있다. 

여기서 나는 `NON REPEATABLE READ`와 `PHANTOM READ`의 차이가 무엇인지 감이 오지 않아서 좀 더 찾아 보았다. 위의 `NON REPEATABLE READ`는 그림에서 볼 수 있듯이 한 트랜잭션 내에서 같은 쿼리를 여러 번 실행했을 때 결과가 다른 것이다. 즉, 다른 트랜잭션이 중간에 값을 바꿨기 때문이다! 

그러면 `REPEATABLE READ` 격리 수준에서 똑같은 상황이라면 어떨까? `REPEATABLE READ` 격리 수준은 트랜잭션이 시작할 때 보다 이전에 커밋된 데이터만 조회하기 때문에 중간 트랜잭션에서 데이터를 바꿨어도 `Undo` 영역에서 조회하기 때문에 문제가 발생하지 않는다!

하지만 `Update`가 아닌 `INSERT`를 했을 때는 말이 다르다. `INSERT`를 했을 때는 데이터 `row`가 늘어나게 난다. 즉, 이 때는 `REPEATABLE READ`도 부정합이 발생하는데 이러한 부정합을 `PHANTOM READ` 라고 하는 것이다. 

<br> <br>

## `Spring에서 @Transacntional 사용했을 때 Default 격리 레벨은?`

![스크린샷 2021-11-25 오후 11 32 11](https://user-images.githubusercontent.com/45676906/143459898-e700a2b9-6b22-4338-86f4-3a7aafd5d7f4.png)

`Spring`에서 `@Transactional` 어노테이션을 아무 설정하지 않고 사용하면 `isolation`은 위의 보이는 `DEFAULT`가 사용된다. 

<br>

![스크린샷 2021-11-25 오후 11 32 48](https://user-images.githubusercontent.com/45676906/143460038-bab3d62e-4b82-4473-97c4-0aa32d5bb1d8.png)

`DEFAULT` 설명을 보면 `JDBC isolation Level` 동일하게 설정된다고 나와 있다. 즉, 나는 `MySQL InnoDB`를 사용하고 있기 때문에 `REPEATABLE READ`가 `DEFAULT`로 사용되고 있는 것이다.

<br> <br>

# `Reference`

- [https://stackoverflow.com/questions/11043712/what-is-the-difference-between-non-repeatable-read-and-phantom-read](https://stackoverflow.com/questions/11043712/what-is-the-difference-between-non-repeatable-read-and-phantom-read)
- [https://www.baeldung.com/spring-transactional-propagation-isolation](https://www.baeldung.com/spring-transactional-propagation-isolation)