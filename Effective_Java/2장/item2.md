# 생성자에 매게변수가 많다면 빌더를 고려하라. 

`정적 팩토리 메소드`와 `생성자`에는 똑같은 제약이 하나 있다. 선택적 매게변수가 많을 때 적절히 대응하기 어렵다는 점이다. 

<br>

식품 포장 영양정보를 표현하는 클래스를 생각해보자. 영양 정보는 아래와 같다. 

- 1회 내용량
- 총 n회 제공량
- 1회 제공량당 칼로리같은 필수 항목 및 몇 개와 총 지방
- 트랜스지방
- 포화지방
- 콜레스테롤
- 나트륨

총 20개가 넘는 선택 항목으로 이뤄진다. 그런데 대부분 제품은 이선택 항목 중 대다수의 값이 그냥 0이다. 

<br>

## 이런 클래스용 생성자 혹은 정적 팩토리 메소드는 어떤 모습일까?

프로그래머들은 이럴 때 `점층적 생성자 패턴(telescoping constructor pattern)`을 즐겨 사용했다. 
필수 매게변수만 받는 생성자, 필수 매게변수와 선택 매게변수를 1개를 받는 생성자, 선택 매게변수를 2개까지 받는 생성자... 등등 형태로 선택 매게변수를 전부다 받는 생성자까지 늘려가는 방식이다. 
아래의 코드가 그 예이다. 

<br>


### 점층적 생성자 패턴(telescoping constructor pattern)

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

이 클래스의 인스턴스를 만들려면 원하는 매게변수를 모두 포함한 생성자 중 가장 짧은 것을 골라 호출하면 된다.

```
NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);
```

보통 이런 생성자는 사용자가 설정하길 원치 않는 매게변수까지 포함하기 쉬운데, 어쩔 수 없이 그런 매게변수에도 값을 지정해줘야 한다. 

<br>

위의 코드에서도 지방(fat)에 0을 넘겼다. 이 예에서는 매게변수가 '겨우' 6개뿐이라 그리 나빠 보이지 않을 수 있지만, 수가 더 늘어나면 금세 걷잡을 수 없게 된다. 

<br>

요약하면 `점층적 생성자 패턴도 쓸 수는 있지만, 매게변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다.` 
코드를 읽을 때 해당 위치의 값이 어떤 의미인지 헷갈릴 것이고, 매게변수가 몇 개인지도 주의해서 세어 보아야 할 것이다. 
타입이 같은 매게변수가 연달아 늘어서 있으면 실수 했을 때 티가 나지 않기 때문에 찾기 어려운 버그로 이어질 수 있다. 

<br>

클라이언트가 실수로 매게변수의 순서를 바꿔 건네줘도 컴파일러는 알아채지 못하고, 결국 런타임에 엉뚱한 동작을 하게 된다. 

<br>

이번에는 선택 매게변수가 많을 때 활용할 수 있는 두 번째 대안인 `자바빈즈 패턴(JavaBeans pattern)`을 보겠다. 

<br>

## 자바빈즈 패턴(JavaBeans pattern)

매게변수가 없는 생성자로 객체를 만든 후, 세터(setter) 메소드들을 호출해 원하는 매게변수의 값을 설정하는 방식이다. 

```java
public class NutritionFacts {
    private int servingSize = -1; // 필수; 기본값 없음
    private int servings = -1;         // 필수; 기본값 없음
    private int calories = 0; 
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public NutritionFacts() {}

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
```

점층적 생성자 패턴의 단점들이 자바빈즈 패턴에서는 더 이상 보이지 않는다. 코드가 길어지긴 했지만 인스턴스를 만들기 쉽고, 그 결과 더 읽기 쉬운 코드가 되었다.

```
NutritionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setSodium(35);
cocaCola.setCarbohydrate(27);
```

하지만 불행히도 자바빈즈는 자신만의 심각한 단점을 가지고 있다. `자바빈즈 패턴에서는 객체 하나를 만들려면 메소드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 된다.`

<br>

점층적 생성자 패턴에서는 매게변수들이 유효한지를 생성자에서만 확인하면 일관성을 유지할 수 있었는데, 그 장치가 완전히 사라진 것이다. 
일관성이 깨진 객체가 만들어지면, 버그를 심은 코드와 그 버그 때문에 런타임에 문제를 겪는 코드를 디버깅 하는 것도 만만치 않을 것이다. 

<br>

이처럼 일관성이 무너지는 문제 때문에 `자바빈즈 패턴에서는 클래스를 불변으로 만들 수 없으며` 쓰레드 안정성을 얻으려면 프로그래머가 추가 작업을 해줘야 한다.

<br>

### 불변 클래스(Immutable Class)이란?


Immutable을 사전적으로 찾아보면, 불변의, 변경할 수 없는 이라는 뜻임을 알 수 있다.
사전적인 의미에서도 알 수 있듯이 Immutable은 변경이 불가하다. 즉 Immutable Class는 변경이 불가능한 클래스이며, 가변적이지 않는 클래스이다. 
만들어진 Immutable Class는 레퍼런스 타입의 객체이기 때문에 heap영역에 생성된다. 
ex)`대표적으로 String이 존재한다.`

<br>

### Immutable 의 특징
 
- 장점 : 생성자, 접근메소드에 대한 방어 복사가 필요없다. 멀티스레드 환경에서 동기화 처리없이 객체를 공유할 수 있습니다.(Thread-safe) 불변이기 때문에 객체가 안전하다.
- 단점 : 객체가 가지는 값마다 새로운 객체가 필요하다. 따라서 메모리 누수와 새로운 객체를 계속 생성해야하기 때문에 성능저하를 발생시킬 수 있다.

<br>

이러한 단점들을 보완할 수 있는 세 번째 대안이 있다. `점층적 생성자 패턴의 안정성과 자바빈드 패턴의 가독성을 겸비한 빌더 패턴(Builder pattern)이다.`

<br>

### 빌더 패턴(Builder pattern)

클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매게변수만으로 생성자(혹은 정적 팩토리)를 호출해 빌더 객체를 얻는다. 

<br>

그런 다음 빌더 객체가 제공하는 일종의 세터 메소드들로 원하는 선택 매게변수들을 설정한다. 마지막으로 매게변수가 없는 build 매소드를 호출해 드디어 우리에게 필요한(불변인) 객체를 얻는다. 
빌더를 생성할 클래스 안에 정적 멤버 클래스로 만들어두는 게 보통이다.

```java
public class NutritionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int fat;
    private int sodium;
    private int carbohydrate;

    public static class Builder {
        // 필수 매게변수
        private final int servingSize;
        private final int servings;

        // 선택 매게변수
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
```

NutritionFacts 클래스는 불변이며, 모든 매게변수의 기본값들을 한곳에 모아뒀다. 빌더의 세터 메소드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출할 수 있다.

<br>

이런 방식을 메소드 호출이 흐르듯 연결된다는 뜻으로 `메소드 연쇄(Method chaining)`라 한다. 

```java
public class NutritionFacts {

    public static void main(String[] args) {
        NutritionFacts cocaCola = new Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
}
```

먼저 servingSize, servings를 필수 매게변수로 선택하고 나머지를 선택 매게변수로 정한 후에 빌더 패턴을 만들었다.
위와 같이 빌더를 이용하여 코드를 작성하면 쓰기도 쉽고, 무엇보다도 읽기 쉽다. 

<br>

빌더 패턴에 장점만 있는 것은 아니다. 객체를 만들려면, 그에 앞서 빌더부터 만들어야 한다. 빌더 생성 비용이 크지는 않지만 성능에 민감한 상황에서는
문제가 될 수 있다. 또한 점층적 생성자 패턴보다는 코드가 장황해서 매게변수가 4개 이상은 되어야 값어치를 한다. 

<br>

하지만 API는 시간이 지날수록 매게변수가 많아지는 경향이 있음을 명심하자. 생성자나 정적 팩터리 방식으로 시작했다가 나중에 매게변수가 많아지면 빌더 패턴으로 전환할 수도 이씾만,
이전에 만들어둔 생성자와 정적 팩터리가 아주 도드라져 보일 것이다. 그러니 애초에 빌더로 시작하는 편이 나을 때가 많다.

<br>

### 핵심 정리

> 생성자나 정적 팩터리가 처리해야 할 매게변수가 많다면 빌더 패턴을 선택하는 게 더 낫다. 
> 매게변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그렇다. 빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨신 간결하고, 자바빈즈보다 훨신 안전하다. 



