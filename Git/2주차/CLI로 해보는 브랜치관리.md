# 들어가기 전

### 브랜치의 필요성

- 협업을 할 때 각자의 작업공간에서 작업할 수 있다.
- 목적과 기능별로 분리를 하여 작업할 수 있어 효율적이다.
- 버전 관리를 모르던 상황이면 복사와 붙여넣기로 실험을 해야 했었을 것이지만, Git의 브랜치 기능을 통해서 복사 붙혀넣기를 하지 않고 실험할 수 있다.

<br>

# CLI로 `git branch`와 `git checkout`: 새로운 브랜치 생성과 이동


## 1. `git branch`: 새로운 브랜치 생성

```
git branch
``` 

위의 명령어는 현재 어떤 브랜치가 있는지 볼 수 있다. 

<img width="662" alt="스크린샷 2020-10-22 오후 4 24 21" src="https://user-images.githubusercontent.com/45676906/96838612-0c072c00-1483-11eb-9f78-e4bd65c0c7a7.png">


```
git branch 브랜치이름
ex) git branch feature_jg
```

`git branch 브랜치이름` 명령을 실행하면 `브랜치이름`의 브랜치가 만들어진다. 위의 명령어라면 `feature_jg` 브랜치가 만들어진다.

<img width="662" alt="스크린샷 2020-10-22 오후 4 26 11" src="https://user-images.githubusercontent.com/45676906/96838815-4ec90400-1483-11eb-9527-cc5ed4531a26.png">


위와 같이 브랜치를 만들고 `git branch`를 통해서 현재 branch를 확인해보면 `feature_jg`가 추가된 것을 볼 수 있다.
(`Tip: 위의 *가 붙어 있는게 현재 위치하고 있는 브랜치이다.`)

<br>

## 2. `git checkout 브랜치이름`: 브랜치 이동

```
git checkout 브랜치이름
ex) git checkout feature_jg
```

<img width="661" alt="스크린샷 2020-10-22 오후 4 31 34" src="https://user-images.githubusercontent.com/45676906/96839424-255ca800-1484-11eb-92e6-7a3c4bba2843.png">


위와 같이 브랜치가 `master -> feature_jg`로 이동이 되었다. 

<br>

## 3. 브랜치 삭제

```
git branch -d 브랜치이름 
ex) git bracnh -d feature_jg (이렇게 삭제가 되지 않는다면 강제삭제 하기)

git branch -D 브랜치이름(강제삭제)
ex) git branch -D feature_jg
```

위의 명령어를 통해서 로컬에서 브랜치를 삭제할 수 있다.

<br>

## 3-1. 원격 브랜치 삭제

```
git push origin :브랜치이름
ex) git push origin :feature_jg
```

자신의 컴퓨터인 로컬에서 브랜치를 만들어 작업을 한 후에 push까지 완료하면 `Github`에도 브랜치가 생긴다. 그리고 `git branch -d 브랜치이름`
으로 로컬에 있는 브랜치를 삭제해도 `Github`에는 반영이 되지 않기 때문에 위의 명령어를 통해서 원격브랜치를 제거해줘야 한다. 

<br>

## 새로운 브랜치 생성과 이동

```
git checkout -b 브랜치이름
ex) git checkout -b feature_jg
```

위의 명령어는 브랜치를 만들면서 바로 체크아웃할 수 있다. 한마디로 `git branch 브랜치이름` + `git checkout 브랜치이름`을 합친 명령어이다.

<br>

이번에는 `feature_jg` 브랜치에서 `branch.md` 파일을 하나 만들어서 내용을 아무거나 입력해보자. 

```
touch branch.md
vi branch.md
```

<img width="661" alt="스크린샷 2020-10-22 오후 4 38 43" src="https://user-images.githubusercontent.com/45676906/96840164-0f031c00-1485-11eb-8f07-0868689c734e.png">


### 그런데 여기서 master 브랜치(다른 브랜치)로 넘어가고 싶다면 어떻게 할까?

- `stash`에 잠시 보관하고 넘어간다.
- 현재까지 했던 작업을 `commit` 하고 넘어간다.


하나씩 어떤 의미인지 살펴보자.

<br>



## `git stash`란?

![image](https://user-images.githubusercontent.com/45676906/96842390-ef212780-1487-11eb-9dc5-6932f3764988.png)

```
어떤 작업을 하던 중에 다른 요청이 들어와 하던 작업을 멈추고 잠시 브랜치를 변경해야 할 일이 있다고 가정하자.
이때, 아직 완료하지 않은 일을 commit하는 것은 껄끄럽다. 

git stash는 아직 마무리하지 않은 작업을 스택에 잠시 저장할 수 있도록 하는 명령어이다. 
이를 통해 아직 완료하지 않은 일을 commit하지 않고 나중에 다시 꺼내와 마무리할 수 있다.
```

- git stash 명령을 사용하면 `워킹 디렉토리에서 수정한 파일들만 저장`된다.
- stash란 아래에 해당하는 파일들을 `보관해두는 장소`이다.
    - `stash란 아래에 해당하는 파일들을 보관해두는 장소`이다.
        - Modified이면서 Tracked 상태인 파일
        - Tracked 상태인 파일을 수정한 경우
    - Tracked: 과거에 이미 commit하여 스냅샷에 넣어진 관리 대상 상태의 파일
        - Staging Area에 있는 파일(Staged 상태의 파일)
        - git add 명령을 실행한 경우
        - Staged 상태로 만들려면 git add 명령을 실행해야 한다.
        - git add는 파일을 새로 추적할 때도 사용하고 수정한 파일을 Staged 상태로 만들 때도 사용한다.
     
<br>

<img width="658" alt="스크린샷 2020-10-22 오후 4 53 44" src="https://user-images.githubusercontent.com/45676906/96841769-26db9f80-1487-11eb-8f40-7117e4793c83.png">


위와 같이 수정했던 내용이 `stash`에 저장이 된 것을 볼 수 있다.

<br>

그리고 `master` 브랜치로 넘어와서 `feature_jg` 브랜치에서 만들었던 `branch.md` 파일이 있는지 확인해보자.

<img width="669" alt="스크린샷 2020-10-22 오후 6 11 49" src="https://user-images.githubusercontent.com/45676906/96850674-0f55e400-1492-11eb-9d90-e2bbff4f8021.png">

위와 같이 `feature_jg` 브랜치에서 `branch.md` 파일을 만들었지만 `master` 브랜치에는 반영이 되지 않는 것을 알 수 있다.
이렇게 `각 브랜치의 독립성`을 확인할 수 있다. 

<br>

### stash 목록 확인하기

```
git stash list
```

<img width="657" alt="스크린샷 2020-10-22 오후 5 07 51" src="https://user-images.githubusercontent.com/45676906/96843304-217f5480-1489-11eb-8cbf-4f8b95532ede.png">

<br>

### stash 적용하기(했던 작업을 다시 가져오기)

이제 `master`에서 해야 할 작업을 마쳤다고 가정하고 다시 `feature_jg` 브랜치로 돌아가자. 

```
git checkout feature_jg
git stash pop (stash의 가장 최근에 넣어둔 것을 꺼내온다)
```

<img width="662" alt="스크린샷 2020-10-22 오후 6 35 57" src="https://user-images.githubusercontent.com/45676906/96853525-6f9a5500-1495-11eb-8475-c183a6b708e4.png">

그러면 위와 같이 stash에 있던 파일이 다시 꺼내지는 것을 확인할 수 있다. 

<br> 

## `commit`한 후에 넘어가기

```
어떤 작업을 하던 중에 다른 요청이 들어와 하던 작업을 멈추고 잠시 브랜치를 변경해야 할 일이 있다고 가정하자.
이때, git stash를 이용해서 브랜치를 넘어가기에는 혹시나 실수로 코드가 날아가는 상황이 발생할 수 있어서 걱정이 된다.

이럴 때는 commit을 하고 브랜치를 넘어가고 기능이 완료가 되면 commit을 합치는 작업을 하면 된다.
```

<img width="705" alt="스크린샷 2020-10-22 오후 6 49 03" src="https://user-images.githubusercontent.com/45676906/96855117-424ea680-1497-11eb-95f1-114a56c0a54d.png">


먼저 `feature_jg` 브랜치에서 `branch.md` 파일을 수정한 후에 위와 같이 `commit`을 하면 다른 브랜치로 넘어갈 수 있다.

<br>

## `git rebase -i`: 커밋 내역 합하기

```
git rebase -i HEAD~~
Tip: ~은 커밋 내역 하나를 의미한다. 포시한 수 만큼 커밋을 되돌린다. 하나면 최종 커밋 내역일 것이고 두 개면 최종 커밋 내역과 바로 전 커밋 내역이 된다. 
```

<img width="712" alt="스크린샷 2020-10-22 오후 7 14 48" src="https://user-images.githubusercontent.com/45676906/96858162-dbcb8780-149a-11eb-9161-830bd5844f6b.png">


`rebase` 명령어를 치면 위와 같은 화면을 볼 수 있다. 두 커밋의 요약 정보가 커밋 메세지로 표시된다.

<br>

이를 수정할 때는 다음과 같은 원칙을 지켜야 한다. 

- 남기는 커밋 메세지 앞에는 접두어로 pick을 붙인다.
- 없애는 커밋 메세지 앞에는 접두어로 fixup을 붙인다.
- 커밋 SHA-1 체크섬 값은 꼭 남겨두어야 한다.
- 기존의 커밋 메세질르 새롭게 수정할 수는 없다.


<img width="714" alt="스크린샷 2020-10-22 오후 7 17 16" src="https://user-images.githubusercontent.com/45676906/96858490-47adf000-149b-11eb-9c33-9731654ed4cd.png">


삭제할 커밋메세지를 `fixup`으로 바꾼 후에 `ESC + wq`로 저장을 하자. 그리고 다시 확인을 해보면 커밋이 하나로 합쳐진 것을 확인할 수 있다.

<br>

## `git commit --amend:` 마지막 커밋 수정하기

```
git commit --amend
```

<img width="702" alt="스크린샷 2020-10-22 오후 7 24 57" src="https://user-images.githubusercontent.com/45676906/96859348-5c3eb800-149c-11eb-85b6-b40ed92f4fa5.png">


위의 명령어를 친 후에 네모 표시에 있는 커밋메세지를 수정한 후에 `ESC + wq`로 저장하면 커밋메세지를 수정할 수 있다. 


