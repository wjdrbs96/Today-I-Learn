# `Queue란?`

`Queue는 FIFO 구조`를 가진 자주 쓰이는 대표적인 자료구조 입니다. 즉, 한쪽방향에서는 삽입이 나머지 방향에서는 삭제가 일어납니다. 

그러면 `Queue`는 보통 어디에서 쓰일까요?

- `프린터를 할 때 예약한 순서대로 출력이 되어야 하니 Queue 구조를 사용합니다.`
- `은행에서 번호표를 뽑고 대기할 때 순서대로 처리하기 때문에 Queue 구조를 사용한다고 할 수 있습니다.`
- `BFS 너비우선탐색 에서 Queue가 사용됩니다.`

<br>

![image](https://user-images.githubusercontent.com/45676906/105799225-585c8d80-5fd7-11eb-9b47-ac3542853222.png)

<br>

## `Queue의 주요 메소드`

- ### `원소 추가하기`
    - add(): 삽입에 성공하면 true, 공간이 꽉 차있으면 예외(IllegalStateException)를 발생
    - offer(): 삽입에 성공하면 true, 실패하면 false 반환

- ### `원소 삭제하기`
    - remove(): Queue 헤드 원소 삭제하기, 만약 Queue가 비어있으면 예외 발생
    - poll(): Queue의 헤드 원소 삭제, 비어있으면 null 반환
    
- ### `원소 하나 꺼내기`
    - element(): Queue Head에 있는 원소 하나를 삭제하지 않고 반환하고 비어있으면 예외 발생
    - peek(): Queue Head에 있는 원소 하나를 삭제하지 않고 반환
    
<br>

## `Queue 시간 복잡도`

|offer()|poll()|peek()|search()|
|------|-----|-----|-----|
|O(1)|O(1)|O(1)|O(n)|