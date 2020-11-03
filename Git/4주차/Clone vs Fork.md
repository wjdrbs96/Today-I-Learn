# `clone` vs `Fork` 의 차이는?

clone과 fork의 차이는 무엇인지 하나씩 알아보자.

<br>

# 1. clone이란?

- clone은 특정 원격 Repository와 나의 로컬 PC를 연결해 데이터를 복사하여 가져오는 기능이다.

- 내가 생성한 원격 저장소를 내 컴퓨터와 연결해서 데이터를 복사하는 작업
- 포크한 원격 저장소를 내 컴퓨터와 연결해서 데이터를 복사하는 작업

![스크린샷 2020-11-03 오전 1 12 01](https://user-images.githubusercontent.com/45676906/97891069-aadc3400-1d71-11eb-8a1c-a013202529fb.png)

위와 같이 특정 레포지토리를 나의 로컬 PC로 다운을 받고 싶다면 위와 같이 `Code`를 입력한 후에 레포지토리 주소를 복사하자.
그리고 아래와 같이 입력하면 clone을 받을 수 있다. 

```
git clone https://github.com/wjdrbs96/Gyunny_Spring_Study.git
```

<br>

# 2. fork란?

fork는 `다른 사람의 Github Repository`에서 내가 어떤 부분을 수정하거나 추가 기능을 넣고 싶을 때 해당 repository를 `내 Github Repository`로 `그대로
복제하는 기능`이다. fork한 저장소는 원본(원래 Repository 주인)과 연결되어 있다. 여기서 연결되어 있다는 의미는 원래 레포지토리에 어떤 변화가 생기면(새로운 commit, push) 
이는 그대로 fork된 repository로 반영할 수 있다. 이 때 fetch, pull의 과정이 필요하다

<br>

그 후 original repository에 변경 사항을 원본 레포지토리에 적용하고 싶으면 원본 저장소에 `pull request`를 보내야 한다.  
`pull request`가 original repository의 관리자로 부터 승인 되었으면 내가 수정한 코드가 commit, merge되어 원본 레포지토리에 반영된다. 
pull request 하기 전까지는 내 github에 있는 fork한 repository에만 변경사항이 적용된다.   

<br>

즉 Repository에 권한이 없는 사용자가 저장소를 fork하고 fork한 자신의 저장소에 변경 사항을 적용한 후 Push한다. 
이 후 내 저장소에 있는 브랜치를 원래 저장소(original repository)에 `Pull Request` 요청을 보낸다. (보내는 법이 궁금하다면 [여기](https://devlog-wjdrbs96.tistory.com/222?category=882255) 에서 확인하자.)
내가 만든 코드가 승인되면 해당 저장소에 Merge 된다.

![스크린샷 2020-11-03 오전 1 25 25](https://user-images.githubusercontent.com/45676906/97892556-7ff2df80-1d73-11eb-9fdc-064586883914.png)

<br>

## Fork 예시

### Fork 한 원본 레포지토리

<img width="1792" alt="스크린샷 2020-11-03 오전 1 30 14" src="https://user-images.githubusercontent.com/45676906/97893173-479fd100-1d74-11eb-81d9-6ebc13087d5e.png">

<br>

### Fork 한 후에 나의 Github Repository

<img width="1792" alt="스크린샷 2020-11-03 오전 1 30 07" src="https://user-images.githubusercontent.com/45676906/97893166-453d7700-1d74-11eb-90df-3e392e3c2195.png">

그러면 위와 같이 `아이디/레포지토리이름`을 보았을 때 `아이디`가 다른 것을 알 수 있다. 


<br>

## 정리

![title](https://media.vlpt.us/post-images/imacoolgirlyo/cbe5ca40-5f44-11e9-88b2-25d00148b532/gitfork.png)

- Larry라는 사람의 Recipes 레포지토리에 Contributors로 등록이 되기 위해서는 먼저 나의 Github 저장소로 Fork를 한다.  
- 그리고 나의 Github Repository에 있는 저장소를 나의 로컬 PC에다 `clone`을 받아서 복사를 하자. 
- 그리고 본인이 원하는 수정사항을 적용하면 된다.
- 마지막으로 Pull Request를 보내 해당 레포지토리 주인의 승인을 기다리면 된다. 