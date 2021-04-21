# `Shell Script 및 리눅스 일부 문법 정리`

```
RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" http://localhost:8080)
```

- `/dev/null`: 위의 명령어가 성공했다면 아무 것도 뜨지 않고 실패하면 에러 메세지가 출력됩니다.
- `curl`: 해당 URL로 요청을 보내는 명령어(-s, -o, -w은 더 찾아보기)
- `"%{http_code}"`: URL 요청의 HTTP Status Code를 파싱하게 된다. 즉, RESPONSE_CODE에 해당 상태 값이 담깁니다.

<br>

## `if-else문`

```shell script
if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
then
    CURRENT_PROFILE=real2
else
    CURRENT_PROFILE=$(sudo curl -s http://3.36.209.141/)
fi
``` 

- `if문`: if 문안에 [] 안에 위와 같이 반드시 띄어쓰기를 해야 에러가 발생하지 않습니다.
- `then`: if문 뒤에 써주어야 할 것입니다.
- `fi`: if문이 끝났음을 알리는 것입니다.

<br>

```shell script
if [ -z ${IDLE_PID} ]
```

- `-z 옵션`: if 문안에 `-z 옵션`을 사용하면 해당 `IDLE_PID`가 null이면 true, null이 아니면 false를 반환합니다.

<br>

## `함수(function)`

```shell script
function find_idle_profile()
{
    RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" http://3.36.209.141/)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(sudo curl -s http://3.36.209.141/)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}
```

- `Bash 스크립트`는 값을 return 하는 기능이 없습니다. 그래서 제일 마지막 줄에 `echo`를 이용해서 결과를 출력하면 클라이언트에서 그 값을 잡아서 `$(find_idle_profile)) 사용합니다.
- 중간에 `echo`를 사용해서는 안됩니다. 

<br>

```shell script
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)   # find_idle_profile 함수에서 마지막에 echo로 출력한 것을 이렇게 사용하면 값을 return 받은 효과를 얻습니다. 

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}
```

<br>

## `source란?`

```shell script
source /home/ec2-user/app/profile.sh
```

위와 같이 상단에 `source`를 이용해서 적어주면 java로 치면 import와 같은 역할을 하게 됩니다. 즉 현재 파일에서 `profile.sh`를 import 한 것입니다. 



<br>

## `for-in문`

```shell script
for 변수명 in {1..10}
ex) for RETRY_COUNT in {1..10}
```

- 셸 스크립트에서 for문은 위와 같이 사용할 수 있습니다. 

<br>

## `echo란?`

- C언어에서 printf와 같이 화면에 출력을 하는 기능입니다. 
- 위에서 보았던 것처럼 Bash 스크립트 함수에서 클라이언트가 echo의 출력 값을 사용할 수 있도록 하는 역할을 합니다. 
- ```
  echo "set \$service_url http://3.36.209.141:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
  ```

- echo의 출력 결과를 `|(파이프라인)`을 만들어서 해당 경로에 쓰도록 할 수도 있습니다. 

<br>

## `dirname 이란?`

`dirname`은 리눅스 명령어입니다. 이름에서 알 수 있듯이 디렉터리 경로를 출력하는 명령어입니다. 하지만 마지막에 존재하는 경로는 출력하지 않는다는 특징이 있습니다 .

![스크린샷 2021-04-21 오후 2 53 17](https://user-images.githubusercontent.com/45676906/115503453-660d7080-a2b1-11eb-92f9-bba93ef787a3.png)

- `dirname`은 위와 같이 마지막에 존재하는 경로를 제외하고 출력을 합니다. (실제로 home, ec2-user, app 모두 디렉터리 인데도 app은 출력되지 않습니다.)

<br>

![스크린샷 2021-04-21 오후 2 55 29](https://user-images.githubusercontent.com/45676906/115503626-b08eed00-a2b1-11eb-9e72-ba2d84ab9e27.png)

- 이번에는 마지막에 존재하는 것이 파일인데 결과는 위와 같게 나오는 것을 볼 수 있습니다. 

<br>

## `lsof란?`

```
lsof -ti tcp:포트번호
ex) sudo lsof -ti tcp:8080
```

<img width="420" alt="스크린샷 2021-04-21 오후 2 58 55" src="https://user-images.githubusercontent.com/45676906/115503921-22673680-a2b2-11eb-9884-dcd1c3fd587b.png">

위의 명령어를 통해서 현재 실행 중인 포트의 `PID`를 확인할 수 있습니다. 

<br>

## `wc 명령어`

- wc 명령어에서 -l 옵션이 존재합니다. -l 옵션은 해당 명령어의 결과의 수를 숫자로 반환해줍니다. (즉, 결과가 3줄이면 3을 반환합니다.)

```shell script
echo real | grep real | wc -l   
# 결과: 1
``` 

<img width="414" alt="스크린샷 2021-04-21 오후 3 03 29" src="https://user-images.githubusercontent.com/45676906/115504382-d23ca400-a2b2-11eb-81fc-7a95680b167c.png">

위와 같이 하나의 결과만 출력되는 것을 볼 수 있습니다. 여기에 `wc -l` 까지 같이 넣어서 출력해보겠습니다.

<img width="487" alt="스크린샷 2021-04-21 오후 3 05 10" src="https://user-images.githubusercontent.com/45676906/115504477-f8624400-a2b2-11eb-9b49-ee3a48b56cdb.png">

위와 같이 한 줄의 출력이라서 결과도 `1`이 나온 것을 볼 수 있습니다. 

<br>

## `readlink 명령어`

```shell script
ABSDIR=$(readlink -f $0)
```

위의 코드를 보았을 때 어떤 뜻이지? 하면서 좀 더 찾아보고 테스트를 해보았습니다. 

![스크린샷 2021-04-21 오후 3 36 11](https://user-images.githubusercontent.com/45676906/115507610-4ed18180-a2b7-11eb-9e05-662c252afe11.png)

현재 `/home/ec2-user` 위치에 `deploy.sh` 파일에 위의 내용을 입력한 후에 만들겠습니다.

<br>

![스크린샷 2021-04-21 오후 3 36 38](https://user-images.githubusercontent.com/45676906/115507792-87715b00-a2b7-11eb-9e2c-865c97c3b068.png)

그리고 `sh ./deploy.sh` 명령을 통해서 스크립트 파일을 실행시켜 보면 위와 같이 결과가 나옵니다. 

- `$0`: 상대 경로로 현재 파일의 위치가 나옵니다.
- `readlink -f $0`: 절대경로로 해당 파일의 위치가 출력됩니다. (즉, $0 파일의 실제 경로를 알려주는 역할입니다.)
