## `JQuery 정리`

`JQuery`를 사용해보면서 알게 되었던 것 기록하기

```js
$('html').click(function(event) {
    if ($(event.target).parent('className').length < 1) {
        $(".className").removeClass('open');
    }
}) 
```

- 체크 박스가 생긴 후에, 체크 박스가 아닌 다른 화면 클릭하면 사라지도록 하기

<br>

