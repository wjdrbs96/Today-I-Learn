## `Heap 이란?`

- `완전 이진 트리`의 일종으로 `우선순위 큐`를 위하여 만들어진 자료구조이다.
- `여러 개의 값들 중에서 최댓값이나 최솟값을 빠르게 찾아내도록 만들어진 자료구조`이다.
- 힙은 느슨한 정렬 상태를 유지한다. (완전히 정렬된 것은 아니지만, 전혀 정렬이 안된 것도 아님)
- 힙 트리에서는 중복된 값을 허용한다.
- 히프의 목적은 삭제 연산이 수행될 때마다 가장 큰 값을 찾아내기만 하면 되는 것이다(가장 큰 값은 루트 노드)

<br> <br>

## `Heap 종류`

### `최대 힙(max heap)`

- 부모 노드의 키 값이 자식 노드의 키 값보다 크거나 같은 완전 이진 트리 
- key(부모 노드) >= key(자식 노드)


### `최소 힙(min heap)`

- 부모 노드의 키 값이 자식 노드의 키 값보다 작거나 같은 완전 이진 트리
- key(부모 노드) <= key(자식 노드)

<br>

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCRFEy%2FbtqAQ4f5oow%2FtukgTGyHgLxmXLptx5mNEk%2Fimg.png)

<br> <br>

## `Heap 구현`

- 힙을 저장하는 표준적인 자료구조는 배열이다.
- 프로그램 구현을 쉽게 하기 위하여 배열의 첫 번째 인덱스인 0는 사용되지 않는다.
- 특정 위치의 노드 번호는 새로운 노드가 추가 되어도 변하지 않는다.

<br>

### `힙에서의 부모 노드와 자식 노드의 관계`

- 왼쪽 자식의 인덱스 = (부모의 인덱스) * 2
- 오른쪽 자식의 인덱스 = (부모의 인덱스) *2 + 1
- 부모의 인덱스 = (자식의 인덱스) / 2

<br>

![2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fo7PtZ%2FbtqAQOR64LB%2F1QAXr9Pqli87XP6UKRdCAK%2Fimg.png)

이렇게 각각의 노드들은 1번 인덱스부터 번호가 지정되어 있다. 이진트리 이므로 루트노드의 왼쪽자식은 2번, 오른쪽 자신은 3번 이런식으로 번호를 정하다 보면 위의 공식이 나오게 된다. 

<br> <br>

## `Heap 삽입 연산`

- 히프에 새로운 요소가 들어오면, 일단 새로운 노드를 히프의 마지막 노드로 삽입된다.
- 따라서 삽입 후에 새로운 노드를 부모 노드들과 교환해서 힙의 성질을 만족시켜 주어야 한다.

<br>

![3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FnJZPN%2FbtqASy8B59k%2F8S0hi1P1HRKanEKbVDYB9k%2Fimg.png)

<br>

```java
public class MaxHeapTest {
    private ArrayList<Integer> heap;

    public MaxHeapTest() {
        heap = new ArrayList<>();
        heap.add(0);  // 인덱스 0번은 버림
    }

    public void insert(int val) {
        heap.add(val);
        int p = heap.size() - 1;

        while (p > 1 && heap.get(p / 2) < heap.get(p)) {
            int temp = heap.get(p / 2);
            heap.set(p / 2, heap.get(p));
            heap.set(p, temp);

            p = p / 2;
        }
    }
}
```

삽입 연산을 Java 코드로 구현하면 위와 같다.

<br> <br>

## `Heap 삭제 연산`

- 최대 힙에서 삭제 연산은 최대값을 가진 요소를 삭제하는 것이다.
- 최대 힙에서 최대값은 루트 노드이므로 루트 노드가 삭제된다. 

<br>

![4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCYgd0%2FbtqBg8gU4WA%2FNXtuOb0W2x2ZSj42TKKqE0%2Fimg.png)

<br> <br>

### `이번에는 최대힙의 삭제 연산을 구현해보자.`

```java
public class MaxHeapTest {
    private ArrayList<Integer> heap;

    public MaxHeapTest() {
        heap = new ArrayList<>();
        heap.add(0);  // 인덱스 0번은 버림
    }

    public int delete() {
        if (heap.size()-1 < 1) {
            return 0;
        }

        int deleteItem = heap.get(1);
        heap.set(1,heap.get(heap.size()-1));
        heap.remove(heap.size()-1);

        int parent = 1;
        int child = 2;
        while (child < heap.size()) {
            int max = heap.get(child);

            if (child < heap.size()-1 && max < heap.get(child + 1)) {
                max = heap.get(child + 1);
                child++;
            }

            if (heap.get(parent) > max) {
                break;
            }

            int temp = heap.get(parent);
            heap.set(parent, heap.get(child));
            heap.set(child, temp);
            parent = child;
            child+=2;

        }
        
        return deleteItem;
    }
}
```

<br> <br>

## `Heap 시간 복잡도 분석`

### `삽입 연산`

삽입 연산에서 새로운 요소 히프트리를 타고 올라가면서 부모 노드들과 교환을 하게 되는데 최악의 경우, 루트 노드까지 올라가야 하므로 거의 트리의 높이에 해당하는 비교 연산 및 이동 연산이 필요하다. 힙이 `완전 이진 트리`임을 생각하면 힙의 높이가 `log2N`이 되어 삽입의 시간 복잡도는 `O(log2N)`이다. 

<br>

### `삭제 연산`

삭제도 마찬가지로 마지막 노드를 루트로 가져온 후에 자식 노드들과 비교하여 교환하는 부분이 가장 시간이 걸리는 부분인데 이 역시 최악인 경우, 가장 아래 레벨까지 내려가야 하므로 역시 트리의 높이만큼의 시간이 걸립니다. 따라서 `삭제의 시간 복잡도는 O(log2N)`이 됩니다.