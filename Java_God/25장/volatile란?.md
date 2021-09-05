# `volatile란?`

멀티 코어 프로세서에서는 코어마다 별도의 캐시를 가지고 있습니다. 

![cached](https://lunatine.net/images/2016/07/cmp.png)

코어는 메모리에서 읽어온 값을 캐시에 저장하고 캐시에서 값을 읽어서 작업합니다. 다시 값을 읽어올 때는 먼저 캐시에 있는지 확인하고 
없을 때만 메모리에서 읽어옵니다. 

그러다보니 메모리의 특정 변수의 값이 변경되었는데 캐시에는 변경된 값이 반영되지 않아 메모리의 값과 캐시의 값이 다른 문제가 발생합니다. 

엄청나게 간단히 설명한 것이지만 이러한 상황에서 두 가지 방법이 있습니다. 

- `volatile을 사용하기 : 그러면 코어가 변수의 값을 읽어올 때 캐시가 아닌 메모리에서 값을 읽어오기 때문에 문제가 해결됩니다.`
- `synchronized 블록 사용하기 : 쓰레드가 synchronized 블록으로 들어갈 때와 나올 때, 캐시와 메모리간의 동기화가 이루어지기 때문에 값의 불일치가 해소되기 때문입니다.`

<br>

## `예제 코드`

```java
public class VolatileSample extends Thread {
    private double instanceVariable = 0;

    public void setDouble(double value) {
        this.instanceVariable = value;
    }

    @Override
    public void run() {
        while (instanceVariable == 0) {
            System.out.println(instanceVariable);
        }
    }
}
```
```java
public class RunVolatile {
    public static void main(String[] args) {
        RunVolatile sample = new RunVolatile();
        sample.runVolatileSample();
    }

    public void runVolatileSample() {
        VolatileSample sample = new VolatileSample();
        sample.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Sleep ended !!!");
        sample.setDouble(-1);
        System.out.println("Set value is completed !!!");
    }
}
```

코드의 예상 시나리오는 아래와 같습니다. 

- `sample이라는 VolatileSample 클래스의 객체를 만들어 쓰레드를 시작합니다.`
- `Thread.sleep() 메소드로 1초 대기 후 instanceVariable 값을 -1로 변경하고 끝냅니다.`

<br>

### `그런데 실제 결과는 어떻게 될까요?` 

값이 -1로 바뀌지 않고 무한루프가 끝나지 않는 결과 나옵니다. 

`결과가 이렇게 나오는 이유가 무엇일까요?`

맨 위의 그림을 보면 알 수 있듯이 `CPU는 각자의 캐시`를 가지고 있습니다. 그런데 위와 같이 반복적으로 참조하게 될 때는 캐시에 넣어놓고 쓰레드가 참조하게 됩니다. 

그런데 main 쓰레드에서 -1로 바꿨기 때문에 `VolatileSample` 쓰레드가 참조하는 캐시는 바뀌지 않습니다. 따라서 while 문이 끝나지 않는 것입니다. 

```
private volatile double instanceVariable = 0;
```

이 때 `volatile` 키워드를 선언해주면 됩니다. volatile을 쓰면 캐시가 아닌 메모리에서 값을 읽어오게 됩니다. 

<br>

### `하지만 항상 volatile을 쓰면 안됩니다.`

volatile을 쓰면 성능상으로 저하가 발생하기 때문에 `캐시간의 다른 데이터를 보지 않으면 굳이 volatiole을 쓸 필요가 없습니다.`

```java
public class VolatileSample extends Thread {
    private double instanceVariable = 0;

    public void setDouble(double value) {
        this.instanceVariable = value;
    }

    @Override
    public void run() {
        try {
            while (instanceVariable == 0) {
                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

그래서 run() 메소드에서 1밀리초를 기다리게 했더니 volatile 키워드를 사용하지 않고도 코드가 잘 끝나는 것을 볼 수 있습니다. 

> 데이터가 문제가 있을 때만 volatile 키워드를 사용하면 됩니다. 

<br>

## `volatile로 long과 double을 원자화하기`

JVM은 데이터를 4byte(32bit)단위로 처리하기 때문에 int와 int보다 작은 타입들은 한 번에 읽거나 쓰는 것이 가능합니다. 즉, 하나의 명령어로 읽거나 쓰기가 가능하다는 뜻입니다. 
하나의 명령어는 더 이상 나눌 수 없는 최소의 작업단위이므로, 작업의 중간에 다른 쓰레드가 끼어들 틈이 없습니다. 

그러나, 크기가 8바이트인 long과 double 타입의 변수는 하나의 명령어로 값을 읽거나 쓸 수 없기 때문에, 변수의 값을 읽는 과정에서 다른 쓰레드가 끼어들 여지가 있습니다. 

이럴 때 다른 쓰레드가 끼어들지 못하게 하려고 지금까지 공부했던 `synchronized 블록`을 사용할 수 있지만, 지금 정리하고 있는 `volatile` 키워드를 사용하는 방법도 있습니다. 

```
volatile long sharedVal;     // long 타입의 변수(8 byte)를 원자화
volatile double sharedVal;   // double 타입의 변수(8 byte)를 원자화
```  

이렇게 `volatile`을 사용한 변수는 읽거나 쓰기에 `원자화` 됩니다. `원자화란 작업을 더 이상 나눌 수 없다는 의미입니다.`