# `MySQL InnoDB Index 어디까지 알고 계시나요?`

- [인덱스란?]()
  - [Primary Key, Secondary Key]()
  - [인덱스 장단점]()
  - [B-Tree 인덱스]()
  - [InnoDB B-Tree Index]()
- [B-Tree 인덱스 키 추가 및 삭제]()
  - [인덱스 키 추가]()
  - [인덱스 키 삭제]()
  - [인덱스 키 변경]()
  - [인덱스 키 검색]()
- [버퍼 풀(Buffer Pool)과 체인지 버퍼(Change Buffer)란?]()
  - [버퍼 풀(Buffer Pool)]()
  - [체인지 버퍼(Change Buffer)]()
  - [B-Tree 인덱스 사용에 영향을 미치는 요소]()
  - [인덱스 키 값의 크기]()
  - [B-Tree는 자식 노드를 얼마나 가질 수 있을까?]()
  - [B-Tree 깊이]()
  - [선택도(기수성)]()
- [B-Tree 인덱스를 통한 데이터 읽기]()
  - [인덱스 레인지 스캔]()
  - [커버링 인덱스]()
  - [인덱스 풀 스캔]()
  - [루스 인덱스 스캔]()
  - [인덱스 스킵 스캔]()
- [다중 컬럼(Multi-column) 인덱스]()
- [B-Tree 인덱스의 가용성과 효율성]()
  - [비교 조건의 종류와 효율성]()
  - [인덱스의 가용성]()
  - [가용성과 효율성 판단]()
- [클러스터링 인덱스]()
  - [Clustered Key vs Non Clustered Key]()
  - [클러스터링 인덱스 장단점]()
- [해시(Hash) 인덱스란?]()
- [정리하며]()
  - [MySQL InnoDB에서 B-Tree 인덱스를 사용하는 이유는 무엇일까?]()
  - [B-Tree 단점은 없을까?]()

이번 글에서는 MySQL InnoDB Index에 대해서 정리 해보겠습니다. 잘못된 점이 있거나 피드백 주실 부분이 있다면 많은 피드백, 의견 부탁 드리겠습니다.

<br>

## `인덱스란?`

인덱스 예시의 대표적인 것은 `책의 맨 끝에 찾아보기`가 있습니다. 각각 몇페이지에 존재하는지가 나와 있는데, 이것이 실제 데이터 피일에 저장된 레코드의 주소에 비유할 수 있습니다.

책에서 페이지를 직접 찾기에는 시간이 오래 걸리는 것처럼 DBMS도 테이블의 모든 데이터를 검색해서 원하는 결과를 가져오려면 시간이 오래 걸립니다. 그래서 컬럼의 값과 해당 레코드가 저장된 주소를 key-value 형태로 저장하여 인덱스를 만들어 두는 것입니다.

`DBMS 인덱스에서 중요한 특징은 정렬 되어 있다는 것입니다.`

<br>

### `Primary Key, Secondary Key`

- Primary Key는 테이블의 레코드를 대표하는 값으로 만들어진 인덱스 (null, 중복 허용 X)
- Secondary Key는 Primary Key를 제외한 나머지 모든 인덱스는 Secondary Index 이다.

<br>

### `인덱스 장단점`

- `장점`: `정렬되어 있는 인덱스를 통해서 데이터를 빠르게 가져올 수 있다.`
- `단점`: `저장, 수정이 발생할 때마다 인덱스 데이터를 정렬하여야 하기 때문에 쓰기 성능이 떨어진다.`

인덱스도 커지게 되면 데이터를 읽는게 시간이 오래 걸리기 때문에 읽기 성능, 쓰기 성능을 고려하여 설계할 필요가 있습니다.

<br>

### `B-Tree 인덱스`

B-Tree는 데이터베이스의 인덱싱 알고리즘 가운데 가장 일반적으로 사용되고, 가장 먼저 도입된 알고리즘 입니다.

B는 Binary가 아니라 Balanced 라는 것을 알아야 합니다. ([B-Tree](https://en.wikipedia.org/wiki/B-tree)) B-Tree를 사용하여 인덱스 구조체 내에서 항상 정렬된 상태로 유지합니다.

<img width="654" alt="스크린샷 2024-02-18 오후 8 08 05" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/0af525d9-225d-4af2-95a9-39457fef8ba8">

최상단에 존재하는 것을 `루트 노드(Root node)` 라고 하고, 가장 하위에 있는 노드를 `리프 노드(Leat node)` 라고 합니다. 루트 노드, 리프 노드도 아닌 중간에 존재하는 노드를 `브랜치 노드(Branch node)` 라고 합니다.

참고로 데이터 파일의 레코드는 INSERT된 순서로 저장되지 않습니다. 만약 테이블의 레코드를 전혀 삭제하거나 변경하지 않고 INSERT만 한다면 맞을 수도 있습니다.

하지만 레코드가 삭제되너 빈 공간이 생기면 그 다음의 INSERT는 가능한 삭제된 공간을 재활용하도록 설계되어 있습니다.

<br>

### `InnoDB B-Tree Index`

<img width="669" alt="스크린샷 2024-02-18 오후 8 08 43" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1853a270-935d-44d7-ba46-22b95b28a114">

InnoDB 테이블에서 인덱스를 통해 데이터를 읽을 때는 리프 노드에서 바로 데이터 파일을 찾아가지 못합니다. 위의 사진에서 리프 노드에 저장되어 있는 주소는 Primary Index가 위치한 주소를 의미하고, Primary Index B-Tree를 다시 탐색하여 데이터 파일에 접근해야 합니다.

- 리프 노드(Leaf Node) -> Primary Index -> 데이터 파일

<br>

## `B-Tree 인덱스 키 추가 및 삭제`

### `인덱스 키 추가`

B-Tree에 저장될 때는 저장될 키 값을 이용해 B-Tree상의 적절한 위치를 검색해야 합니다. 저장될 위치가 결정되면 레코드의 키 값과 대상 레코드의 주소 정보를 B-Tree의 리프 노드에 저장합니다.

만약 리프 노드가 꽉찬다면 리프 노드가 분리 되어야 하는데, 이는 상위 브랜치 노드까지 처리의 범위가 넓어지기에 상대적으로 비용이 많이드는 것으로 알려져 있습니다.

비용이 비싼 것은 디스크로부터 인덱스 페이지를 읽고 쓰기를 하기 때문 인데요. InnoDB 스토리지 엔진은 이 작업을 효율적으로 하기 위해서 필요하다면 인덱스 키 추가 작업을 지연시켜 나중에 처리할 수 있습니다.

`하지만 프라이머리 키나 유니크 인덱스의 경우 중복 체크가 필요하기 때문에 즉시 B-Tree에 추가하거나 삭제합니다.`

<br>

### `인덱스 키 삭제`

해당 키 값이 저장된 B-Tree의 리프 노드를 찾아서 그냥 삭제 마크만 하면 작업이 완료되기 때문에 상당히 간단하다. 인덱스 키 삭제로 인한 마킹 작업 또한 디스크 쓰기가 필요하므로 이 작업 역시 디스크 I/O가 필요한 작업입니다.

MySQL 5.5 이상 버전의 InnoDB 스토리지 엔진에서는 이 작업 또한 버퍼링 되어 지연 처리될 수도 있습니다.

<br>

### `인덱스 키 변경`

인덱스의 키 값은 그 값에 따라 저장될 리프 노드의 위치가 결정되므로 B-Tree의 키 값이 변경되는 경우에는 단순히 인덱스상의 키 값만 변경하는 것은 불가능합니다.

B-Tree의 키 값 변경 작업은 먼저 키 값을 삭제한 후, 다시 새로운 키 값을 추가하는 형태로 처리됩니다.

InnoDB 스토리지 엔진을 사용하는 테이블에 대해서는 이 작업 모두 [체인지 버퍼](https://dev.mysql.com/doc/refman/8.0/en/innodb-change-buffer.html)를 활용해 지연 처리 될 수 있습니다.

<br>

### `인덱스 키 검색`

INSERT, UPDATE, DELETE 작업을 할 때 인덱스 관리에 따르는 추가 비용을 감당하면서 인덱스를 구축하는 이유는 빠른 검색을 위해서 입니다.

B-Tree의 루트 노드부터 시작해 브랜치 노드를 거쳐 최종 리프 노드까지 이동하면서 비교 작업을 수행하는데, 이 과정을 `트리 탐색` 이라고 합니다.

<br>

## `버퍼 풀(Buffer Pool)과 체인지 버퍼(Change Buffer)란?`

이번 글은 인덱스에 대해서 정리하는 글이기 때문에 `체인지 버퍼(Change Buffer)`와 `버퍼 풀(Buffer Pool)`에 대해서 간단하게 알아보겠습니다.

<br>

### `버퍼 풀(Buffer Pool)`

디스크 데이터 파일이나 인덱스 정보를 메모리에 캐시해 두는 공간이며, 쓰기 작업을 지연시켜 일광 작업으로 처리할 수 있게 해주는 버퍼 역할도 같이 합니다. 

INSERT, UPDATE, DELETE 처럼 데이터를 변경하는 쿼리는 데이터 파일의 이곳저곳에 위치한 레코드를 변경하기 때문에 랜덤한 디스크 작업을 발생시킵니다. 하지만 버퍼 풀이 이러한 변경된 데이터를 모아서 처리하면 랜덤한 디스크 작업의 횟수를 줄일 수 있습니다.

좀 더 자세한 것을 알고 싶다면 [Buffer Pool](https://dev.mysql.com/doc/refman/8.0/en/innodb-buffer-pool.html)에 대해서 좀 더 알아보면 좋을 것 같습니다.

<br>

### `체인지 버퍼(Change Buffer)`

![12313123123](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1edead80-de8c-42d2-a296-f35c6629d022)

RDBMS에서 레코드가 INSERT 되거나 UPDATE 될 때는 데이터 파일을 변경하는 작업 뿐 아니라 해당 테이블에 포함된 인덱스를 업데이트 하는 작업도 필요합니다. 

그런데 인덱스를 업데이트 하는 작업은 랜덤하게 디스크를 읽는 작업이 필요하므로 테이블에 인덱스가 많다면 작업에 부담이 생길 것입니다.

그래서 InnoDB는 변경해야 할 인덱스 페이지가 버퍼 풀에 있으면 바로 업데이트를 수행하지만, 디스크로 읽어와서 업데이트 해야 한다면 이를 즉시 실행하지 않고 임시 공간에 저장해 두고 한번에 처리하는 방식을 사용합니다.

이 때 사용하는 `임시 메모리 공간을 체인지 버퍼(Change Buffer)` 라고 합니다. 참고로 반드시 중복 여부를 체크해야 하는 유니크 인덱스는 체인지 버퍼를 사용할 수 없습니다.

좀 더 자세한 것을 알고 싶다면 [Change Buffer](https://dev.mysql.com/doc/refman/8.0/en/innodb-change-buffer.html)에 대해서 좀 더 알아보면 좋을 것 같습니다. 

<br>

### `B-Tree 인덱스 사용에 영향을 미치는 요소`

B-Tree 인덱스는 인덱스를 구성하는 컬럼의 크기와 레코드의 건수, 그리고 유니크한 인덱스 키 값의 개수 등에 의해 검색이나 변경 작업의 성능이 영향을 받는다.

<br>

### `인덱스 키 값의 크기`

InnoDB 스토리지 엔진은 디스크에 데이터를 저장하는 가장 기본 단위를 페이지(Page) 또는 블록(Block) 이라고 하며, 디스크의 모든 읽기 및 쓰기 작업의 최소 작업 단위가 됩니다.

페이지는 `버퍼 풀(Buffer Pool)`에서 관리하는 기본 단위이기도 합니다. 인덱스도 페이지 단위로 관리되며 B-Tree에서 루트, 브랜츠, 리프 노드를 구분한 기준이 바로 `페이지` 단위 입니다.

<br>

### `B-Tree는 자식 노드를 얼마나 가질 수 있을까?`

B-Tree의 자식 노드 개수는 인덱스 페이지 크기와 키 값의 크기에 따라 결정됩니다. MySQL 5.7 버전 부터는 InnoDB 스토리지 엔진의 페이지 크기를 [innodb_page_size](https://dev.mysql.com/doc/refman/8.0/en/innodb-init-startup-configuration.html) 시스템 변수를 이용해 4KB ~ 64KB 사이의 값을 선택할 수 있지만 `기본 값은 16KB` 입니다.

B-Tree 그림에서 페이지에 `인덱스 키`, `자식 노드 주소`를 담고 있는 것을 볼 수 있습니다.

인덱스 키를 16바이트, 주소 값을 12바이트라고 가정하고 계산해보면 인덱스 페이지(16KB)는 `16 * 1024(=KB) / (16 + 12) = 585개` 저장할 수 있습니다. 즉, 자식 노드를 585개 가질 수 있는 B-Tree 라는 것을 의미합니다.

키 값이 16 -> 32바이트로 늘어나면 한 페이지에 인덱스 키를 `16 * 1024 / (32 + 12) = 372개` 저장할 수 있습니다. 레코드 500개를 읽어올 때 전자의 경우 인덱스 페이지 하나로 읽어올 수 있지만, 후자의 경우 최소한 2번 이상 디스크로부터 읽어와야 합니다. 

결국 인덱스를 구성하는 키 값의 크기가 커지면 가질 수 있는 자식 노드 수가 줄어 디스크로부터 읽어와야 하는 횟수가 늘어나서 그만큼 느려질 수 있습니다. (버퍼풀, 체인지 버퍼 등등 있겠지만.. 결국 B-Tree 탐색이 더 필요한 것이니..)

<br>

### `B-Tree 깊이`

인덱스 키가 16바이트인 경우에는 depth = 3 일 때 최대 2억(585 * 585 * 585)개 정도 인덱스 페이지를 가질 수 있지만, 키 값이 32바이트 라면 5천만(372 * 372 * 372)개로 줄어듭니다.

결론적으로 인덱스 키 값의 크기가 커지면 커질수록 하나의 인덱스 페이지가 담을 수 있는 인덱스 키 값의 개수가 적어지기 때문에, 같은 레코드 건수라 하더라도 B-Tree 깊이가 깊어져서 디스크 읽기가 더 많이 필요하게 됩니다. (인덱스 자체도 디스크에 존재하니 B-Tree 탐색 시간이 더 늘어나기 때문이 아닐까 함)

그리고 만약에 B-Tree가 Binary Tree 였다면 Depth가 어마어마하게 깊었을 것이고 검색하는게 시간이 엄청나게 걸렸을 것이고 사용할 수 없었을 것입니다. 

<br>

### `선택도(기수성)`

인덱스에서 `선택도/기수성(Cardinality)`는 모든 인덱스 키 값 가운데 유니크한 값의 수를 의미합니다. 

인덱스 키 값 가운데 중복된 값이 많아질수록 `Cardinality` 낮아집니다. 즉, `Cardinality`가 높을수록 검색 대상이 줄어들기 때문에 그만큼 빠르게 처리됩니다.

<br>

## `B-Tree 인덱스를 통한 데이터 읽기`

[총 데이터 수 10,000건]

- 케이스 A: country 컬럼의 유니크한 값의 개수가 10개 (country 별로 1000건 있다고 가정)
- 케이스 B: country 컬럼의 유니크한 값의 개수가 1,000개 (country 별로 10건 있다고 가정)

```sql
mysql> SELECT *
       FROM tb_test
       WHERE country = 'KOREA' AND city = 'SEOUL'
```

만약에 위의 쿼리 조건에 맞는 케이스가 A, B 각각 1건 존재한다면 어떤 경우가 더 효율적일까요?

- country 컬럼의 유니크 값이 10개일 때
  - 케이스 A를 보면 1,000건의 데이터를 읽어와서 그 중에 1건을 찾아야 합니다. 999건의 레코드를 더 읽었다고 할 수 있습니다.
- country 컬럼의 유니크 값이 1,000개일 때
  - 반면에 케이스 B는 10건 중에 1건을 찾으면 되기 때문에 9건의 데이터만 더 읽었다고 할 수 있습니다. 

이처럼 인덱스에서 유니크한 값의 개수는 인덱스 쿼리의 효율성에 큰 영향을 미치게 됩니다.

<br>

### `읽어야 하는 레코드의 건수`

인덱스를 통해 테이블의 데이터를 읽는 것은 인덱스를 거치지 않고 바로 테이블의 레코드를 읽는 것보다 높은 비용이 드는 작업입니다.

인덱스를 통해 읽어야 할 레코드의 건수가 전체 테이블 레코드의 20 ~ 25%를 넘어서면 인덱스를 이용하지 않고 테이블을 모두 직접 읽어서 필요한 레코드만 가려내는 방식으로 처리하는 것이 효율적입니다.(MySQL 옵티마이저는 인덱스를 이용하지 않고 직접 테이블을 처음부터 끝까지 읽어서 처리할 것임)

<br>

## `B-Tree 인덱스를 통한 데이터 읽기`

### `인덱스 레인지 스캔`

![스크린샷 2024-02-18 오후 10 19 34](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/e3b53442-fec5-44de-9333-9b16baf5f78b)

```sql
mysql> SELECT *
       FROM employees
       WHERE name BETWEEN 'Ilia' And 'NaNa'
```

인덱스 레인지 스캔은 위의 쿼리처럼 검색해야 할 인덱스의 범위가 결정되었을 때 사용하는 방식입니다. 

<br>

<img width="924" alt="스크린샷 2024-02-18 오후 10 29 02" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4d63ab2d-f866-4691-8be5-18357d1e07bc">

인덱스 리프 노드에서 검색 조건에 일치하는 건들은 데이터 파일에서 레코드를 읽어오는 과정이 필요합니다. 리프 노드에서 저장된 레코드 주소로 데이터 파일의 레코드를 읽어오는데, 레코드 한 건 한 건 단위로 랜덤 I/O가 한 번씩 발생합니다.

이러한 이유 때문에 인덱스를 통해 데이터 레코드를 읽는 작업은 비용이 많이 드는 작업으로 분류 되고, 인덱스를 통해 읽어야 할 데이터 레코드가 20 ~ 25퍼가 넘으면 인덱스를 통한 읽기보다 테이블의 데이터를 직접 읽는 것이 더 효율적인 처리 방식이 됩니다.

1. 인덱스 조건을 만족하는 값이 저장된 위치를 찾는다.
2. 1번에서 탐색된 위치부터 필요한 만큼 인덱스를 쭉 읽고, 이 과정을 인덱스 스캔 이라고 합니다.
3. 2번에서 읽어 들인 인덱스 키와 레코드 주소를 이용해 레코드가 저장된 페이지를 가져오고, 최종 레코드를 읽어온다.

인덱스를 통해 데이터를 가져올 때 빠르게 찾아갈 수는 있겠지만 랜덤 I/O가 빈번하게 발생하는 구조입니다.

인덱스는 데이터를 효율적으로 찾는 방법이지만, MySQL InnoDB의 경우 인덱스안에 포함된 데이터가 존재하기 때문에, 쿼리에 인덱스 데이터가 다 포함되어 있다면 실제 데이터까지 접근할 필요가 없습니다.

이처럼 쿼리를 충족시키는 데 필요한 모든 데이터를 갖고 있는 인덱스를 `커버링 인덱스 (Covering Index 혹은 Covered Index)` 라고 합니다.

커버링 인덱스로 처리되는 쿼리는 디스크의 레코드를 읽지 않아도 되기 때문에 랜덤 읽기가 상당히 줄어들고 성능은 그만큼 빨라집니다.

<br>

### `커버링 인덱스`

- 데이터 추출을 인덱스에서만 수행하는 것을 커버링 인덱스라고 합니다. ([참고 Link](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_covering_index))

<br>

### `인덱스 풀 스캔`

인덱스의 처음부터 끝까지 모두 읽는 방식을 `인덱스 풀 스캔`이라고 합니다. 대표적으로 쿼리의 조건절에 사용된 컬럼이 인덱스의 첫 번째 컬럼이 아닌 경우 인덱스 풀 스캔 방식이 사용됩니다.

예를 들어, 인덱스는 (A, B, C) 컬럼의 순서로 만들어져 있지만 쿼리의 조건절은 B, C 컬럼으로 검색하는 경우 입니다.

<br>

### `루스 인덱스 스캔`

루트 인덱스 스캔이란 듬성듬성하게 인덱스를 읽는 것을 의미합니다. 중간에 필요치 않은 인덱스 키 값은 무시하고 다음으로 넘어가는 형태로 처리합니다.

```sql
mysql> SELECT dept_no, MIN(emp_no)
       FROM dept_emp
       WHERE dept_no BETWEEN 'd002' AND 'd004'
       GROUP BY dept_no;
```

위의 쿼리처럼 `GROUP BY` 함수 또는 `MIN`, `MAX` 집계 함수에서 루스 인덱스 스캔을 사용하면 최적화 할 수 있습니다.

<img width="637" alt="스크린샷 2024-02-19 오전 7 03 11" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4e25f442-21ec-4aaf-bbaf-da43e05585c5">

위의 그림을 보면 `d002` 일 때 최소의 `emp_no` 하나만 찾고 나머지를 스킵하고 `d003`을 스캔하고 있는 것을 볼 수 있습니다. `d003` 일 때도 마찬가지로 최소 `emp_no`만 찾고 다음으로 넘어가게 되는데, 이것을 `루스 인덱스 스캔` 이라고 합니다.

<br>

### `인덱스 스킵 스캔`

인덱스는 정렬 되어 있고, 인덱스를 구성하는 컬럼의 순서가 매우 중요하다는 것은 잘 알고 있을 것입니다.

```sql
mysql ALTER TABLE employees
        ADD INDEX ix_gender_birthdate (gender, birth_date);
```

```sql
// 인덱스를 사용하지 못하는 쿼리
mysql> SELECT * FROM employees WHERE birth_date >= '1965-02-01';

// 인덱스를 사용할 수 있는 쿼리
mysql> SELECT * FROM employees WHERE gender = 'M' AND birth_date >= '1965-02-01';
```

인덱스가 `gender, birth_date` 순서로 생성되어 있기 때문에 첫 번째 쿼리는 인덱스를 사용할 수 없다는 것을 알 수 있습니다. 인덱스를 사용하려면 `birth_date` 컬럼부터 시작하는 인덱스를 새로 생성해야 합니다.

`MySQL 8.0 버전부터는 옵티마이저가 gender 컬럼을 건너뛰어서 birth_date 컬럼만으로도 인덱스 검색이 가능하게 해주는 인덱스 스킵 스캔(index sip scan) 최적화 기능이 도입 되었습니다.`

```sql
mysql> SET optimizer_switch='sip_scan=on'
    
mysql> EXPLAIN
       SELECT gender, birth_date,
       FROM employees
       WHERE birth_date >= '1965-02-01';
```

위처럼 `sip_scan=on` 으로 설정을 해주면 `인덱스 스킨 스캔` 기능을 사용할 수 있습니다.

<img width="714" alt="스크린샷 2024-02-19 오전 7 09 49" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4d3761c3-e94e-4ab0-b3f8-5e5751d2d8ae">

MySQL 옵티마이저는 gender 컬럼에서 유니크한 값을 모두 조회해서 주어진 쿼리에 gender 컬럼의 조건을 추가해서 쿼리를 다시 실행하는 형태로 처리합니다. 

```sql
mysql> SELECT gender, birth_date FROM employees WHERE gedner = 'M' AND birth_date >= '1965-02-01';
mysql> SELECT gender, birth_date FROM employees WHERE gedner = 'F' AND birth_date >= '1965-02-01';
```

gender 경우 `M`, `F` 값을 가지고 있기 때문에 옵티마이저는 2개의 값으로 쿼리를 실행합니다. 이 기능을 보자마자 그러면 유니크한 값이 많은 컬럼이면 어떻게 될까? 라는 생각이 들었는데요.

- WHERE 조건절에 조건이 없는 인덱스의 선행 컬럼의 유니크한 값의 개수가 적어야 함
- 쿼리가 인덱스에 존재하는 컬럼만으로 처리 가능해야 함 (커버링 인덱스)

해당 기능은 유니크한 값이 많다면 MySQL 옵티마이저는 인덱스에서 스캔해야 할 시작 지점을 검색하는 작업이 많이 필요해져, 오히려 성능이 떨어질 수 있습니다.

<br>

## `다중 컬럼(Multi-column) 인덱스`

<img width="565" alt="스크린샷 2024-02-21 오후 10 52 06" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/beb97b77-8103-43ad-a4ac-3081e96715a7">

실제 서비스를 운영하다보면 하나의 인덱스를 사용하는 경우보다 2개 이상의 컬럼을 포함하는 인덱스를 더 많이 사용합니다. 인덱스가 두 개 이상이라면 `두 번째 컬럼은 첫 번째 컬럼에 의존해서 정렬되어 있다는 것`을 기억해야 합니다.

인덱스의 설정 순서대로 정렬이 되기 때문에 다중 컬럼 인덱스에서는 인덱스 내에서 각 컬럼의 위치(순서)가 상당히 중요합니다.

<br>

## `B-Tree 인덱스의 가용성과 효율성`

쿼리의 WHERE 조건이나 GROUP BY, 또는 ORDER BY 절이 어떤 경우에 인덱스를 사용할 수 있고 어떤 방식으로 사용할 수 있는지 알고 있어야 합니다.

<br>

### `비교 조건의 종류와 효율성`

```sql
mysql> SELECT * FROM dept_emp
       WHERE dept_no = 'd002' AND emp_no >= 10114;
```

- 케이스 A: INDEX (dept_no, emp_no)
- 케이스 B: INDEX (emp_no, dep_no)

위의 쿼리를 실행하는데 케이스 A, B 처럼 인덱스가 존재할 때 각각 어떻게 실행되는지 한번 알아보겠습니다.

<img width="814" alt="스크린샷 2024-02-21 오후 11 20 41" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/d09c39bd-1664-49c1-a858-9937a016b5e0">

케이스 A 인덱스는 `dep_no가 d002가 아닐 때까지 인덱스를 그냥 쭉 읽기만 하면` 됩니다. 즉, 조건을 만족하는 레코드가 5건이라고 할 때, 5건의 레코드를 찾는 데 꼭 필요한 5번의 비교 작업만 수행하여 효율적으로 인덱스를 이용한 것을 볼 수 있습니다.

하지만 케이스 B 인덱스는 `d002가 아닌 컬럼까지 비교를 해야하기 때문에 케이스 A보다 더 많은 비교`를 해야 합니다. 

위에서 설명했던 것처럼 인덱스는 첫 번째 컬럼을 기준으로 정렬되고 그 값이 같다면 다음 컬럼 기준으로 정렬되기 때문에 이러한 현상이 생기는 것입니다.

<br>

### `인덱스의 가용성`

B-Tree 인덱스의 특징은 왼쪽 값에 기준해서 오른쪽 값이 정렬되어 있습니다.

<img width="362" alt="스크린샷 2024-02-21 오후 11 24 39" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/02570bde-9e3b-4a24-bbde-523584b8581f">

```sql
SELECT * FROM employees WHERE first_name LIKE '%mer'
```

인덱스는 왼쪽 값부터 정렬 되어 있는데, 위의 쿼리는 앞에 %가 붙어 있기 때문에 `인덱스 레인지 스캔`을 사용할 수 없습니다. 맨 앞 값이 아닌 정렬 우선순위가 상대적으로 낮은 값을 가지고 비교를 시작해야 하기 때문에 B-Tree 인덱스 효과를 얻을 수 없습니다.

<br>

### `가용성과 효율성 판단`

기존적으로 B-Tree 인덱스의 특성상 다음 조건에서는 사용할 수 없습니다. 

- `NOT-EQUAL로 비교된 경우`
  - WHERE column <> 'N' (!=)
  - WHERE column NOT IN (10, 11, 12)
  - WHERE column IS NOT NULL`
- `LIKE %?? 앞부분이 아닌 뒷부분 일치`
  - WHERE column LIKE '%xx'
  - WHERE column LIKE '_xx'
  - WHERE column LIKE '%x%`
- `스토어드 함수나 다른 연산자로 인덱스 컬럼이 변경된 후 비교된 경우`
  - WHERE SUBSTRING(column, 1,1) = 'x'
  - WHERE DAYOFMONTH(column) = 1
- `데이터 타입이 서로 다른 비교`
  - WHERE char_column = 10
- `문자열 데이터 타입의 콜레이션이 다른 경우`
  - WHERE utf8_bin_char_column = euckr_bin_char_column

<br>

## `클러스터링 인덱스`

MySQL 클러스터링 인덱스는 InnoDB 스토리지 엔진에서만 지원하며, 나머지 스토리이지 엔진에서는 지원되지 않습니다.

> 클러스터링 인덱스는 테이블의 프라이머리 키에 대해서만 적용되는 내용이다. 즉, 프라이머리 키 값이 비슷한 레코드끼리 묶어서 저장하는 것을 클러스터링 인덱스라고 표현한다.

<br>

### `Clustered Key vs Non Clustered Key`

| ---               | 대상                                                                            | 제한          |
|-------------------|-------------------------------------------------------------------------------|-------------|
| Clustered Key     | 1) PK <br> 2) PK가 없을땐 유니크키 <br> 3) 1~2 둘다 없을 경우 6byte의 Hidden Key를 생성 (rowid) | 테이블당 한개만 가능 |
| Non Clustered Key | 일반적인 인덱스                                                                      | 테이블당 여러개 가능 |

<br>

### `클러스터링 인덱스 장단점`

- 장점
  - 프라이머리 키로 검색할 때 처리 성능이 매우 빠름
  - 테이블의 모든 세컨더리 인덱스가 프라이머리 키를 가지고 있기 때문에 인덱스만으로 처리될 수 있는 경우가 많음(커버링 인덱스)
- 단점
  - 테이블의 모든 세컨더리 인덱스가 클러스터링 키를 갖기 때문에 클러스터링 키 값의 크기가 클 경우 전체적인 인덱스의 크기가 커짐
  - 세컨더리 인덱스를 통해 검색할 때 프라이머리 키로 다시 한번 검색해야 하므로 처리 성능이 느림
  - INSERT 할 때 프라이머리 키에 의해 레코드의 저장 위치가 결정되기 때문에 처리 성능이 느림
  - 프라이머리 키를 변경할 때 레코드를 DELETE 하고 INSERT 하는 작업이 필요하기 때문에 처리 성능이 느림

대부분 클러스터링 인덱스의 장점은 빠른 읽기(SELECT) 이먀, 단점은 느린 쓰기(INSERT, UPDATE, DELETE) 라는 것을 알 수 있습니다.

<br>

## `해시(Hash) 인덱스란?`

<img width="1456" alt="스크린샷 2024-03-26 오후 6 29 22" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/cf788595-62af-4d70-a8c2-566362309290">

해시 인덱스는 B-Tree 만큼 범용적으로 사용되지는 않지만 고유의 특성, 용도를 가진 인덱스입니다. `해시 인덱스는 동등 비교 검색에는 최적화되어 있지만 범위를 검색한다거나 정렬된 결과를 가져오는 목적으로는 사용할 수 없습니다.`

일반적인 DBMS에서 해시 인덱스는 메모리 기반의 테이블에 주로 구현되어 있으며 디스크 기반의 대용량 테이블용으로는 거의 사용되지 않는다는 특징이 있습니다.

- 장점:
  - 실제 키 값을 저장하는 것이 아니라 해시 함수 결과인 숫자를 저장하기 때문에 인덱스 크기가 작고 검색이 빠르다.
- 단점:
  - 해시 인덱스는 키 값 자체가 변환되어 저장되기 때문에 [범위를 검색](https://dev.mysql.com/doc/refman/8.0/en/explain-output.html#jointype_range)하거나 원본 값 기준으로 정렬할 수 없다.

<br>

## `정리하며`

### `MySQL InnoDB에서 B-Tree 인덱스를 사용하는 이유는 무엇일까?`

- [Why we use B+ tree for clustered index rather than hashing?](https://stackoverflow.com/questions/66547095/why-we-use-b-tree-for-clustered-index-rather-than-hashing) 

스택 오버플로우에 보면 위와 같이 해시 인덱스가 아니라 B-Tree가 아니라 쓰는지 질문들이 보이는데요. 해당 질문에 대한 답변을 요약하자면 `범위 검색`, `정렬` 2가지를 할 수 없다는 해시 인덱스의 문제점 때문에 범용적으로 사용할 수 없습니다.

<br>

### `B-Tree 단점은 없을까?`

[쓰기, 삭제 작업에 불리함]
1. B-Tree에 저장될 때는 저장될 키 값을 이용해 B-Tree상의 적절한 위치를 검색
2. 저장될 위치가 결정되면 레코드의 키 값과 대상 레코드의 주소 정보를 B-Tree의 리프 노드에 저장
3. 만약 리프 노드가 꽉찬다면 리프 노드가 분리 되어야 하는데, 이는 상위 브랜치 노드까지 처리의 범위가 넓어지기에 상대적으로 비용이 많이드는 것으로 알려져 있음 
4. 비용이 비싼 것은 디스크로부터 인덱스 페이지를 읽고 쓰기를 하기 때문

위에서 정리한 것처럼 B-Tree는 쓰기, 변경이 일어날 때마다 위의 작업이 일어나기 때문에 비효율 적이라 할 수 있습니다. 

<br>

[디스크 용량]
- B-Tree는 Balanced Tree 이기 때문에 해당 Tree 구조를 유지하기 위해서 디스크 공간이 많이 낭비될 수 있다는 특징이 있음

<br>

### `궁금했던 점`

InnoDB에서 PK 경우 클러스터링 인덱스를 사용한다는 특징이 있고, 클러스터링 인덱스는 데이터를 가져오기 위해서 한번 더 B-Tree를 탐색해야 합니다.

그렇다면.. 이것은 `커버링 인덱스`가 아닐까? 라는 생각도 했었는데요. 다시 생각해보면 두 개는 다른 것 같습니다.

- `커버링 인덱스`: 인덱스 내에서 조회할 데이터가 모두 존재하여 데이터 파일을 접근하지 않아도 됨
- `클러스터링 인덱스`: 인덱스의 리프노드를 통해서 실제 데이터 파일에 바로 접근하는 것이 아니라 데이터가 존재하는 B-Tree를 조회함

즉, 클러스터링 인덱스는 인덱스에서 데이터를 바로 가져오는 것이 아니라 인덱스에서 한번 더 조회하는 과정이 필요합니다. (기존과 동일) 하지만!! B-Tree에 데이터가 모두 존재하기 때문에 데이터 파일에 접근하지 않아도 되어서 논 클러스터링 인덱스 보다 빠르다는 특징이 있는 것입니다.

<br>

## `Referenece`

- [https://blog.jcole.us/innodb/](https://blog.jcole.us/innodb/d)
- [https://en.wikipedia.org/wiki/B-tree](https://en.wikipedia.org/wiki/B-tree)
- [https://en.wikipedia.org/wiki/B%2B_tree](https://en.wikipedia.org/wiki/B%2B_tree)
- [https://dev.mysql.com/doc/refman/8.0/en/innodb-change-buffer.html](https://dev.mysql.com/doc/refman/8.0/en/innodb-change-buffer.html)
- [https://dev.mysql.com/doc/refman/8.0/en/innodb-buffer-pool.html](https://dev.mysql.com/doc/refman/8.0/en/innodb-buffer-pool.html)
- [https://dev.mysql.com/doc/refman/8.0/en/innodb-init-startup-configuration.html](https://dev.mysql.com/doc/refman/8.0/en/innodb-init-startup-configuration.html)
- [https://dev.mysql.com/doc/refman/8.0/en/innodb-index-types.html](https://dev.mysql.com/doc/refman/8.0/en/innodb-index-types.html)
- [https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_hash_index](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_hash_index)
- [https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html](https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html)
- [https://dev.mysql.com/doc/refman/8.0/en/range-optimization.html](https://dev.mysql.com/doc/refman/8.0/en/range-optimization.html)
- [https://oriondigestive.medium.com/about-introduction-of-index-in-mysql-d8165239c2aa](https://oriondigestive.medium.com/about-introduction-of-index-in-mysql-d8165239c2aa)
- [https://www.quora.com/What-are-some-of-the-disadvantages-of-using-a-B-Tree-index-in-SQL](https://www.quora.com/What-are-some-of-the-disadvantages-of-using-a-B-Tree-index-in-SQL)
- [Real MySQL-1 8장 - 인덱스]()