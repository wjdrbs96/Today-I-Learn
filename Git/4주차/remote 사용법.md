# git remote : 로컬 저장소와 원격 저장소를 연결하기

원격 저장소를 자신의 로컬 PC로 복사를 해서 가져오려면 `clone`을 하면 되었다. 

<br>

### 그러면 만약 이미 작업해놓은 로컬 저장소가 있고 이를 원격 저장소와 연결한다면 어떻게 해야할까?

빈 원격 저장소를 클론하고 기존에 작업하던 파일들을 옮기는 것도 가능하겠지만 번거롭기도 하고 뭔가 효율적이지 못하다.

<br>

이럴 때 사용하는 명령어가 `git remote`이다. 먼저 일단 레포지토리를 하나 만들었다고 가정하자. (`이번 실습이 아니라 평소에 레포지토리를 새로만들 때 README.md를 만들지 말자. 이유는 아래에서 설명한다.`)


<img width="1792" alt="스크린샷 2020-11-03 오전 10 36 57" src="https://user-images.githubusercontent.com/45676906/97937274-85771680-1dc1-11eb-9f2b-69c25a80dcf8.png">

먼저 레포지토리를 만들어보면 위와 같이 만들 수 있고, 여기서는 `아래의 실습을 위해 README.md를 체크하고 레포지토리를 만들어보자.`

<br>

![스크린샷 2020-11-03 오전 10 43 16](https://user-images.githubusercontent.com/45676906/97937360-d0912980-1dc1-11eb-8192-859941ebac04.png)

README.md 파일을 체크하고 만들었다면 위와 같은 화면이 뜨지 않고 README.md가 만들어진 레포지토리 화면일 것이다. 나는 위의 명령어를 캡쳐하기 위해 일단 README.md를 체크하지 않고 만들었다. 

<br>

위의 명령어를 보면 레포지토리와 로컬 프로젝트를 연결하는 방법에 대해 설명이 되어 있다. 바로 실습을 해보자. 


<img width="1183" alt="스크린샷 2020-11-03 오전 10 51 39" src="https://user-images.githubusercontent.com/45676906/97937774-f1a64a00-1dc2-11eb-97d0-8511902e5a35.png">

현재 나는 `spring-boot-lecture`라는 나의 로컬에 있는 프로젝트를 원격 저장소와 연결시킬 것이다. 따라서 `spring-boot-lecture` 디렉토리가 있는 곳으로 cd를 통해 이동하자. 
그리고 아래의 명령어를 순서대로 입력해보자. 

- git init : `로컬 저장소`를 만든다. 
- git add, git commit : 설명 생략

```
git remote add 저장소별칭 https://github.com/사용자이름/원격저장소이름.git
ex) git remote add origin https://github.com/wjdrbs96/Gyunny_Spring_Study.git 

지금은 '원격 저장소 별칭'을 'origin'이라고 하였다. 
```
 
<img width="1182" alt="스크린샷 2020-11-03 오전 10 52 03" src="https://user-images.githubusercontent.com/45676906/97937877-45189800-1dc3-11eb-9746-0bee1bd0680a.png">

- `git remote -v` 를 입력했을 때 위와 같이 fetch, push 2개가 나온다면 잘 연결이 된 것이다.  


<br>

# git push : 로컬 작업 내역을 원격 저장소에 올리기

이제 push를 통해 로컬 저장소의 내용을 원격 저장소로 올리면 된다. 방법은 아래와 같다. 

```
git push 원격저장소별칭 로컬브랜치이름
ex) git push origin master
```

## 하지만 push를 하면 안되는데 왜 그럴까?

<img width="696" alt="스크린샷 2020-11-03 오전 11 10 52" src="https://user-images.githubusercontent.com/45676906/97938677-5bbfee80-1dc5-11eb-8927-fd2801602a9a.png">

위의 내용을 보면 push를 하기 전에 pull을 먼저해라 라는 문구가 보일 것이다. 그 이유 중에 하나가 원격 저장소에 README.md를 먼저
만들었기 때문이다. 이러한 내용 때문에 REAMDE.md를 먼저 만들지 말라고 했던 것이다.  

<br>

## 그러면 왜 레포지토리를 만들 때 README.md를 만들면 안될까?

Git은 원격 저장소에 로컬 저장소의 브랜치와 같은 이름의 브랜치가 있다면 해당 브랜치를 변경하고 없다면 새 브랜치를 원격 저장소에 만든다.
`단, 주의할 점은 같은 이름의 브랜치가 있는데 서로의 내역이 다르다면 푸시를 거부한다.` (지금은 Github default 브랜치가 main으로 바뀌었기 때문에 로컬 브랜치가 master인지 main인지 잘 확인을 하자)

<br>

즉, 백지상태인 원격 저장소에 로컬 저장소에 작업한 것을 푸시해야 하므로 README.md 파일을 생성하면 안된다고 했던 것이다. 

<br>

하지만 이번에는 실습을 위해서 레포지토리를 만들 때 README.md를 같이 만들었다. 그러면 아래와 같은 상황일 것이다. 
 
<img width="1792" alt="스크린샷 2020-11-03 오전 10 58 54" src="https://user-images.githubusercontent.com/45676906/97938013-a5a7d500-1dc3-11eb-9bc3-6feb25b6e568.png">

현재의 상황을 정리하면 Github 원격 레포지토리에서 `Create README.md`라는 하나의 커밋이 생겼고, 위에서 로컬저장소에 `initial commit`이라는 하나의 커밋을 만들었다. 

<img width="1792" alt="스크린샷 2020-11-03 오전 11 01 35" src="https://user-images.githubusercontent.com/45676906/97938295-67f77c00-1dc4-11eb-8184-5a72f94d1120.png">

`GitKraken`으로 상황을 살펴보면 위와 같은 상황이다. 

이 때 원래 Github의 업데이트가 된 내용을 로컬 저장소로 가져와 합하는 방법 중에 하나가 `git pull`이다. pull의 내용은 다른 파일에서 정리할 예정이다.


<br>

## 그러면 여기서 git pull을 하면 원격저장소와 로컬저장소가 Merge가 될까? 

<img width="701" alt="스크린샷 2020-11-03 오전 11 14 30" src="https://user-images.githubusercontent.com/45676906/97938819-ca04b100-1dc5-11eb-92be-32e65d3a4f2b.png">

pull을 했는데 `fatal: refusing to merge unrelated histories`라는 것이 뜨면서 제대로 작동하지 않는다. 

<br>

## pull이 제대로 작동하지 않는 이유는 무엇일까? 

git에서는 서로 관련 기록이 없는 이질적인 두 프로젝트를 병합할 때 기본적으로 거부하기 때문이다.
따라서 이것을 허용해주기 위해서는 아래의 명령어를 입력해야 한다. 

```
git pull origin 브랜치명 --allow-unrelated-histories
```