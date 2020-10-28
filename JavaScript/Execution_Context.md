# 실행 컨텍스트(Execution Context)란?

실행 컨텍스트를 이해하면 자바스크립트가 왜 그렇게 동작하는 지 알 수 있다. 바로 코드를 보면서 본론으로 들어가보자. 

 ```javascript
var name = 'Gyuuny'; // (1)변수 선언 (6)변수 대입

function wow(word) { // (2)변수 선언 (3)변수 대입
  console.log(word + ' ' + name); // (11)
}

function say() {   // (4)변수 선언 (5)변수 대입
  var name = 'Gyun'; // (8)
  console.log(name); // (9)
  wow('hello'); // (10)
}

say(); // (7)
```

(1) ~ (10)은 위의 코드의 실행 순서를 적어놓은 것이다. 위의 코드의 결과는 무엇이 나올 꺼 같은가? `lexical scoping`의 개념을 공부했다면 예측할 수 있을 것이다.

```
Gyun
hello Gyuuny
```

결과는 위와 같다.

<br>

처음 코드를 실행(브라우저가 스크립트를 로딩해서 실행)하는 순간 모든 것을 포함하는 `전역 컨텍스트`가 생긴다. 모든 것을 관리하는 환경이다. 
페이지가 종료될 때 까지 유지된다.

<br> 

컨텍스트에는 `전역 컨텍스트` 말고도 `함수 컨텍스트`가 존재한다. 자바스크립트는 `함수 스코프`를 따라는데 함수를 호출할 때 마다 함수 컨텍스트가 하나씩 더 생긴다. 

<br>

### 컨텍스트의 원칙은 아래와 같다. 

- 전역 컨텍스트 하나 생성 후, 함수 호출 시 마다 컨텍스트가 생긴다. 
- 컨텍스트 생성 시 컨텍스트 안에 `변수객체(arguments, variable), scope chain, this`가 생성된다. 
- 컨텍스트 생성 후 함수가 실행되는데, 사용되는 변수들은 변수 객체 안에서 값을 찾고, 없다면 스코프 체인을 따라 올라가며 찾는다.
- 함수 실행이 마무리되면 해당 컨텍스트는 사라진다.(클로저 제외) 페이지가 종료되면 전역 컨텍스트가 사라진다.

<br>

4가지의 원칙을 가지고 위의 코드를 분석해보자. 

<br>

## 전역 컨텍스트

전역 컨텍스트가 생성된 후 두 번째 원칙에 따라 변수 객체 `scope chain, this`가 들어온다. 위의 코드를 보았을 때, 전역 컨텍스트는 
`arguments(함수의 인자)`가 없고 `variable(name, wow, say)` 해당 스코프의 변수들이 존재한다. 

<br>

`scope chain(스코프 체인, 자신과 상위 스코프들의 변수객체)`은 자기 자신인 전역 변수 객체이다. 그리고 `this`는 따로 설정되어 있지 않으면
`window`이다. (this를 바꾸는 방법이 바로 new를 호출하는 것이다.)

<br>

`전역 컨텍스트`를 객체 형식으로 표현하면 아래와 같다.

```
'전역 컨텍스트' : {
    변수객체 : {
        arguments: null,
        variable : ['name', 'wow', 'say'],        
    },
    scopeChain: ['전역 변수객체'],
    this: window,
}
```

wow랑 say는 호이스팅 때문에 선언과 동시에 대입이 된다. 그 후 variable의 name에 'Gyunny'가 대입된다.
```
variable: [{ name: 'zero' }, { wow: Function }, { say: Function }]
```

<br>

## 함수 컨텍스트 

처음 봤던 코드의 (7)에서 `say()`를 하는 순간  새로운 컨텍스트인 say 함수 컨텍스트가 생긴다. 위의 전역 컨텍스트는 그대로 존재하고,
지금 생긴 함수 컨텍스트의 `arguments`는 없고, `variable`은 name이다. `scope chain`은 say 변수 객체와 상위의 전역 변수객체이다. 
`this`는 따로 설정하지 않았기 때문에 `window`이다. 

```
'say 컨텍스트': {
  변수객체: {
    arguments: null,
    variable: ['name'], // 초기화 후 [{ name: 'Gyun' }]이 됨
  },
  scopeChain: ['say 변수객체', '전역 변수객체'],
  this: window,
}
```

say를 호출한 후 위에서부터 차례대로 (8)~(10) 실행한다. 
name 변수는 say 컨텍스트 안에서 찾으면 된다. 그 다음엔 wow('hello')가 있다. 
say 컨텍스트 안에서 wow 변수를 찾을 수 없다. 찾을 수 없다면 scope chain을 따라 올라가 상위 변수객체에서 찾는다. 
그래서 전역 변수객체에서 찾는다. 전역 변수객체의 variable에 wow라는 함수가 있다. 이것을 호출한다.

<br>

(10)번에서 wow함수가 호출되었으니 wow 컨텍스트도 생길 것이다. arguments는 word = 'hello'고, scope chain은 wow 스코프와 전역 스코프이다. 
여기서 중요한 게 lexical scoping에 따라 wow 함수의 스코프 체인은 선언 시에 이미 정해져 있다는 것이다. 
따라서 say 스코프는 wow 컨텍스트의 scope chain이 아니다. variable은 없고, this는 window이다.


```
'wow 컨텍스트': {
  변수객체: {
    arguments: [{ word : 'hello' }],
    variable: null,
  },
  scopeChain: ['wow 변수객체', '전역 변수객체'],
  this: window,
```

이제 컨텍스트가 생긴 후 함수가 실행 된다. say 함수는 아직 종료된 게 아니다. 
wow 함수 안에서 console.log(word + ' ' + name);이 있는데, 
word랑 name 변수는 wow 컨텍스트에서 찾으면 된다. word는 arguments에서 찾을 수 있고, 
name은 wow 변수객체에는 값이 없으니, scope chain을 따라 전역 스코프에서 찾으면 된다. 

<br>

전역 변수객체로 올라가니 variable에 name이 Gyunny라고 되어 있다. 
그래서 hello Gyunny가 되는 겁니다. wow 컨텍스트에 따르면 wow 함수는 애초에 say 컨텍스트와 일절 관련이 없었던 것이다

<br>

이제 wow 함수 종료 후 wow 컨텍스트가 사라지고, say 함수의 실행이 마무리된다. 
따라서 say 컨텍스트도 사라지고, 마지막에 전역 컨텍스트도 사라진다. 

