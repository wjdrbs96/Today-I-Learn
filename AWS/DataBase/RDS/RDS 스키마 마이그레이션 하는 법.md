# `AWS RDS 스키마 마이그레이션 하기`

AWS RDS를 사용하다가 프리티어 기간이 만료되어서, 다른 RDS로 스키마를 이전해야 하는 상황이 왔는데요. 이번 글에서는 아주 쉽게 `A RDS에서 B RDS로 데이터베이스 마이그레이션 하는 법`에 대해서 정리해보겠습니다. 

![스크린샷 2021-10-27 오후 2 21 49](https://user-images.githubusercontent.com/45676906/139004613-dafa8daf-67fb-4f54-8bde-cbaca64b55c5.png)

제가 위의 `MARU 스키마`를 다른 RDS로 마이그레이션 해보려 합니다. 

<br>

![스크린샷 2021-10-27 오후 2 25 33](https://user-images.githubusercontent.com/45676906/139004949-bfcb8424-2046-4d69-8d6d-8e9b8326e9ba.png)

위의 탭에서 `Database -> Migration Wizard`를 누르겠습니다. 

<br>

<img width="983" alt="스크린샷 2021-10-27 오후 2 27 50" src="https://user-images.githubusercontent.com/45676906/139005244-75ec97e2-4f51-45bd-ba35-05782dd07b5c.png">

그리고 위에서 `Start Migration`을 누르겠습니다. 

<br> 

<img width="794" alt="스크린샷 2021-10-27 오후 2 32 37" src="https://user-images.githubusercontent.com/45676906/139005761-a90974ff-a6e5-4489-8b59-f3b5fa9f358a.png">

다음 화면에서는 위와 같은데요. 여기에 보면 `Source RDBS`가 나옵니다. 즉, 옮기고자 하는 RDS를 선택하면 됩니다. 

<br>


![스크린샷 2021-10-27 오후 2 37 43](https://user-images.githubusercontent.com/45676906/139006299-4dae36a4-879f-4799-b01b-8f96c38a3d4a.png)

그리고 이번에는 `Target RDBMS`가 나오는데요. 여기는 스키마를 옮길 대상 RDS를 선택하라는 것입니다. 여기에 옮기고자 하는 RDS를 선택하겠습니다. 

<br>

<img width="380" alt="스크린샷 2021-10-27 오후 2 40 05" src="https://user-images.githubusercontent.com/45676906/139006430-eb667413-286d-4306-aa02-15c00e555059.png">

계속 `Next`를 누르다 보면, `Migration 하려는 스키마`를 선택하겠습니다. 그리고 계~속 `Next`를 누르면 DB 마이그레이션이 완료됩니다. 

<br>

![스크린샷 2021-10-27 오후 2 44 31](https://user-images.githubusercontent.com/45676906/139006795-8dde6625-09dc-478e-9c32-6e00525fa0d3.png)

위와 같이 새로운 RDS에 기존 RDS 스키마 마이그레이션이 잘 된 것을 확인할 수 있습니다.  