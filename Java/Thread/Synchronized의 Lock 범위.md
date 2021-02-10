# `Synchronized 키워드 Lock의 범위는 어떻게 될까?`

Synchronized 키워드의 lock 범위는 어떻게 될까요? 라는 질문을 들으면 어떤 대답을 할 수 있을까요? 정답을 말하기 전에 예제 코드를 보면서 알아보겠습니다. 

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

위의 코드의 결과는 무엇일까요? 먼저 코드를 정리해보면 공유하고 있는 객체는 `MusicBox` 입니다. 그리고 클래스 내부에 `playMusicA`, `playMusicB` 메소드에 `synchronized`가 붙어있습니다. 

즉, 결과는 `playMusicA` 메소드가 다 실행되고, `playMusicB`가 실행이 될 것을 쉽게 예상할 수 있습니다. 

<br>

## `그러면 playMusicA 에만 synchronized 키워드가 존재하면 결과는 어떻게 될까요?`

즉, 메소드에 하나에만 synchronized가 존재합니다. lock의 개념을 조금 헷갈린다면 결과는 똑같다고 생각할 수도 있습니다. 하지만 이번에는 결과가 뒤죽박죽 출력이 됩니다. 

이유가 무엇일까요? playMusicA에는 synchronized가 있는데 말이죠... lock의 개념을 잘 모를 때는 synchronized 키워드가 있는 블럭 or 메소드에 들어가면 무조건 해당 쓰레드가 계속 점유를 하고 있다고 생각을 했습니다. 

하지만 synchronized 메소드에 들어갔더라도 해당 메소드에 여러 쓰레드가 동시에 접근이 불가능할 뿐이지, `문맥 교환은 발생합니다.` 그렇기 때문에 playMusicA에 synchronized 키워드가 있음에도 번갈아 출력이 되는 것입니다. 

여기서 하나 더 알 수 있는게 있습니다. `Lock의 범위는 객체 단위라는 것`을 하나 더 알 수 있습니다. 즉, 객체당 Lock을 하나만 가질 수 있습니다. 

그렇기 때문에 `synchronized`가 메소드 둘 다 붙어있다면 하나의 메소드가 이미 `lock`을 쥐고 있기 때문에 나머지 메소드는 기다리게 되는 것입니다. 

<br>

## `static 메소드에서 lock을 사용하면 어떻게 될까요?`

```java
public class MusicBox {
    public static synchronized void playMusicA( ) {
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

이번에는 둘다`synchronized` 키워드가 존재하고, `playMusicA`에 static을 넣어보았습니다. 결과는 어떻게 될까요?

결과는 `playMusicA`, `playMusicB`가 번갈아 출력되게 됩니다. 이번에는 왜 그럴까요?

`static` 메소드는 객체의 것이 아니라 클래스 메소드 입니다. 그렇기 때문에 Lock을 클래스도 하나를 가지고, 객체들 마다 하나씩 가지게 되는 것입니다. 그래서 둘 다 락을 가지고 메소드에 접근을 할 수 있는 것입니다. 

둘 다 static 메소드이며 `synchronized` 키워드를 사용한다면, 클래스도 lock을 1개만 가질 수 있기 때문에 위에서 말했던 개념들과 같습니다. 
