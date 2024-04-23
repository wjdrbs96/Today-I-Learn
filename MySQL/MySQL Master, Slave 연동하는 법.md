# `Mysql Master, Slave 연동하기`

## `MySQL 설치`

- 서버 2대 (Master, Slave) 필요
- Master Slave 복제는 Major 버전만 같으면 복제시 이슈 (ex: 같은 5.7끼리 이슈없음)
- 설치할 때 root 계정을 잘 기억해놓기

<br>

## `MySQL Master 계정 생성`

```
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT, EXECUTE on *.* to 계정@"ip" identified by ‘비밀번호 ;

FLUSH PRIVILEGES; 
```

위의 명령어를 통해서 Master 에서 Slave가 사용할 계정 생성이 필요하다. 생성 후에 Slave 쪽으로 GRANT 명령어 그대로를 전달해주면 된다. (root 권한으로 실행해야 한다.)

해당 계정을 Slave 에서도 생성이 필요하다. (Slave 사용을 위한 계정 생성한 내용 동기화 시키기 위함인듯 하다.)

<br>

## `MySQL Master 설정 파일`

```
서버경로/my.cnt
```

Master MySQL 역할을 하기 위해서 bin log 활성화가 필요하다. bin log 활성화를 위해서는 위의 경로에 `my.cnt` 라는 파일에 설정이 필요한데, MySQL 설치 스크립트로 설치했다면 특별히 수정할 것이 없다고 한다.

- log-bin=mysql-bin      # bin log 활성화 하는 의미인듯
- server-id={serverId}
- skip-log-bin (제거 필요) # 이거 있으면 bin log 활성화 안됨
- `binlog-do-db=test` => 이 설정을 하면 `test` 스키마만 Slave 연동이 된다.
    - 따로 설정하지 않으면 해당 디비에 존재하는 전체 스키마가 대상이다.

그런데 설치 스크립트로 설치 했어도 Master 역할을 하는 MySQL 서버에 위처럼 3가지 설정이 되어 있는지는 확인이 필요할 것 같다.

만약에 설정 변경이 있었다면 MySQL 서버 재시작이 필요하다.

<br>

## `Slave MySQL 스키마 생성`

```
CREATE DATABASE test;
```

Slave DB에 원하는 이름의 스키마 이름을 생성한다.

<br>

## `Master MySQL Dump 파일 생성`

```
/서버경로/bin/mysqldump -u 유저네임 -p 비밀번호 -S /서버경로/tmp/mysql.sock —master-data=2 > test.sql
```

Master MySQL에 존재하는 데이터를 Slave MySQL에 복제하는 과정이 필요하기 때문에 위처럼 데이터를 Dump 하는 과정이 필요하다. (마이그레이션 하는 느낌..?)

```
test.sql
```

Master 복제된 Dump 파일을 Slave DB에 전달해주면 된다.


<br>

```
./mysqldump -u 계정 -p 비밀번호 -S /경로/tmp/mysql.sock —master-data=2 db1 db2 db3  > DB명.sql
```

여러 개의 스키마를 한번에 복제하고 싶다면 각각 생성하는 것이 아닌 위처럼 Dump 파일을 한번에 생성해야 한다.

<br>

## `Slave Mysql Dump 복제`

```
/경로/bin/mysql -u 유저네임 -p 비밀번호 -A -S /경로/tmp/mysql.sock --database=스키마이름 < 덤프파일이름.sql
ex) /경로/bin/mysql -u root -p root -A -S /경로/tmp/mysql.sock --database=test < test.sql
```

Master MySQL에서 생성한 Dump 파일을 Slave DB에서 복제해야 하는데 위의 명령어로 진행할 수 있다.

그러면 Slave DB에서 위처럼 Master DB에 있던 테이블들이 복제된 것을 볼 수 있다.

<br>

## `Slave, Master MySQL 연동`

```
RESET SLAVE;

CHANGE MASTER TO MASTER_HOST='',    # Master DB HOST 정보 
MASTER_PORT=,                       # Master DB Port 정보
MASTER_USER='',                     # Master Replication 전용 계정 정보
MASTER_PASSWORD='',                 # Master Replication 전용 계정 Password 정보
MASTER_LOG_FILE='',                 # Dump 파일에 존재하는 Bin log 파일
MASTER_LOG_POS=;                    # Dump 파일에 존재하는 POSITION

START SLAVE;
```

LOG_FILE, POST 값은 Dump 파일을 열어보면 위와 같이 확인할 수 있는데 이 값들을 넣고 명령어를 실행하면 된다.

참고로 위의 명령어는 Slave DB의 root 권한으로 실행해야 실행되는 듯하다.

<br>

```
SHOW STATUS MASTER;
```

Master MySQL에 접근할 수 있는 상황이라면 위의 명령어를 통해서 MASTER_LOG_FILE, MASTER_LOG_POS 값을 확인할 수도 있다.

<br>

## `Slave 연동 확인`

```
SHOW SLAVE STATUS\G;
```

위의 명령어로 확인 했을 때 `Waiting for master to send event` 가 나오면 Slave 연동이 성공했다고 할 수 있다.

이거 뿐만 아니라 Master DB에서 값을 변경 해보았을 때 Slave DB에도 결과가 잘 반영된 것을 보아 Slave 연동이 잘 된 것 같다.

<br>

## `특정 스키마만 복제하고 싶을 때`

```
replicate-do-db=test   # 복제할 DB명
```

Slave 에서 특정 스키마만 복제하고 싶다면 `my.cnt` 파일에서 위의 설정을 추가하면 된다. 이것도 마찬가지로 설정하지 않는다면 연결한 Master 디비에 존재하는 전체 스키마가 대상이다.
