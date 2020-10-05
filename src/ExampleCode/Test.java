package ExampleCode;

public final class Test {                  // 조상이 될 수 없는 클래스
    final int max_size = 100;              // 값을 변경할 수 없는 멤버 변수 (상수)

    final void getMaxSize() {              // 오버라이딩 할 수 없는 메소드
        final int localValue = max_size;   // 값을 변경할 수 없는 지역 변수 (상수)
    }
}