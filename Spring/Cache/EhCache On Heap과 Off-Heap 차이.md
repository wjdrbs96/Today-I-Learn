## `EhCache Heap과 Off-Heap 차이`

### `Heap`

![image](https://github.com/ben-manes/caffeine/assets/45676906/c03119aa-f923-48d2-afe8-a5a69fa311d8)

On-Heap 저장소는 Java 힙에 존재할 개체(또한 GC에 종속됨)를 의미한다.

<br>

### `Off-Heap`

Off-Heap 저장소는 EHCache에 의해 관리되지만 힙 외부에 저장되고 GC에 종속되지 않는 Object를 의미한다. Off-Heap 저장소는 메모리에서 계속 관리되므로 On-Heap 저장소보다 약간 느리지만 디스크 저장소보다는 빠르다.

EhCache의 Off-Heap 저장소는 일반 오브젝트를 힙에서 가져와서 직렬화하여 EhCache가 관리하는 메모리 덩어리에 바이트로 저장한다. 디스크에 저장하는 것과 같지만 RAM에 있다.

이 상태에서는 객체를 직접 사용할 수 없어서 역직렬화를 해야 한다.

<br>

### `Off-Heap 어디를 말하는걸까?`

![image](https://github.com/ben-manes/caffeine/assets/45676906/84a969f2-3263-48be-baa1-e21051294d30)

Off-Heap이 어디를 말하는 건지 좀 더 찾아보니, Metaspace 영역(Native Memory, Off-Heap, Non-heap, Direct Memory 등)을 말하는 것 같다.

Metaspace는 OS 레벨에서 관리한다고 알고 있는데 EhCache 에서도 여기 영역을 사용하는 것 같다.(??)

<br>

### `Reference`

- [https://stackoverflow.com/questions/6091615/difference-between-on-heap-and-off-heap](https://stackoverflow.com/questions/6091615/difference-between-on-heap-and-off-heap)