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