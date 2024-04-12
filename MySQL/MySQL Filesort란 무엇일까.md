## `MySQL Filesort란 무엇일까?`

이번 글에서는 MySQL Filesort 정렬이란 무엇인지 알아보겠습니다.

<br>

## `요약`

- 인덱스를 이용한 정렬을 위해서는 반드시 ORDER BY에 명시된 컬럼이 제일 먼저 읽는 테이블(JOIN이 사용된 경우 드라이빙 테이블)에 속하고, ORDER BY의 순서대로 생성된 인덱스가 있어야 한다.
- WHERE 절에 첫 번째로 읽는 테이블의 컬럼에 대한 조건이 있다면 그 조건과 ORDER BY는 같은 인덱스를 사용할 수 있어야 한다.
- MySQL이 ORDER BY 절에 인덱스를 사용할 수 없을 때 Filesort가 사용된다.

<br>

## `ORDER BY 처리 (Using filesort)`

MySQL 쿼리에서 정말 많이 사용되는 `ORDER BY`는 `인덱스를 이용하는 방법`과 `Filesort 라는 별도의 처리를 이용하는 방법`으로 나눌 수 있습니다.

| --          | 장점                                                                                 | 단점                                                                                     |
|-------------|------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| 인덱스 이용      | INSERT, UPDATE, DELETE 쿼리가 실행될 때 이미 인덱스가 정렬되어 있어서 이 순서대로 읽기만 하면 되어서 매우 빠르다.        | INSERT, UPDATE, DELETE 작업 시 부가적인 인덱스 추가/삭제 작업이 필요하므로 느리다. 인덱스 디스크 공간 및 버퍼 풀 메모리가 필요하다. |
| Filesort 이용 | 인덱스를 생성하지 않아도 되므로 인덱스의 단점이 장점이다. 정렬해야 할 레코드가 많지 않으면 메모리에서 Filesort가 처리되므로 충분히 빠르다. | 정렬 작업이 쿼리 실행 시 처리되므로 레코드 대상 건수가 많아질수록 쿼리의 응답 속도가 느리다.                                  |

<br>

### `소트 버퍼`

MySQL은 정렬을 수행하기 위해 별도의 메모리 공간을 할당받아서 사용하는데, 이 메모리 공간을 `소트 버퍼(Sort Buffer)` 라고 합니다.

소트 버퍼의 공간은 [sort_buffer_size](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_sort_buffer_size) 라는 시스템 변수로 설정할 수 있고, 소트 버퍼를 위한 메모리 공간은 쿼리의 실행이 완료되면 즉시 시스템으로 반납됩니다.

```sql
show variables where Variable_Name like '%sort_buffer%';
```

위의 명령어로 sort_buffer 사이즈를 획인할 수 있습니다.

<br>

### `Filesort 정렬에는 어떤 것이 문제일까?`

정렬해야 할 레코드가 적어서 메모리에 할당된 sort buffer만으로 정렬할 수 있다면 아주 빠르게 정렬이 될 것인데요. 하지만 정렬해야 할 레코드의 수가 sort buffer로 할당된 공간보다 많다면 얘기는 달라집니다.

MySQL은 정렬해야 할 레코드를 여러 조각으로 나눠서 처리하는데, 이 과정에서 `임시 저장을 위해 디스크를 사용`합니다. 즉, 이러한 작업들은 모두 디스크 I/O를 발생시킵니다.

<img width="874" alt="스크린샷 2024-04-11 오후 10 31 49" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/3135b9e2-a9bd-4511-bd41-704024272eca">

위의 그림은 [여기](https://blog.naver.com/seuis398/70039224614)를 참고 했습니다.

MySQL 4.1 이전에는 Filesort 방식은 위처럼 진행 되었다고 하는데요. 이 그림을 보고 `READ REANDOM BUFFER`에서 데이터 파일을 왜 다시 접근하지 라는 생각을 했는데, 이 방식에는 `데이터 Block에 대한 Access가 중복으로 발생한다는 단점`이 설명하고 있습니다.

<br>

<img width="932" alt="스크린샷 2024-04-11 오후 10 39 37" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/93cbe988-56bb-41f6-9e8e-0e7293e72c4e">

즉, 이러한 문제를 해결하기 위해 MySQL 4.1 이후부터는 `Multi-merge` 과정을 거친 후에 결과 파일을 만들고 다시 데이터 파일에 접근하지 않도록 개선되었다고 합니다.

1. 메모리의 sort buffer에서 정렬을 수행하고, 그 결과를 임시로 디스크에 기록합니다.
2. 다음 레코드를 가져와서 다시 정렬해서 반복적으로 디스크에 임시 저장합니다.
3. 각 버퍼 크기만큼 정렬된 레코드를 다시 병합하면서 정렬을 수행해야 하는데, 이 작업을 `멀티 머지(Multi-merge)` 라고 합니다.

<br>

### `Filesort 쿼리 예시`

```sql
SELECT ... FROM single_table ... ORDER BY non_index_column [DESC] LIMIT [M,]N;
```
```sql
SELECT col1, ... FROM t1 ... ORDER BY name LIMIT 10;
SELECT col1, ... FROM t1 ... ORDER BY RAND() LIMIT 15;
```

위와 같이 인덱스가 걸려있지 않은 컬럼을 order by에 사용할 때 `Filesort`가 사용됩니다.

<br>

## `마무리 하며`

이번 글은 간단하게만 정리해보았는데, 기회가 된다면 JOIN 쿼리의 드라이빙, 드리븐에서의 정렬(Order by)에서 임시 테이블, Filesort가 사용되는 과정에 대해서 알아봐도 좋을 것 같습니다.

데이터 수가 적을 때 무심코 사용했던 Order By 였지만 데이터 양이 많을 때 인덱스의 중요성을 한번 더 깨닫게 되는 것 같습니다.

<br>

## `Referenece`

- [Real MySQL - 1]()
- [https://dev.mysql.com/doc/refman/8.0/en/order-by-optimization.html](https://dev.mysql.com/doc/refman/8.0/en/order-by-optimization.html)
- [https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_sort_buffer_size](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_sort_buffer_size)
- [https://blog.naver.com/seuis398/70039224614](https://blog.naver.com/seuis398/70039224614)