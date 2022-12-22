## `Kotlin JPA에서 Entity에서 data class를 사용하면 안되는 이유`

### `1. equals, hashCode, toString`

Java에서 JPA를 사용할 때 객체간의 연관관계를 맺은 후에 Lombok toString을 사용하게 되면 객체간의 서로 참조가 되어 무한루프가 돌 수 있는 문제가 생긴다.

또 equals, hashCode, toString 메소드를 자동으로 생성해주는 것을 사용하기 때문에 직렬화 과정에서 문제가 생길수도 있다고 한다.

<br>

### `2. Data class는 상속이 불가능하다.`

Kotlin data class는 상속이 불가능하기 때문에 JPA 리플렉션을 이용하지 못하게 된다.

<br>

### `3. Entity Dirty Checking`

`copy` 메소드를 사용해서 `Entity Dirty Checking`을 하려고 하면 동작하지 않는다. 이유는 `copy` 메소드는 새로운 객체를 생성하는 것이기 때문에 동작하지 않는 것이 당연하다.

그런데 이러한 이유는 `val` -> `var`로 바꾸고 필드 값을 바꾸면 될 거 같긴 하다.(뇌피셜)