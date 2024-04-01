## `MySQL Master, Slave 구조에 대해 알아보자`

<img width="648" alt="스크린샷 2024-04-01 오후 11 54 06" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/d84d38a6-5fd0-4a86-a8db-4c26296fcca2">

- `MySQL 복제(Replication)`: 2대 이상의 MySQL 서버가 동일한 데이터를 담도록 실시간으로 동기화하는 기술

<br>

## `Master`

- MySQL의 바이너리 로그가 활성화되면 어떤 MySQL 서버든 Master가 될 수 있음
- Master 서버에서 실행되는 DML, DDL 가운데 데이터 구조나 내용을 변경하는 모든 쿼리는 바이너리 로그에 기록함
- Slave 서버에서 변경 내역을 요청하면 Master 장비는 그 바이너리 로그를 읽어 Slave로 넘긴다.
- Master 장비의 프로세스 가운데 `Binlog dump` 라는 쓰레드가 이 일을 전담하는 쓰레드다. (만약 Slave 10대라면 `Binlog dump` 쓰레드도 10개가 될 것임)

<br>

## `Slave`

- Master 서버가 바이너리 로그를 가지고 있다면 Slave 서버는 릴레이 로그를 가지고 있다.
- Master 서버와 Slave의 데이터를 동일한 상태로 유지하기 위해 Slave 서버는 읽기 전용(READ ONLY)로 유지한다.
- `Slave 서버의 I/O 쓰레드는 Master 서버에 접속해 변경 내역을 요청하고, 받아 온 변경 내역을 릴레이 로그에 기록한다.`
  - Slave 서버의 SQL 쓰레드가 릴레이 로그에 기록된 변경 내역을 재실행함으로써 데이터를 Master와 동일한 상태로 유지한다.

<br>

### `Slave는 하나의 Master만 설정 가능`

- MySQL의 복제에서 하나의 Slave는 하나의 Master만 가질 수 있다.

<br>

### `Master와 Slave의 데이터 동기화를 위해 Slave는 읽기 전용으로 설정`

Slave 에서 데이터를 변경하면 Master에 반영이 되지 않아서 데이터 동기화에 어려움이 생길 수 있기 때문에 Slave는 READ_ONLY로 설정한다.