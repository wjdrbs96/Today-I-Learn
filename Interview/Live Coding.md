## 손 코딩 문제

1. 소수 판별하기
2. Snake 포맷에서 CamelCase 포맷 바꾸기
3. 만화 별점주는 DB 구축해보기

<br>

Q. 문자열에서 처음으로 반복되지 않는 문자를 찾아내는 효율적인 함수를 작성하라.
예들 들어, “total” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘o’ 이며, “teeter” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘r’이다.

```
public static void main(String[] args) throws IOException {
/*
Q.
    문자열에서 처음으로 반복되지 않는 문자를 찾아내는 효율적인 함수를 작성하라.
    예들 들어, “total” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘o’ 이며,
     “teeter” 에서 처음으로 등장하는 반복되지 않는 문자는 ‘r’이다.
 */

    String s = "total";

    int[] alpha = new int[27];

    for(int i=0;i<s.length();i++){

        char nowChar = s.charAt(i);

        alpha[nowChar-'a']++;

    }

    for(int i=0;i<s.length();i++){

        char nowChar = s.charAt(i);

        if(alpha[nowChar-'a']==1){
            System.out.println(nowChar);
            break;
        }

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
public static String pickup(List<ADS> ads){
	  //0~100
	  double value = Math.random();

    int total = 0;

    for(ADS ad : ads){
        total+=ad.adMoney;
    }

    List<Double> adsValue = new ArrayList<>();

    for(ADS ad : ads){
        adsValue.add((double)ad.adMoney/(double)total);
    }

    for(int i=0;i<adsValue.size();i++){

        if(value <= adsValue.get(i)){

            return ads.get(i).adTitle;
        }

        value-=adsValue.get(i);

    }

    return "empty";
}

public static class ADS{
    Integer adMoney;
    String adTitle;

    public ADS(Integer adMoney, String adTitle) {
        this.adMoney = adMoney;
        this.adTitle = adTitle;
    }
}

```

<br>

```java
    
public static String pickup(List<ADS> ads){
		double value = Math.random();

    int total = 0;

    for(ADS ad : ads){
        total+=ad.adMoney;
    }

    List<Double> adsValue = new ArrayList<>();

    for(ADS ad : ads){
        adsValue.add((double)ad.adMoney/(double)total);
    }

    for(int i=0;i<adsValue.size();i++){

        if(value <= adsValue.get(i)){

            return ads.get(i).adTitle;
        }

        value-=adsValue.get(i);

    }

    return "empty";
}

public static class ADS{
    Integer adMoney;
    String adTitle;

    public ADS(Integer adMoney, String adTitle) {
        this.adMoney = adMoney;
        this.adTitle = adTitle;
    }
}

```


<br>

Q. 21, 9, 4, 1, 5 (높은수부터 차례대로 정렬)

```java
    
public static int[] sort(List<Integer> numbers){
		int[] sortedNums = new int[numbers.size()];

    sortedNums[0]=numbers.get(0);

    for(int i=1;i<numbers.size();i++){

        int nowIndex = i;
        sortedNums[nowIndex]=numbers.get(nowIndex);

        while(nowIndex>0){
            if(sortedNums[nowIndex]>sortedNums[nowIndex-1]){
                int tmp = sortedNums[nowIndex];
                sortedNums[nowIndex]=sortedNums[nowIndex-1];
                sortedNums[nowIndex-1]=tmp;
                nowIndex--;
            }
            else{
                break;
            }
        }

    }

    return sortedNums;

}

```

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