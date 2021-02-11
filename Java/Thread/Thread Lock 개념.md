# `Java Thread Lock 개념 정리`

Java에서 `synchronized` 키워드를 사용하는 방법은 크게 3가지로 나눌 수 있습니다. 어쩌면 단순한 내용이지만 하나씩 `Lock의 개념`을 이해하면서 정리해보겠습니다. 

<br>

## `synchronized 메소드`

```java
public class MusicExam {
    public static void main(String[] args) {
        MusicBox box = new MusicBox();

        MusicPlayer musicPlayer1 = new MusicPlayer(1, box);
        MusicPlayer musicPlayer2 = new MusicPlayer(2, box);

        musicPlayer1.start();
        musicPlayer2.start();
    }
}
```
```java
public class MusicBox {
    public synchronized void playMusicA( ) {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicA !!");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playMusicB() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicB !!");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
```java
public class MusicPlayer extends Thread {
    int type;
    MusicBox musicBox;

    public MusicPlayer(int type, MusicBox musicBox) {
        this.type = type;
        this.musicBox = musicBox;
    }

    @Override
    public void run() {
        switch (type) {
            case 1:
                musicBox.playMusicA();
                break;
            case 2:
                musicBox.playMusicB();
                break;
        }
    }
}
```

위의 코드를 간단히 먼저 이해를 하고 계속 글을 따라오시면 됩니다. (간단하게 말하자면.. MusicBox를 공유하고 있습니다.) 

지금 MusicBox 클래스에 playMusicA, playMusicB에 `synchronized` 키워드가 메소드 레벨에서 붙어있는 것을 볼 수 있습니다. 그러면 위의 코드 결과는 어떻게 될까요? 

playMusicA 찍히고 MusicB가 찍히는 것을 알 수 있습니다. 왜 그럴까요? 바로 `객체 단위로 Lock을 가지고 있기 때문입니다.` 즉, 객체당 하나의 Lock만 가질 수 있기 때문에 playMusicA가 먼저 Lock을 가지고 메소드를 실행하고 있기 때문에 playMusicB는 기다리게 되는 것입니다. 

위의 개념이 이번 글의 핵심입니다. 그러면 이번에는 `synchronized 블럭`에 대해서 알아보겠습니다. 

<br>

## `synchronized 블럭`

```java
public class MusicBox {
    public void playMusicA() {
        synchronized(this) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicA !!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void playMusicB() {
        synchronized (this) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicB !!");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

이번에는 `synchronized 블럭`을 사용했습니다. 현재 코드로만 보면 메소드 레벨에서 쓰나 블럭으로 쓰나 차이가 없습니다. 하지만 synchronized 블럭을 쓰면 메소드 전체에 Lock을 거는 것이 아니라 본인이 필요한 부분에만 Lock을 걸 수 있다는 유연함?을 가질 수 있습니다. 

이정도의 개념까지는 괜찮았는데, `this`가 정확히 의미하는게 어떤 것일까? 라는 생각을 했습니다. `this.name = name` 또는 `this.method()`와 같이 쓰는 것을 보았을 것입니다. 이 때 this의 의미는 클래스 내부 필드, 메소드를 의미합니다. 

`그러면 synchronized 블럭에서 this는 어떤 것을 의미할까요?`

제가 생각한 결론은 `this`라고 해도 그냥 `객체 단위의 Lock을 거는 것`이라고 생각했습니다. 즉, MusicBox 객체의 단위의 Lock을 거는 것입니다. 그래서 위의 코드를 실행해보면 역시나 playMusicA, playMusicB가 차례대로 출력이 되는 것을 볼 수 있습니다. 
(이유는 위의 synchronized 메소드와 같습니다.)

<br>

## `특정 객체로 Lock 걸기`

```java
public class MusicBox {
    private final Object object = new Object();

    public void playMusicA() {
        synchronized(object) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicA !!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void playMusicB() {
        synchronized (this) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicB !!");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

위와 같이 코드를 바꾼다면 결과는 어떻게 출력될까요? 이번에는 `Object 객체`를 만들어서 synchronized 블럭 괄호안에 넣었습니다. 

결과는 playMusicA, playMusicB가 번갈아가면서 출력이 됩니다. 왜 그럴까요? 위에서 Lock의 개념을 이해했다면 충분히 결과를 예측할 수 있습니다. 

`Object 객체도 Lock을 1개 가지고`, `MusicBox 객체도 Lock을 1개 가지기 때문에` 각자 Lock을 사용하면서 메소드에 접근할 수 있는 것입니다. 

```java
public class MusicBox {
    private final Object object = new Object();
    public void playMusicA() {
        synchronized(object) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicA !!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void playMusicB() {
        synchronized (object) {
            for (int i = 0; i < 10; ++i) {
                System.out.println("MusicB !!");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
``` 

그래서 당연히 위와 같이 `Object 객체 하나로만 잠금을 설정했다면 당연히 결과는 MusicA, MusicB 차례대로 출력이 됩니다.`

