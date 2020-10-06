## 그리디 알고리즘 

- 그리디 알고리즘(탐욕법)은 `현재 상황에서 지금 당장 좋은 것만 고르는 방법`이다.
- 일반적인 그리디 알고리즘은 문제를 풀기 위한 최소한의 아이디를 떠올릴 수 있는 능력을 요구한다.
- 그리디 해법은 그 정당성 분석이 중요하다.
    - 단순히 가장 좋아 보이는 것을 반복적으로 선택해도 최적의 해를 구할 수 있는지 검토한다.
    
    
<br>

### 상당수 그리디 문제는 탐욕법으로 얻은 해가 최적의 해라는 것을 추론할 수 있어야 풀리도록 출제된다.

<img src="https://user-images.githubusercontent.com/45676906/95219022-de3da880-082f-11eb-9838-9a9e1eb02b43.png">

<br>

위의 그림에서는 `그리디 알고리즘`을 이용하면 `최적의 해`를 구할 수 없다. 그렇지만 `그리디 알고리즘`을 적용해도 `최적의 해`가
보장된다는 것을 추론할 수 있으면 `그리디 알고리즘`을 사용하면 된다.

<br>

## 거스름 돈

<img src="https://user-images.githubusercontent.com/45676906/95219726-b0a52f00-0830-11eb-800c-58b786924d7d.png">

<br>

* 최적의 해를 빠르게 구하기 위해서는 `가장 큰 화폐 단위부터`돈을 거슬러 주면 된다.
* N원을 거슬러 줘야 할 때, 가장 먼저 500원으로 거슬러 줄 수 있을 만큼 거슬러 준다.
    * 이후에 100원, 50원, 10원짜리 동전을 차례대로 거슬러 줄 수 있을 만큼 거슬러 주면 된다.
    
    
<br>

### 거스름 돈 : 정당성 분석

* 가장 큰 화폐 단위부터 돈을 거슬러 주는 것이 최적의 해를 보장하는 이유는 무엇일까요?
    * 가지고 있는 동전 중에서 `큰 단위가 항상 작은 단위의 배수이므로 작은 단위의 동전들을 종합해 다른 해가 나올 수 없기 때문`이다.

* 만약에 800원을 거슬러 주어야 하는데 화폐 단위가 500원, 400원, 100원 이라면 어떻게 될까?
    * 이런 경우에는 `Dynamic Programming`을 활용하면 된다. 
    
* 그리디 알고리즘 문제에서는 정당성 분석을 할 수 있어야 한다. 
