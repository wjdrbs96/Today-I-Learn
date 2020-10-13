# `Git Study 1주차`

<br>

## `CLI 환경`에서 Git 명령어 실습

### 1. git --version 

- Git 버전을 확인하자 (Git이 설치되어 있지 않다면 [여기](https://git-scm.com/downloads) 에서 설치를 하자.)

<img src="https://user-images.githubusercontent.com/45676906/95771059-5fe07b00-0cf5-11eb-8933-5d3640c2f282.png">

<br>

### 2. git init 

- 빈 저장소를 새롭게 만들거나 기존의 저장소를 다시 초기화 하는 명령어이다. 

<img width="545" alt="스크린샷 2020-10-13 오전 1 50 48" src="https://user-images.githubusercontent.com/45676906/95771740-85ba4f80-0cf6-11eb-9969-c8d6dab9ab68.png">

<br>

`git init` 명령어 후에 `ls -al`을 해보면 `.git` 파일이 생성 된 것을 알 수 있다. `.git`은 `로컬 Git 저장소`라고 생각하면 된다.
`이 저장소 안에 모든 커밋들이 들어있다`. (아래에서 다시 설명한다.)

```
. : 현재 디렉토리
.. : 부모 디렉토리
.git : 로컬 저장소
```

<br>

### 3. git add

<img width="591" alt="스크린샷 2020-10-13 오전 1 57 01" src="https://user-images.githubusercontent.com/45676906/95772251-64a62e80-0cf7-11eb-8cbd-c168199a005f.png">

<br>

- 파일이 Tracked 상태이면서 커밋에 추가될 Staged 상태를 만들어 준다.(변경되거나 새로운 파일을 추적함)

```
git add 파일이름 (하나의 파일만을 add)
git add . (.을 사용하면 변경된 모든 파일을 add)
```

<br>

### 3-1 git status

- 파일의 상태 확인하는 명령어 

- git status 명령어를 쳤을 때 `초록색`으로 수정된 파일이 `Tracked(관리대상임)`이 되면 위의 사진과 같이 뜰 것이다. 

<img width="566" alt="스크린샷 2020-10-13 오전 2 01 56" src="https://user-images.githubusercontent.com/45676906/95772662-147b9c00-0cf8-11eb-9c22-70260dffcd5a.png">

<br>

- 그러나 변경사항이 없을 때는 `git status`를 입력하면 위와 같이 위의 내용은 파일을 하나도 수정하지 않았다는 문구를 보게될 것이다.

<br>

<img width="543" alt="스크린샷 2020-10-13 오전 2 25 37" src="https://user-images.githubusercontent.com/45676906/95774433-640f9700-0cfb-11eb-8345-914d7f107a1f.png">


<br>

- 그리고 test.md 파일을 만든 후에 `git status`를 입력했을 때 위와 같이 `빨간색`으로 `Untracked(관리대상이 아님)`도 보게될 것이다.
- Git이 수정된 파일을 추적할 수 없으면 위와 같이 빨간색이 뜰 것이다. (git add를 하지 않고 git status를 쳤을 때 빨간색이 뜬다.)

```
git status를 쳤을 때 빨간색으로 뜨는 것은 test.md 파일이 Untracked 상태라는 것을 말한다. 
Git은 Untracked 파일을 아직 스테이지에 넣어지지 않은 파일이라고 본다. 
파일이 Tracked 상태가 되기 전까지는 Git은 절대 그 파일을 커밋하지 않는다. ('git add 파일이름' 을 통해서 파일이 git이 추적할 수 있게 스테이지에 올려놔야 커밋의 대상이 될 수 있다.)

수정된 파일이나 새로운 파일이 존재하는 위치와 같거나 상위 디렉토리에서 'git add 파일이름' 를 적용해야 Git이 파일을 추적할 수 있다.
ex) A디렉토리 안에 test.md와 B디렉토리가 존재한다고 가정해보자. 그리고 test.md를 수정하고 B디렉토리를 들어가서 `git add 파일이름`을 하게 되면 Git이 test.md파일을 추적할 수 없다는 뜻이다
```

<br>

## Tracked(관리대상임) vs Untracked(관리대상이 아님)는 무엇일까?

```
버전관리를 하는 폴더안에 모든 파일은 크게 Tracked(관리대상임)와 Untracked(관리대상이 아님)로 나눈다. 
Tracked 파일은 스테이지에 포함돼 있는 파일이다. 
Tracked 파일은 또 Unmodified(수정하지 않음)와 Modified(수정함) 그리고 로컬저장소(커밋으로 저장소에 기록할) 상태 중 하나이다. 
간단히 말하자면 Git이 알고 있는 파일이라는 것이다.

그리고 나머지 파일은 모두 Untracked 파일이다. Untracked 파일은 작업디렉토리(워킹 디렉토리)에 있는 파일 중 스테이지와 로컬저장소에도 포함되지 않은 파일이다. 
처음 저장소를 Clone 하면 모든 파일은 Tracked이면서 Unmodified 상태이다. 

마지막 커밋 이후 아직 아무것도 수정하지 않은 상태에서 어떤 파일을 수정하면 Git은 그 파일을 Modified 상태로 인식한다. 
실제로 커밋을 하기 위해서는 이 수정한 파일을 Staged 상태로 만들고, Staged 상태의 파일을 커밋한다. 
이런 라이프사이클을 계속 반복한다.
```


<br>


### git commit 

```
수정한 것을 커밋하기 위해 'git add 파일이름'을 통해서 스테이지에 파일을 정리했다. 
Unstaged 상태의 파일은 커밋되지 않는다는 것을 기억해야 한다. 
Git은 생성하거나 수정하고 나서 git add 명령으로 추가하지 않은 파일은 커밋하지 않는다. 
그 파일은 여전히 Modified 상태로 남아 있다. 
커밋하기 전에 git status 명령으로 모든 것이 Staged 상태인지 확인할 수 있다. 
그 후에 git commit 을 실행하여 커밋한다.
```

<img width="675" alt="스크린샷 2020-10-13 오전 2 35 35" src="https://user-images.githubusercontent.com/45676906/95775135-c6b56280-0cfc-11eb-92f9-c186c2154849.png">

<br>

`git add`, `git status`, `git commit` 까지 진행해보면 아래와 같이 할 수 있다. 

<img width="602" alt="스크린샷 2020-10-13 오전 2 36 42" src="https://user-images.githubusercontent.com/45676906/95775202-ef3d5c80-0cfc-11eb-87c7-5124459ca113.png">

<br>

# 전체 요약

![image](https://user-images.githubusercontent.com/45676906/95775271-11cf7580-0cfd-11eb-817a-a3316448dd5c.png)

<br>

위의 그림에서 보이는 `로컬 저장소`가 `git init`으로 만들었던 `.git`이라고 생각하면 된다. 그리고 위에서 말했던 것처럼 `git add`를 통해서
`스테이지 영역`으로 파일들을 올리고 스테이지에 들어있지 않은 파일들은 `절대 커밋`되지 않는다는 점을 기억하자. 

![스크린샷 2020-10-13 오전 11 16 48](https://user-images.githubusercontent.com/45676906/95807611-96de7d00-0d45-11eb-9ca7-a773f53740ba.png)

<br>

- `작업디렉토리`는 실제 작업하는 공간(실제 파일이 위치하는 곳)
- `index`는 git add 명령을 통해서 git의 추적을 받는 파일들이 위치한 곳이다.
- `Head`는 최종확정본(commit)을 의미한다.(HEAD는 항상 작업트리의 가장 최근 커밋을 가리킨다)

그렇게 `commit`까지 했다면 이제 원격 서버인 `Github`에 올리면 된다 방법은 아래와 같다.

<br>

### git push origin 브랜치이름 (ex: git push origin master)

`origin`은 원격 저장소 이름이고, `master`은 현재 사용하는 컴퓨터의 브랜치 이름이다. 위에서 `commit`내용이 저장되어 있는 로컬저장소의 내용 원격 서버로 올리는 과정이다.

![스크린샷 2020-10-13 오전 2 45 07](https://user-images.githubusercontent.com/45676906/95775836-1d6f6c00-0cfe-11eb-93eb-b8f0d86d4fa1.png)

<br>


## Git 명령어 정리

```
터미널에서 git 을 치면 아래와 같이 나온다. 
```

<img width="806" alt="스크린샷 2020-10-13 오전 2 57 43" src="https://user-images.githubusercontent.com/45676906/95776739-df734780-0cff-11eb-992d-c0b7db371d3d.png">


![image](https://user-images.githubusercontent.com/45676906/95775937-50196480-0cfe-11eb-9619-1d62d5c13200.png)

<br>

위의 명령어를 모두 다 알아야 하는 것은 아니지만 이런 명령어들이 있구나 하면서 참고하면 좋을 것 같다. 
`branch`를 만들고 이동하고 뒤로 되돌리는 것은 다른 글에서 정리해보려 한다.
다음 글에서는 `GitKraken`을 이용해서 `Git`을 사용하는 법을 정리할 예정이다. 





