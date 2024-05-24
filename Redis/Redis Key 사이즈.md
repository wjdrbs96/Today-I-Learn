## `Redis Key 사이즈`

- key dump 예상 사이즈는 알수없음
- 보통 string type 들이 많다면, Redis 메모리 사용량과 비슷함
- hash 나 list 등이 있다면, Redis 메모리 사용량보다 파일 사이즈가 더 커질 수 있음
- 메모리 사용량이 40% 정도라면 약 50G 덤프 파일 정도로 생각해볼 수 있다.