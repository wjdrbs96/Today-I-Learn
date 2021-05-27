# `MyBatis INSERT 쿼리 성능 테스트`

MyBatis에는 `foreach` 라는 것을 이용해서 `Batch Insert`를 할 수 있는 기능을 제공해줍니다. 그런데 문득 `foreach`를 통해서 데이터를 INSERT 하는 것과 한 건씩 여러번 INSERT 하는 성능 차이가 얼마나 날까? 하는 의문점이 생겨 한번 글을 정리해보려 합니다.

당연히 성능은 `foreach`를 쓰는 것이 빠를 것인데요. 그럼에도 대략이라도 얼마나 차이나는지 한번 알아보겠습니다. 

<br>

## `일반 INSERT 쿼리`

![스크린샷 2021-05-27 오후 2 38 41](https://user-images.githubusercontent.com/45676906/119772740-9d97ba00-befa-11eb-99ea-6ee0082a6c57.png)

먼저 위와 같이 `for` 문을 10000번 반복해서 데이터를 INSERT 해보겠습니다. 누가봐도 비효율적.. 말이 안되는 코드지만 그럼에도 몇초나 걸리는지 한번 실행해보았습니다.
실행해보니 예상했던대로 꽤나 시간이 걸렸는데요. 결과는 아래와 같습니다. 

<br>

![스크린샷 2021-05-27 오후 2 47 24](https://user-images.githubusercontent.com/45676906/119773337-72619a80-befb-11eb-9472-fde0639e1f2c.png)

콘솔에 찍힌 시간은 대략 `136`초 정도가 걸렸습니다. 고작 만건의 데이터를 INSERT 하는데 for문으로 만번을 반복해서 하니 136초나 걸렸습니다. 데이터도 아래의 쿼리로 잘 들어갔는지 확인해보니 
잘 들어간 것도 확인해보았습니다. 

```sql
SELECT * FROM 테이블이름
ORDER BY id DESC 
```

![스크린샷 2021-05-27 오후 2 46 09](https://user-images.githubusercontent.com/45676906/119773477-a8068380-befb-11eb-88cc-41011ea72199.png)

<br>

## `foreach로 INSERT 하기`

이번에는 MyBatis에서 제공해주는 foreach를 통해서 데이터를 INSERT 해보겠습니다. (완전히 똑같은 환경에서 진행합니다.)

<br>

![스크린샷 2021-05-27 오후 2 58 59](https://user-images.githubusercontent.com/45676906/119774191-c5881d00-befc-11eb-9ec6-9e1b935b2359.png)

이번에는 위와 같이 10000개의 데이터를 가진 List의 사이즈를 만든 후에 INSERT 해보겠습니다. 참고로 데이터베이스 테이블은 `Truncate`를 하고 다시 실행하였습니다. 

<br>

![스크린샷 2021-05-27 오후 3 07 37](https://user-images.githubusercontent.com/45676906/119774643-6e367c80-befd-11eb-8bdd-3d9fd65a5350.png)

약 1초 정도 걸린 것을 볼 수 있습니다. 처음의 경우보다 약 13배 정도 차이가 나는 것을 볼 수 있습니다. 똑같이 테이블의 데이터가 잘 들어갔나 확인해보아도 만건의 데이터가 잘 INSERT 된 것을 볼 수 있습니다. 

<br>


