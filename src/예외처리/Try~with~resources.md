### 자동 자원 반환 - try - with - resources

JDK1.7부터 `try-with-resources` 이라는 `try-catch`문의 변형이 새로 추가되었다. 주로 입출력에 사용되는 클래스 중에서는
사용한 후에 꼭 닫아 줘야 하는 것들이 있다. 그래야 사용했던 자원(resources)이 반환되기 때문이다.

```java
class test {
    public void tryCatch() {
        try {
            fis = new FileInputStream("score.dat");
            dis = new DataInputStream(fis);
        } catch (IOException io) {
            ie.printStackTrace();
        } finally {
            dis.close();
        }
    }   
}
```

위의 코드는 `DataInputStream`을 사용해서 파일로부터 데이터를 읽는 코드인데, 데이터를 읽는 도중에 예외가 발생하더라도
DataInputStream이 닫히도록 finally 블럭 안에 close()를 넣었다. 여기까지는 문제가 없어보이지만 `진짜 문제는 close()가 예외를 발생시킬 수 있다는데 있다`

<br>

```java
class test {
    public void tryCatch() {
        try {
            fis = new FileInputStream("score.dat");
            dis = new DataInputStream(fis);
        } catch (IOException io) {
            ie.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }   
}
```

finally 블록안에 `try-catch` 추가해서 close()에서 발생할 수 있는 예외를 처리하도록 변경했는데, 코드가 복잡해져서 보기가 좋지 않다.
더 나쁜 것은 try블럭과 finally블럭에서 모두 예외가 발생하면, try블럭의 예외는 무시된다는 것이다. 한마디로 두개의 에러가 모두 발생할 수는 없으니까
뒤에 발생한 에러만 발생하게 된다.
이러한 점을 개선하기 위해 `try-with-resources`이 추가되었다. 위의 코들르 `try-with-resources`로 바꾸면 아래와 같다.

```java
class test {
    public void tryCatch() {
        try (FileInputStream fis = new FileInputStream("score.dat");
             DataInputStream dis = new DataInputStream(fis)) {
            while (true) {
                score = dis.readInt();
                System.out.println(score);
                sum += score;
            }
        } catch (EOFException e) {
            System.out.println("점수의 총합은 " + sum + "입니다");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }   
}
```

`try-with-resources`문의 괄호()안에 객체를 생성하는 문장을 넣으면, 이 객체는 따로 close()를 호출하지 않아도 try 블록을 벗어나는
순간 자동적으로 close()가 호출된다. 그 다음에 catch 블 또는 finally 블럭이 수행된다.

<br>

이처럼 `try-with-resources`문에 의해 자동으로 객체의 close()가 호출될 수 있으려면, 클래스가 `AutoCloseable`이라는 인터페이스를 구현한 것이어야 한다.

```java
class test {
    public interface AutoCloseable {
        void close() throws Exception;
    }     
}
```

<br>

### 예제

```java
class test {
    public static void main() {
        try (CloseableResource cr = new CloseableResource()) {
            cr.exceptionWork(false); // 예외 발생하지 않는다.
        } catch (WorkException e) {
            e.printStackTrace();
        } catch (CloseException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        
        try (CloseableResource cr = new CloseableResource()) {
            cr.exceptionWork(true); // 예외가 발생한다.
        } catch (workException e) {
            e.printStackTrace();
        } catch (CloseException e) {
            e.printStackTrace();
        }

    }
}
```

```java
class CloseableResource implements AutoCloseable {
    public void exceptionWork(boolean exception) throws WorkException {
        System.out.println("exceptionWork("+exception+")가 호출됨");
        
        if (exception) {
            throw new WorkException("WorkException발생!!!");
        }
    }   
    
    public void close() throws CloseException {
        System.out.println("close()가 호출됨");
        throw new CloseException("CloseException발생!!");
    }
}

class WorkException extends Exception {
    WorkException(String msg) {
       super(msg);
    }
}

class CloseException extends Exception {
    CloseException(String msg) {
        super(msg);
    }   
}
```

main메소드에 두 개의 `try-catch`문이 있는데, 첫 번째 것은 close()에서만 예외를 발생시키고, 
두 번째 것은 exceptionWork()와 close()에서 모두 예외를 발생시킨다. 

<br>

첫 번째는 일반적인 예외가 발생했을 때와 같은 형태로 출력되었지만, 두 번째는 출력형태가 다르다. 먼저 exceptionWork()에서 발생한
예외에 대한 내용이 출력되고, close()에서 발생한 예외는 `억제된(suppressed)`이라는 것이 출력된다.
두 예외가 동시에 발생할 수는 없기 때문에, 실제 발생한 예외를 WorkException 으로 하고, CloseException은 억제된 예외로 다룬다.

<br>

만일 기존의 `try-catch`문을 사용했다면, 먼저 발생한 WorkException은 무시되고, 
마지막으로 발생한 `CloseException`에 대한 내용만 출력되었을 것이다.




