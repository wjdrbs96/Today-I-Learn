# `들어가기 전에`

이번 글에서는 Intellij에서 env 설정을 하고 이것을 `application.yml`에서 참조하는 법에 대해서 알아보겠습니다. 이렇게 하고자 하는 이유는 지금까지는 `jwt secret key` 라던지, `DB url`, `DB id`, `DB pw` 같은 것들을 yml 파일에 적은 후에 gitignore에 등록하고 Github에 올리지 않는 방법을 선택했습니다. 

하지만 이것도 하나의 방법일 순 있겠지만, 자꾸 실수로 Github에 yml을 올리는 실수를 할 수도 있다는 것때문에.. 이번에는 Intellij에서 환경변수를 등록하고 yml에서 해당 환경변수를 참조해서 사용하도록 바꿔보겠습니다.

<br> <br>

# `Intellij에서 env 설정 후 yml 파일에서 참조하는 법`

<img width="570" alt="스크린샷 2021-07-13 오후 12 55 18" src="https://user-images.githubusercontent.com/45676906/125387998-b5a79800-e3d9-11eb-8d99-6320cb2a7d00.png">

인텔리제이 오른쪽 위를 보면 위와 같이 `Edit Configurations`가 있는데 이것을 누르겠습니다. 

<br>

<img width="920" alt="스크린샷 2021-07-13 오후 1 33 51" src="https://user-images.githubusercontent.com/45676906/125391292-2ef5b980-e3df-11eb-95b0-ff189558750e.png">

위와 같이 `Main 메소드`에서 실행하는데 사용하는 것에 바로 환경변수를 등록할 수 있습니다.

<br>

<img width="1100" alt="스크린샷 2021-07-13 오후 1 37 25" src="https://user-images.githubusercontent.com/45676906/125391590-b80cf080-e3df-11eb-9121-312bc3d4238c.png">

위와 이름이 동일한 것을 찾아 `Environment variables`에서 환경 변수를 등록하면 됩니다.

<br>

<img width="1104" alt="스크린샷 2021-07-13 오후 12 59 49" src="https://user-images.githubusercontent.com/45676906/125388409-64e46f00-e3da-11eb-824c-00148084f0c3.png">

또는 위와 같이 `add new run configuration`을 통해서 새로 하나 생성해서 환경변수를 등록할 수도 있습니다. 

<br> 

<img width="1098" alt="스크린샷 2021-07-13 오후 1 02 47" src="https://user-images.githubusercontent.com/45676906/125388707-f48a1d80-e3da-11eb-88c0-f61356c5c001.png">

이제 둘 중에 원하는 곳에 위와 같이 `Environment variables`에서 환경 변수를 등록하겠습니다. 

<br>

<img width="594" alt="스크린샷 2021-07-13 오후 1 30 09" src="https://user-images.githubusercontent.com/45676906/125390921-919a8580-e3de-11eb-97a0-c4edd04d4076.png">

위와 같이 원하는 `key-value` 형태로 환경변수를 등록할 수 있습니다. 저는 DB 정보, JWT 정보, AWS 정보들을 환경변수로 등록했습니다. 

<br>

<img width="469" alt="스크린샷 2021-07-13 오후 1 31 59" src="https://user-images.githubusercontent.com/45676906/125391073-cf97a980-e3de-11eb-9819-e83d9a833208.png">

그러면 `application.yml` 파일에서 `${KEY이름}`을 통해서 환경변수를 읽어올 수 있습니다. 