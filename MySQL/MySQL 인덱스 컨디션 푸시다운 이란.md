## `MySQL 인덱스 컨디션 푸시다운 이란?`

MySQL 5.6 버전부터는 인덱스 컨디션 푸시다운(`Index Condition Pushdown`) 이라는 기능이 도입 되었는데요. 어떤 내용인지 알아보겠습니다.

- `인덱스 컨디션 푸시 다운`
    - secondary index에만 사용됩니다.
    - 전체 row 읽기의 수를 줄여 I/O 작업을 줄이는 것.
    - 인덱스를 범위 제한 조건으로 사용하지 못하는 쿼리에 한해서 발생하는 것 같음 (`LIKE %name%`, `like > 10` 같이 인덱스를 이용하지 못하는 범위 검색을 의미하는 것 같음)
    - InnoDB Clustered index의 경우, 전체 레코드가 InnoDB 버퍼에 존재하기 때문에, 인덱스 컨디션 푸시다운을 사용해도 I/O가 감소하지 않음

<br>

### `MySQL SQL 수행 절차`

<img width="780" alt="스크린샷 2024-04-10 오전 12 51 30" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/bd4a6fce-87d2-4e4d-93c5-6fed1c1749b8">

- `MySQL 엔진`: 스토리지 엔진에서 받은 데이터 가공 처리 역할 (쿼리의 최적화된 실행을 위한 옵티마이저가 중심)
- `스토리지 엔진`: 실제 데이터를 디스크 스토리지에 저장하거나 디스크 스토리지로부터 데이터를 읽어오는 부분 담당 (대표적으로 인덱스를 비교하는 담당)

<br>

### `인덱스 푸시다운 예제`

```
+----+-------------+-------+------------+-------+-----------------------+-----------------------+---------+------+------+----------+-------------------------+
| id | select_type | table | partitions | type  | possible_keys         | key                   | key_len | ref  | rows | filtered |          Extra          |
+----+-------------+-------+------------+-------+-----------------------+-----------------------+---------+------+------+----------+-------------------------+
|  1 | SIMPLE      | c     | NULL       | index | idx_last_name_address | idx_last_name_address |  10     | NULL |   10 |   100.00 |  Using index condition  |
+----+-------------+-------+------------+-------+-----------------------+-----------------------+---------+------+------+----------+-------------------------+
```

인덱스 푸시다운은 쿼리의 실행 계획 extra 컬럼에서 `Using index condition`로 표시되는데요. 어떤 상황에서 발생하는지 확인 해보겠습니다.

```sql
SET optimizer_switch='index_condition_pushdown=on';
ALTER TABLE people ADD INDEX idx_last_name_address (zipcode, lastname, address)
```

```sql
SELECT * FROM people
  WHERE zipcode='95054'
  AND lastname LIKE '%sal'
  AND address LIKE '%Main Street%';
```

만약에 인덱스 컨디션 푸시다운이 없을 때 위의 쿼리가 실행된다면 어떻게 실행 될까요?

1. `zipcode=95054`에 해당하는 값들을 스토리지 엔진을 통해서 읽어옴 (`idx_last_name_address` 인덱스를 통해서 데이터 파일에 접근하여 데이터를 읽어옴)
2. `lastname LIKE '%sal'`에 해당하는 조건을 MySQL 엔진에서 필터링 함
    - last_name이 `Marsja`, `Masaki` 같이 굳이 테이블을 읽지 않아도 되는 데이터도 데이터 파일에 접근하여 불필요한 Disk I/O 발생
    - `만약에 10만건 읽고 필터링 되어서 1건 남았다면 99,999건의 불필요한 Disk I/O 발생`

<img width="975" alt="스크린샷 2024-04-10 오전 1 04 28" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/5e857cd2-01eb-4381-804d-418593faeb50">

여기서 2번 과정을 보면 비효율적이라는게 보이는데요.

비효율적인 이유가 무엇이냐면 이미 1번 과정(`zipcode=95054`)에서 인덱스 파일을 읽어서 데이터 파일에 접근하여 데이터를 가져온 것을 볼 수 있습니다. (스토리지 엔진에서 인덱스 사용하여 디스크 파일에 접근하여 데이터를 MySQL 엔진으로 반환했음)

MySQL 엔진에서는 굳이 디스크 파일에 접근하지 않더라도 스토리지 엔진에서 반환한 값을 보면 `lastname LIKE '%sal'` 해당하는 결과가 1건이라는 것을 알 수 있습니다.

즉, 굳이 필터링 되어 버려진 값들에 대해서 불필요하게 Disk 접근을 할 필요가 없다는 뜻입니다.

`하지만 MySQL 5.5 버전까지는 인덱스를 범위 제한 조건으로 사용하지 못하는 lastname 조건은 MySQL 엔진이 스토리지 엔진으로 아예 전달하지 못한다고 합니다.`

```
MySQL 엔진 (핸들러 API 사용)-> 스토리지 엔진 -> 디스크 파일
```

정리하면 MySQL 엔진에서 lastname 조건 처럼 인덱스를 사용하지 못하는 범위 검색의 경우 핸들러 API에서 스토리지 엔진으로 넘겨주지 않기 때문에 스토리지 엔진의 입장에서는 모두 디스크 파일에 접근하여 I/O를 발생시킬 수 밖에 없었던 것입니다.

<br>

<img width="1066" alt="스크린샷 2024-04-10 오전 1 18 44" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1e2b2dc3-f763-4f0a-bd69-01eb9b50ea37">

그래서 MySQL 5.6 버전부터는 인덱스를 사용하지 못하는 범위 검색이라 하더라도 모두 같이 스토리지 엔진으로 전달할 수 있게 핸들러 API가 개선되어, 위처럼 스토리지 엔진에서 MySQL 엔진으로 부터 받은 필터링 조건으로 불필요한 Disk I/O를 줄이는 것을 볼 수 있습니다.

> 핸들러 API: MySQL 엔진의 쿼리 실행기에서 데이터를 쓰거나 읽어야 할 때는 각 스토리지 엔진에 쓰기 또는 읽기를 요청 API

<br>

### `인덱스 컨디션 푸시 다운 on/off`

```sql
SET optimizer_switch='index_condition_pushdown=off';
SET optimizer_switch='index_condition_pushdown=on';
```

위의 명령어로 인덱스 푸시 다운 설정을 on/off 할 수 있습니다.

<br>

### `정리하며`

- MySQL 아키텍쳐에서 MySQL 엔진, 스토리지 엔진의 역할 등등 전체적인 개념에 대해서 학습해보면 좋을 거 같습니다.
- 데이터 수가 많아짐에 따라 DB에서 랜덤 Disk I/O에 대한 관리는 아주 중요한 것 같습니다.
- 요즘 대부분의 MySQL 버전에서는 `인덱스 컨디션 푸시다운`이 디폴트로 활성화 되어 있겠지만 데이터 수가 수억건 같이 많은 곳에서 쿼리 실행을 해보면 쿼리를 했을 때 디스크 I/O를 줄이는 것이 쿼리 성능에 얼마나 도움되는지 경험할 수 있을 거 같습니다.

<br>

### `Reference`

- [Real MySQL - 1]()
- [https://dev.mysql.com/doc/refman/8.0/en/index-condition-pushdown-optimization.html](https://dev.mysql.com/doc/refman/8.0/en/index-condition-pushdown-optimization.html)
- [https://jojoldu.tistory.com/474](https://jojoldu.tistory.com/474)