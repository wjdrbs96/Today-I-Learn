# `1장: JPA 소개`

> JPA를 사용해서 얻은 가장 큰 성과는 애플리케이션을 SQL이 아닌 객체 중심으로 개발하니 생산성과 유지보수가 확연히 좋아졌고 테스트를 작성하기도 편해진 점

> 반복적인 CRUD SQL을 작성하고 객체를 SQL에 매핑하는 데 시간을 보내기에는 우리의 시간이 너무 아깝다. 이미 많은 자바 개발자들이 오랫동안 비슷한 고민을 해왔고 문제를 해결하려고 많은 노력을 기울여왔다.

<br>

## `SQL을 직접 다룰 때 발생하는 문제점`

```sql
SELECT meber_id FROM Member M WHERE member_id = ?
```

- 회원 조회용 SQL 

위의 SQL을 실행하기 위해서 JDBC를 이용한다면 아래와 같은 많은 작업들이 필요합니다.

<br>

```java
ResultSet rs = stmt.executeQuery(sql)
```

실행하는 것 중에 하나이지만 위와 같이 쿼리를 실행하고 결과를 받기 위해서는 아래와 같이 매핑이 또 필요합니다. 

<br>

```java
Member member = rs.getString("member_id")
member.setMeberId(memberId);
member.setName(name);
```

단순히 `SELECT` 하나만 실행 했지만 상당히 번거로운 작업들이 많습니다. 다른 INSERT 등등 CUD에 해당하는 쿼리들도 마찬가지 입니다.

<br> <br>

### `Member class 필드 추가`

```java
public class Member {
    private String memberId;
    private String name;
    private String tel;  // 추가
    
}
```

Member 클래스의 위와 같이 `tel` 필드가 하나 추가 됐다면 어떻게 될까요? 기존에 작성했던 `SELECT`, `INSERT`, `UPDATE`, `DELETE` 등등 Member 관련 쿼리들에 필요하다면 모두 수정을 해야 할 것입니다. 
`매우 불편한 상황인 것인데요.` 객체지향임에도 불구하고 `SQL에 의존적인` 코드를 작성하고 있다고 할 수 있습니다.

<br> <br>

### `연관된 객체`

회원은 어떤 한 팀에 필수로 소속되어야 한다는 요구사항이 추가되었습니다. 

```java
class Member {
    
    private String memberId;
    private String name;
    private String tel;
    private Team team;  // 추가
    
}

// 추가된 팀
class Team {
    private String teamName;
    
}
```

그래서 위와 같이 코드를 수정했다고 가정하겠습니다. 

<br>

```
이름: member:getName();
소속 팀: member:getTeam().getTeamName();   // 추가
```

코드를 실행해보니 `member.getTeam()`이 항상 null 이었는데요. 

<br> 

```java
public class MemberDAO {
    public Member find(String memberId) { ... }
    public Member findWithTeam(String memberId) { ... }  // 추가
    
}
```

이유가 왜 그런지 봤더니 추가된 `findWithTeam`을 사용하지 않고 `find`를 사용해서 그랬던 것인데요. `즉, DAO를 열어서 SQL을 확인해야 원인을 알 수 있다는 문제점`이 존재하는 것입니다. 

<br>

### `SQL 문제점 정리`

- Member 객체가 연관된 Team 객체를 사용할 수 있을지 없을지는 전적으로 사용하는 SQL에 달려 있다.

- 데이터 접근 계층을 사용해서 SQL을 숨겨도 어절 수 없이 DAO를 열어서 어떤 SQL이 실행되는지 확인해야 한다는 점

- SQL에 너무 의존적이라 개발자들이 엔티티를 신뢰하고 사용할 수 없다. (엔티티와 SQL이 너무 강한 의존관계를 가지고 있음!!)

<br> <br>

## `JPA와 문제 해결`

JPA를 사용하면 객체를 데이터베이스에 저장하고 관리할 때, 개발자가 직접 SQL을 작성하는 것이 아니라 JPA가 제공하는 API를 사용하면 됩니다.

```java
jpa.persist(member);
```

persist() 메소드는 객체를 데이터베이스에 저장합니다. 

<br>

```java
Member member = jpa.find(Member.class, MemberId);
```

이런식으로 조회, 수정, 삭제, 생성 모두 JPA API를 사용해서 간단하게 할 수 있습니다. (직접 SQL을 작성하지 않고!)

<br>

```java
Member member = jpa.find(Member.class, MemberId);
Team team = member.getTeam();
```

위와 같이 연관된 객체를 조회할 때도 상당히 편리합니다!

<br> <br>

## `패러다임의 불일치`

위의 경우처럼 Member 클래스 안에 Team 클래스가 존재한다면 데이터베이스에 어떻게 Member 객체를 저장해야 할까요? 여기서 부터 관계형 데이터베이스와 객체지향과의 패러다임의 불일치가 존재하는데요.

- 관계형데이터베이스: 데이터 중심으로 구조화 되어 있고, 집합적인 사고를 요구
- 객체지향: 추상화, 상속, 다형성 같은 개념이 없음

`객체와 관계형 데이터베이스는 지향하는 목적이 서로 다르므로 둘의 기능과 표현 방법도 다릅니다. 이것을 객체와 관계형 데이터베이스의 패러다임 불일치 문제`라고 합니다.

<br>

<img width="1220" alt="스크린샷 2021-08-11 오후 1 31 54" src="https://user-images.githubusercontent.com/45676906/128969971-f97d0505-74e6-4aa6-b24a-7588f625509e.png">

```java
abstract class Item {
    Long id;
    String name;
    int price;
}

class Album extends Item {
    String artist;
}

class Movie extends Item {
    String director;
    String actor;
}

class Book extends Item {
    String author;
    String isbn;
}
```

Album 객체를 저장하려면 이 객체를 분해해서 아래의 두 SQL을 작성해야 합니다.

```sql
INSERT INTO ITEM ...
INSERT INTO ALBUM ...
```

이러한 패러다임 불일치를 해결하려고 많은 코드량이 만만치 않습니다. 

<br> <br>

## `JPA 상속`

JPA는 상속과 관련된 패러다임의 불일치 문제를 개발자 대신 해결해줍니다. 개발자는 자바 컬렉션에 객체를 저장하듯이 JPA에게 객체를 저장하면 됩니다.

```java
jpa.persist(album);
```

위와 같이 저장하면 JPA는 알아서 ITEM, ALBUM을 나눠서 각각 테이블에 저장합니다. 

<br> <br>

## `연관 관계`

객체는 `참조`를 사용해서 다른 객체와 연관관계를 가지고 참조에 접근해서 연관된 객체를 조회합니다. 반면에 테이블은 `외래 키`를 사용해서 다른 테이블과 연관관계를 가지고 `조인을 사용해서 연관된 테이블을 조회`합니다.

<img width="789" alt="스크린샷 2021-08-11 오후 1 42 10" src="https://user-images.githubusercontent.com/45676906/128970699-4001359d-6d72-46eb-8c48-934973513391.png">

참조를 사용하는 객체와 외래키를 사용하는 관계형 데이터베이스 사이의 패러다임 불일치 문제가 존재합니다.

```java
class Member {
    Team team;
}

class Team {
    ...
}
```

Member 테이블은 member_id 외래 키 컬럼을 사용해서 Team 테이블과 관계를 맺습니다. Member 안에 Team이 존재해서 Member -> Team 으로 참조할 순 있지만, Team -> Member로는 참조할 수 없습니다. 반면에 테이블에서는 한 쪽에만 외래키를 가지고 있어도 양방향 참조를 할 수 있습니다. 

<br> <br>

## `객체를 테이블에 맞춰 모델링`

객체와 테이블의 차이를 알아보기 위해 테이블에 맞춰서 모델링을 먼저 해보고 어떤 문제점이 있는지 봐보겠습니다. 

```java
class Member {
    String id;
    Long teamId;
    String username;
    
}

class Team {
    Long id;
    String name;
}
```

Member 클래스와 MEMBER 테이블을 그대로 매핑 시켰습니다. 이렇게 하면 객체를 테이블에 저장하거나 조회할 때는 편리합니다. 하지만 Member 에서 Team 을 조회하기 위해서는 많은 작업들이 필요한데요.

```java
Member member = jpa.find(Member.class, memberId);
Long teamId = member.getTeamId();
Team team = jpa.find(Team.class, teamId);
```

뭔가 객체지향 스럽지 않다는 느낌을 받을 수 있는데요. 이번엔 객체지향 모델링을 보면서 비교를 해보겠습니다.

<br> <br>

## `객체지향 모델링`

```java
class Member {
    String id;
    Team team;   // 참조로 연관관계를 맺음
    String username;
    
}

class Team {
    Long id;
    String name;
}
```

이번에는 teamId를 가지는 것이 아니라 Team의 참조를 보관하고 있는 것을 볼 수 있습니다.

```java
Member member = jpa.find(Member.class, memberId);
Team team = member.getTeam();
```

위와 같이 바로 Team 객체를 찾을 수 있어 좀 더 객체지향 스럽다는 것을 느낄 수 있습니다. 이렇게 JPA를 사용하면 패러다임 불일치 문제를 해결할 수 있습니다. 

<br> <br>

## `객체 그래프 탐색`

객체는 자유롭게 객체 그래프를 탐색할 수 있어야 합니다. 

<img width="813" alt="스크린샷 2021-08-11 오후 1 58 00" src="https://user-images.githubusercontent.com/45676906/128971908-5f6f1cf0-4b29-4e87-a8ca-978e5f2953a2.png">

SQL을 직접 다루면 처음 실행하는 SQL에 따라 객체 그래프를 어디까지 탐색할 수 있는지 정해집니다. (객체지향에겐 너무 큰 제약,,,)

```java
class MemberService {
    
    public void process() {
        Member member = memberDAO.find(memberId);
        member.getTeam();               // member -> team 객체 그래프 탐색이 가능?
        member.getOrder.getDelivery();  // ???
    }
}
```

Member 객체를 조회했지만 이 객체와 연관된 Team, Order를 조회할 수 있는지는 코드만 보고는 예측할 수 없습니다. 그래서 결국 SQL을 확인해보아야 알 수 있다는 문제점이 존재하는데요. 

<br> <br>

## `JPA와 객체 그래프 탐색`

JPA를 사용하면 객체 그래프를 마음껏 탐색할 수 있습니다. 위에서 말한 `SQL`을 직접 다룰 때의 문제점은 JPA를 사용하면 실제 객체를 사용하는 시점까지 데이터베이스 조회를 미루는 기능인 `지연 로딩`을 제공해주는데요.

```java
Member member = jpa.find(Member.class, meberId);

Order order = member.getOrder();
order.getOrderDate();   // Order를 사용하는 시점에 SELECT ORDER SQL
```

Member를 사용할 때마다 Order를 함께 사용하면, 이렇게 한 테이블씩 조회하는 것보다는 Member를 조회하는 시점에 SQL 조인을 사용해서 Member와 Order를 함께 조회하는 것이 효과적입니다. 

<br> <br>

## `JPA란 무엇인가`

<img width="931" alt="스크린샷 2021-08-11 오후 2 08 42" src="https://user-images.githubusercontent.com/45676906/128972803-a19d3e52-ad20-4d15-bf95-6d05ecc77158.png">

