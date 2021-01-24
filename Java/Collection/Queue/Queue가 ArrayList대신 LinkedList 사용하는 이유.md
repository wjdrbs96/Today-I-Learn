# `Queue의 구현체가 ArrayList가 아니라 LinkedList인 이유는?`

먼저 ArrayList와 LinkedList의 차이를 잘 모른다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/List/ArrayList%20vs%20LinkedList.md) 에서 데이터를 먼저 읽고 오는 것을 추천드립니다. 

간단하게 요약하자면 `순차적으로 데이터를 추가/삭제 하는 경우에는 ArrayList를 사용하고`, `처음, 중간 데이터를 추가/삭제하는 경우에는 LinkedList를 사용하라`는 것입니다.

그러면 Queue에서는 왜 ArrayList 대신 LinkedList를 사용했는지에 대해서 알아보겠습니다. 

<br>

## `Queue 구조`

![queue](https://cdn.programiz.com/sites/tutorial2program/files/queue-implementation.png)

큐의 구조는 한쪽에서는 삽입만 일어나고 한쪽에서는 삭제만 하는 자료구조 입니다. 즉, 먼저 들어간 것이 먼저 나오는 FIFO 구조입니다. (예시로는 줄서기, 프린터 출력 같은 것이 있습니다.)

즉, 큐는 항상 첫 번재 저장된 데이터를 삭제하므로, ArrayList와 같은 배열 기반의 자료구조를 사용하게 되면 빈공간을 채우기 위해서 데이터의 복사가 발생하므로 매우 비효율적입니다.

`그래서 Queue는 ArrayList보다 데이터의 추가/삭제가 쉬운 LinkedList로 구현하는 것이 적합합니다.`

<br>

## `ArrayList vs LinkedList 시간복잡도`

![time](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fc47wrr%2FbtqNG0s9sD1%2FGE9KaZbmsXUbPKVzOkon20%2Fimg.png)

