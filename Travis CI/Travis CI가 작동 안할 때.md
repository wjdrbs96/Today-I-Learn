# `Travis CI가 작동 안할 때`

![스크린샷 2021-04-26 오후 11 00 24](https://user-images.githubusercontent.com/45676906/116095312-41672d80-a6e3-11eb-90bc-b4522963def2.png)

위와 같이.. 버튼을 활성화 시켰습니다! 근데 Github Repository로 push를 해도 Travis CI는 반응하지 않습니다.. 처음에는 왜그럴까?? 하고 계속 검색하고 삽질을 했는데.. 원하는 결과는 찾기 쉽지 않았습니다. ㅠ

혹시나 하는 마음에 Webhooks가 등록되지 않았나? 하고.. 들어가보니 등록되어 있지 않았습니다... (왜그런지 모르겠지만,, 나만 그런 현상 같은데 ㄷㄷ)

그래서 `Github에 Webhooks를 등록하는 법`에 대해서 정리해보겠습니다. 

<br>

## `Travis CI Webhooks 등록하기`

![스크린샷 2021-04-26 오후 11 10 13](https://user-images.githubusercontent.com/45676906/116096904-9eafae80-a6e4-11eb-8386-56ae5948c7aa.png)

`Settings -> Webhooks`를 눌러서 들어가겠습니다.  

![스크린샷 2021-04-26 오후 11 12 26](https://user-images.githubusercontent.com/45676906/116097211-e59da400-a6e4-11eb-9991-4bd1b2c3b552.png)

<br>

![스크린샷 2021-04-26 오후 11 13 40](https://user-images.githubusercontent.com/45676906/116097569-39a88880-a6e5-11eb-8146-205fe998734b.png)

```
https://notify.travis-ci.org
```

<br>

![스크린샷 2021-04-26 오후 11 13 51](https://user-images.githubusercontent.com/45676906/116097638-4a58fe80-a6e5-11eb-8b6e-70b91821210f.png)

<br> 

<img width="682" alt="스크린샷 2021-04-26 오후 11 14 11" src="https://user-images.githubusercontent.com/45676906/116097667-504edf80-a6e5-11eb-977b-4fe91bce13a0.png">

<br>

<img width="656" alt="스크린샷 2021-04-26 오후 11 14 22" src="https://user-images.githubusercontent.com/45676906/116097772-68befa00-a6e5-11eb-9af7-930334bf6519.png">

위와 같이 체크한 후에 `Update webhook`을 누르겠습니다. 

<br>

![스크린샷 2021-04-26 오후 11 17 00](https://user-images.githubusercontent.com/45676906/116097892-8e4c0380-a6e5-11eb-996e-8efe1387fc7b.png)

그러면 위와 같이 `Travis CI`가 `Webhooks`로 잘 등록되는 것을 볼 수 있습니다. 그리고 나서 Github Repository로 push하면 Travis CI가 잘 동작하는 것을 볼 수 있습니다. 