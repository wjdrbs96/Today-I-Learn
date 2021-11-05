# 자바 스크립트 엔진

JavaScript 엔진의 예는 Google의 V8 엔진이다. V8 엔진은 NodeJS 에서도 사용된다.

![image](https://user-images.githubusercontent.com/45676906/97333418-e765e680-18be-11eb-8e1b-1b1fff3e830e.png)

엔진은 두 가지 주요 구성 요소로 구성된다.

### - `메모리 힙` : 메모리 할당이 발생하는 곳이다.
### - `호출 스택` : 코드가 실행될 때 스택 프레임이 있는 곳이다.

<br>

# 런타임

거의 모든 자바스크립트 개발자들이 setTimeout 과 같은 브라우저 내장 API를 사용한다. 하지만, 이 API를 자바스크립트 엔진에서 제공하지는 않는다.

그럼 이것들은 대체 어디서 오는 것일까?

![image](https://user-images.githubusercontent.com/45676906/97473212-16975900-198e-11eb-8e2f-7ff183c6cf33.png)

위 그림처럼, 자바스크립트 엔진 이외에도 자바스크립트에 관여하는 다른 요소들이 많다. `DOM, Ajax, setTimeout` 과같이 브라우저에서 제공하는 API 들을 Web API라고 한다. 그리고 아래쪽에 `이벤트 루프`와 `콜백 큐`도 있다.


<br>

# 호출 스택이란?

`호출 스택`은 기본적으로 우리가 프로그램에서 어디에 있는지 기록하는 데이터 구조이다. 함수에 들어가면 스택 맨 위에 쌓인다. 함수가 종료되면 스택의 맨 위에서 꺼내진다.

<br>

JavaScript는 `단일 스레드 프로그래밍 언어`이므로 `단일 호출 스택`이 있다. 따라서 한 번에 한 가지만 수행 할 수 있다. 이것은 콜 스택이 `동기식` 이라는 것을 의미한다. (호출 스택에 대한 이해는 `비동기` 프로그래밍에 필수적이다.)

<br>


```javascript
function foo(b) {
  var a = 5;
  return a * b + 10;
}

function bar(x) {
  var y = 3;
  return foo(x * y);
} 

console.log(bar(6));
```

위의 코드가 어떤 순서로 실행될지 머리속으로 먼저 생각해보자.


![image](https://miro.medium.com/max/1200/1*E3zTWtEOiDWw7d0n7Vp-mA.gif)

위의 코드의 실행과정을 생각해보면 `스택`과 비슷하다는 것을 알 수 있다. 그리고 위의 단순한 코드를 해석하는데는 크게 무리는 없겠지만 JavaScript 언어를 이해하는 데 가장 중요하고 근본적인 개념은 `실행 컨텍스트`를 이해하는 것이기 때문에 `실행 컨텍스트` 내용을 보면서 정확히 개념을 알고 `콜 스택`에 대해 마무리를 해보자.

<br> <br>

# JavaScript 실행 컨텍스트

먼저 `실행 컨텍스트`에 대한 내용은 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/JavaScript/Execution_Context.md)도 같이 참고해서 보면 좋을 것 같다.

<br>

`실행 컨텍스트(Execution Context)`는 scope, hoisting, this, function, closure 등의 동작원리를 담고 있는 자바스크립트의 핵심원리이다. 다른 자바스크립트 코드를 읽기 위해서는 실행컨텍스트에 대한 이해가 반드시 필요하다.

<br>

`ECMAScript` 스펙에 따르면 실행 컨텍스트를 실행 가능한 코드를 형상화하고 구분하는 추상적인 개념이라고 정의한다. 좀 더 쉽게 말하자면 `실행 컨텍스트는 실행 가능한 코드가 실행되기 위해 필요한 환경` 이라고 말할 수 있겠다. 일반적으로 실행가능한 코드는 `전역 코드`와 `함수 내 코드`이다.


- 변수 : 전역변수, 지역변수, 매게변수, 객체의 프로퍼티
- 함수 선언
- 변수의 유효범위(Scope)
- this

이와 같이 실행에 필요한 정보를 형상화하고 구분하기 위해 자바스크립트 엔진은 실행 컨텍스트를 물리적 객체의 형태로 관리한다. 아래의 코드를 살펴보자.

```javascript
var x = 'xxx';

function foo() {
  var y = 'yyy';

  function bar() {
    var z = 'zzz';
    console.log(x + y + z); // xxxyyyzzz
  }
  bar();
}

foo();
```

위 코드를 실행하면 아래와 같이 `실행 컨텍스트 스택(Stack)이 생성하고 소멸`한다. 현재 실행 중인 컨텍스트에서 이 컨텍스트와 관련없는 코드(예를 들어 다른 함수)가 실행되면 새로운 컨텍스트가 생성된다. 이 함수 컨텍스트는 스택에 쌓이게 되고 컨트롤(제어권)이 이동한다.


![image](https://poiemaweb.com/img/ec_1.png)

- 컨트롤이 실행 가능한 코드로 이동하면 논리적 스택 구조를 가지는 새로운 실행 컨텍스트 스택이 생성된다.

- 전역 코드(Global code)로 컨트롤이 진입하면 전역 실행 컨텍스트가 생성되고 실행 컨텍스트 스택에 쌓인다. 전역 실행 컨텍스트는 애플리케이션이 종료될 때(웹 페이지에서 나가거나 브라우저를 닫을 때)까지 유지된다.

- 함수를 호출하면 해당 함수의 실행 컨텍스트가 생성되며 직전에 실행된 코드 블록의 실행 컨텍스트 위에 쌓인다.

- 함수 실행이 끝나면 해당 함수의 실행 컨텍스트를 파기하고 직전의 실행 컨텍스트에 컨트롤을 반환한다.

<br>

# 실행 컨텍스트의 3가지 객체

`실행 컨텍스트`는 실행 가능한 코드를 형상화하고 구분하는 추상적인 개념이지만 물리적으로는 객체의 형태를 가지며 아래의 `3가지 프로퍼티`를 소유한다.

![iamge](https://poiemaweb.com/img/excute_context_structure.png)

<br>

### Variable Object란(VO / 변수객체)란?

실행 컨텍스트가 생성되면 자바스크립트 엔진은 실행에 필요한 여러 정보들을 담을 객체를 생성한다. 이를 `Variable Object(VO / 변수 객체)`라고 한다. Variable Object는 코드가 실행될 때 엔진에 의해 참조되며 코드에서는 접근할 수 없다.

<br>

Variable Object는 아래의 정보를 담는 객체이다.

- `변수`
- `매개변수(parameter)와 인수 정보(arguments)`
- `함수 선언(함수 표현식은 제외)`

`Variable Object`는 실행 컨텍스트의 프로퍼티이기 때문에 값을 갖는데 이 값은 다른 객체를 가리킨다. 그런데 전역 코드 실행시 생성되는 전역 컨텍스트의 경우와 함수를 실행할 때 생성되는 함수 컨텍스트의 경우, 가리키는 객체가 다르다. 이는 전역 코드와 함수의 내용이 다르기 때문이다. 예를 들어 전역 코드에는 매개변수가 없지만 함수에는 매개변수가 있다.

<br>

`Variable Object`가 가리키는 객체는 아래와 같다.

```
- 전역 컨텍스트의 경우 -
Variable Object는 유일하며 최상위에 위치하고 모든 전역 변수, 전역 함수 등을 포함하는 전역 객체(Global Object / GO)를 가리킨다. 
전역 객체는 전역에 선언된 전역 변수와 전역 함수를 프로퍼티로 소유한다.
```

![image](https://poiemaweb.com/img/ec-vo-global.png)

```
- 함수 컨텍스트의 경우 -
Variable Object는 Activation Object(AO / 활성 객체)를 가리키며 
매개변수와 인수들의 정보를 배열의 형태로 담고 있는 객체인 arguments object가 추가된다.
```

![image](https://poiemaweb.com/img/ec-vo-foo.png)

<br>

### Scope-Chain (SC)

`스코프 체인(Scope Chain)`은 일종의 리스트로서 전역 객체와 중첩된 함수의 스코프의 레퍼런스를 차례로 저장하고 있다. 다시 말해, 스코프 체인은 해당 전역 또는 함수가 참조할 수 있는 변수, 함수 선언 등의 정보를 담고 있는 `전역 객체(GO)` 또는 `활성 객체(AO)`의 리스트를 가리킨다.

<br>

현재 실행 컨텍스트의 활성 객체(AO)를 선두로 하여 순차적으로 상위 컨텍스트의 활성 객체(AO)를 가리키며 마지막 리스트는 전역 객체(GO)를 가리킨다.

![image](https://poiemaweb.com/img/ec-sc.png)

> 스코프 체인은 식별자 중에서 객체(전역 객체 제외)의 프로퍼티가 아닌 식별자, 즉 변수를 검색하는 메커니즘이다.

엔진은 스코프 체인을 통해 `렉시컬 스코프`를 파악한다. 함수가 중첩 상태일 때 하위함수 내에서 상위함수의 스코프와 전역 스코프까지 참조할 수 있는데 이것는 `스코프 체인`을 검색을 통해 가능하다. 함수가 중첩되어 있으면 중첩될 때마다 부모 함수의 Scope가 자식 함수의 스코프 체인에 포함된다. 함수 실행중에 변수를 만나면 그 변수를 우선 현재 Scope, 즉 Activation Object에서 검색해보고, 만약 검색에 실패하면 스코프 체인에 담겨진 순서대로 그 검색을 이어가게 되는 것이다. 이것이 스코프 체인이라고 불리는 이유이다.

<br>

예를 들어 함수 내의 코드에서 변수를 참조하면 엔진은 스코프 체인의 첫번째 리스트가 가리키는 AO에 접근하여 변수를 검색한다. 만일 검색에 실패하면 다음 리스트가 가리키는 Activation Object(또는 전역 객체)를 검색한다. 이와 같이 순차적으로 스코프 체인에서 변수를 검색하는데 결국 검색에 실패하면 정의되지 않은 변수에 접근하는 것으로 판단하여 `Reference 에러`를 발생시킨다. 스코프 체인은 함수의 감추인 프로퍼티인 [[Scope]]로 참조할 수 있다.

<br>

### this value란?

this 프로퍼티에는 this 값이 할당된다. this에 할당되는 값은 함수 호출 패턴에 의해 결정된다.

<br>

앞에서 살펴본 아래의 코드를 가지고 실제로 어떻게 실행 컨텍스트가 생성되는지 알아보자.

```javascript
var x = 'xxx';

function foo() {
  var y = 'yyy';

  function bar() {
    var z = 'zzz';
    console.log(x + y + z); // xxxyyyzzz
  }
  bar();
}

foo();
```

<br>

### 전역 코드의 진입

컨트롤이 실행 컨텍스트에 진입하기 이전에 유일한 `전역 객체(Global Object)`가 생성된다. 전역 객체는 단일 사본으로 존재하며 이 객체의 프로퍼티는 코드의 어떠한 곳에서도 접근할 수 있다. 초기 상태의 전역 객체에는 빌트인 객체(Math, String, Array 등)와 BOM, DOM이 설정되어 있다.

![image](https://poiemaweb.com/img/ec_3.png)

<br>

전역 객체가 생성된 이후, 전역 코드로 컨트롤이 진입하면 전역 실행 컨텍스트가 생성되고 실행 컨텍스트 스택에 쌓인다.

![image](https://poiemaweb.com/img/ec_4.png)

그리고 이후 이 실행 컨텍스트를 바탕으로 이하의 처리가 실행된다

```
1. 스코프 체인의 생성과 초기화
2. Variable Instantiation(변수 객체화) 실행
3. this value 결정
```

<br>

### 1. 스코프 체인의 초기화

실행 컨텍스트가 생성된 이후 가장 먼저 스코프 체인의 생성과 초기화가 실행된다. 이때 스코프 체인은 전역 객체의 레퍼런스를 포함하는 리스트가 된다.

![image](https://poiemaweb.com/img/ec_5.png)

<br>

### 2. Variable Instantiation(변수 객체화) 실행

`스코프 체인의 생성과 초기화가 종료`하면 `변수 객체화(Variable Instantiation)`가 실행된다.

Variable Instantiation은 Variable Object에 프로퍼티와 값을 추가하는 것을 의미한다. 변수 객체화라고 번역하기도 하는데 이는 `변수, 매개변수와 인수 정보(arguments), 함수 선언`을 Variable Object에 추가하여 객체화하기 때문이다.

<br>

전역 코드의 경우, Variable Object는 Global Object를 가리킨다.

![image](https://poiemaweb.com/img/ec_6.png)

Variable Instantiation(변수 객체화)는 아래의 순서로 Variable Object에 프로퍼티와 값을 set한다. (반드시 1→2→3 순서로 실행된다.)

```
1. (Function Code인 경우) 매개변수(parameter)가 Variable Object의 프로퍼티로, 인수(argument)가 값으로 설정된다.
2. 대상 코드 내의 함수 선언(함수 표현식 제외)을 대상으로 함수명이 Variable Object의 프로퍼티로, 생성된 함수 객체가 값으로 설정된다.(함수 객체가 값으로 설정된 다는 것은 function {} 을 의미한다)(함수 호이스팅)
3. 대상 코드 내의 변수 선언을 대상으로 변수명이 Variable Object의 프로퍼티로, undefined가 값으로 설정된다.(변수 호이스팅)
```

위 예제 코드를 보면 전역 코드에 `변수 x와 함수 foo(매개변수 없음)`가 선언되었다. Variable Instantiation의 실행 순서 상,

- 우선 2. 함수 foo의 선언이 처리되고(함수 코드가 아닌 전역 코드이기 때문에 1. 매개변수 처리는 실행되지 않는다.)
- 그 후 3. 변수 x의 선언이 처리된다.

<br>

### 함수 foo의 선언 처리

![image](https://poiemaweb.com/img/ec_7.png)

생성된 함수 객체는 `[[Scopes]] 프로퍼티`를 가지게 된다. [[Scopes]] 프로퍼티는 함수 객체만이 소유하는 내부 프로퍼티(Internal Property)로서 함수 객체가 실행되는 환경을 가리킨다. 따라서 현재 실행 컨텍스트의 스코프 체인이 참조하고 있는 객체를 값으로 설정한다. 내부 함수의 [[Scopes]] 프로퍼티는 자신의 실행 환경(Lexical Enviroment)과 자신을 포함하는 외부 함수의 실행 환경과 전역 객체를 가리키는데 이때 자신을 포함하는 외부 함수의 실행 컨텍스트가 소멸하여도 [[Scopes]] 프로퍼티가 가리키는 외부 함수의 실행 환경(Activation object)은 소멸하지 않고 참조할 수 있다. 이것이 `클로저`이다.

<br>

지금까지 살펴본 실행 컨텍스트는 아직 코드가 실행되기 이전이다. 하지만 `스코프 체인`이 가리키는 변수 객체(VO)에 이미 함수가 등록되어 있으므로 이후 코드를 실행할 때 함수선언식 이전에 함수를 호출할 수 있게 되었다.

<br>

이때 알 수 있는 것은 함수선언식의 경우, `변수 객체(VO)에 함수표현식과 동일하게 함수명을 프로퍼티로 함수 객체를 할당한다는 것이다`. 단, 함수선언식은 변수 객체(VO)에 함수명을 프로퍼티로 추가하고 즉시 함수 객체를 즉시 할당하지만 함수 표현식은 일반 변수의 방식을 따른다. 따라서 함수선언식의 경우, 선언문 이전에 함수를 호출할 수 있다. 이러한 현상을 함수 `호이스팅`(Function Hoisting)이라 한다.

<br>

# 변수 x의 선언 처리

변수 선언은 `Variable Instantiation` 선언된 변수명( x )이 Variable Object의 프로퍼티로, `undefined`가 값으로 설정된다. 이것을 좀 더 세분화 해보면 아래와 같다.

```
- 선언 단계(Declaration phase) 
변수 객체(Variable Object)에 변수를 등록한다. 이 변수 객체는 스코프가 참조할 수 있는 대상이 된다.

- 초기화 단계(Initialization phase) 
변수 객체(Variable Object)에 등록된 변수를 메모리에 할당한다. 이 단계에서 변수는 undefined로 초기화된다.

- 할당 단계(Assignment phase)
undefined로 초기화된 변수에 실제값을 할당한다.
```

`var 키워드로 선언된 변수는 선언 단계와 초기화 단계가 한번에 이루어진다`. 다시 말해 `스코프 체인이 가리키는 변수 객체에 변수가 등록되고 변수는 undefined로 초기화`된다. 따라서 변수 선언문 이전에 변수에 접근하여도 Variable Object에 변수가 존재하기 때문에 에러가 발생하지 않는다. 다만 undefined를 반환한다. 이러한 현상을 `변수 호이스팅(Variable Hoisting)`이라한다.

<br>

아직 변수 x는 ‘xxx’로 초기화되지 않았다. 이후 변수 할당문에 도달하면 비로소 값의 할당이 이루어진다.

![image](https://poiemaweb.com/img/ec_8.png)

<br>

### this value 결정

`변수 선언 처리가 끝나면 다음은 this value가 결정`된다. this value가 결정되기 이전에 this는 전역 객체를 가리키고 있다가 함수 호출 패턴에 의해 this에 할당되는 값이 결정된다. 전역 코드의 경우, this는 전역 객체를 가리킨다.

![image](https://poiemaweb.com/img/ec_9.png)

전역 컨텍스트(전역 코드)의 경우, `Variable Object, 스코프 체인, this` 값은 언제나 `전역 객체`이다.

<br>

### 전역 코드의 실행

지금까지는 코드 실행 환경을 갖추기 위한 사전 준비였다. 코드의 실행은 지금부터 시작된다.

```javascript
var x = 'xxx';

function foo () {
  var y = 'yyy';

  function bar () {
    var z = 'zzz';
    console.log(x + y + z);
  }
  bar();
}

foo();
```
위 예제를 보면 전역 변수 x에 문자열 ‘xxx’ 할당과 함수 foo의 호출이 실행된다.

<br>

### 변수 값의 할당

전역 변수 x에 문자열 ‘xxx’를 할당할 때, 현재 실행 컨텍스트의 스코프 체인이 참조하고 있는 Variable Object를 선두(0)부터 검색하여 변수명에 해당하는 프로퍼티가 발견되면 값(‘xxx’)을 할당한다.

![image](https://poiemaweb.com/img/ec_10.png)

<br>

### 함수 foo의 실행

전역 코드의 함수 foo가 실행되기 시작하면 `새로운 함수 실행 컨텍스트가 생성`된다. 함수 foo의 실행 컨텍스트로 컨트롤이 이동하면 전역 코드의 경우와 마찬가지로

1. 스코프 체인의 생성과 초기화
2. Variable Instantiation 실행
3. this value 결정이 순차적으로 실행된다.

<br>

`단, 전역 코드와 다른 점은 이번 실행되는 코드는 함수 코드라는 것`이다. 따라서

1. 스코프 체인의 생성과 초기화
2. Variable Instantiation 실행
3. this value 결정은 전역 코드의 룰이 아닌 함수 코드의 룰이 적용된다.

<br>

![image](https://poiemaweb.com/img/ec_11.png)

<br>

### 스코프 체인의 생성과 초기화

함수 코드의 `스코프 체인의 생성과 초기화`는 우선 Activation Object에 대한 레퍼런스를 스코프 체인의 선두에 설정하는 것으로 시작된다.

<br>

`Activation Object는 우선 arguments 프로퍼티의 초기화`를 실행하고 그 후, Variable Instantiation가 실행된다. Activation Object는 스펙 상의 개념으로 프로그램이 Activation Object에 직접 접근할 수 없다. (Activation Object의 프로퍼티로의 접근은 가능하다)

![image](https://poiemaweb.com/img/ec_12.png)


그 후, Caller(전역 컨텍스트)의 Scope Chain이 참조하고 있는 객체가 스코프 체인에 push된다. 따라서, 이 경우 함수 foo를 실행한 직후 실행 컨텍스트의 스코프 체인은 Activation Object(함수 foo의 실행으로 만들어진 AO-1)과 전역 객체를 순차적으로 참조하게 된다.

![image](https://poiemaweb.com/img/ec_13.png)

<br>

### Variable Instantiation 실행

Function Code의 경우, 스코프 체인의 생성과 초기화에서 생성된 Activation Object를 Variable Object로서 Variable Instantiation가 실행된다. 이것을 제외하면 전역 코드의 경우와 같은 처리가 실행된다. 즉, 함수 객체를 Variable Object(AO-1)에 바인딩한다. (프로퍼티는 bar, 값은 새로 생성된 Function Object. bar function object의 [[Scope]] 프로퍼티 값은 AO-1과 Global Object를 참조하는 리스트）

![image](https://poiemaweb.com/img/ec_14.png)

변수 y를 Variable Object(AO-1)에 설정한다 이때 프로퍼티는 y, 값은 undefined이다.

![image](https://poiemaweb.com/img/ec_15.png)

<br>

### this value 결정

변수 선언 처리가 끝나면 다음은 `this value`가 결정된다. this에 할당되는 값은 함수 호출 패턴에 의해 결정된다.

<br>

내부 함수의 경우, this의 value는 전역 객체이다.

![image](https://poiemaweb.com/img/ec_16.png)

<br>

### foo 함수 코드의 실행

이제 함수 foo의 코드 블록 내 구문이 실행된다. 위 예제를 보면 변수 y에 문자열 ‘yyy’의 할당과 함수 bar가 실행된다.

<br>

### 변수 값의 할당

지역 변수 y에 문자열 ‘yyy’를 할당할 때, 현재 실행 컨텍스트의 스코프 체인이 참조하고 있는 Variable Object를 선두(0)부터 검색하여 변수명에 해당하는 프로퍼티가 발견되면 값 ‘yyy’를 할당한다

![image](https://poiemaweb.com/img/ec_17.png)

<br>


### 함수 bar의 실행

함수 bar가 실행되기 시작하면 새로운 실행 컨텍스트이 생성된다.

![image](https://poiemaweb.com/img/ec_18.png)

이전 함수 foo의 실행 과정과 동일하게 아래와 같다.

1. 스코프 체인의 생성과 초기화
2. Variable Instantiation 실행한다.
3. this value 결정이 순차적으로 실행된다.

![image](https://poiemaweb.com/img/ec_19.png)

이 단계에서 console.log(x + y + z); 구문의 실행 결과는 xxxyyyzzz가 된다.

- x : AO-2에서 x 검색 실패 → AO-1에서 x 검색 실패 → GO에서 x 검색 성공 (값은 ‘xxx’)
- y : AO-2에서 y 검색 실패 → AO-1에서 y 검색 성공 (값은 ‘yyy’)
- z : AO-2에서 z 검색 성공 (값은 ‘zzz’)

<br>

지금까지 `실행 컨텍스트`에 대해 알아보았으니 다시 `콜 스택`으로 돌아와서 정리해보자

### 다른 코드의 예시를 보면서 좀 더 알아보자.

```javascript
function multiply(x, y) { 
    return x * y; 
}

function printSquare(x) { 
    var s = multiply(x, x); 
    console.log(s); 
}

printSquare(5);
```

![image](https://user-images.githubusercontent.com/45676906/97334299-f13c1980-18bf-11eb-90e2-e44e18afe14f.png)

코드의 실행 순서는 위의 그림과 같다. 위에서 설명했던 것 처럼 함수가 호출되는 순서대로 `호출 스택`에 하나씩 쌓이는 것을 볼 수 있다.

<br>

그리고 여기서 호출 스택의 각 항목을 `스택 프레임`이라고 한다.
2개의 예시를 보면서 `호출 스택`에 대한 개념이 어느정도 생겼을 것이다.

<br>

그리고 아래의 코드는 예외가 발생하는 코드이다.

```javascript
function foo() { 
  throw new Error('에러가 발생했습니다'); 
}

function bar() { 
  foo(); 
}

function start() { 
  bar(); 
}

start();
```

위의 코드를 실행하면 아래와 같은 결과를 볼 수 있다.

![스크린샷 2020-10-28 오전 1 57 51](https://user-images.githubusercontent.com/45676906/97335256-fd74a680-18c0-11eb-972d-fff819389fb8.png)

위와 같이 Error 가 던져진 이후의 위의 로그를 보면 `콜스택 (Call Stack)`을 확인할 수 있다.

<br>

## 스택 오버플로우

그리고 다음과 같은 에러를 본 적이 있을 것이다.

```
Uncaught RangeError: Maximum call stack size exceeded
```

`호출 스택`이 가득 찼을 때 발생하는 에러이다. 재귀함수같이 함수 안에서 계속 다른 함수를 호출하다보면 저기 스택이 가득 차다못해 터져버린다. 그래서 발생하는 에러이다.

```javascript
function foo() { 
    foo(); 
}

foo();
```

![image](https://user-images.githubusercontent.com/45676906/97336440-5a249100-18c2-11eb-98f8-ebf404725376.png)


<br>

## 단일 호출 스택의 문제점

단일 스레드에서 코드를 실행하는 것은 멀티 스레드 환경에서 발생하는 복잡한 시나리오(예: deadlocks)를 고려할 필요가 없으므로 매우 쉽다. 그러나 단일 스레드에서 실행하는 것도 상당히 제한적이다. 자바스크립트에서는 하나의 호출 스택만 있기 때문에, 하나의 함수 처리가 엄청 느려서 다른 함수 실행에 지장을 줄 때는 어떻게 해야 할까?

<br>

예를 들어, 브라우저에서 복잡한 이미지 처리를 한다고 생각해보자. 앞서 배운 호출 스택의 동작 방식을 생각 해볼 때, 이미지 처리 작업 스택을 차지하고 있으면 자바스크립트는 후속 작업들을 처리할 수 없다. 단일 스레드, 단일 호출 스택이기 때문이다.

<br>

문제는 이것뿐만이 아니다. 브라우저가 호출 스택에서 많은 작업을 처리하기 시작하면 꽤 오랜 시간 동안 응답을 멈출 수 있다. 대부분의 브라우저는 이 상황에서 웹 페이지를 종료할지 여부를 묻는 오류 메시지를 표시한다. 그렇다면 해결 방법은 무엇일까?

![image](https://user-images.githubusercontent.com/45676906/97470954-83f5ba80-198b-11eb-880f-d6621c9c3263.png)

<br>

## 비동기 콜백(Asynchronous callbacks)

가장 쉬운 해결책은 `비동기 콜백`을 사용하는 것이다. 즉, 우리의 코드 일부를 실행하고 나중에 실행될 콜백(함수)를 제공한다. 비동기 콜백은 즉시가 아닌, 특수한 시점에 실행되므로 console.log와 같은 동기 함수와는 다르게 스택 안에 바로 push 될 필요가 없습니다. 그런데 스택이 아니라면 이 콜백 함수들은 누가 관리하는 것일까?

<br>

## 이벤트 큐(Event Queue)와 비동기 콜백의 처리 과정

자바스크립트 실행환경(Runtime)은 `이벤트 큐(Event Queue)`를 가지고 있습니다. 이는 처리할 메시지 목록과 실행할 콜백 함수 들의 리스트이다.

![image](https://user-images.githubusercontent.com/45676906/97471275-e64ebb00-198b-11eb-8950-9a89328fa84d.png)

지금은 이런 것이 있다는 정도만 알고 나중에 `이벤트 루프` 개념을 배울 때 같이 보면서 개념을 익히며 좋을 것 같다.

<br>

# Reference

- [https://blog.sessionstack.com/how-does-javascript-actually-work-part-1-b0bacc073cf](https://blog.sessionstack.com/how-does-javascript-actually-work-part-1-b0bacc073cf)

- [https://medium.com/@gaurav.pandvia/understanding-javascript-function-executions-tasks-event-loop-call-stack-more-part-1-5683dea1f5ec](https://medium.com/@gaurav.pandvia/understanding-javascript-function-executions-tasks-event-loop-call-stack-more-part-1-5683dea1f5ec)

- [https://ui.dev/ultimate-guide-to-execution-contexts-hoisting-scopes-and-closures-in-javascript/](https://ui.dev/ultimate-guide-to-execution-contexts-hoisting-scopes-and-closures-in-javascript/)

- [https://joshua1988.github.io/web-development/translation/javascript/how-js-works-inside-engine/](https://joshua1988.github.io/web-development/translation/javascript/how-js-works-inside-engine/)

- [https://poiemaweb.com/js-execution-context](https://poiemaweb.com/js-execution-context)