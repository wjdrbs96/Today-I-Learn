## `Intellij 에서 ERD 만드는 법`

이번 글에서는 `Intellij`로 `ERD`를 이쁘게 만드는 법이 있어서 공유해보려 합니다! (대신 제가 알기로는 `Ultimate` 사용자만 가능한 것으로 알고 있습니다.😅)

<br>

![스크린샷 2021-11-29 오전 12 24 22](https://user-images.githubusercontent.com/45676906/143774447-e32f7957-ec24-4679-832f-58675129f3db.png)

`Intellij` 오른쪽에 보면 위와 같이 `DB` 관련 설정을 할 수 있는 곳이 있는데요. 위에 보이는 순서대로 누르겠습니다! 

<br>

![스크린샷 2021-11-29 오전 12 27 29](https://user-images.githubusercontent.com/45676906/143774548-692c2c75-7741-4bdb-8a09-1b1c4dd0aeac.png)

- URL: jdbc:mysql://myEndPoint.ap-northeast-2.rds.amazonaws.com 

<br>

만약 `AWS RDS`를 사용한다면 URL에 위의 형태와 같은 `MySQL EndPoint` 주소가 있을 것인데요. 그 주소를 위의 형태처럼 `URL`에 적겠습니다. 그리고 `Driver`는 저는 그냥 `MySQL`을 사용해서 `MySQL`을 찾아서 선택했습니다. 

<br>

![스크린샷 2021-11-29 오전 12 29 08](https://user-images.githubusercontent.com/45676906/143774874-459301bc-d29d-4496-99c6-1954d8e21b14.png)

`URL`을 잘 입력했다면 위와 같이 화면이 잘 뜰 것입니다. `Port`에는 사용하는 DB 포트를 입력해주면 됩니다. MySQL 이라면 3306을 입력하면 됩니다. 그리고 `DB` 접속 `username`, `password`를 알고 있을 것인데 그것을 입력하고 `Test Connection`으로 잘 연결이 되는지 테스트 해보겠습니다. 

<br>

![스크린샷 2021-11-29 오전 12 36 34](https://user-images.githubusercontent.com/45676906/143775016-46bcd00b-7255-4aa6-8f95-3f9cee082b50.png)

그러면 위와 같이 연결된 `DB`가 뜰 것입니다. 아래의 스키마가 보이지 않는다면 위의 새로고침 버튼을 누르면 뜰 것입니다. 

<br>

![스크린샷 2021-11-29 오전 12 38 42](https://user-images.githubusercontent.com/45676906/143775067-b5c15951-8dd5-4f9a-90ef-593084f3dc33.png)

저는 `Test` 스키마의 `ERD` 볼 것이기 때문에 `Test` 스키마에서 오른쪽 마우스를 누르고 `Diagrams` -> `Shopw Visualization`을 누르겠습니다. 

<br>

![스크린샷 2021-11-29 오전 12 40 18](https://user-images.githubusercontent.com/45676906/143775098-ab2a2a1b-b02e-4efb-8fd3-f5c9d34e3718.png)

그러면 위와 같이 `ERD`를 만들어줍니다. 여기서 좀 더 설정을 할 수 있는데요. 

<br>

![스크린샷 2021-11-29 오전 12 40 56](https://user-images.githubusercontent.com/45676906/143775135-7bc88eb4-0924-470b-913e-bdccc53941c0.png)

<br>

![스크린샷 2021-11-29 오전 12 42 48](https://user-images.githubusercontent.com/45676906/143775187-b7f30cf2-806f-4371-a4dc-e7c2ed4d83f2.png)

저는 `Layout -> Orthogonal -> Hierachic`을 선택했는데 위와 같이 이쁘게 잘 정돈이 된 것을 확인할 수 있습니다. 