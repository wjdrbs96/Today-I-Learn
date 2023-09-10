## `Cassandra Partition Key, Composite Key, and Clustering Key`

```sql
CREATE TABLE application_logs (
  id                    INT,
  app_name              VARCHAR,
  hostname              VARCHAR,
  log_datetime          TIMESTAMP,
  env                   VARCHAR,
  log_level             VARCHAR,
  log_message           TEXT,
  PRIMARY KEY (app_name)
);
```

`app_name`이 `Primary Key` 이자 `Partition Key` 라고 할 수 있다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/d990fe94-7364-4f2c-beb1-3560d3897d26)

`파티션 키(app_name)`의 해시 값을 생성하고 행 데이터를 노드 내 파티션 범위에 할당한다.

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/8742cf92-0e0f-4430-89ab-3b0309540342)

이렇게 데이터는 파티션 범위에 해당하는 서버인 노드에 저장된다. 

<br>

```sql
CREATE TABLE application_logs (
  id                    INT,
  app_name              VARCHAR,
  hostname              VARCHAR,
  log_datetime          TIMESTAMP,
  env                   VARCHAR,
  log_level             VARCHAR,
  log_message           TEXT,
  PRIMARY KEY ((app_name, env))
);
```

`Primary Key` 안에 `()` 로 묶어서 `Composite Key` 형태로 사용할 수 있다. 즉, `app_name, env` 가 `Partition Key`가 된다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/03ac3ef6-cdb9-435b-8c64-674fbd8636d1)

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/ca1f901b-3e11-43be-982f-2f35f6c06de0)

```sql
select * application_logs where app_name = 'app1' and env = 'prod';
```

카산드라 테이블을 조회할 때 파티션 키를 `where` 절에 넣어주지 않으면 모든 노드를 조회하여 엄청 비효율적인 스캔이 되기 때문에 파티션 키를 지정해서 조회하는 것이 필요하다.

<br>

<br>

```sql
CREATE TABLE application_logs (
  id                    INT,
  app_name              VARCHAR,
  hostname              VARCHAR,
  log_datetime          TIMESTAMP,
  env                   VARCHAR,
  log_level             VARCHAR,
  log_message           TEXT,
  PRIMARY KEY ((app_name, env), hostname, log_datetime)
);
```

`Clusterting Key`는 옵셔널한 값인데, `Partition Key` 다음에 나오는 값이 `Clusterting Key` 라고 할 수 있다.

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1fddabbe-60f5-40dc-936f-97deeed881e2)

즉, `Partition Key`가 같으면 `hostname`, `log_datetime` 순서로 정렬 키가 되는 것이다.

<br>

```sql
CREATE TABLE application_logs (
  id                    INT,
  app_name              VARCHAR,
  hostname              VARCHAR,
  log_datetime          TIMESTAMP,
  env                   VARCHAR,
  log_level             VARCHAR,
  log_message           TEXT,
  PRIMARY KEY ((app_name,env), hostname, log_datetime)
) 
WITH CLUSTERING ORDER BY (hostname ASC, log_datetime DESC);
```

위처럼 정렬 조건을 설정할 수도 있다.

<br>

## `Referenece`

- [https://www.baeldung.com/cassandra-keys](https://www.baeldung.com/cassandra-keys)