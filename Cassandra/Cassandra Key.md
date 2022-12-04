## `Cassandra Key`

### `primary key`

- 한개(row)의 데이터가 유니크하게 보장해주는 Key 를 말하고 1개 이상 필요하다.
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

