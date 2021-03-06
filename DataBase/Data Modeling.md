# 데이터 모델링(Data Modeling)이란?

![title](https://t1.daumcdn.net/cfile/tistory/993267335A212E010B)

<br>

## 3가지 모델링

- 개념적 모델링 : 개체(Entity)와 개체(Entity)들 간의 관계에서 ER 다이어그램을 만드는 과정

![title2](https://t1.daumcdn.net/cfile/tistory/997C4B335A21306B2E)

<br>

- 논리적 모델링 : ER 다이어그램을 사용하여 관계 스키마 모델을 만드는 과정(DBMS에 맞게 Mapping하여 실제 데이터베이스로 구현하기 위한 관계 스키마 모델을 만드는 과정)

![title3](https://t1.daumcdn.net/cfile/tistory/99F6C3335A21306B03)

<br>

- 물리적 모델링 : 관계 스키마 모델의 물리적 구조를 정의하고 구현하는 과정

![title4](https://t1.daumcdn.net/cfile/tistory/99BE04335A21306C27)


<br>

## 설계 대안

데이터베이스 설계 과정의 중요한 부분은 사람, 장소, 물건 같은 다양한 형태를 설계할 때 어떻게 표현할 것인지 결정하는 것이다. 

<br>

`개체(Entity)`라는 용어는 이렇게 식별할 수 있는 물품을 지칭하는 데 사용한다. 대학 데이터베이스에서 개체의 예로는 `교수, 학생, 부서, 수업, 수업분반`등이 있다.
다양한 개체는 여러 가지 방법으로 다른 개체와 관련이 되어 있다. 이런 모든 관계들이 데이터베이스 설계 시에 파악을 해야 한다. 

<br>

데이터베이스 스키마를 설계할 때 우리는 `두 가지 중요한 위험성`들을 피할 수 있도록 확인해야 한다. 
지금은 한 가지만 정리하려 한다. 

## `1. 중복성(Redundancy)`

좋지 않은 설계는 정보를 여러 번 반복한다. 예를 들면, 우리가 수업을 구별할 수 있는 식별자와 수업 제목을 각각의 수업분반과 함께 저장한다면, 제목이 각각의 수업분반과 함께 중복되어 저장될 것이다. 

<br>

`수업 식별자만을 수업분반과 함께 저장하고, 그 제목을 수업 개체에 있는 수업 식별자와 단 한번 만 연결하는 것으로도 충분하다.`

<br>

## 예시

|course_id|title|dept_name| division | 
|------|---|---|---|
|1|데이터베이스| 정보통신학부 | A |
|2| 운영체제 | 정보통신학부 | B |
|1|데이터베이스| 정보통신학부 | B |
|2| 운영체제 | 정보통신학부 | A |

테이블을 보면서 위에서 말했던 것을 적용해보자. 위와 같이 같은 수업의 속하는 과가 계속 중복되어 저장되는 일이 발생한다. 그러면 아래와 같은 문제점이 발생할 수 있다. 

<br>

이러한 정보의 중복적인 표현이 지니는 가장 큰 문제는 그 정보가 수정될 때, 중복되어 저장되어 있는 모든 복사본들을 수정할 수 있는 예방책들을 세우지 않음으로 인해,
어떤 일부 복사본들은 일관되지 않은 정보를 지니고 있을 수도 있다는 것이다. 

<br>

`가령, 어떤 수업의 서로 다른 분반은 동일한 수업 식별자를 지니면서도 서로 다른 제목을 지닐 수도 있다.`
정보는 정확히 한 장소에만 위치해야 한다. 

<br>

### 과목 테이블

|course_id|title| division | 
|------|---|---|
|1|데이터베이스 | A |
|2| 운영체제  | B |

<br>

### 학과 테이블

|dept_id|dept_name| course_id |  
|------|---|---|
|1| 정보통신학부 | 1 |
|2| 컴퓨터공학부 | 3 |

<br>

### 분반 테이블

|division| course_id |
|---|---|
| A | 1 |
| B | 1 |


위와 같이 테이블을 나눠서 저장한다면 중복을 방지할 수 있다. 

<br>



