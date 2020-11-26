# `var 키워드로 선언한 변수의 문제점`

ES5까지 변수를 선언할 수 있는 유일한 방법은 var 키워드를 사용하는 것이었다. var 키워드로 선언된 변수는 다음과 같은 특징이 있다. 

<br>

## `변수 중복 선언 허용`

var 키워드로 선언한 변수는 중복 선언이 가능하다.

```javascript
var x = 1;
var y = 1;

// var 키워드로 선언된 변수는 같은 스코프 내에서 중복 선언을 허용한다. 
// 초기화문이 있는 변수 선언문은 자바스크립트 엔진에 의해 var 키워드가 없는 것처럼 동작한다. 
var x = 100;
var y;

console.log(x); // 100
console.log(y); // 1
``` 

위 예제의 var 키워드로 선언한 x 변수와 y 변수는 중복 선언 되었다. 이처럼 var 키워드로 선언한 변수를 중복 선언하면 초기화문(변수 선언과 동시에 초기값을 할당하는 문) 유무에 따라 다르게 동작한다. 
초기화문이 있는 변수 선언문은 자바스크립트 엔진에 의해 var 키워드가 없는 것처럼 동작하고 초기화문이 없는 변수 선언문은 무시된다. 이때 에러는 발생하지 않는다. 

<br>

위 예제와 같이 만약 동일한 이름의 변수가 이미 선언되어 있는 것을 모르고 변수를 중복 선언하면서 값까지 할당했다면 의도치 않게 먼저 선언된 변수 값이 변경되는 부작용이 발생한다.

<br>

## `함수 레벨 스코프`

var 키워드로 선언한 변수는 오로지 함수의 코드 블록만을 지역 스코프로 인정한다. 따라서 함수 외부에서 var 키워드로 선언한 변수는 코드 블록 내에서 선언해도 모두 전역 변수가 된다. 

```javascript
var x = 1;

if (true) {
  // x는 전역 변수이다. 이미 선언된 전역 변수 x가 있으므로 중복 선언된다. 
  var x = 10;
}

console.log(x);  // 10
```

for문의 변수 선언문에서 var 키워드로 선언한 변수도 전역 변수가 된다. 

```javascript
var i = 10;

for (var i = 0; i < 5; ++i) {
  console.log(i);   // 0 1 2 3 4
}

// 의도치 않게 i 변수의 값이 변경되었다. 
console.log(i);   // 5
```

함수 레벨 스코프는 전역 변수로 만들 확률이 높다. 이로 인해 의도치 않게 전역 변수가 중복 선언되는 경우가 생긴다.

<br>

## `변수 호이스팅`

var 키워드로 변수를 선언하면 변수 호이스팅에 의해 변수 선언문이 스코프의 선두로 끌어 올려진 것처럼 동작한다. `즉, 변수 호이스팅에 의해 var 키워드로 선언한 변수는 변수 선언문 이전에 참조할 수 있다.`
단 할당문 이전에 변수를 참조하면 언제나 `undefined`를 반환한다.

```javascript
// 이 시점에는 변수 호이스팅에 의해 이미 foo 변수가 선언되었따. (1. 선언 단계)
// 변수 foo는 undefined로 초기화된다. (2. 초기화 단계)
console.log(foo);

// 변수에 값을 할당한다. (3. 할당 단계)
foo = 123;

console.log(foo); // 123

// 변수 선언은 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 실행된다. 
var foo;
```

변수 선언문 이전에 변수를 참조하는 것은 변수 호이스팅에 의해 에러를 발생시키지는 않지만 프로그램의 흐름상 맞지 않을뿐더라 가독성을 떨어뜨리고 오류를 발생시킬 여지를 남긴다.

<br>

# `let 키워드`

앞에서 삺쳐본 var 키워드의 단점을 보완하기 위해 ES6에서는  새로운 변수 선언 키워드인 `let`과 `const`를 도입했다. 

<br>

## `변수 중복 선언 금지`

var 키워드로 이름이 동일한 변수를 중복 선언하면 아무런 에러가 발생하지 않는다. 이때 변수를 중복 선언하면서 값까지 할당했다면 의도치 않게 먼저 서넝ㄴ된 변수 값이
재할당되어 변경되는 부작용이 발생한다. 

<br>

`하지만 let 키워드로 이름이 같은 변수를 중복 선언하면 문법 에러가 발생한다.`

```javascript
var foo = 123;

var foo = 456;

let bar = 123;

let bar = 456 // SyntaxError: Identifier 'bar' has already been declared
```

<br>

## `블록 레벨 스코프`

var 키워드로 서넌한 변수는 오로지 함수의 코드 블록만을 지역 스코프로 인정하는 함수 레벨 스코프를 따른다. 하지만 `let 키워드로 선언한 변수는 모든 코드 블록(함수, if문, for문, while 문, try/catch 문`등을
지역 스코프로 인정하는 `블록 레벨 스코프`를 따른다. 

```javascript
let foo = 1;    // 전역 변수

{
  let foo = 2;  // 지역 변수
  let bar = 3;  // 지역 변수
}

console.log(foo);
console.log(bar); // ReferenceError: bar is not defined
```

let 키워드로 선언된 변수는 `블록 레벨 스코프`를 따른다. 따라서 위 예제의 코드 블록 내에서 선언된 foo 변수와 bar 변수는 지역 변수다.
전역에서 선언된 foo 변수와 코드 블록 내에서 선언된 foo 변수는 다른 별개의 변수다. 또한 bar 변수도 블록 레벨 스코프를 갖는 지역 변수다. 
따라서 전역에서는 bar 변수를 참조할 수 없다. 

<br>

## `변수 호이스팅`

var 키워드로 선언한 변수와 달리 let 키워드로 선언한 변수는 변수 호이스팅이 발생하지 않는 것처럼 동작한다. 

```javascript
console.log(foo); // ReferenceError: Cannot access 'foo' before initialization
let foo;
```

이처럼 let 키워드로 선언한 변수를 변수 선언문 이전에 참조하면 `참조 에러(ReferenceError)`가 발생한다.
var 키워드로 선언한 변수는 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 `선언 단계`와 `초기화 단계`가 한번에 진행된다.

<br>

즉, 선언 단계에서 스코프에 변수 식별자를 등록해 자바스크립트 엔진에 변수의 존재를 알린다. 그리고 즉시 초기화 단계에서 `undefined`로 변수를 초기화한다. 

![스크린샷 2020-11-26 오후 9 21 39](https://user-images.githubusercontent.com/45676906/100350534-60c33700-302d-11eb-929e-c64e0753e88d.png)

<br>

### `let 키워드로 선언한 변수는 '선언 단계'와 '초기화 단계'가 분리되어 진행된다.` 

즉. 런타임 이전에 자바스크립트 엔진에 의해 암묵적으로 선언단계가 먼저 실행되지만 초기화 단계는 변수 선언문에 도달했을 때 실행된다.

<br>

만약 초기화 단계가 실행되기 이전에 변수에 접근하려고 하면 `참고 에러(ReferenceError)`가 발생한다. let 키워드로 선언한 변수는 스코프의 시작 지점부터 초기화 단계 시작 지점까지 변수를 참조할 수 없다. 
스코프의 시작 지점부터 초기화 시작 지점까지 변수를 참조할 수 없는 구간을 `일시적 사각지대`라고 부른다. 

```javascript
console.log(foo);    // ReferenceError: Cannot access 'foo' before initialization

let foo;             // 변수 선언문에서 초기화 단계가 실행된다. 
 
console.log(foo);    
  
foo = 1;             // 할당문에서 할당 단계가 실행된다. 
console.log(foo);    // 1
```

![스크린샷 2020-11-26 오후 9 27 16](https://user-images.githubusercontent.com/45676906/100351054-2a39ec00-302e-11eb-87c2-fcf930fd0392.png)

결국 `let 키워드로 선언한 변수는 변수 호이스팅이 일어나지 않는 것처럼 보인다. 하지만 그렇지 않다.`

```javascript
let foo = 1;

{
  console.log(foo);  // ReferenceError: Cannot access 'foo' before initialization
  let foo = 2;
}
```

let 키워드로 선언한 변수의 경우 변수 호이스팅이 발생하지 않는다면 위 예제는 전역 변수 foo의 값을 출력해야 한다. 하지만 let 키워드로 선언한 변수도
여전히 호이스팅이 발생하기 때문에 `참조 에러(ReferenceError)`가 발생한다. 

<br>

자바스크립트 ES6에서 도입된 `let`, `const`를 포함해서 모든 선언(var, let, const, function, function*, class 등)을 호이스팅한다. 

<br>

## `전역 객체와 let`

var 키워드로 선언한 전역 변수와 전역 함수, 그리고 선언하지 않은 변수에 값을 할당한 암묵적 전역은 전역 객체 window의 프로퍼티가 된다. 
전역 객체의 프로퍼티를 참조할 때 window를 생략할 수 있다. 

```javascript
// 이 예제는 브라우저 환경에서 실행해야 한다. 

var x = 1;

y = 2;

function foo() {}

console.log(window.x);  // 1
console.log(x);         // 1

console.log(window.y);  // 2
console.log(y);         // 2

// 함수 선언문으로 정의한 전역 함수는 전역 객체 window의 프로퍼티다. 
console.log(window.foo);  // f foo() {}
// 전역 객체 window의 프로퍼티는 전역 변수처럼 사용할 수 있다. 
console.log(foo); // f foo() {}
```

let 키워드로 선언한 전역 변수는 전역 객체의 프로퍼티가 아니다. 즉, window.foo와 같이 접근할 수 없다. 

```javascript
// 이 예제는 브라우저에서 실행해야 한다.
let x = 1;

console.log(window.x);    // undefined
console.log(x);           // 1
```

<br>

# `const 키워드`

const 키워는 상수를 선언하기 위해 사용한다. 하지만 반드시 상수만을 위해 사용하지는 않는다. 

<br>

## `선언과 초기화`

`const 키워드로 선언한 변수는 반드시 선언과 동시에 초기화해야 한다.`

```javascript
const foo = 1;
```

그렇지 않으면 다음과 같은 문법 에러가 발생한다. 

```javascript
const foo;  // SyntaxError: Missing initializer in const declaration
```

const 키워드로 선언한 변수는 let 키워드로 선언한 변수와 마찬가지로 블록 레벨 스코프를 가지며, 변수 호이스팅이 발생하지 않는 것처럼 동작한다. 

```javascript
{
  // 변수 호이스팅이 발생하지 않는 것처럼 동작한다. 
  console.log(foo);   // ReferenceError: Cannot access 'foo' before initialization
  const foo = 1;
  console.log(foo);   // 1
}

console.log(foo);    // ReferenceError: foo is not defined
```

<br>

## `재할당 금지`

var 또는 let 키워드로 선언한 변수는 재할당이 자유로우나 `const 키워드로 선언한 변수는 재할당이 금지된다.`

```javascript
const foo = 1;
foo = 2;       // TypeError: Assignment to constant variable.
```

<br>

## `상수`

const 키워드로 선언한 변수에 원시 값을 할당한 경우 변수 값을 변경할 수 없다. const 키워드로 선언된 변수는 재할당이 금지된다. 
`const 키워드로 선언된 변수에 원시 값을 할당한 경우 원시 값은 변경할 수 없는 값이고 const 키워드에 의해 재할당이 금지되므로 할당된 값을 변경할 수 있는 방법은 없다.`

<br>

## `const 키워드와 객체`

const 키워드로 선언된 변수에 원시 값을 할당한 경우 값을 변경할 수 없다. 하지만 `const 키워드로 선언된 변수에 객체를 할당한 경우 값을 변경할 수 있다.`
변경 가능한 값인 객체는 재할당 없이도 직접 변경이 가능하다. 

```javascript
const person = {
  name: 'Lee'
}

person.name = 'Kim';

console.log(person);   // { name: 'Kim' }
```

`const 키워드는 재할당을 금지할 뿐 "불변"을 의미하지는 않는다.` 다시 말해, 새로운 값을 재할당하는 것은 불가능하지만 프로퍼티 동적 생성, 삭제, 프로퍼티 값의 변경을
통해 객체를 변경하는 것은 가능하다. 이때 객체가 변경되더라도 변수에 할당된 참조 값은 변경되지 않는다. 

<br>

# `var vs let vs const`

`변수 선언에는 기본적으로 const를 사용`하고 `let은 재할당이 필요한 경우에 한정해 사용`하는 것이 좋다. const 키워드를 사용하면 의도치 않은 재할당을 방지하기 때문에 좀 더 안전하다. 

- ### ES6를 사용한다면 var 키워드를 사용하지 않는다. 
- ### 재할당이 필요한 경우에 한정해 let 키워드를 사용한다. 이때 변수의 스코프는 최대한 좁게 만든다.
- ### 변경이 발생하지 않고 일긱 전용으로 사용하는 원시 값과 객체에는 const 키워드를 사용한다. 






