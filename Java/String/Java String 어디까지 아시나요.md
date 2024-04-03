## `Java String 어디까지 아시나요?`

Java String에 대해서 알아본 것을 공유 드리려 합니다. 글에 틀린 정보가 있다면 피드백 부탁드리겠습니다.

<br>

### `StringBuilder append는 비효율적이다.`

<img width="480" alt="스크린샷 2023-01-04 오후 6 36 25" src="https://user-images.githubusercontent.com/45676906/210525974-585196b3-276c-46da-8430-8a56a91d2d4b.png">

`StringBuilder`의 `default capacity`는 `16` 입니다.

<br>

<img width="434" alt="스크린샷 2023-01-04 오후 6 39 26" src="https://user-images.githubusercontent.com/45676906/210526470-6b26702d-7b40-4099-ba54-0bc66729d560.png">

<br>

<img width="456" alt="스크린샷 2023-01-04 오후 6 39 34" src="https://user-images.githubusercontent.com/45676906/210526518-24600a7c-ea73-420e-ab65-340dafac4edf.png">

StringBuilder append를 통해서 추가할 때 `capacity 16`이 넘어가면 위와 같이 `Arrays.copyOf()`를 사용해서 `기본 char[]에서 새로운 char[]`로 복사하는 과정이 일어납니다.

capacity를 넘을 때마다 배열을 복사하기 때문에 비효율적이라 할 수 있습니다.

<br>



- 상수풀 GC 여부
- String + 연산자 JDK 1.5 어떻게 변화 했는지