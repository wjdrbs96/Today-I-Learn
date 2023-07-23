## `Cassandra BloomFilter란?`

이번 글에서는 가볍게 `BloomFilter가 무엇인가?` 정도만 정리하려 한다. 이런식으로 Cassandra 전반적인 지식을 가볍게 빠르게 정리하고 전체적인 내용을 디스크 조각모음 하고 하나씩 딥하게 공부해가려 한다.

> Bloom Filter는 data 파일의 키값을 모아, 주어진 정보가 있는지 없는지 알려주는 확률 기반의 필터이다.

이거만 보면.. 약간 모호하지만 어떤 느낌으로 사용되는지 살짝 감은 오는거 같다.

<br>

> In the read path, Cassandra merges data on disk (in SSTables) with data in RAM (in memtables). To avoid checking every SSTable data file for the partition being requested, Cassandra employs a data structure known as a bloom filter.

Cassandra는 Disk의 데이터(SSTables)와 RAM의 데이터(memtables)를 병합한다. 요청되는 파티션에 대한 모든 SSTable 데이터 파일을 확인하지 않기 위해 Cassandra는 `Bloom Filter`라고 하는 데이터 구조를 사용한다.

<br>

> Bloom filters are a probabilistic data structure that allows Cassandra to determine one of two possible states: - The data definitely does not exist in the given file, or - The data probably exists in the given file.

> While bloom filters can not guarantee that the data exists in a given SSTable, bloom filters can be made more accurate by allowing them to consume more RAM. Operators have the opportunity to tune this behavior per table by adjusting the the bloom_filter_fp_chance to a float between 0 and 1.

Bloom filter는 Cassandra가 두 가지 가능한 상태 중 하나를 결정할 수 있는 확률론적 데이터 구조이다. `데이터가 지정된 파일에 존재하지 않거나 데이터가 지정된 파일에 존재할 수 있습니다.` 

- 위의 내용을 보고 드는 생각은.. `지정된 파일` 이라는게 정확히 어떻게 파일들이 지정되어 저장되는지 모르겠지만, 당연히 `데이터가 있거나 없거나` 일 거 같긴 하다.
 
Bloom filter는 주어진 SSTable에 데이터가 존재한다고 보장할 수 없지만 Bloom filter는 RAM을 더 많이 사용할 수 있도록 하여 더 정확하게 만들 수 있다. 연산자는 bloom_filter_fp_chance를 0과 1 사이의 부동으로 조정하여 테이블당 이 동작을 조정할 수 있다.

- 디스크에 있는 데이터를 메모리에 많이 가져와서 확률 판단을 더 잘할 수 있게 하는 것에 대해서 설명하는 것 같다. (감이 올랑 말랑..)

<br>

> The default value for bloom_filter_fp_chance is 0.1 for tables using LeveledCompactionStrategy and 0.01 for all other cases.

bloom_filter_fp_chance의 기본값은 LeveledCompactionStrategy를 사용하는 테이블의 경우 0.1이고 다른 모든 경우에는 0.01이다.

> Bloom filters are stored in RAM, but are stored offheap, so operators should not consider bloom filters when selecting the maximum heap size. As accuracy improves (as the bloom_filter_fp_chance gets closer to 0), memory usage increases non-linearly - the bloom filter for bloom_filter_fp_chance = 0.01 will require about three times as much memory as the same table with bloom_filter_fp_chance = 0.1.

bloom_filter는 RAM에 저장되지만 오프힙에 저장되므로 운영자가 최대 힙 크기를 선택할 때 Bloom Filter를 고려하지 않아야 한다.

- 오프힙이라는 것에 대해 좀 더 찾아봐야 겠지만, 힙이랑 오프힙 이랑 다른거기 때문에 최대 힙 크기를 선택할 때 Bloom Filter 고려하지 말라는 뜻인가? 싶다.

정확도가 향상되면(bloom_filter_fp_filter가 0에 가까워질수록) 메모리 사용량이 증가한다. bloom_filter_fp_fp_filter = 0.01의 Bloom Filter는 동일한 테이블의 bloom_filter_fp_fp_fp= 0.1보다 약 3배 많은 메모리가 필요하다.

<br>

> Typical values for bloom_filter_fp_chance are usually between 0.01 (1%) to 0.1 (10%) false-positive chance, where Cassandra may scan an SSTable for a row, only to find that it does not exist on the disk.

bloom_filter_fp_chance의 일반적인 값은 일반적으로 0.01(1%)에서 0.1(10%) 사이의 false-positive 확률이며, 여기서 Cassandra는 SSTable에서 행을 검색할 수 있고, 해당 데이터가 디스크에 존재하지 않음을 알 수 있다.

- `false positives` : 참이 아닌데, 실제로는 참이다,  
- `false negatives` : 참인데, 실제로는 거짓이다.
- SSTable을 조회해서 디스크에 있는지 없는지 판단하는건가 좀 애매한 문장이네.. ?

<br>

> Users with more RAM and slower disks may benefit from setting the bloom_filter_fp_chance to a numerically lower number (such as 0.01) to avoid excess IO operations

RAM이 더 많고 디스크 속도가 느린 사용자는 과도한 IO 작업을 방지하기 위해 bloom_filter_fp_chance를 숫자적으로 더 낮은 숫자(예: 0.01)로 설정할 수 있다.

> Users with less RAM, more dense nodes, or very fast disks may tolerate a higher bloom_filter_fp_chance in order to save RAM at the expense of excess IO operations

RAM이 적거나 노드 밀도가 높거나 디스크 속도가 매우 빠른 사용자는 과도한 IO 작업을 감수하면서 RAM을 절약하기 위해 bloom_fp_chance를 더 많이 허용할 수 있다. 

> In workloads that rarely read, or that only perform reads by scanning the entire data set (such as analytics workloads), setting the bloom_filter_fp_chance to a much higher number is acceptable.

드물게 발생하는 워크로드의 경우 읽기 또는 전체 데이터 세트를 검색하여 읽기만 수행하는 경우 bloom_filter_fp_dll을 훨씬 더 큰 수로 설정할 수 있다.

위의 내용은 [Bloom Filter Document](https://cassandra.apache.org/doc/latest/cassandra/operating/bloom_filters.html)를 보고 나의 생각 및 번역을 한 것이다.

<br>


![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/bb9d1411-36b7-4538-8e90-042a0c1e7487)

```
보통의 경우 데이터는 접근 시간이 느린 디스크에 저장이 되게 됩니다.

만약 외부에서 해당 데이터에 접근을 하고자 할 경우 느린 데이터 저장소에 접근하여 데이터가 있는지 판단을 하는 것은 아무래도 시간이 오래 걸릴 것입니다.

이에 일정한 공간을 미리 할당하여 해당 저장소의 정보를 간소화한 정보로 존재 유무를 판단할 근거 테이블을 제작하고 이 곳에 미리 접근하여 해당 데이터가 있는지를 파악 후 검증이 되면 저장소의 정보에 접근하는 구조를 따르게 됩니다.

블룸 필터를 사용하면 일종의 키가 있는지 없는지에 대한 빠른 확인이 가능하다. (전문용어로 to save IO when performing a key lookup) 이렇게 함으로서 disk access를 조금이나다 적게 해서 속도를 높일 수 있다. 캐쉬는 아니지만, 캐쉬 처럼 비슷한 개념이 있다고 말할 수 있다.

블룸 필터는 두가지를 제공한다. 하나는 추가하는 add(), 하나는 존재하는지에 대한 isExist() 이다.  add() 할 때, key를 hash 알고리즘을 이용해서 여러 개의 hash 값을 나누고, 버킷이라는 저장장소에 저장한다. 그래서 isExist할때 그 버킷를 활용해서 있는지를 확인한다. 확률상 없는데, 있다고 나오지 않도록 적절하게 잡는 알고리즘이 최상의 알고리즘이라 할 수 있을 것 이다.
```


<br>

### `Reference`

- [https://en.wikipedia.org/wiki/Bloom_filter](https://en.wikipedia.org/wiki/Bloom_filter)
- [https://cassandra.apache.org/doc/latest/cassandra/operating/bloom_filters.html](https://cassandra.apache.org/doc/latest/cassandra/operating/bloom_filters.html)
- [https://knight76.tistory.com/entry/%EC%B9%B4%EC%82%B0%EB%93%9C%EB%9D%BCcassandra%EC%9D%98-%EB%B8%94%EB%A3%B8%ED%95%84%ED%84%B0-bloom-filter#recentComments](https://knight76.tistory.com/entry/%EC%B9%B4%EC%82%B0%EB%93%9C%EB%9D%BCcassandra%EC%9D%98-%EB%B8%94%EB%A3%B8%ED%95%84%ED%84%B0-bloom-filter#recentComments)