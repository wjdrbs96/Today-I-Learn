# pull과 fetch의 차이는?

`remote`에 대한 정리를 하는 글에서 잠깐 pull에 대해 사용을 해보았는데, 여기서 어떤 의미인지 살펴보자. 

```
git pull : git remote 명령을 통해 서로 연결된 원격 저장소의 최신 내용을 로컬 저장소로 가져오면서 병합한다. git push와 반대의 성격이다.
git fetch : 로컬 저장소와 원격 저장소의 변경 사항이 다를 때 이를 비교 대조하고 git merge 명령어와 함께 최신 데이터를 반영하거나 충돌 문제 등을 해결한다. 
```

`Git Study`에서는 혼자서 진행하는 실습이므로 아무런 문제가 없었을 것이다. 하지만 다른 사람과 같이 협업을 진행하면서 Github 원격 저장소를
이용하다 보면 `같은 팀원이 같은 레포지토리에 커밋을 하는 경우가 있을 것이다.` 

<br>

내가 로컬에서 작업하고 있을 때, 같은 팀원이 원격 저장소에 먼저 변경 사항을 커밋하고 push하는 상황은 존재할 수 밖에 없다. 
`이런 경우 Git은 push를 허용하지 않는다.` 

<br>

## 1. git fetch

만약 같은 팀원이 원격 저장소에 푸시를 했다면 나의 로컬 저장소에는 최신의 상태가 반영이 되어 있지 않기 때문에 원격 저장소와 같게 맞춰야한다.
`이럴 때 하는 것이 페치(fetch)이다.` 

<br>

`페치(fetch)`는 원격 저장소의 커밋들을 로컬 저장소로 가져온다. 

<br>

# 2. git pull

`git pull`은 원격 저장소의 정보를 가져오면 자동으로 로컬 브랜치에 `병합`까지 수행해준다. 

<br>

하지만 pull을 이용해서 `fetch`와 `Merge`를 동시에 수행하면 단점이 있다. 어떤 내용이 병합되면서 바뀌게 되는지 알 수 없기 때문이다. 
그래서 보통 pull을 이용해서 원격 저장소의 커밋을 가져오는 것을 추천하지 않는다고 한다. `그 대신 fetch를 이용해서 원격 저장소의 커밋을 가져오고,
로컬 저장소에서 이를 확인한 다음 수동으로 병합하는 방법을 추천한다.` 

<br>

하지만 나는 `pull`이 편하기 때문에 여기서는 pull을 이용해서 실습을 진행하려 한다.

<br>

# 실습

먼저 실습하고자 하는 Github 레포지토리를 들어가보자. 

<img width="1792" alt="스크린샷 2020-11-03 오후 11 27 08" src="https://user-images.githubusercontent.com/45676906/97997279-26022080-1e2c-11eb-9cce-ee963466b82b.png">

위의 나의 저장소는 브랜치를 만들어 관리하는 것이 아니라 master에서 commit만 해왔기 때문에 위와 같은 상태이다. 

<br>

여기서 다른 사람이 나의 저장소에 커밋을 할 수 없기 때문에 내가 임의로 Github Repository에 README.md을 수정하고 커밋을 해보자.  

<img width="1792" alt="스크린샷 2020-11-03 오후 11 25 47" src="https://user-images.githubusercontent.com/45676906/97997520-61045400-1e2c-11eb-9f74-044f42744ebc.png">

<br>

위의 연필 버튼을 클릭하자. 

![스크린샷 2020-11-03 오후 11 30 00](https://user-images.githubusercontent.com/45676906/97997655-898c4e00-1e2c-11eb-9cf5-fdfb453af941.png)

간단하게 수정을 하고 커밋을 해보자. 

<br>

<img width="1792" alt="스크린샷 2020-11-03 오후 11 31 09" src="https://user-images.githubusercontent.com/45676906/97997860-c5271800-1e2c-11eb-98f6-a0f241b647d1.png">

그리고 `GitKraken`을 사용해보면 위와 같이 Local 저장소와 remote 저장소가 차이를 생긴 것을 볼 수 있다.

<br>


```
git add .
git status 
git commit -m "커밋 메세지"
git push origin master
```

위와 같이 진행하면 아래와 같이 거절 메세지를 볼 수 있다.

<img width="695" alt="스크린샷 2020-11-03 오후 11 33 54" src="https://user-images.githubusercontent.com/45676906/97998143-1800cf80-1e2d-11eb-95b0-6e6ac496168c.png">

자세히 보면 push를 하기 전에 pull을 하라고 한다. 

<br>

## push하기 전에 pull하라는 이유가 무엇일까?

지금까지 잘 따라왔다면 바로 알 수 있을 것이다. Github 레포지토리의 README.md를 수정한 내용이 나의 로컬 저장소에 최신화 되어 있지 않기 때문에
pull을 통해 remote Repository의 커밋을 가져와 나의 로컬 저장소와 합친 후에 push를 해야한다.

<br>

```
git pull origin master
```

위의 명령어를 입력하면 아래와 같이 Merge를 하는 화면을 볼 수 있다.


<img width="692" alt="스크린샷 2020-11-03 오후 11 36 51" src="https://user-images.githubusercontent.com/45676906/97998491-82197480-1e2d-11eb-931d-d04d15464e7c.png">

그러면 아래와 같은 결과를 볼 수 있다. 

<img width="686" alt="스크린샷 2020-11-03 오후 11 37 57" src="https://user-images.githubusercontent.com/45676906/97998588-a5dcba80-1e2d-11eb-83e3-5b4a26aca7f3.png">

+는 추가 -는 삭제가 된 것을 뜻한다. 

<br>

그리고 `git push origin master`를 진행하면 remote 레포지토리에 push를 할 수 있다. 

<img width="1792" alt="스크린샷 2020-11-03 오후 11 39 40" src="https://user-images.githubusercontent.com/45676906/97998810-e9cfbf80-1e2d-11eb-8821-127a35ec5d04.png">

그리고 `GitKraken`을 확인하면 위와 같이 잘 Merge가 된 것도 확인할 수 있다. 