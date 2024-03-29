# `트랜잭션 ACID 정리`

## `Atomic(원자성)`

- 중간 단계까지 실행되고 실패하는 일은 없다.
- 구매자의 돈이 빠졌지만 판매자의 돈이 들어오지 않는 경우

<br>

## `Consistency(일관성)`

- 트랜잭션 작업 처리 결과는 항상 일관성 있어야 한다. 
- 마이너스 통장을 허락하지 않는다면 조건에 위배되면 트랜잭션 종료

<br>

## `Isolation(독립성)`

- 둘 이상의 트랜잭션이 동시 실행되고 있을 때 다른 트랜잭션이 끼어들 수 없다.
- 구매자의 돈이 빠졌지만 판매자의 돈이 아직 안들어왔는데 다른 트랜잭션이 끼어들 수 없다.

<br>

## `Durability(지속성)`

- 트랜잭션이 성공적으로 완료되었으면 결과는 영구히 반영되어야 한다.

<br>

