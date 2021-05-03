# `MySQL 기본 쿼리 문법`

- ## `오름차순 정렬`
    ```sql
    SELECT * FROM table ORDER BY 컬럼 ASC;
    ```
    - 기본 정렬 순서가 오름차순임
     
- ## `내림차순 정렬`
    ```sql
    SELECT * FROM table ORDER BY 컬럼 DESC;
    ```
    - 내림차순(ex: 10, 9, 8, 7, 6)
    
- ### `중복 제거하기`
    ```sql
    SELECT DISTINCT userName FROM users
    ```
    - `DISTINCT 키워드를 사용해서 중복 제거`