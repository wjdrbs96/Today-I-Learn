## 손 코딩 문제

1. 소수 판별하기
3. 만화 별점주는 DB 구축해보기

<br>

Q. 문자열에서 처음으로 반복되지 않는 문자를 찾아내는 효율적인 함수를 작성하라.
예를 들어, “total” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘o’ 이며, “teeter” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘r’이다.

```java
/**
 * 문자열에서 처음으로 반복되지 않는 문자를 찾아내는 효율적인 함수를 작성하라.
 * 예들 들어, “total” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘o’ 이며,
 * “teeter” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘r’이다.
 */
public class Five {
    public static String solution(String s) {
        Map<Character, Integer> hm = new HashMap<>();

        for (int i = 0; i < s.length(); ++i) {
            hm.putIfAbsent(s.charAt(i), 1);
        }

        char ch = ' ';
        for (int i = 0; i < s.length(); ++i) {
            if (hm.get(s.charAt(i)) > 1) {
                ch = s.charAt(i);
                break;
            }

            hm.put(s.charAt(i), hm.get(s.charAt(i)) + 1);
        }

        return String.valueOf(ch);
    }

    public static void main(String[] args) {
        System.out.println(solution("total"));
    }
}
```

<br>

Q. 광고를 무작위로 노출하되, "금액에 비례"하여 노출빈도를 높이려고 합니다.
0~1 사이 실수를 발생시키는 랜덤함수 rand() (ex. java 의 경우 Math.rand()) 를 사용하여,
금액에 비례하여 광고가 노출되도록 반환하는 함수를 구현해주세요.
(함수를 호출하면 S/A/B/C 중 하나를 반환)
Ex. String pickup(List<?> ads);

- 100만원 짜리 광고 S
- 50만원 짜리 광고 A
- 30만원 짜리 광고 B
- 30만원 짜리 광고 C

```java

```

<br>

Q. 21, 9, 4, 1, 5 (높은수부터 차례대로 정렬)

```java
public class Four {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int[] list = new int[6];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < 6; ++i) {
            list[i] = Integer.parseInt(st.nextToken());
        }

        sort(list);

        System.out.println(Arrays.toString(list));
    }

    private static void sort(int[] list) {
        int i = 0, j = 0, key = 0;
        for (i = 1; i < list.length; ++i) {
            key = list[i];
            for (j = i - 1; j >= 0 && key > list[j]; --j) {
                list[j + 1] = list[j];
            }
            list[j + 1] = key;
        }
    }
}
```

- 삽입 정렬

<br>

Q.문자열이 주어질때 reverse 하는 코드 작성("apple" -> "elppa")

```java
public class Three {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine();

        for (int i = s.length() - 1; i >=0; --i) {
            System.out.print(s.charAt(i));
        }
    }
}
```

<br>

Q. 소수(Prime)세기 (주어진 숫자안에 있는 소수개수를 리턴
//, 예를들어 10이 주어지면 2,3,5,7이 소수이므로 4를 리턴)

```java
public class Two {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        int count = 1; // 2 포함
        for (int i = 3; i <= N; ++i) {
            if (isPrime(i)) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static boolean isPrime(int N) {
        int count = 0;
        for (int i = 2; i < Math.sqrt(N)+ 1; ++i) {
            if (N % i == 0) {
                return false;
            }
        }

        return true;
    }
}

```

<br> 

Q. 2017년 a월 b일의 요일인?

- a와 b를 입력 받아 요일 찾기

```java
public class One {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int month = sc.nextInt();
        int day = sc.nextInt();
        sc.close();

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String[] dayOfTheWeeks = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        int totalDays = day;
        for (int i = 0; i < month - 1; ++i) {
            totalDays += daysInMonth[i];
        }

        System.out.println(dayOfTheWeeks[totalDays % 7 + 1]);
    }
}
```

- 2017년 1월 1일이 무슨 요일인지 찾기
- 월요일이라면 7로 나누고 나머지 요일은 생각하기

<br>

Q. 정수를 입력 받을 것이고, 뒤에서부터 세자리마다 콤마를 찍어주는 코드를 작성하세요. (천 원하면 1,000 인 것처럼)

```java
public class Six {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String money = br.readLine();

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = money.length() - 1; i >= 0; --i) {
            count++;
            sb.append(money.charAt(i));
            if (count == 3) {
                sb.append(",");
                count = 0;
            }
        }

        System.out.println(sb.reverse());
    }
}
```

<br>

Q. 주어진 문자열에서 "x"를 제외하세요. 단, 문자열의 맨 앞 또는 맨 뒤에 "x"가 나타나는 경우는 제외하지 않습니다.

```
ex)

stringX("xxHxix") => "xHix"

stringX("abxxxcd") => "abcd"

stringX("xabxxxcdx") => "xabcdx"
```

```java
public class Seven {
    public static String solution(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s.charAt(0));
        for (int i = 1; i < s.length() - 1; ++i) {
            if (s.charAt(i) == 'x') continue;
            sb.append(s.charAt(i));
        }

        sb.append(s.charAt(s.length() - 1));

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(solution("xabxxxcdx"));
    }
}
```

<br>

Q. Snake 포맷에서 CamelCase 포맷 바꾸기 ex) User_id => UserId

```java
public class Eight {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String name = br.readLine();

        String[] list = name.split("_");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; ++i) {
            String s = list[i];
            for (int j = 0; j < s.length(); ++j) {
                if (j == 0) {
                    sb.append(String.valueOf(s.charAt(j)).toUpperCase());
                } else {
                    sb.append(s.charAt(j));
                }
            }
        }

        System.out.println(sb);
    }
}
```

<br>

Q. 1 ~ 10000까지 중 8의 개수 구하기

```java
public class Nine {
    public static void main(String[] args) {
        int count = 0;
        for (int i = 1; i <= 10000; ++i) {
            String num = String.valueOf(i);
            for (int j = 0; j < num.length(); ++j) {
                if (num.charAt(j) == '8') count++;
            }
        }

        System.out.println(count);
    }
}
```

<br>

Q. 인터페이스를 구현해서 큐를 구현해보세요.

```java
public interface Queue<T> {
    void enqueue(T item);
    void dequeue();
}

public class Test implements Queue<String> {

    private List<String> list;

    public Test() {
        list = new LinkedList<>();
    }

    @Overide
    void enqueue(String item) {
        list.add(item);
    }

    @Overide
    String dequeue(String item) {
        if (list.size() > 0) {
            String result = list.get(0);
            list.remove(0);
            return result;
        }
    }
}
```

<br>

Q. emp.name 별 salary.amount 합계 산출 - name은 uniq, not null, 추가로 그러면 여기서 합계가 200 이상인 사람만 출력

```
emp
| id | name |
| 1  | AA   |
| 2  | BB   |
| 3  | CC   |
| 4  | DD   |

salary
| id | emp_id    | payed_date    | amount |
| 1  | 1         | 2021-01-01    | 100    |
| 2  | 1         | 2021-02-01    | 100    |
| 3  | 2         | 2021-01-01    | 50     |
| 4  | 2         | 2021-02-01    | 100    |
| 5  | 3         | 2021-01-01    | 200    |
| 6  | 3         | 2021-02-01    | 300    |

SELECT e.name, SUM(s.amount)
FROM emp e 
LEFT JOIN salary s
ON e.id = s.emp_id
GROUP BY e.name 
HAVING SUM(s.amount) >= 200
```
