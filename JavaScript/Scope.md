# 스코프(Scope)란?

Scope를 우리말로 번역하면 `범위`라는 뜻을 가지고 있다. 즉, `스코프(Scope)`란 `변수에 접근할 수 있는 범위`라고 할 수 있다.

<br>

자바스크립트에서 스코프는 2가지 타입이 있다. `global(전역)`과 `local(지역)`이다. 

<br>

`전역 스코프(Global Scope)`는 말 그대로 전역에 선언되어있어 어느 곳에서든지 해당 변수에 접근할 수 있다는 의미이고 `지역 스코프(Local Scope)`는 해당 지역에서만 접근할 수 있어
지역을 벗어난 곳에선 접근할 수 없다는 의미이다. 

<br>

자바스크립트에서 함수를 선언하면 함수를 선언할 때마다 새로운 스코프를 생성하게 된다. 그러므로 함수 몸체에 선언한 변수는 해당 함수안에서만 접근할 수 있다.
이것은 `함수 스코프(Function-Scoped)`라고 한다. 함수 스코프가 지역 스코프의 예 라고 할 수 있다.

```javascript
var x = 'global';

function ex() {
    var x = 'local';
    x = 'change';
}

ex();
console.log(x);   // global
```

위의 코드를 보면 같은 x라도 함수 바깥의 x는 `전역변수`이고, ex 함수 안의 x는 `지역변수`이다. 

<br>

## 스코프(Scope)

위의 상황에서 지역변수는 아무리 해도 전역변수에 영향을 끼칠 수 없다. 함수 스코프가 있기 때문이다. 함수 스코프는 말 그대로 함수 내부에서만
사용할 수 있다. 이번에는 위와 살짝 다른 코드를 봐보자.

```javascript
var x = 'global';

function ex() {
    x = 'change';
}

ex();
console.log(x);
```

아까와는 달리 이번에는 ex 함수 안에서 var을 선언하지 않았다. 함수 내부에서 `x = 'change'`를 했지만 x의 값이 바뀌는 것을 볼 수 있다.
자바스크립트는 변수의 범위를 호출한 함수의 `지역 스코프`로부터 `전역 스코프`까지 점차 넓혀가며 찾기 때문이다. 따라서 이번 코드에서는
지역 스코프가 존재하지 않기 때문에 전역 스코프를 찾아 값을 바꾼 것이다.

<br>

## 스코프 체인

`전역 변수`와 `지역 변수`의 관계에서 `스코프 체인(scope chain)`이라는 개념이 나온다. 내부 함수에서는 외부 함수의 변수에 접근 가능하지만 외부 함수에서는 내부 함수의 변수에 접근할 수 없다.

```javascript
var name = 'Gyunny';

function outer() {
  console.log('외부', name);
  function inner() {
    var enemy = 'Gyun';
    console.log('내부', name);
  }
  inner();
}
outer();
console.log(enemy);  // ReferenceError: enemy is not defined
```

위의 코드를 실행하면 `enemy`를 참조할 수 없다는 에러가 발생한다. 그 이유가 무엇일까? `inner 함수`는 name 변수를 찾기 위해 먼저 자기 자신의
스코프에서 찾고, 없으면 한 단계 올라가 outer 스코프에서 찾고 여기서도 없으면 전역 스코프에서 name 변수를 찾아 `Gyunny`라는 값을 얻는다.
만약 전역 스코프에도 없다면 변수를 찾지 못하였다는 에러가 발생한다. `이렇게 꼬리를 물고 범위를 넓혀가면서 찾는 관계를 스코프 체인`이라고 부른다. 

<br>

## 렉시컬 스코핑(lexical scoping)

스코프는 함수를 `호출`할 때가 아니라 `선언`할 때 생긴다. 

```javascript
var name = 'Gyunny';
function log() {
  console.log(name);
}

function wrapper() {
  name = 'Gyun';
  log();
}
wrapper();
```

위의 코드는 무엇이 찍힐까? 정답은 `Gyun`이다. 

```javascript
var name = 'Gyunny';
function log() {
  console.log(name);
}

function wrapper() {
  var name = 'Gyun';
  log();
}
wrapper();
```

이번에는 무엇이 찍힐까? 정답은 `Gyunny`이다. 위에서 보았던 예제와 비슷하게 바로 위의 코드에서 log 함수는 전역 변수 name을 가리키고 있기 때문이다. 
이런 것을 `lexical scoping`이라고 한다. 어떤 의미인지 자세히 알아보자. 

<br>

 함수를 처음 선언하는 순간, 함수 내부의 변수는 자기 스코프로부터 가장 가까운 곳(상위 범위에서)에 있는 변수를 계속 참조하게 된다. 
 위의 예시에서는 log 함수 안의 name 변수는 선언 시 가장 가까운 전역변수 name을 참조하게 됩니다. 
 그래서 wrapper 안에서 log를 호출해도 지역변수 name='Gyun'를 참조하는 게 아니라 그대로 전역변수 name의 값인 Gyunny가 나오는 것이다.