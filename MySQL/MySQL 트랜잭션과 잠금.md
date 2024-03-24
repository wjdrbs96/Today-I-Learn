## `MySQL 트랜잭션과 잠금`

`트랜잭션`은 작업의 완전성을 보장해 주는 것입니다. 즉 논리적인 작업 셋을 모두 완벽하게 처리하거나, 처리하지 못할 경우에는 원 상태로 복구해서 작업의 일부만 적용되는 현상(Partition Update)이 발생하지 않게 만들어주는 기능입니다.

<br>

## `MySQL 엔진의 잠금`

MySQL에서 사용되는 잠금은 크게 스토리지 엔진 레벨과 MySQL 엔진 레벨로 나눌 수 있습니다. 

참고: (글로벌 락, 테이블 락, 네임드 락, 메타데이터 락 같은 것들이 있는데 이건 딱히 지금 공감되지 않아서 따로 정리하지 않음)

<br>

## `InnoDB 스토리지 엔진 잠금`

InnoDB 스토리지 엔진은 MySQL에서 제공하는 잠금과는 별개로 스토리지 엔진 내부에서 레코드 기반의 잠금 방식을 탑재하고 있습니다.

<img width="484" alt="스크린샷 2024-03-10 오후 8 08 46" src="https://media.oss.navercorp.com/user/30855/files/75f45542-ecc0-4754-8c5a-17916e390809">

<br>

### `레코드 락 (Record Locks)`

```sql
SELECT c1 FROM t WHERE c1 = 10 FOR UPDATE; 
```

레코드 자체만을 잠그는 것을 `레코드 락(Record lock)` 이라고 합니다. 즉, 다른 트랜잭션에서 t.c1 값이 10인 행을 INSERT, UPDATE 또는 DELETE 할 수 없도록 합니다. 

한 가지 중요한 특징은 InnoDB 스토리지 엔진은 레코드 자체가 아니라 `인덱스의 레코드를 잠근다는 점` 입니다.

즉, c1 = 10 인 레코드를 검색하게 되는데 이 때 검색하는 레코드들이 모두 잠긴다는 뜻 같습니다.

<br>

### `갭 락 (Gap Locks)`

```sql
SELECT c1 FROM t WHERE c1 BETWEEN 10 and 20 FOR UPDATE;
```

갭 락은 레코드 자체가 아니라 레코드와 바로 인접한 레코드 사이의 간격만을 잠그는 것을 의미합니다. 갭 락의 역할은 레코드와 레코드 사이의 간격에 새로운 레코드가 생성(INSERT) 되는 것을 제어하는 것입니다.

위의 쿼리로 보면 c1 값 10 ~ 20 범위 사이가 잠겨 있기 때문에 다른 트랜잭션에서 t.c1에 15 값을 INSERT 할 수 없도록 합니다.

<br>

### `넥스트 키 락 (Next-Key Locks)`

`레코드 락`과 `갭 락`을 합쳐 놓은 형태의 잠금을 `넥스트 키 락(Next Key Lock)` 이라고 합니다.

```sql
SELECT * FROM child WHERE id > 100 FOR UPDATE;
```

- 100보다 큰 값의 범위 스캔을 지정합니다. (스캔하면서 읽은 레코드 잠금 => 레코드 락)
- 100보다 큰 값을 INSERT, DELETE 할 수 없음 (갭 락) 

즉, 위처럼 `레코드 락`, `갭 락` 자체로 사용되지 않고 합쳐서 `넥스트 키 락 (Next-Key Locks)`으로 사용됩니다.

<br>

### `자동 증가 락`

MySQL에서는 자동 증가하는 숫자 값을 추출하기 위해 AUTO_INCREMENT 라는 컬럼 속성을 제공합니다. AUTO INCREMENT 컬럼이 사용된 테이블에 동시에 여러 레코드가 INSERT 되는 경우, 저장되는 각 레코드는 중복되지 않고 저장된 순서대로 증가하는 일련번호 값을 가져야 합니다. InnoDB 스토리지 엔진에서는 이를 위해 내부적으로 `AUTO_INCREMENT 락` 이라고 하는 테이블 수준의 잠금을 사용합니다.

AUTO_INCREMENT 락은 INSERT와 REPLACE 쿼리 문장과 같이 새로운 레코드를 저장하는 쿼리에서만 필요하며, UPDATE나 DELETE 등의 쿼리에서는 걸리지 않습니다.

자동 증가 값이 한 번 증가하면 절대 줄어들지 않는 이유가 AUTO_INCREMENT 잠금을 최소화하기 위해서입니다. INSERT 쿼리가 실패했더라도 한 번 증가된 AUTO_INCREMENT 값은 다시 줄어들지 않고 그대로 남습니다.

<br>

## `인덱스와 잠금`

InnoDB의 잠금과 인덱스는 상당히 중요한 연관 관계가 있습니다. `레코드 락`에서 InnoDB의 잠금은 레코드를 잠그는 것이 아니라 인덱스를 잠그는 방식으로 처리된다고 하였습니다.

즉, 변경해야 할 레코드를 찾기 위해 검색한 인덱스의 레코드를 모두 락을 걸어야 합니다.

```sql
mysql> SELECT COUNT(*) FROM employees WHERE first_name = 'Gyunny';
```

- 253건

<br>

```sql
mysql> SELECT COUNT(*) FROM employees WHERE first_name = 'Gyunny' AND last_name = 'Choi';
```

- 1건

<br>

```sql
mysql> UPDATE employees SET hire_date=NOW() WHERE first_name='Gyunny' AND last_name = 'Choi';
```

여기서 `first_name` 에만 인덱스가 존재할 때 UPDATE 쿼리가 실행된다면 검색해야 할 레코드인 253건이 모두 잠기게 됩니다.

이 테이블에 인덱스가 하나도 없다면 테이블을 풀 스캔하면서 UPDATE 작업을 하는데, 이 과정에서 테이블에 있는 모든 레코드를 잠그게 됩니다. 즉, MySQL InnoDB에서 인덱스 설계가 중요한 이유 입니다.

<br>

## `MySQL 격리 수준`

트랜잭션의 격리 수준(isolation level)이란 여러 트랜잭션이 동시에 처리될 때 특정 트랜잭션이 다른 트랜잭션에서 변경하거나 조회하는 데이터를 볼 수 있게 허용할지 말지를 결정하는 것입니다.

4개의 격리 수준에서 순서대로 뒤로 갈수록 각 트랜잭션 간의 데이터 격리 정도가 높아지며, 동시 처리 성능도 떨어지는 것이 일반적이라고 볼 수 있습니다.

<br>

### `READ UNCOMMITTED`

![스크린샷 2021-05-25 오전 12 41 32](https://user-images.githubusercontent.com/45676906/119372146-f47e7300-bcf1-11eb-99b8-4c1f6dd96037.png)

`READ UNCOMMITTED` 격리 수준에서는 각 트랜잭션에서 변경 내용 COMMIT, ROLLBACK 여부 관계 없이 다른 트랜잭션에서 볼 수 있습니다.

위의 그림을 보면 `A`에서 INSERT 한 후에 COMMIT 되지 않았지만 B가 해당 데이터를 조회하는 것을 볼 수 있습니다.

이처럼 어떤 트랜잭션에서 처리한 작업이 완료되지 않았는데도 다른 트랜잭션에서 볼 수 있는 현상을 `더티 리드(Dirty Read)` 라고 합니다.

<br>

### `READ COMMITTED`

`READ COMMITTED`는 오라클 DBMS에서 기본으로 사용되는 격리 수준입니다. `READ COMMIITTED` 격리 수준에서는 커밋이 완료된 데이터만 다른 트랜잭션에서 조회할 수 있기 때문에 `더티 리드(Dirty Read)`는 발생하지 않습니다.

![스크린샷 2021-05-25 오전 12 56 01](https://user-images.githubusercontent.com/45676906/119373965-fa755380-bcf3-11eb-87c5-d4449975d31e.png)

만약에 위처럼 A 트랜잭션에서 Update 했다면, 변경하기 이전 내용을 `언두 영역`에 저장해놓고 B 트랜잭션에서 `언두 영역`을 읽는 방식 입니다.

하지만 `READ COMMITTED` 격리 수준에서도 `NON-REPEATABLE-READ` 부정합이 발생할 수 있다는 문제점이 존재합니다.

<br>

### `NON-REPEATABLE READ`

![스크린샷 2021-05-25 오전 1 08 48](https://user-images.githubusercontent.com/45676906/119375425-c3a03d00-bcf5-11eb-9aa8-a70d537a0968.png)

트랜잭션 B가 BEGIN 명령으로 트랜잭션을 시작하고 first_name이 `Toto`인 사용자를 검색했는데 일치하는 사용자가 존재하지 않았습니다. 하지만 트랜잭션 A에서 사원번호가 500000인 사원의 이름을 `Toto`로 변경하고 커밋을 실행하였습니다. 

트랜잭션 B가 다시 SELECT로 같은 사원을 조회하면 이번에는 `Toto`가 반환이 되는 것을 볼 수 있습니다.

이처럼 하나의 트랜잭션 내에서 똑같은 SELECT 쿼리를 실행했을 때는 항상 같은 결과를 가져와야 한다는 `REPEATABLE READ` 정합성에 어긋나는 것을 `NON-REPEATABLE READ` 라고 합니다.

이러한 부정합 현상은 일반적인 상황에서는 크게 중요하지 않을 수 있지만 `금전적인` 처리와 연결되면 문제가 될 수도 있습니다. (Redis GET & SET 느낌이 든다.)

<br>

### `REPEATABLE READ`

![스크린샷 2021-05-25 오전 10 08 54](https://user-images.githubusercontent.com/45676906/119424845-371b6c00-bd41-11eb-98fe-d8c99e185485.png)

`REPEATABLE READ`는 MySQL의 InnoDB 스토리지 엔진에서 기본으로 사용되는 격리 수준입니다. 바이너리 로그를 가진 MySQL 서버에서는 최소 `REPEATABLE READ` 격리 수준 이상을 사용해야 합니다.

`REPEATABLE READ`의 특징은 트랜잭션 안에서 실행되는 SELECT 쿼리는 현재 트랜잭션 번호 보다 작은 트랜잭션 번호에서 변경한 것만 바라본다는 특징이 있습니다.

즉, 위의 그림에서 B 트랜잭션(10번) 에서는 A 트랜잭션(12번) 에서 변경하여도 트랜잭션 번호가(10번 < 12번) 더 작기 때문에 변경한 내용을 바라보지 못해서 하나의 트랜잭션에서 SELECT 쿼리의 결과가 같습니다.

즉, `REPEATABLE READ` 에서는 `NON-REPEATABLE READ` 부정합은 발생하지 않습니다.

하지만 `REPEATABLE READ` 격리 수준에서도 부정합이 발생할 수 있습니다.

<br>

## `PHANTOM READ`

![스크린샷 2021-05-25 오전 10 23 37](https://user-images.githubusercontent.com/45676906/119425959-456a8780-bd43-11eb-8f98-bb4efcfa2921.png)

위의 그림을 보면 B 트랜잭션에서 첫 번째 실행한 SELECT 쿼리와 두 번째 실행한 SELECT 쿼리의 결과가 다른 것을 볼 수 있습니다. 즉, 처음에는 안보였던 데이터가 두 번째 보이게 되는 상황인데요.

이처럼 트랜잭션에서 수행한 변경 작업에 의해 레코드가 보였다 안 보였다 하는 현상을 `PHANTOM READ` 라고 합니다.

<br>

### `SERIALIZABLE`

가장 단순한 격리 수준이지만 가장 엄격한 격리 수준입니다. 또한 그만큼 동시 처리 성능도 다른 트랜잭션 격리 수준보다 떨어집니다.

InnoDB 스토리지 엔진에서는 갭 락과 넥스트 키락 덕분에 `REPETABLE READ` 격리 수준에서도 이미 `PHANTOM READ`가 발생하지 않기 때문에 굳이 `SERIALIZABLE` 격리 수준을 사용할 필요성은 없습니다.

<br>

## `NON REPEATABLE READ vs PHANTOM READ`

`NON REPEATABLE READ`와 `PHANTOM READ`의 차이를 좀 더 알아보겠습니다.

- `NON REPEATABLE READ`
    - 한 트랜잭션 내에서 같은 쿼리를 여러 번 실행했을 때 결과가 다르다.
    - 다른 트랜잭션이 중간에 값을 추가한 것이 아니리 변경하였기 때문이다. 그러면 REPEATABLE READ 격리 수준에서 똑같은 상황이라면 어떨까? REPEATABLE READ 격리 수준은 트랜잭션이 시작할 때 보다 이전에 커밋된 데이터만 조회하기 때문에 중간 트랜잭션에서 데이터를 바꿨어도 Undo 영역에서 조회하기 때문에 문제가 발생하지 않는다.
- `PHANTOM READ`
    - 하나의 트랜잭션에서 레코드가 보였다 안 보였다 하는 현상 또는 보였다가 안보이는 현상 (다른 트랜잭션에서 값을 추가 또는 삭제)
    - Update가 아닌 INSERT or DELETE를 했을 때는 말이 다르다. INSERT or DELETE를 했을 때는 데이터 row가 늘어나게 되고 언두 영역을 사용하게 되지 않는다. 즉, 이 때는 안보였던 레코드가 보이게 되는데 이러한 부정합을 `PHANTOM READ` 라고 하는 것이다.

<br>

## `MySQL InnoDB PHANTOM READ 왜 발생하지 않을까?`

여기서 추가로 참고해야 할 점은 MySQL InnoDB를 사용한다면 `PHANTOM READ`는 발생하지 않는다. 그 이유는 InnoDB는 넥스트 키 락, 갭 락을 사용하는데, DELETE를 하기 위해 검색하면서 잠그기 때문에 다른 트랜잭션에서 데이터를 삭제하거나 추가할 수 없기 때문이다.

```sql
SELECT * FROM child WHERE id > 100 FOR UPDATE;
```

- 100보다 큰 값의 범위 스캔을 지정합니다. (스캔하면서 읽은 레코드 잠금 => 레코드 락)
- 100보다 큰 값을 INSERT, DELETE 할 수 없음 (갭 락)

위에서 `넥스트 키 락`에 대해서 설명한 예시인데요. 위의 쿼리로 한번 더 확인해보면 100보다 큰 값에 대해서 INSERT, DELETE 하는 것을 막아주기 때문에 `PHANTOM READ` (보이지 않았던 것이 보이거나, 보였던 것이 보이지 않는 상황)은 발생하지 않게 됩니다.   

<br>

## `Reference`

- [Real MySQL - 5장]()
- [https://stackoverflow.com/questions/11043712/non-repeatable-read-vs-phantom-read](https://stackoverflow.com/questions/11043712/non-repeatable-read-vs-phantom-read)
- [https://dev.mysql.com/doc/refman/8.3/en/innodb-locking.html](https://dev.mysql.com/doc/refman/8.3/en/innodb-locking.html)
- [https://dev.mysql.com/doc/refman/8.3/en/innodb-next-key-locking.html](https://dev.mysql.com/doc/refman/8.3/en/innodb-next-key-locking.html)
- [https://dev.mysql.com/doc/refman/8.3/en/innodb-transaction-isolation-levels.html](https://dev.mysql.com/doc/refman/8.3/en/innodb-transaction-isolation-levels.html)