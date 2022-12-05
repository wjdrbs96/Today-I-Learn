## `Cassandra Key 개념과 Select 쿼리시 알아두어야 할 것`

이번 글에서는 `Cassandra Key`의 특징과 `cql SELECT 할 때 알아두어야할 것`에 대해서 정리해보려 한다.

<br>

### `Primary key`

- 한개(row)의 데이터가 유니크하게 보장해주는 Key를 말하고 1개 이상 필요하다.
- Partition Key + Clustering Key 로 구성되어 있다.

```sql
create table test_1 (
  key text PRIMARY KEY,
  data text      
);
```

- 기본 Primary Key

```sql
 create table test_2 (
    t_partition_key text,
    t_clustering_key int,
    data text,
    PRIMARY KEY(t_partition_key, t_clustering_key)      
 );
```

- Composite Primary Key

<br>

### `Partition key`

- Primary key의 첫번째 key를 의미함.
- 해시 알고리즘에 따라 클러스터에 분배되어 저장된다.

<br>

### `Clustering key`

- Primary Key에서 Partition Key를 뺀 나머지 Key를 말함.
- 지정한 순서대로 데이터가 저장 됨.

> with clustering order by (test_col_1 desc)

<br>

## `Reference`

- [https://cassandra.apache.org/doc/latest/cassandra/cql/ddl.html#primary-key](https://cassandra.apache.org/doc/latest/cassandra/cql/ddl.html#primary-key)
- [https://www.baeldung.com/cassandra-keys](https://www.baeldung.com/cassandra-keys)

<br>

## `SELECT 쿼리시 알아두어야 할 것`

```sql
CREATE TABLE posts (
    userid text,
    blog_title text,
    posted_at timestamp,
    entry_title text,
    content text,
    category int,
    PRIMARY KEY (userid, blog_title, posted_at)
);
```

```sql
# 올바른 예
SELECT entry_title, content FROM posts
WHERE userid = 'john doe';
```

```sql
# 잘못된 예
SELECT entry_title, content FROM posts
WHERE category = 'Server';
```

<br>

## `2. Partition Key는 단독으로 사용할수 있지만, Clustering Key를 사용할땐, Partition Key를 먼저 조건에 추가해줘야한다.`

```sql
# 올바른 예
SELECT entry_title, content FROM posts
WHERE userid = 'john doe' AND blog_title="Gyunny"
```

```sql
# 잘못된 예
SELECT entry_title, content FROM posts
WHERE blog_title="Gyunny"
```

<br>

## `3. Clustering Key는 Table에 생성시 지정한 Clustering 순서대로 순서대로 사용해야한다.`

```sql
# 올바른 예
SELECT entry_title, content FROM posts
WHERE userid = 'john doe'
  AND blog_title='John''s Blog'
  AND posted_at >= '2012-01-01' AND posted_at < '2012-01-31';
```

```sql
# 잘못된 예
SELECT entry_title, content FROM posts
WHERE userid = 'john doe'
   AND posted_at >= '2012-01-01' AND posted_at < '2012-01-31';
```

<br>

## `Reference`

- [https://cassandra.apache.org/doc/latest/cassandra/cql/dml.html#where-clause](https://cassandra.apache.org/doc/latest/cassandra/cql/dml.html#where-clause)
- [https://log-laboratory.tistory.com/230](https://log-laboratory.tistory.com/230)