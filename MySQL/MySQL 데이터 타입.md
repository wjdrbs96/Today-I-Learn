# `MySQL 데이터 타입`

## `문자열(CHAR와 VARCHAR)`

문자열 컬럼을 사용할 때는 우선 CHAR 타입과 VARCHAR 타입 중 어떤 타입을 사용할지 결정해야 합니다. 

<br>

### `저장 공간`

CHAR와 VARCHAR의 `공통점은 문자열을 저장할 수 있는 데이터 타입`이라는 점이고, `가장 큰 차이는 고정 길이냐 가변 길이`냐 입니다.

- 고정 길이는 실제 입력되는 컬럼 값의 길이에 따라 사용하는 저장 공간의 크기가 변하지 않습니다. CHAR 타입은 이미 저장 공간의 크기기 고정적입니다. 실제 저장된 값의 유효 크기가 얼마인지 별도로 저장할 필요가 없으므로 추가로 공간이 필요하지 않습니다.
- 가변 길이는 최대로 저장할 수 있는 값의 길이는 제한돼 있지만, 그 이하 크기의 값이 저장되면 그만큼 저장 공간이 줄어듭니다. 하지만 VARCHAR 타입은 저장된 값의 유효 크기가 얼마인지를 별도로 저장해 둬야 하므로 1 ~ 2 바이트의 저장 공간이 추가로 더 필요합니다.

<br>

## `Reference`

- [https://juneyr.dev/mysql-collation](https://juneyr.dev/mysql-collation)
- [https://dev.mysql.com/doc/refman/8.0/en/charset-collation-implementations.html](https://dev.mysql.com/doc/refman/8.0/en/charset-collation-implementations.html)