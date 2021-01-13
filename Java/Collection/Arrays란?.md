# `Arrays 클래스란?`

`Arrays` 클래스는 배열을 다르는데 유용한 클래스입니다. (클래스의 모든 메소드는 static으로 선언되어 있습니다.)

<br>

## `배열의 복사 - copyOf(), copyOfRange()`

- `copyOf(): 배열 전체를 복사해서 새로운 배열을 만든다.`
- `copyOfRange(): 배열의 일부를 복사해서 새로운 배열을 만들어 반환합니다.`

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 4};
        int[] arr2 = Arrays.copyOf(arr, 3);   // [0, 1, 2]
        int[] arr3 = Arrays.copyOf(arr, 7);   // [0, 1, 2, 3, 4, 0, 0]

        int[] arr4 = Arrays.copyOfRange(arr, 2, 4);  // [2, 3]  마지막 인덱스는 포함되지 않습니다.
        
    }
}
```

<br>

## `배열 채우기 - fill()`

- `fill(): 배열의 모든 요소를 지정된 값을 채웁니다.`

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] arr = new int[5];
        
        Arrays.fill(arr, 7);  // [7, 7, 7, 7, 7]
    }
}
```

<br>

## `배열의 정렬과 검색 - sort(), binarySearch()`

- `sort(): 배열을 정렬할 때`
- `binarySearch(): 배열에 저장된 요소를 검색할 때`

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] arr = {3, 2, 0, 1, 4};
        int i = Arrays.binarySearch(arr, 2);
        System.out.println(i);  // -5  => 정렬되지 않은 배열에서 검색했기 때문에 잘못된 결과 반환

        Arrays.sort(arr);  // 정렬
        int p = Arrays.binarySearch(arr, 2);
        System.out.println(p);   // 2  (정렬된 배열에서의 해당 값의 인덱스 반환)
    }
}
```

<br>

## `문자열의 비교와 출력 - equals(), toString()`

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 4};
        int[][] arr2D = {{11, 12}, {21, 22}};
        int[][] arr12D = {{11, 12}, {21, 22}};
        int[] arr1 = {0, 1, 2, 3, 4};

        System.out.println(Arrays.toString(arr));          // [0, 1, 2, 3, 4]
        System.out.println(Arrays.deepToString(arr2D));    // [[11, 12], [21, 22]]

        System.out.println(Arrays.equals(arr, arr1));          // true
        System.out.println(Arrays.deepEquals(arr2D, arr12D));  // true 
    }
}
```

- `Arrays.toString(): 1차원 배열에만 사용 가능`
- `Arrays.deepToString(): 다차원 배열에 사용 가능`
- `Arrays.equals(): 1차원 배열의 원소들이 같으면 true, 다르면 false`
- `Arrays.deepEquals(): 다차원 배열을 비교할 때 사용`

<br>

## `배열을 List로 변환 - asList(Object... a)`

```java
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        integers.add(5);   // Exception in thread "main" java.lang.UnsupportedOperationException
    }
}
```

- `asList()가 반환한 List의 크기를 변경할 수 없습니다. 즉, 추가 또는 삭제가 불가능합니다.`
- `저장된 내용은 변경 가능합니다.(ex: integers.set(1, 1);)`

크기를 변경하고 싶다면 아래와 같이 하면 됩니다. 

```java
public class Test {
    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        integers.add(5);
    }
}
```
