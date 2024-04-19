## `Cassandra Insert와 Update는 어떻게 동작할까?`

카산드라는 append 모델을 사용하기 때문에 INSERT와 UPDATE 작업 사이에 근본적인 차이가 없습니다. 

기존 행에 존재하는 PK와 동일한 데이터를 INSERT 하면 UPDATE가 되고 UPDATE를 했는데 PK가 존재하지 않으면 신규 row를 생성한다는 특징을 가지고 있습니다.

실제 테스트를 해보면서 INSERT, UPDATE 각각 어떤 특징을 가지고 있는지 좀 더 알아보겠습니다. (테스트 Cassandra 버전: 4.0.6)

```sql
CREATE TABLE experiment ( id text PRIMARY KEY, info text);
```

카산드라 테이블 하나를 생성 하겠습니다. 그리고 2가지 경우의 쿼리를 실행 해보겠습니다.

<br>

## `Part1`

```sql
INSERT INTO experiment (id, info) VALUES ('Gyunny', 'Gyunny is a cat');

UPDATE experiment set info = 'Gyuns is a house mouse' where id = 'Gyuns';

SELECT * FROM experiment
```

<img width="290" alt="스크린샷 2024-04-19 오후 10 45 31" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/d1e7231d-1b63-4f6e-8327-e31824d4cb4d">

INSERT, UPDATE 쿼리를 실행하고 데이터를 조회해보면 위처럼 저장되어 있는 것을 볼 수 있습니다. (INSERT는 신규 생성이고, UPDATE 쿼리의 id는 기존에 존재하지 않는 PK기 때문에 신규 생성)

```
nodetool flush

sstabledump nb-1-big-Summary.db
```

그리고 Memtable에 존재하는 데이터를 SSTable로 저장하기 위해서 flush 시키고 sstable dump를 통해서 좀 더 자세히 확인 해보겠습니다. 

<br>

<img width="891" alt="스크린샷 2024-04-19 오후 10 42 12" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1b5e02f8-5fc8-4ea4-b3ab-ba08d4985fd9">

- `INSERT 쿼리`: row 하위에 liveness_info인 tstamp 필드 존재
- `UPDATE 쿼리`: liveness_info 필드가 없고 cells 하위에 liveness_info인 tstamp 필드 존재

INSERT 쿼리는 row 하위에 liveness_info를 설정하고 UPDATE 명령은 cell 하위에 liveness_info를 설정한다는 차이점이 존재합니다.

위의 특징은 이번 글에서 아주 중요한 내용인데 INSERT, UPDATE 쿼리의 차이점을 기억하고 Part2 쿼리를 실행 해보겠습니다.

<br>

## `Part2`

```sql
INSERT INTO experiment (id, info) VALUES ('Gyunny', NULL);

UPDATE experiment set info = NULL where id = 'Gyuns';

SELECT * FROM experiment;
```

<img width="176" alt="스크린샷 2024-04-19 오후 10 53 59" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/f05a1f33-a508-4687-bc96-9ea5f09d7ba5">

이번에는 위와 같이 NULL을 저장하는 쿼리로 위처럼 실행 하였는데 UPDATE 쿼리의 결과는 사라진 것을 볼 수 있는데요. 이렇게 결과가 나온 이유는 무엇일까요?

```
nodetool flush

sstabledump nb-1-big-Summary.db
```

그래서 이번에 한번 더 sstable dump를 진행 해보았습니다.

<br>

<img width="786" alt="스크린샷 2024-04-19 오후 10 59 12" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/40539241-7903-4655-9f01-ee3ec3b9fedd">

INSERT, UPDATE로 실행 했던 필드 모두 cells 하위에 deletion_info 필드가 업데이트 된 것을 볼 수 있습니다.

그리고 liveness_info와 tstamp 필드는 모두 업데이트 된 시점으로 변경된 것을 볼 수 있습니다.

즉, 카산드라는 PK가 아닌 필드에 모두 NULL로 설정되어 있어도 row 레벨의 만료되지 않는 liveness_info가 있는 경우 row가 자동으로 삭제되지 않는다는 특징이 있습니다.

<br>

### `Part3`

```sql
INSERT INTO experiment (id, info) VALUES ('GyunnyCassandra', 'a cat');

UPDATE experiment USING TTL 50 set info = 'GyunnyCassandra is my friend' where id = 'GyunnyCassandra';

// after 50 seconds
SELECT * FROM experiment;
```

<img width="416" alt="스크린샷 2024-04-19 오후 11 27 00" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/61dd24f6-2643-4481-be56-07a570eaeced">

쿼리에 TTL을 설정하고 실행하면 위처럼 저장이 잘 될텐데요. 50초가 지난 후에 조회해보면 어떻게 될까요?

<br>

```
nodetool flush

sstabledump nb-1-big-Summary.db
```

<img width="1492" alt="스크린샷 2024-04-19 오후 11 23 50" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/97146a01-32b5-4072-b4da-066cc3c68983">

TTS 설정을 했을 때는 cells 레벨 하위에 expires_at 필드가 추가된 것을 볼 수 있습니다.

<br>

<img width="221" alt="스크린샷 2024-04-19 오후 11 28 10" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1e4f299f-cb07-4551-a7d4-ecc9e6c03554">

결과는 50초 이후에 info 필드가 null로 변경됩니다. 이유는 rows 레벨 하위에 liveness_info가 존재하기 때문에 cells 필드에 설정된 TTL 시간이 지나면 null로 업데이트만 되는 것입니다.

<br>

### `Part4`

```sql
UPDATE experiment USING TTL 50 set info = 'GyunnyTTLUpdate is my friend' where id = 'GyunnyTTLUpdate';

// after 50 seconds
SELECT * FROM experiment;
```

<img width="417" alt="스크린샷 2024-04-19 오후 11 35 36" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/35b99fd9-9138-40d2-a873-f897a3dbc112">

PK에 GyunnyTTLUpdate 값이 없기 때문에 저장이 된 것을 볼 수 있습니다.

<br>

```
nodetool flush

sstabledump nb-1-big-Summary.db
```

![스크린샷 2024-04-19 오후 11 34 41](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/a3c0a368-88ce-4aa2-8952-f959c3cb4c8a)

UPDATE 쿼리를 통해서 저장이 된 것이기 때문에 위에서 정리 했던 내용처럼 rows 레벨 하위에 liveness_info가 없는 것을 볼 수 있습니다.

이런 경우에 50초가 지난 후에 어떻게 될까요?

<br>

<img width="243" alt="스크린샷 2024-04-19 오후 11 36 42" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/fd79f0b8-7477-421a-8472-d3a9f053d973">

rows 레벨에 liveness_info가 없기 때문에 50초가 지난 후에 GyunnyTTLUpdate PK를 가진 row가 삭제된 것을 볼 수 있습니다.

<br>

## `요약`

- 기존 행에 존재하는 PK와 동일한 데이터를 INSERT 하면 UPDATE가 되고m UPDATE를 했는데 PK가 존재하지 않으면 신규 row를 생성
- INSERT 쿼리는 row 레벨의 liveness_info를 설정하고 UPDATE 쿼리는 cell 레벨의 liveness_info를 설정
- `PK가 아닌 필드에 모두 NULL로 설정되어 있어도 row 레벨의 만료되지 않는 liveness_info가 있는 경우 row가 자동으로 삭제되지 않음`

<br>

## `Reference`

- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/tools/toolsFlush.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/tools/toolsFlush.html)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/tools/ToolsSSTabledump.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/tools/ToolsSSTabledump.html)
- [https://medium.com/@HobokenDays/cassandra-commands-unveiled-exploring-the-nuances-of-update-insert-39fdb06eadb0](https://medium.com/@HobokenDays/cassandra-commands-unveiled-exploring-the-nuances-of-update-insert-39fdb06eadb0)