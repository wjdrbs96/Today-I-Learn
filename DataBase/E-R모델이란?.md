# `개체 - 관계(E-R) 모델이란?`

- 개체 관계 모델(E-R model: Entity-Relationship model)

- E-R 모델을 이용해 현실 세계를 개념적으로 모델링한 것

<br>

## `개체(Entity)란?`

- 현실 세계에서 조직을 운영하는 데 꼭 필요한 `사람`이나 `사물`과 같이 구별되는 모든 것
- 저장할 가치가 있는 중요 데이터를 가지고 있는 사람이나 사물, 개념, 사건 등
- 다른 개체와 구별되는 이름을 가지고 있고, 각 개쳄만의 고유한 특성이나 상태, 즉 속성을 하나 이상 가지고 있음
- 예) 서점에 필요한 개체: 고객, 책
- 예) 학교에 필요한 개체: 학과, 과목

<br>

## `속성(Attribute)란?`

- 개체나 관계가 가지고 있는 고유의 특성
- 의미 있는 데이터의 가장 작은 논리적 단위
- E-R 다이어그램에서 타원으로 표현하고 타원 안에 이름을 표기

![스크린샷 2020-11-18 오후 3 13 17](https://user-images.githubusercontent.com/45676906/99492281-9b372f00-29b0-11eb-9aca-b9e0c4a55461.png)

<br>

- ### `개체 타입(Entity type)`
    - 개체를 고유의 이름과 속성들로 정의한 것
 
- ### `개체 인스턴스(Entity instance)`
    - 개체를 구성하고 있는 속성이 실제 값을 가짐으로써 실체화된 개체
    
- ### `개체 집합(Entity set)`
    - 특정 개체 타입에 대한 개체 인스턴스들을 모아놓은 것
    
![스크린샷 2020-11-18 오후 3 15 54](https://user-images.githubusercontent.com/45676906/99492459-f701b800-29b0-11eb-8db1-e91fe3ffb67a.png)

위와 같이 `사용자`라는 `Entity`로 생각하고, 그 `속성`으로 어떤 것이 들어갈지를 정해서 위와 같이 만들 수 있다. 

<br>

## `속성의 분류`

![스크린샷 2020-11-18 오후 3 18 32](https://user-images.githubusercontent.com/45676906/99492668-5364d780-29b1-11eb-9c48-d4b7fb6e5e3a.png)

<br>

- ### 단일 값 속성과 다중 값 속성
    - 단일 값 속성(single-valued attribute)
        - 값을 하나만 가질 수 있는 속성
        - 예) 고객 개체의 이름
        
    - 다중 값 속성(multi-valued attribute)
        - 값을 여러 개 가질 수 있는 속성
        - 고객 개체의 연락처 속성
        - 책 개체의 저자 속성
        - E-R 다이어그램에서 이중 타원으로 표현한다. 

![스크린샷 2020-11-18 오후 3 21 28](https://user-images.githubusercontent.com/45676906/99492928-bc4c4f80-29b1-11eb-802d-226db808a0ed.png)

<br>

- ### 단순 속성과 복합 속성
    - 단순 속성(simple attribute)
        - 의미는 더는 분해할 수 없는 속성
        - 책 개체의 이름, ISBN, 가격 속성
    - 복합 속성(composite attribute)
        - 의미를 분해할 수 있는 속성
        - 고객 개체의 주소 속성(도, 시, 동, 우편번호 등으로 세분화 가능)
        - 고객 개체의 생년월일 속성(연, 월, 일로 의미를 세분화 가능)
         

![스크린샷 2020-11-18 오후 3 24 38](https://user-images.githubusercontent.com/45676906/99493222-2e249900-29b2-11eb-9db8-102582d316f9.png)

<br>

- ### 유도 속성(derived attribute)
    - 기존의 다른 속성의 값에서 유도되어 결정되는 속성
    - 책 개체의 가격과 할인율 속성으로 계싼되는 판매 가격 속성
    - 고객 개체의 출생연도 속성으로 계산되는 나이 속성
    - E-R 다이어그램에서 점선 타원으로 표현
    
![스크린샷 2020-11-18 오후 3 27 45](https://user-images.githubusercontent.com/45676906/99493477-9d01f200-29b2-11eb-8ac0-594f00b78142.png)

<br>

- ### 키 속성(key attribute)
    - 각 개체 인스턴스를 식별하는 데 사용되는 속성
    - 모든 개체 인스턴스의 키 속성 값이 다름
    - 고객 개체의 고객 아이디 속성
    - E-R 다이어그램에서 밑줄로 표현
    
![스크린샷 2020-11-18 오후 3 28 45](https://user-images.githubusercontent.com/45676906/99493547-c0c53800-29b2-11eb-863e-8a6bb8d6e665.png)

<br> <br>

- ## 관계(Relationship)
    - 개체와 개체가 맺고 있는 의미 있는 연관성
    - 개체 집합들 사이의 대응 관계, 즉 매핑(mapping)을 의미
    - `고객 - 책` 개체의 구매 관계
    - E-R 다이어그램에서 마름모로 표현
    
![스크린샷 2020-11-18 오후 3 30 12](https://user-images.githubusercontent.com/45676906/99493654-f5d18a80-29b2-11eb-9ef5-dd16be1a5631.png)

<br>

- ## 관계의 유형
    - ### `일대일(1:1) 관계`
        - 개체 A의 각 개체 인스턴스가 개체 B의 개체 인스턴스 하나와 관계 를 맺을 수 있고, 개체 B의 각 개체 인스턴스도 개체 A의 개체 인스턴스 하나와 관계를 맺을 수 있음
        ![스크린샷 2020-11-18 오후 3 34 20](https://user-images.githubusercontent.com/45676906/99493954-890ac000-29b3-11eb-94ee-105d57bad9b3.png)
            
   - ### `일대다(1:n) 관계`
        - 개체 A의 개체 인스턴스가 개체 B의 개체 인스턴스 여러 개와 관계를 맺을 수 있지만, 개체 B의 각 개체 인스턴스는 개체 A의 개체 인스턴스 하나와 관계를 맺을 수 있음 
        ![스크린샷 2020-11-18 오후 3 35 43](https://user-images.githubusercontent.com/45676906/99494082-b9eaf500-29b3-11eb-8849-8a895963d707.png)

   - ### `다대다(n:m) 관계`
        - 개체 A의 개체 인스턴스가 개체 B의 개체 인스턴스 여러 개와 관계를 맺을 수 있고, 개체 B의 각 개체 인스턴스도 개체 A의 개체 인스턴스 여러 개와 관계를 맺을 수 있음
        ![스크린샷 2020-11-18 오후 3 36 33](https://user-images.githubusercontent.com/45676906/99494137-d850f080-29b3-11eb-8fb6-d334f2d5cec6.png)

    - ### 이항 관계
        - 개체 타입 두개가 맺는 관계
    - ### 삼항 관계
        - 개체 타입 세 개가 맺는 관계
    - ### 순환 관계: 개체 타입 하나가 자기 자신과 맺는 관계
        - 개체 타입 하나가 자기 자신과 맺는 관계
        
![스크린샷 2020-11-18 오후 3 41 38](https://user-images.githubusercontent.com/45676906/99494546-8e1c3f00-29b4-11eb-80b2-6cbe0159f5db.png)

왼쪽이 `이항 관계`, 오른쪽이 `순환 관계`라고 할 수 있다. 

<br>

- ## `관계의 종속성`
    - E-R 다이어그램에서 약한 개체는 이중 사각형으로 표현하고, 약한 개체가 강한 개체와 맺는 관계는 `이중 마름모`로 표현한다.
    - 예) 어떤 회사를 다니는 직원이 회사에 부양가족을 등록했는 경우이다. 이 회사를 다니는 사람이 회사를 그만두면 등록된 부양가족도 사라지기 때문이다. 
    ![스크린샷 2020-11-18 오후 3 44 58](https://user-images.githubusercontent.com/45676906/99494824-0551d300-29b5-11eb-9332-cce928d83415.png)

<br>

 
## E-R 다이어그램 예시

![스크린샷 2020-11-18 오후 3 45 38](https://user-images.githubusercontent.com/45676906/99494887-1dc1ed80-29b5-11eb-817e-7f79c01283b4.png)

여러명의 고객이 여러개의 책을 살 수 있다. 같은 책을 사는 것도 가능하기 때문에 관계는 `다대다`이다. 그리고 책의 출판사는 하나이고 출판사에서는 여러 개의 책을 집필할 수 있기 때문에
관계는 `일대다`이다. 