## `Offset 페이징 vs Cursor 페이징 차이`

### `Offset 페이징이 무엇일까?`

<img width="264" alt="스크린샷 2022-12-18 오후 7 45 00" src="https://user-images.githubusercontent.com/45676906/208293983-04f9b92f-2b43-4e18-8bfe-466433103f7f.png">

`Offset 페이징`은 주로 게시판에서 볼 수 있는 것처럼 페이지 버튼을 눌러서 이동할 때 사용하는 방법을 말합니다. 

<br>

```sql
SELECT * FROM salaries ORDER BY salary LIMIT n, m;
```

- `n`: Page Number
- `m`: Size

<br>

쿼리로 보면 `LIMIT`에서 몇 번째 페이지(Page Number)부터 몇 개(Size) 가져올 것인지 정하여 쿼리에 작성하는 방법이 `Offset 페이징` 방식이라고 합니다. 

그런데 단순히 어드민 페이지와 같이 게시판 형태에서는 `Offset 페이징`도 괜찮을 수 있지만, 실제 서비스가 되고 있는 무한 스크롤이 들어가있는 뷰에서는 `Offset 페이징` 방식이 권장하는 방식은 아닌데요. 

`Offset 페이징`을 권장하지 않는 이유는 무엇인지 알아보겠습니다.

<br>

### `쿼리 실행 시간이 오래걸릴 수 있다.`

만약 LIMIT의 n, m의 수치가 매우 커진다면, 쿼리 실행 시간에 상당히 오래걸릴 수 있습니다. `LIMIT 200000, 10`이라고 가정하면 MySQL은 `2000010`건의 레코드를 읽은 후 `200000`건은 버리고 마지막 10건만 사용자에게 반환하게 됩니다. 

즉, MySQL 서버는 200010건의 레코드를 읽어야 하기 때문에 쿼리가 느려지는 것입니다.(LIMIT의 n 값이 더 커진다면 데이터를 읽어오는데 시간은 더 걸릴 것입니다.)

<br>

## `Count 쿼리를 사용한다.`

Offset 기반으로 페이징을 하게 되면 총 페이지가 몇 개인지 알아야 하기 때문에 Count 쿼리를 사용하게 됩니다. 

> InnoDB 스토리지 엔진을 사용하는 테이블에서는 Where 조건이 없는 COUNT(*) 쿼리라 할지라도 직접 데이터나 인덱스를 읽어야만 레코드 건수를 가져올 수 있기 때문에 큰 테이블에서 COUNT() 함수를 사용하는 작업은 주의해야 한다.

COUNT 쿼리는 위와 같은 특징이 있기 때문에 Offset 기반은 테이블의 데이터가 많을 수록 DB에 부하를 줄 수 있다는 단점을 가지고 있습니다.

<br>

## `데이터가 중복되어 보일수 있다.`

<img width="864" alt="스크린샷 2022-12-18 오후 8 03 52" src="https://user-images.githubusercontent.com/45676906/208294789-6ce9b24f-7241-47a5-a45d-e30da9a67d26.png">

데이터를 조회할 때 최신 글부터 보여지게 될 것인데요. 만약 현재 1페이지의 데이터를 보고 있는데, 새로운 데이터가 막 추가되었다면 어떻게 될까요?

최신 데이터가 추가되어 1페이지에서 보았던 데이터가 2페이지로 밀리기 때문에 2페이지로 넘어갔을 때 1페이지에서 보았던 데이터가 또 보일 수 있다는 문제점이 생길 수 있습니다. 

<br>

## `Offset 페이징은 구현이 상대적으로 단순하다.`

Offset 페이징은 Cursor 기반 페이징보단 구현이 단순하다고 생각합니다. 왜냐하면 Spring에서 Pageable을 사용하면 쉽고 간단하게 페이징을 구현할 수 있는데 이것도 Offset 페이징으로 구현되기 때문입니다.

<br>

## `커서(Cursor) 기반 페이징이란?`

커서 기반 페이징은 `Cursor` 라는 개념을 사용하는 것입니다. `Cursor` 라는 것은 여러가지가 될 수 있는데 대표적으로 DB Table의 `PK` 값이 될 수 있습니다.

```sql
SELECT s.* 
FROM salaries s 
WHERE s.id > salary_id
ORDER BY s.id DESC
LIMIT 10
```

만약 Cursor가 `salaries` 테이블의 PK 라면 id 값 이후부터 데이터를 조회하는 것입니다.

즉, 클라이언트에게 마지막으로 조회했던 Cursor(salaries 테이블의 PK)를 반환하고, 클라이언트는 마지막 조회했던 Cursor를 서버에게 요청하여 서버는 해당 커서 이후부터 데이터를 조회해서 클라이언트에게 반환합니다.(물론 이건 너무 단순하게 예시를 들은 것이고 서버 - 클라이언트 사이에 Cursor 인터페이스를 어떻게 잡냐에 따라 로직 및 구현이 달라질 것입니다.)

<br>

### `Cursor 페이징은 구현이 어려울 수 있다.`

Cursor 기반 페이징은 정말 단순하게 구현한다면 Offset 페이징과 같이 쉽고 빠르게 구현할 수 있을 것입니다. 하지만 조회할 때 정렬 조건이 많아진다거나 서버 - 클라이언트 사이에 Cursor 인터페이스가 추가될수록 구현이 복잡하고 어려워질 것입니다.  

<br>

### `제한된 정렬 조건`

### `제한된 정렬 기능`

> 커서(Cursor)는 고유(Unique)하거나 연속적(Sequence) 이어야 한다.

커서 페이징에서 커서를 만들 때 위의 내용을 고려하여야 합니다. 만약에 `Last Name` 처럼 고유하지 않은 값을 커서(Cursor)로 사용한다면 어떤 문제점이 있을까요?

```
╔═════════════╦═══════════════╗
║  Last Name  ║   First Name  ║   
╠═════════════╬═══════════════╣
║ Bagshot     ║ Bathilda      ║ 
║ Black       ║ Sirius        ║ 
║ Brown       ║ Lavender      ║ 
║ Chang       ║ Cho           ║ 
║ Creevey     ║ Colin         ║ 
║ Crouch      ║ Bartemius     ║ 
║ Delacour    ║ Fleur         ║ 
║ Diggle      ║ Dedalus       ║
║ Diggory     ║ Cedric        ║ 
║ Dumbledore  ║ Aberforth     ║ 
║ Dumbledore  ║ Albus         ║ 
║ Dursley     ║ Dudley        ║
║ Dursley     ║ Petunia       ║
║ Dursley     ║ Vernon        ║
║ Filch       ║ Argus         ║ 
║ Finnigan    ║ Seamus        ║ 
║ Fletcher    ║ Mundungus     ║ 
╚═════════════╩═══════════════╝
```

위의 데이터가 있다고 가정하고 순차적으로 커서 페이징으로 데이터를 조회한다고 가정해보겠습니다.

<br>

```
Page 1:
╔═════════════╦═══════════════╗
║  Last Name  ║   First Name  ║   
╠═════════════╬═══════════════╣
║ Bagshot     ║ Bathilda      ║ 
║ Black       ║ Sirius        ║ 
║ Brown       ║ Lavender      ║ 
║ Chang       ║ Cho           ║ 
║ Creevey     ║ Colin         ║ 
╚═════════════╩═══════════════╝

Page 2:
╔═════════════╦═══════════════╗
║  Last Name  ║   First Name  ║   
╠═════════════╬═══════════════╣
║ Crouch      ║ Bartemius     ║ 
║ Delacour    ║ Fleur         ║ 
║ Diggle      ║ Dedalus       ║
║ Diggory     ║ Cedric        ║ 
║ Dumbledore  ║ Aberforth     ║
╚═════════════╩═══════════════╝
```

그리고 Page 2까지 조회하면 위처럼 조회될 것입니다.

```sql
SELECT * FROM users WHERE last_name > 'Dumbledore' ORDER BY last_name LIMIT 5 
```

다음에 Page 3을 조회하려면 `Dumbledore 값 다음부터 5개 조회해줘!` 라고 쿼리를 실행하게 될 것인데요.

```
╔═════════════╦═══════════════╗
║  Last Name  ║   First Name  ║   
╠═════════════╬═══════════════╣
║ Dumbledore  ║ Albus         ║ 
╚═════════════╩═══════════════╝
```

그러면 바로 다음 컬럼이 조회될 것으로 예상했지만, 중복된 값으로 인해서 해당 컬럼은 스킵(SKIP) 하고 넘어가게 될 것입니다. 즉, 이러한 이유 때문에 커서(Cursor) 값은 중복된 값을 단일로 사용하면 안되는 것입니다.

이러한 이유로 `고유(Unique)하거나 연속적(Sequence)`인 값 중에 대표적인 PK, timestamp 중에 하나를 사용하거나, 조합해서 많이 사용하는 것 같습니다.

<br>

## `글을 마무리하며`

만약에 어떤 커뮤니티와 같이 새로운 데이터가 금방 추가되는 곳이라면 `Offset` 페이징이 위에서 말한 단점들 때문에 적절하지 않을 수 있다고 생각합니다.

하지만 데이터 양이 많지 않아 DB에 부하가 크지 않고, 데이터 중복이 일어날 문제가 적고 일어나더라도 크게 문제가 되지 않는 곳은 Offset 페이징으로 구현해도 큰 문제 없다고 생각합니다.(구현이 훨씬 더 쉽지 때문에 생산성도 좋을 수 있다고 생각합니다.)

Cursor 페이징도 조회 조건이 복잡해져서 쿼리의 정렬 조건이 복잡해지다 보면 오히려 Offset 쿼리보다 성능이 안좋을 수 있기 때문에 성능 테스트 및 쿼리가 인덱스는 잘 타는지 등등 확인할 필요도 있어보입니다.

많은 분들이 Spring Pageable을 사용하면 페이징을 쉽게 구현했을 것이고, 다른 프레임워크로 서버 개발을 하더라도 Offset, Size를 클라이언트에게 받아와서 SQL LIMIT에 넣어 쉽게 페이징을 구현했을 것인데요. 

Cursor, Offset 페이징의 장단점, 차이를 모르면서 기계적으로 Offset으로 구현하는 것은 좋지 않다고 생각하고 페이징의 특징을 보고 현재 상황에 맞게 구현하는 것이 좋다고 생각합니다.  

<br>

## `Reference`

- [https://medium.com/swlh/how-to-implement-cursor-pagination-like-a-pro-513140b65f32](https://medium.com/swlh/how-to-implement-cursor-pagination-like-a-pro-513140b65f32)
- [https://bbbicb.tistory.com/40](https://bbbicb.tistory.com/40)
- [https://velog.io/@minsangk/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Cursor-based-Pagination-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0](https://velog.io/@minsangk/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Cursor-based-Pagination-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- [Real MySQL- 2 80 ~ 83p]()