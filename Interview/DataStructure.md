# `자료구조`

## ArrayList vs LinkedList 차이

- ArrayList 는 값을 꺼내올 때 O(1) 안에 가져올 수 있음, 값을 뒤에서 추가 삭제 하는 경우는 빠르게 할 수 있다. 하지만 값을 앞, 중간에서 삭제, 추가 하는건 데이터들의 이동이 필요해서 성능상 좋지 않다. (데이터가 연속적으로 존재)
- LinkedList 값을 가져올 때는 앞, 뒤에서 Head 에서 탐색해야 하기 때문에 O(n) 이 걸림, 하지만 값을 중간에 추가하거나, 삭제하는 것도 ArrayList 처럼 데이터들을 이동시키지 않아도 되기 때문에 ArrayList 보다는 성능이 좋다. (데이터가 불연속적으로 존재)

<br>

## Heap INSERT, DELETE 동작 방식을 설명해주세요.

- HEAP 은 `완전 이진 트리`의 일종으로 제일 큰 값, 제일 작은 값을 찾는 자료구조이다.
- Heap 은 최소 힙(제일 작은 값이 Root), 최대 힙(제일 큰 값이 Root)이 존재한다. 
- INSERT 는 맨 아래 Leaf 노드에 추가된 후에 부모 Root 노드와 비교해서 더 크다면 부모 노드로 올라오는 과정을 계속 반복해서 자기의 위치를 찾아간다.
- DELETE 는 루트 노드를 삭제한다. 그리고 맨 마지막에 존재하는 LEFT 노드를 ROOT 로 올리고, 현재 위치에 맞게 찾아가는 과정을 거친다.

<br>

## 이진트리가 무엇인가요?

- 모든 노드가 최대 2개의 서브트리를 갖는 것을 말한다. 즉, 자식 노드가 0개 ~ 2개 까지 가질 수 있다는 뜻이다. 

<br>

## 이진트리 삽입 과정에 대해서 설명해주세요. 

<br>

## 이진 탐색 트리가 무엇인가요?

- 루트의 왼쪽 노드는 루트보다 작은 값, 루트의 오른쪽 노드는 루트보다 큰 값이 위치한다. 
- 경사 트리가 아닌 이상 일반적으로 트리의 높이는 logN 이기 때문에 이 때 `탐색, 삽입, 삭제의 시간복잡도는 O(logN)`이 된다.
- 트리 높이에 시간 복잡도가 비례한다. (탐색 시간)

<br>

## Map 에 대해서 설명해주세요. 

- Map 은 Key 는 중복될 수 없고, Value 는 중복될 수 있습니다.
- 하나의 버킷에 여러 개 값이 충돌 날 수 있음
  - Separate Chaining : 분리 연결법 => 하나의 버킷 옆에 List 로 연결지어서 저장
  - Open Addressing : 개방 주소법 => 충돌 났다면 다음 버킷에 저장

<br>

## 정렬 알고리즘 마다 시간 복잡도는 어떻게 되나요? 

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FILcRQ%2FbtqAVGGm10z%2FP6KkQjprSAxNyVBBgpXRWk%2Fimg.png)

<br>

## 이진 탐색에 대해서 설명해주세요. 

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbQ6Vxe%2FbtqBDYefh2g%2FKHfFVCkTOPnXxBi7wgBDZ1%2Fimg.png)

이진 탐색에서는 비교가 이루어질 때마다 탐색 범위가 급격하게 줄어든다. 찾고자하는 항목이 속해있지 않은 부분은 전혀 고려할 필요가 없기 때문이다. `이진 탐색을 적용하려면 탐색하기 전에 배열이 반드시 정렬`되어 있어야 한다. 따라서 이진 탐색은 데이터의 삽입이나 삭제가 빈번할 시에는 적합하지 않고, 주로 고정된 데이터에 대한 탐색에 적합하다.

<br> 

