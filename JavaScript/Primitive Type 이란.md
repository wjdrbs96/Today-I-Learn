# 변수

프로그래밍은 `변수`를 통해 값을 저장하고 참조하며 연산자로 값을 연산, 평가하고 조건문과 반복문에 의한 흐름제어로 데이터의 흐름을 제어하고 함수로 재사용이 가능한 구문의 집합을 만들며 객체, 배열 등으로 자료를 구조화하는 것이다.

<br>

`변수`는 값의 위치(주소)를 기억하는 저장소이다. 값의 위치란 값이 위치하고 있는 메모리 상의 주소(address)를 의미한다. 즉, `변수란 값이 위치하고 있는 메모리 주소(Memory Address)`에 접근하기 위해 사람이 이해할 수 있는 언어로 명명한 식별자(identifier)이다.

![title](https://poiemaweb.com/img/memory_address.png)

메모리에 값을 저장하기 위해서는 먼저 메모리 공간을 확보해야 할 `메모리의 크기(byte)`를 알아야한다. 이는 값의 종류에 따라 확보해야 할 메모리의 크기가 다르기 때문이다. 이때 값의 종류, `즉 데이터의 종류를 데이터 타입(Data Type)이라 한다.`

<br>

예를들어 1byte(8bit)로 표현할 수 있는 경우의 수, 즉 값의 총 개수는 256개(2의 8제곱)으로 `아스키코드(ASCII)`로 표현할 수 있으며, 4byte(32bit)로 표현할 수 있는 값의 총수는 4,294,967,296(2의 32제곱)로 -2,147,483,648 ~ 2,147,483,647의 정수를 표현할 수 있다.

<br>

다음은 C에서 정수형 변수를 선언하는 예이다.

```c
// 1byte 정수형: -128 ~ 127
char c;

// 4byte 정수형: -2,124,483,648 ~ 2,124,483,647
int num;
```

![title2](https://poiemaweb.com/img/int_num.png)

C 언어의 경우, 4byte 정수형인 int형 변수 선언을 만나면 시스템은 이후 할당될 값과는 상관없이 4byte의 메모리 영역을 확보한다. 이후 int형 변수에 할당할 때에는 int형 값을 할당해야 한다.

<br>

다음은 C에서 정수형 변수에 문자열을 잘못 할당한 예이다.

```c
int main(void) {
  int num = 46;
  char * str = "String";

  num = "String"; // warning: incompatible pointer to integer conversion assigning to 'int' from 'char [7]'

  return 0;
}
```

자바스크립트는 `동적 타입(Dynamic/Weak Type)`언어이다. 변수의 타입 지정(Type annotaion)없이 값이 할당되는 과정에서 자동으로 변수의 타입이 결정된다. 즉, 변수는 고정된 타입이 없다. 따라서 같은 변수에 여러 타입의 값을 자유롭게 할당할 수 있다.

```javascript
var str  = 'Hello';
var num  = 1;
var bool = true;

var foo = 'string';
console.log(typeof foo); // string
foo = 1;
console.log(typeof foo); // number
```

이제 자바스크립에는 어떠한 데이터 타입이 있는지 그리고 변수는 어떻게 사용하는지 알아보도록 하자.

<br>


# 데이터 타입

`데이터 타입(Data Type)`은 프로그래밍 언어에서 사용할 수 있는 데이터(숫자, 문자열, 불리언 등)의 종류를 말한다.

<br>

코드에서 사용되는 모든 데이터는 메모리에 저장하고 참조할 수 있어야 한다. 데이터 타입은 데이터를 메모리에 저장할 때 확보해야 하는 메모리 공간의 크기와 할당할 수 있는 유효한 값에 대한 정보, 그리고 메모리에 저장되어 있는 `2진수 데이터를 어떻게 해석`할 지에 대한 정보를 컴퓨터와 개발자에게 제공한다.

<br>

데이터 타입은 한정된 메모리 공간을 효율적으로 사용하기 위해서, 그리고 2진수 데이터로 메모리에 저장된 데이터를 다양한  형태로 사용하기 위해 존재한다.

<br>

자바스크립트의 모든 값은 데이터 타입을 갖는다. ECMAScript 표준(ECMAScript 2015)은 7개의 데이터 타입을 제공한다.

- ### 원시 타입(primitive data type)
    - `boolean`
    - `null`
    - `undefined`
    - `number`
    - `string`
    - `symbol` (EC6에서 추가)

- ### 객체 타입(object/reference type)
    - `object`


예를 들어 숫자(number) 타입 1과 문자열(string) 타입 ‘1’은 비슷하게 보이지만 다른 타입의 값이다. 숫자 타입의 값은 주로 산술 연산을 위해 만들지만 문자열 타입의 값은 주로 텍스트로 출력하기 위해 만든다.

<br>


자바스크립트에서 제공하는 7개의 데이터 타입은 크게 `원시 타입(primitive data type)`과 `객체 타입(object/reference type)`으로 구분할 수 있다.

<br>

## 1. 원시 타입(Primitive Data Type)

원시 타입의 값은 `변경 불가능한 값(immutable value)`이며 `pass-by-value(값에 의한 전달)`이다.

<br>

### 1.1 number

C나 Java의 경우, 정수와 실수를 구분하여 int, float, double 등과 같은 다양한 숫자 타입이 존재한다. `하지만 자바스크립트는 독특하게 하나의 숫자 타입만 존재한다.`

<br>

ECMAScript 표준에 따르면, 숫자 타입의 값은 배정밀도 64비트 부동소수점 형(double-precision 64-bit floating-point format: -(2^53 - 1)와 2^53 - 1의 사이의 숫자값)을 따른다. 즉, 모든 수를 실수로 처리하며 정수만을 표현하기 위한 특별한 데이터 타입(integer type)은 없다.

```javascript
var integer = 10;        // 정수
var double = 10.12;      // 실수
var negative = -20;      // 음의 정수
var binary = 0b01000001; // 2진수
var octal = 0o101;       // 8진수
var hex = 0x41;          // 16진수
```

2진수, 8진수, 16진수 리터럴은 메모리에 동일한 배정밀도 64비트 부동소수점 형식의 2진수로 저장된다. 자바스크립트는 2진수, 8진수, 16진수 데이터 타입을 제공하지 않기 때문에 이들 값을 참조하면 모두 10진수로 해석된다.

<br>

```javascript
console.log(binary); // 65
console.log(octal);  // 65
console.log(hex);    // 65

// 표기법만 다를뿐 같은 값이다.
console.log(binary === octal); // true
console.log(octal === hex);    // true
```

자바스크립트의 숫자 타입은 정수만을 위한 타입이 없고 모든 수를 실수로 처리한다. 정수로 표시된다해도 사실은 실수다. 따라서 정수로 표시되는 수 끼리 나누더라도 실수가 나올 수 있다.

<br>

```javascript
console.log(1 === 1.0); // true

var result = 4 / 2;
console.log(result); // 2
result = 3 / 2;
console.log(result); // 1.5
```

추가적으로 3가지 특별한 값들도 표현할 수 있다.

- `Infinity` : 양의 무한대
- `-Infinity` : 음의 무한대
- `NaN` : 산술 연산 불가(not-a-number)

```javascript
var pInf = 10 / 0;  // 양의 무한대
console.log(pInf);  // Infinity

var nInf = 10 / -0; // 음의 무한대
console.log(nInf);  // -Infinity

var nan = 1 * 'string'; // 산술 연산 불가
console.log(nan);       // NaN
```

<br>

### 1.2 string

문자열(String) 타입은 텍스트 데이터를 나타내는데 사용한다. 문자열은 0개 이상의 16bit 유니코드 문자(UTF-16) 들의 집합으로 대부분의 전세계의 문자를 표현할 수 있다. 문자열은 작은 따옴표('') 또는 큰 따옴표("")안에 텍스트를 넣어 생성한다.

```javascript
var str = "string"; // 큰 따옴표
str = 'string';     // 작은 따옴표
str = `string`;     // 백틱(ES6 템플릿 리터럴)

str = "큰 따옴표로 감싼 문자열 내의 '작은 따옴표'는 문자열이다.";
str = '작은 따옴표로 감싼 문자열 내의 "큰 따옴표"는 문자열이다.';
```

C와 같은 언어와는 다르게, 자바스크립트의 문자열은 원시 타입이며 `변경 불가능(immutable)`하다. 이것은 한 번 문자열이 생성되면, 그 문자열을 변경할 수 없다는 것을 의미한다.

```javascript
var str = 'Hello';
str = 'world';
```

첫 번째 구문이 실행되면 메모리에 문자열 'Hello'가 생성되고 식별자 str은 메모리에 생성된 문자열 'Hello'의 메모리 주소를 가리킨다. 그리고 두 번째 구문이 실행되면 `이전에 생성된 문자열 'Hello'을 수정하는 것이 아니라 새로운 문자열 'world'를 메모리에 생성하고 식별자 str은 이것을 가리킨다.`

```javascript
var str = 'string';
// 문자열은 유사배열이다.
for (var i = 0; i < str.length; i++) {
  console.log(str[i]);
}

// 문자열을 변경할 수 없다.
str[0] = 'S';
console.log(str); // string
```

문자열은 배열처럼 인덱스를 통해 접근할 수 있다. 이와 같은 특성을 갖는 데이터를 `유사 배열`이라 한다. Java에서도 String 클래스는 char 배열의 확장이라 생각하면 된다.

<br>

str[0] = 'S'처럼 이미 생성된 문자열의 일부 문자를 변경해도 반영되지 않는다(이때 에러가 발생하지 않는다). 한번 생성된 문자열은 read only로서 변경할 수 없다. 이것을 `변경 불가능(immutable)`이라 한다.

<br>

그러나 새로운 문자열을 재할당하는 것은 물론 가능하다. 이는 기존 문자열을 변경하는 것이 아니라 새로운 문자열을 새롭게 할당하는 것이기 때문이다.

```javascript
var str = 'string';
console.log(str); // string

str = 'String';
console.log(str); // String

str += ' test';
console.log(str); // String test

str = str.substring(0, 3);
console.log(str); // Str

str = str.toUpperCase();
console.log(str); // STR
```

<br>

### 1.3 boolean

불리언(boolean) 타입의 값은 논리적 참, 거짓을 나타내는 `true`와 `false`뿐이다.

```javascript
var foo = true;
var bar = false;

// typeof 연산자는 타입을 나타내는 문자열을 반환한다.
console.log(typeof foo); // boolean
console.log(typeof bar); // boolean
```

`boolean`타입의 값은 참과 거짓으로 구분되는 조건에 의해 프로그램의 흐름을 제어하는 조건문에서 자주 사용한다.

<br>

비어있는 문자열과 `null`, `undefined`, 숫자 0은 `false`로 간주된다.

<br>

### 1.4 undefined

undefined 타입의 값은 `undefined`가 유일하다. 선언 이후 값을 할당하지 않은 변수는 `undefined`값을 가진다. 즉, 선언은 되었지만 값을 할당하지 않은 변수에 접근하거나 존재하지 않는 객체 프로퍼티에 접근할 경우 undefined가 반환된다.

<br>

이는 변수 선언에 의해 확보된 메모리 공간을 처음 할당이 이루어질 때까지 빈 상태(다른 언어에서는 대부분 비어있지 않고 쓰레기 값(Garbage value)이 들어있다)로 내버려두지 않고 `자바스크립트 엔진이 undefined로 초기화`한다.

```javascript
var foo;
console.log(foo); // undefined
```

이처럼 `undefined`는 개발자가 의도적으로 할당한 값이 아니라 자바스크립트 엔진에 의해 초기화된 값이다. 변수를 참조했을 때 undefined가 반환된다면 참조한 변수가 선언 이후 값이 할당된 적인 없는 변수라는 것을 개발자는 간파할 수 있다.

<br>

`그럼 변수의 값이 없다는 것을 명시하고 싶은 경우 어떻게 하면 좋을까?` 그런 경우는 undefined를 할당하는 것이 아니라 `null`을 할당한다.

<br>

### 1.5 `null`

null 타입의 값은 `null`이 유일하다. 자바스크립트는 대소문자를 구별(case-sensitive)하므로 `null`은 Null, NULL등과 다르다.

<br>

프로그래밍 언어에서 `null`은 의도적으로 변수에 값이 없다는 것을 명시할 때 사용한다. 이는 변수가 기억하는 Memory Address의 참조 정보를 제거하는 것을 의미하며 자바스크립트 엔진은 누구도 참조하지 않는 메모리 영역에 대해 `가비지 컬렉션`을 수행할 것이다.

```javascript
var foo = 'Lee';
foo = null;  // 참조 정보가 제거됨
```

타입을 나타내는 문자열을 반환하는 typeof 연산자로 null 값을 연산해 보면 null이 아닌 object가 나온다. 이는 자바스크립트의 설계상의 오류이다.

```javascript
var foo = null;
console.log(typeof foo); // object
```

따라서 null 타입을 확인할 때 typeof 연산자를 사용하면 안되고 `일치 연산자(===)`를 사용하여야 한다.

```javascript
var foo = null;
console.log(typeof foo === null); // false
console.log(foo === null);        // true
```

<br>

### 1.6 `symbol`

`심볼(symbol)`은 ES6에서 새롭게 추가된 7번째 타입으로 변경 불가능한 원시 타입의 값이다. 심볼은 주로 이름의 충돌 위험이 없는 유일한 객체의 프로퍼티 키(property key)를 만들기 위해 사용한다. 심볼은 Symbol 함수를 호출해 생성한다. 이 때 생성된 심볼 값은 다른 심볼 값들과 다른 유일한 심볼 값이다.

```javascript
var key = Symbol('key');
console.log(typeof key); // symbol

var obj = {};
obj[key] = 'value';
console.log(obj[key]); // value
```

<br>

## 1.2 객체 타입 (Object type, Reference type)

객체는 데이터와 그 데이터에 관련한 동작(절차, 방법, 기능)을 모두 포함할 수 있는 개념적 존재이다. 달리 말해, 이름과 값을 가지는 데이터를 의미하는 프로퍼티(property)와 동작을 의미하는 메소드(method)를 포함할 수 있는 독립적 주체이다.

<br>

자바스크립트는 객체(object) 기반의 스크립트 언어로서 자바스크립트를 이루고 있는 거의 “모든 것”이 객체이다. 원시 타입(Primitives)을 제외한 나머지 값들(배열, 함수, 정규표현식 등)은 모두 객체이다. 또한 객체는 `pass-by-reference(참조에 의한 전달) 방식`으로 전달된다.

<br>

# Reference

- [https://poiemaweb.com/js-data-type-variable](https://poiemaweb.com/js-data-type-variable)
