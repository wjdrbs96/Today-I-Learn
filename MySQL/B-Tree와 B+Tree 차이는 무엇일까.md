## `B-Tree와 B+Tree 차이는 무엇일까?`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/9ccea7a5-8444-4f6f-b253-a577b9c0f999)

- 균형 트리 
  - 루트로부터 리프까지의 거리가 일정한 트리 구조 
  - 균형을 유지할 경우 어떤 데이터를 검색할 때 빠른 속도로 데이터를 찾을 수 있다는 장점이 있음 
  - 하지만 노드가 수정되거나 삭제된다면 재정렬을 해줘야 한다는 오버헤드가 있음
- B+-Tree는 B는 Balanced 약자로 균형 트리임

<br>

## `B-Tree`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/7ff404cc-5f5c-40e7-b204-4cc919077f67)

- 모든 노드(internal node + leaf node)에 키와 값이 함께 저장
- internal 노드의 포인터를 통해서만 리프 노드로 이동 가능

<br>

## `B+Tree`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/aa0d25c5-5bc8-454d-a640-313704389213)

- internal 노드에는 키만 저장, 리프 노드에는 키와 값이 저장
- 디스크 파일에 접근할 수 있는 데이터 포인터는 리프 노드에만 존재 (데이터 포인터와 함께 모든 키 값을 저장)
- 리프 노드끼리 서로 연결 리스트로 연결되어 있음

<br>

## `MySQL 인덱스와 B+Tree`

### `인덱스 키 값의 크기`

InnoDB 스토리지 엔진은 디스크에 데이터를 저장하는 가장 기본 단위를 페이지(Page) 또는 블록(Block) 이라고 하며, 디스크의 모든 읽기 및 쓰기 작업의 최소 작업 단위가 됩니다.

인덱스도 페이지 단위로 관리되며 B+Tree에서 루트, 브랜치, 리프 노드를 구분한 기준이 바로 `페이지(Page)` 단위 입니다.

<br>

### `B+Tree는 자식 노드를 얼마나 가질 수 있을까?`

B-Tree의 자식 노드 개수는 인덱스 페이지 크기와 키 값의 크기에 따라 결정됩니다. MySQL 5.7 버전 부터는 InnoDB 스토리지 엔진의 페이지 크기를 [innodb_page_size](https://dev.mysql.com/doc/refman/8.0/en/innodb-init-startup-configuration.html) 시스템 변수를 이용해 4KB ~ 64KB 사이의 값을 선택할 수 있지만 `기본 값은 16KB` 입니다. (B+Tree 리프 노드 페이지에 `인덱스 키`, `자식 노드 주소`를 담고 있음)

인덱스 키를 16바이트, 주소 값을 12바이트라고 가정하고 계산해보면 인덱스 페이지(16KB)는 `16 * 1024(=KB) / (16 + 12) = 585개` 저장할 수 있습니다. 즉, 자식 노드를 585개 가질 수 있는 B+Tree 라는 것을 의미합니다.

키 값이 16 -> 32바이트로 늘어나면 한 페이지에 인덱스 키를 `16 * 1024 / (32 + 12) = 372개` 저장할 수 있습니다. 

레코드 500개를 읽어올 때 전자의 경우 인덱스 페이지 하나로 읽어올 수 있지만, 후자의 경우 최소한 2번 이상 디스크로부터 읽어와야 합니다.

결국 인덱스를 구성하는 키 값의 크기가 커지면 가질 수 있는 자식 노드 수가 줄어 디스크로부터 읽어와야 하는 횟수가 늘어나서 그만큼 느려질 수 있습니다.

<br>

### `B+Tree 깊이`

인덱스 키가 16바이트인 경우에는 depth = 3 일 때 최대 2억(585 * 585 * 585)개 정도 인덱스 페이지를 가질 수 있지만, 키 값이 32바이트 라면 5천만(372 * 372 * 372)개로 줄어듭니다.

결론적으로 인덱스 키 값의 크기가 커지면 커질수록 하나의 인덱스 페이지가 담을 수 있는 인덱스 키 값의 개수가 적어지기 때문에, 같은 레코드 건수라 하더라도 B+Tree 깊이가 깊어져서 디스크 읽기가 더 많이 필요하게 됩니다. (리포 노드에만 포인터를 저장하고 있는 B+Tree와 전체에 포인트 값을 가지고 있는 B-Tree 차이를 보면 좋을 듯)

<br>

## `Referenece`

- [https://www.geeksforgeeks.org/difference-between-b-tree-and-b-tree/?ref=lbp](https://www.geeksforgeeks.org/difference-between-b-tree-and-b-tree/?ref=lbp)
- [https://www.geeksforgeeks.org/what-is-b-tree-b-tree-meaning-2/?ref=lbp](https://www.geeksforgeeks.org/what-is-b-tree-b-tree-meaning-2/?ref=lbp)
- [https://www.geeksforgeeks.org/what-is-b-plus-tree-b-plus-tree-meaning/?ref=lbp](https://www.geeksforgeeks.org/what-is-b-plus-tree-b-plus-tree-meaning/?ref=lbp)
- [Real MySQL - 1]()