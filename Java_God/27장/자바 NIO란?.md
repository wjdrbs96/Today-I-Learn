# `자바 NIO란?`

JDK 1.4에서부터 NIO(New I/O)가 추가되었습니다. 추가된 이유는 `속도` 때문입니다.  

NIO는 지금까지 사용한 스트림을 사용하지 않고, `채널(Channel)`, `버퍼(Buffer)`를 사용합니다. 

자바 NIO에서 데이터를 주고 받을 때에는 버퍼를 통해서 처리합니다. 예제 코드를 보면서 알아보겠습니다. 

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioSample {
    public static void main(String[] args) {
        NioSample sample = new NioSample();
        sample.basicWriteAndRead();
    }

    public void basicWriteAndRead() {
        String fileName = "/Users/choejeong-gyun/Documents/test.md";
        try {
            writeFile(fileName, "My first NIO sample!!");
            readFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String fileName, String data) throws IOException {
        FileChannel channel = new FileOutputStream(fileName).getChannel();
        byte[] byteData = data.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteData);
        channel.write(buffer);
        channel.close();
    }

    public void readFile(String fileName) throws IOException {
        FileChannel channel = new FileInputStream(fileName).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char)buffer.get());
        }
        channel.close();
    }
}
```

- allocate(1024) 메소드를 통해서 버퍼의 크기를 정해줍니다.
- flip() 메소드는 buffer에 담겨있는 데이터의 가장 앞으로 이동합니다. 

이렇게 `ByteBuffer`와 `FileChannel`을 통해서 파일 데이터를 Read & Write를 해보았습니다. 예제 코드를 보면 그러려니 할 수 있지만 저에겐 아직 생소한 클래스들이 많은 것 같습니다 ㅜㅠ

<br>

## `NIO의 Buffer 클래스`

NIO에서 중요한 개념 3가지가 있습니다. 바로 `capacity()`, `limit()`, `position()` 입니다. 

- 현재의 `위치`를 나타내는 메소드가 position() 입니다.
- 읽거나 쓸 수 없는 `위치`를 나타내는 메소드가 limit() 입니다. 
- 버퍼의 `크기(capacity)`를 나타내는 것이 capacity() 입니다. 

3개 값의 관계는 아래와 같습니다. 

```
0 <= position <= limit <= 크기(capacity)
```

NIO를 제대로 이해하려면 위의 3개 관계를 꼭 이해하고, 기억해야 합니다. 

예제 코드를 보면서 좀 더 자세히 알아보겠습니다. 

```java
import java.nio.IntBuffer;

public class NioDetailSample {
    public static void main(String[] args) {
        NioDetailSample sample = new NioDetailSample();
        sample.checkBuffer();
    }

    public void checkBuffer() {
        try {
            IntBuffer buffer = IntBuffer.allocate(1024);
            for (int loop = 0; loop < 100; loop++) {
                buffer.put(loop);
            }
            System.out.println("Buffer capacity = " + buffer.capacity());
            System.out.println("Buffer limit = " + buffer.limit());
            System.out.println("Buffer position = " + buffer.position());
            buffer.flip();
            System.out.println("Buffer flipped !!! ");
            System.out.println("Buffer limit = " + buffer.limit());
            System.out.println("Buffer position = " + buffer.position());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
```
Buffer capacity = 1024
Buffer limit = 1024
Buffer position = 100
Buffer flipped !!! 
Buffer limit = 100
Buffer position = 0
```

- limit의 위치를 별도로 지정하지 않았으므로, 이 값은 기본 크기인 1024입니다. 
    - 버퍼에서 접근할 수 있는 데이터의 끝. 버퍼의 용량이 더 남아 있더라도 한도 지점을 변경하기 전에는 한도를 넘어서서 읽거나 쓸 수 없습니다. 
- 데이터를 추가한 후 버퍼의 position은 100입니다.
- flip()이라는 메소드를 호출한 다음에는 limit 값은 100이 되고, position은 0이 됩니다. 
    - flip() 메소드는 limit 값을 position으로 지정한 후, position을 0(가장 앞)으로 이동합니다. => 한도를 현재 위치로 설정합니다. 
