# `아이템2 : 생성자에 매게변수가 많다면 빌더를 고려하라`

앞서 보았던 `정적 팩토리 메소드`와 `생성자`에는 똑같은 제약이 하나 있습니다. 바로 `선택적 매게변수`가 많다면 적절히 대응하기가 어렵다는 것입니다.

<br>

예를들어, 식품 포장의 영양정보를 표현하는 클래스에 `1회 내용량`, `총 n회 제공량`, `1회 제공량당 칼로리`와 같은 필수 항목과 `총 지방`, `트랜스지방`, `포화지방`. . . . 
등 총 20개가 넘는 선택항목이 있다고 생각해보겠습니다. 

<br>

이러한 클래스를 `생성자` or `정적 팩토리`를 사용한다면 어떻게 할 수 있을까요? 하나씩 알아보겠습니다. 

<br>

### `대안A) 점층적 생성자 패턴(telescoping constructor pattern)을 사용해보겠습니다.` 

`점층적 생성자 패턴`이란 쉽게 말하면 매게변수가 2개인, 3개인, 4개인 . . . 생성자를 점층적으로 늘려서 만드는 것을 의미합니다. 
코드를 보면서 자세히 알아보겠습니다. 

```java
public class NutritionFacts {
    private final int servingSize;  // (mL, 1회 제공량)     필수
    private final int servings;     // (회, 총 n회 제공량)  필수
    private final int calories;     // (1회 제공량당)       선택
    private final int fat;          // (g/1회 제공량)       선택
    private final int sodium;       // (mg/1회 제공량)      선택
    private final int carbohydrate; // (g/1회 제공량)       선택

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }
    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize  = servingSize;
        this.servings     = servings;
        this.calories     = calories;
        this.fat          = fat;
        this.sodium       = sodium;
        this.carbohydrate = carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFacts cocaCola =
                new NutritionFacts(240, 8, 100, 0, 35, 27);
    }
    
}
```

위와 같이 매게변수가 2개, 3개, 4개, 5개를 갖고 있는 생성자를 볼 수 있습니다. 이렇게 작성을 했을 때, `지금은 필드의 개수가 적지만 개수가 많아졌을 때는
클라이언트 코드를 작성하거나 가독성이 좋지 않다는 단점이 있습니다.` 그리고 객체를 만들 때 해당 매게변수가 어떤 것인지 헷갈릴 수도 있고, 개수가 몇개인지 등등 여러가지로 혼란스럽게 만들 것입니다. 

<br>

### `대안B) 자바빈즈 패턴(JavaBeans pattern)`

자바빈즈 패턴이란 `매게변수가 없는 생성자로 객체를 만든 후에 setter 메소드를 호출해 원하는 매게변수의 값을 설정하는 방식입니다.`

```java
public class NutritionFacts {
    // 매개변수들은 (기본값이 있다면) 기본값으로 초기화된다.
    private int servingSize  = -1; // 필수; 기본값 없음
    private int servings     = -1; // 필수; 기본값 없음
    private int calories     = 0;
    private int fat          = 0;
    private int sodium       = 0;
    private int carbohydrate = 0;

    public NutritionFacts() { }

    // Setters
    public void setServingSize(int val)  { servingSize = val; }
    public void setServings(int val)     { servings = val; }
    public void setCalories(int val)     { calories = val; }
    public void setFat(int val)          { fat = val; }
    public void setSodium(int val)       { sodium = val; }
    public void setCarbohydrate(int val) { carbohydrate = val; }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts();
        cocaCola.setServingSize(240);
        cocaCola.setServings(8);
        cocaCola.setCalories(100);
        cocaCola.setSodium(35);
        cocaCola.setCarbohydrate(27);
    }
}
```

위의 코드를 보면 사용하고 싶은 필드만 넣을 수 있고 위에서 보았던 단점을 보완한 것 같습니다. 하지만 여기서도 단점이 심각한 단점이 존재합니다. 

`자바빈즈 패턴에서는 객체 하나를 만들려면 메소드(setter)를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 됩니다.`

정리하자면, 어디서나 setter가 호출될 수 있기 때문에 인스턴스가 중간에 다른 쓰레드에 의해 사용되어 버리는 경우, 안정적이지 않은 상태가 될 수 있기 때문에 쓰레드 안전 보장 `locking, synchronized 등`을 고려해야 합니다.

이렇게 클래스를 `불변`으로 만들 수 없고 `일관성`이 깨지게 되면 나중에 디버깅 할 때도 엄청나게 어렵게 되는 큰 단점을 가지고 있습니다. 
이러한 단점을 보완하기 위해 나온 것이 `빌더 패턴(Builder pattern) 입니다.`

<br>

### `대안C) 빌더 패턴(Builder pattern)`

점층적 생성자 패턴의 안정성과 자바빈즈 패턴의 가독성을 겸비한 `빌더 패턴(Builder pattern)`에 대해 알아보겠습니다. 

<br>

클라이언트는 필수 매게변수만으로 생성자를 호출해 빌더 객체를 얻습니다. 그리고 나머지 원하는 선택 매게변수들을 setter 메소드를 이용해 설정하고 build 메소드를 호출해 객체를 만들게 됩니다. 

<br>

어떤 말인지 코드를 보면서 알아볼까요?

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다.
        private int calories      = 0;
        private int fat           = 0;
        private int sodium        = 0;
        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
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
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();
    }
}
```

`NutritionFacts` 클래스는 불변이며, 내부 클래스로 `Builder` 클래스가 존재하는 것을 볼 수 있습니다. 그리고 필수 매게변수는 생성자로 만들고 선택 매게변수는 메소드를 통해 만든 것도 볼 수 있습니다.

<br>

그리고 main 메소드에서 객체를 만드는 과정을 보겠습니다. 처음에 필수 매게변수로 `Builder` 클래스의 객체를 만들고, 선택 매게변수는 메소드를 통해 추가하는 것을 볼 수 있습니다. 
메소드의 반환 값이 `Builder` 클래스이기 때문에 `메소드 체이닝`으로 사용할 수 있습니다. 그리고 마지막으로 `build()` 메소드를 사용해서 클라이언트가 필수 매개변수 + 선택매개변수로 값을 넘긴 것으로 `NutritionFacts`의 객체를 만들게 됩니다.

<br>

## `빌더 패턴의 쓰임새`

`빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋습니다.` 어떤 의미인지 코드의 예시를 보면서 이해해보겠습니다. 

![images](https://media.vlpt.us/images/hygoogi/post/26817898-e829-43f3-b15f-7df72e09126f/Untitled%20Diagram.png)

지금 예제 코드의 계층 구조를 나타내면 위와 같습니다. 그리고 책의 예제 코드를 보겠습니다. 

<br>

### `Pizza 클래스`

```java
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        // 하위 클래스는 이 메서드를 재정의(overriding)하여
        // "this"를 반환하도록 해야 한다.
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone(); // 아이템 50 참조
    }
}
```

Pizza 클래스 내부에 추상 클래스인 Builder 클래스가 있습니다. 여기서 `재귀적 타입 한정`을 이용하였습니다. 용어가 조금 어려운데 정리하면 아래와 같습니다. 

- `<? extends T> 와일드 카드의 상한 제한(upper bound) - T와 그 자손들을 구현한 클래스들만 매개변수로 가능합니다.`

위의 키워드 개념을 가지고 보면 Pizza.Builder 클래스의 제너릭 타입은 Pizza.Builder 클래스이거나 Pizza.Builder 클래스를 구현한 클래스들만 제너릭 타입T로 가능하다는 것입니다.

이렇게 사용하는 이유는 Pizza 클래스의 하위 클래스에서 형변환을 하지 않고도 사용하기 위해서 입니다. (이것은 `재귀적 타입 한정을 이용하는 이유를 생각해보면 될 것 같습니다.`)

<br>

그러면 바로 Pizza 클래스의 하위 클래스인 `NyPizza` 클래스를 보겠습니다. 

### `NyPizza 클래스`

```java
import java.util.Objects;

// 코드 2-5 뉴욕 피자 - 계층적 빌더를 활용한 하위 클래스 (20쪽)
public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    @Override
    public String toString() {
        return toppings + "로 토핑한 뉴욕 피자";
    }
}
```

일단 첫 번째로 볼 것은 NyPizza 클래스는 size를 필수 매개변수로 지정해놓았습니다. 그리고 NyPizza.Builder 클래스가 Pizza.Builder 클래스를 extends 하고 있는 것을 볼 수 있습니다.

<br> 

따라서 NyPizza.Builder 클래스가 Pizza.Builder 클래스를 extends 했기 때문에 `Pizza.Builder<Builder>` 이 코드 제너릭 타입에 Builder가 들어갈 수 있는 것입니다.

<br>

그리고 Pizza 클래스의 하위 클래스인 Calzone 클래스도 보겠습니다. 

### `Calzone 클래스`

```java
public class Calzone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false; // 기본값

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }

    @Override
    public String toString() {
        return String.format("%s로 토핑한 칼초네 피자 (소스는 %s에)",
                toppings, sauceInside ? "안" : "바깥");
    }
}
```

NyPizza와 같은 구성으로 되어 있는 것을 볼 수 있습니다. 여기서 하나 더 알아보아야 할 점은 `NyPizza` 클래스와 `Calzone` 클래스 모두 `Pizza` 클래스의 `self()` 메소드를 오버라이딩 했다는 점입니다. 

<br>

Pizza 클래스의 addTopping() 메소드를 보면 `return self()`의 코드를 볼 수 있습니다. Pizza 클래스의 자식 클래스에서 addTopping() 메소드를 호출했을 때 자식 클래스에서 오버라이딩 한 self() 메소드가 호출됩니다. 
자바에서는 self 타입이 없기 때문에 이렇게 우회 방법을 `시뮬레이트한 셀프 타입(simulated self-type) 관용구`라고 합니다. 

<br>

그리고 `재너릭 재귀적 타입 한정`의 개념과 `셀프타입 관용구`의 개념을 통해서 하위 클래스에서 형변환하지 않고도 메소드 체이닝을 사용할 수 있는 것입니다.


### `Main 메소드`

```java
public class PizzaTest {
    public static void main(String[] args) {
        NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder()
                .addTopping(HAM).sauceInside().build();

        System.out.println(pizza);
        System.out.println(calzone);
    }
}
```

그리고 Main 메소드에서 코드를 작성하는 것은 일반적인 빌더를 만드는 과정과 같습니다. 

<br>

## `위의 내용을 정리하자면 다음과 같습니다.`

- `빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋습니다.`
- `Pizza.Builder 클래스에 재귀적 타입 한정을 이용하여 제너릭 타입을 만들어서 하위 클래스에서 형변환을 하지 않고도 사용하게 해줍니다.`
- `자바에서는 self라는 개념이 없기 때문에 self() 메소드를 메소드 체이닝이 가능하게 구현해서 사용합니다.`

<br>

지금까지는 빌더 패턴의 장점을 살펴보았지만, `단점도 존재합니다.`

<br>

## `빌더 패턴의 단점`

- 객체를 만들 때 Builder 부터 만들어야 하는데, 빌더 생성 비용이 크지는 않지만, 성능에 민감한 상황에서는 문제가 될 수 있습니다.
- 점층적 생성자 패턴보다는 코드가 장황해서 매게변수가 4개 이상은 되어야 값어치를 합니다.

<br>

## `핵심 정리`

> 생성자나 정적 팩토리가 처리해야 할 매게변수가 많다면 빌더 패턴을 선택하는 게 더 낫다. 매게 변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그렇다. 빌더는 
> 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고, 자바빈즈보다 훨씬 안전하다.


