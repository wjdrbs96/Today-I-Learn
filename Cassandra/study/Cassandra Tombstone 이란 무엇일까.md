## `Cassandra Tombstone 이란 무엇일까?`

카산드라에서 데이터를 삭제하면 실제로 데이터가 삭제되지 않고, Tombstone 이라는 플래그로 삭제 예정임을 표시합니다. 

Tombstone이 마크된 데이터는 쿼리를 하더라도 데이터가 나타나지 않습니다. 하지만 tombstones이 너무 많으면 카산드라 읽기 성능이 떨어질 수 있다는 점도 알아두어야 합니다.



<br>

- DELETE 쿼리 사용
- TTL 쿼리 사용 
- INSERT or UPDATE 에서 null value 사용
- UPDATE 에서 collection 컬럼 사용
- [materialized views](https://docs.datastax.com/en/cql/dse/docs/developing/materialized-views-overview.html) 사용 => 이건 뭔지 모르겠음

기본적으로 Cassandra는 데이터를 삭제할 때 Tombstone을 생성하게 되는데요. Tombstone이 생성되는 상황을 정리해보면 위와 같습니다.

- Partition tombstones
- Row tombstones
- Range tombstones
- ComplexColumn tombstones
- Cell tombstones
- TTL tombstones

그리고 상황에 따라 위처럼 tombstones이 생성되게 되는데 각각 어떤 특징을 가지고 있는지 하나씩 알아보겠습니다.

참고로 tombstones은 하나 이상의 노드에서 SSTables에 기록되고, 테이블에 설정한 값인 `gc_grace_seconds` 설정된 기간이 지날 때까지 tombstones 으로 남아있으면 [Compaction](https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/how-data-maintain.html#dml-compaction) 과정을 거치면서 tombstones은 삭제됩니다.

<br>

## `테이블 생성 및 데이터 세팅`

```sql
CREATE KEYSPACE gyunny WITH replication = 
{'class': 'SimpleStrategy', 'replication_factor': '1'} AND durable_writes = true;

CREATE TABLE gyunny.rank_by_year_and_name (
    race_year int,
    race_name text,
    rank int,
    cyclist_name text,
    PRIMARY KEY ((race_year, race_name), rank)
) WITH CLUSTERING ORDER BY (rank ASC);

INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2015, 'Tour of Japan - Stage 4 - Gyunny1 > Shinshu', 'Benjamin PRADES', 1);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2015, 'Tour of Japan - Stage 4 - Gyunny2 > Shinshu', 'Adam PHELAN', 2);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2015, 'Tour of Japan - Stage 4 - Gyunny3 > Shinshu', 'Thomas LEBAS', 3);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2015, 'Giro d''Italia - Stage 11 - Gyunny4 > Imola', 'Ilnur ZAKARIN', 1);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2015, 'Giro d''Italia - Stage 11 - Gyunny5 > Imola', 'Carlos BETANCUR', 2);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2014, '4th Tour of Beijing', 'Gyunny6 GILBERT', 1);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2014, '4th Tour of Beijing', 'Gyunny7 MARTIN', 2);
INSERT INTO post_dev.rank_by_year_and_name (race_year, race_name, cyclist_name, rank) VALUES (2014, '4th Tour of Beijing', 'Gyunny8 Esteban CHAVES', 3);

CREATE TABLE cycling.cyclist_career_teams (
    id UUID PRIMARY KEY,
    lastname text,
    teams set<text>
);
```

위처럼 테이블을 생성하고 테스트 데이터를 저장 하고 데이터를 SSTable로 보내기 위해서 flush 명령어를 진행 하겠습니다.

```
/cassandra/bin/nodetool flush
```

<br>

## `Partition tombstones`

```sql
DELETE from post_dev.rank_by_year_and_name 
   WHERE race_year = 2014 AND race_name = '4th Tour of Beijing';
```

rank_by_year_and_name 테이블의 partition key는 `race_year`, `race_name` 입니다. 즉, 위의 쿼리는 해당 파티션을 다 삭제하는 쿼리인데요. 

위처럼 partition을 삭제하는 쿼리를 진행하면 어떻게 tombstones이 남는지 SSTable Dump를 이용해서 확인 해보겠습니다.

```
./cassandra/tools/bin/sstabledump /cassandra/data/{keyspace}/{tableName**}/nb-1-big-Summary.db
```

대략 위의 명령어로 sstable dump를 진행할 수 있다.

<img width="875" alt="스크린샷 2024-05-04 오후 4 37 54" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/369a8055-4b06-4a0f-8401-530034c7c395">

sstable dump를 진행해보면 위와 같이 결과가 나온 것을 볼 수 있습니다. partition 단위로 delete 쿼리를 수행했기 때문에 partition 하위에 `deletion_info`가 존재하고 그 안에 tombstones 표시인 `marked_deleted`가 존재하는 것을 확인할 수 있습니다.

<br>

## `Row tombstones`

Row tombstones은 말 그대로 파티션 내의 특정 row를 명시적으로 삭제될 때 생성됩니다.

```sql
DELETE from gyunny.rank_by_year_and_name 
   WHERE race_year = 2015 
     AND race_name = 'Giro d''Italia - Stage 11 - Gyunny5 > Imola'
     AND rank = 2;
```

위의 쿼리로 특정 row 하나를 삭제하고 flush -> sstable dump 과정을 한번 더 반복 해보겠습니다.

<img width="889" alt="스크린샷 2024-05-04 오후 4 47 27" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/d6876638-3cd4-45ac-a7a3-9ef688cb86b6">

이번에는 rows 하위에 `deletion_info`가 존재하고 그 안에 tombstones 표시인 `marked_deleted`가 존재하는 것을 확인할 수 있습니다.

<br>

## `Range tombstones`

```sql
DELETE from gyunny.rank_by_year_and_name 
   WHERE race_year = 2015 
     AND race_name = 'Tour of Japan - Stage 4 - Gyunny2 > Shinshu' 
     AND rank > 1;
```

위처럼 특정 파티션 안에서 범위 쿼리를 통해서 삭제하는 쿼리는 `Range tombstones`이 생성 되는데요. 이거는 sstable dump 에서 어떻게 표시되는지 확인 해보겠습니다.

![스크린샷 2024-05-04 오후 4 52 48](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/05d030d0-034d-475e-a3d9-801db7be386f)

범위 삭제 쿼리는 `range_tombstone_bound`로 표시되고 tombstones 마크가 남는 것을 볼 수 있습니다.

<br>

## `ComplexColumn tombstones`

```sql
CREATE TABLE gyunny.cyclist_career_teams (
    id UUID PRIMARY KEY,
    lastname text,
    teams set<text>
);
```


<br>

## `Cell tombstones`

```sql
INSERT INTO gyunny.rank_by_year_and_name (
    race_year,
    race_name,
    cyclist_name,
    rank)
VALUES (2018, 'Giro d''Italia - Stage 11 - Osimo > Imola', null, 1);
```

위처럼 null을 포함한 쿼리를 실행 했을 때 어떤 결과가 나오는지 확인 해보겠습니다.

<img width="744" alt="스크린샷 2024-05-04 오후 4 58 41" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/76b7230c-5055-4b7b-8883-eddf69e20332">

이 경우는 null로 넣은 컬럼이 cell 하위에 tombstones 마크가 표시된 것을 볼 수 있습니다.

<br>

## `TTL tombstones`

```sql
INSERT INTO gyunny.cyclist_career_teams (
    id,
    lastname,
    teams
) VALUES (e7cd5752-bc0d-4157-a80f-7523add8dbcd, 'VAN DER BREGGEN', {
     'Rabobank-Liv Woman Cycling Team','Sengers Ladies Cycling Team','Team Flexpoint' }) USING TTL 1;
```

위처럼 TTL을 사용하여 데이터 저장을 진행하고, TTL 시간이 지난 후에 sstable dump 에서는 어떻게 기록이 되어 있는지 확인 해보겠습니다.

<img width="1057" alt="스크린샷 2024-05-04 오후 5 06 23" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/456cc4e0-46fa-4a82-89a3-f3070357f5d9">

위처럼 cell 하위에 tombstones 마크가 표시되어 있는 것을 볼 수 있습니다. 즉, TTL 시간이 만료되면 tombstones 마크가 생성되는 것을 알 수 있습니다.

<br>

```sql
UPDATE gyunny.rank_by_year_and_name USING TTL 1
  SET cyclist_name = 'Cloudy Archipelago' WHERE race_year = 2018 AND 
  race_name = 'Giro d''Italia - Stage 11 - Osimo > Imola' AND rank = 1;
```

![스크린샷 2024-05-04 오후 5 11 27](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/c1f2061d-0b76-4258-a179-e8b66cb45efd)

반면에 Update 쿼리와 함께 TTL을 적용 했을 경우 cyclist_name 컬럼 값이 사라지고 위처럼 만료 되었다는 표시가 생기는 것을 확인할 수 있습니다.  



<br>

## `Referenece`

- [https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/architecture-tombstones.html](https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/architecture-tombstones.html)
- [https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/how-data-maintain.html#dml-compaction](https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/how-data-maintain.html#dml-compaction)