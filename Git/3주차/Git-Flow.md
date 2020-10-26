# Git Flow란?

`Git Flow`는 Git 브랜치를 관리하는 전략 중 하나이다. Git을 사용하여 협업을 할 때, 각자의 코드를 합치는 과정인 머지(merge)과정에서 
많은 충돌이 일어나는 것을 경험해봤을 것이다. 이러한 충돌을 최대한 피하고자 다양한 전략을 사용하는데, 그 중 한 전략이 `Git Flow`이다.

<br>

## Git-Flow 전략 간단하게 살펴보기

Git-Flow에는 5가지 종류의 브랜치가 존재한다. 항상 유지되는 `메인 브랜치들(master, develop)`과 일정 기간 동안만 유지되는 `보조 브랜치들(feature, release, hotfix)`가 있다.

- `master` : 제품으로 출시될 수 있는 브랜치 (실제 배포가 될 브랜치)
- `develop` : 디음 출시 버전을 개발하는 브랜치
- `feature` : 기능을 개발하는 브랜치 (feature 브랜치는 항상 develop에 머지를 한다.)
- `release` : 이번 출시 버전을 준비하는 브랜치
- `hotfix` : 출시 버전에서 발생한 버그를 수정 하는 브랜치

<br>

![image](https://user-images.githubusercontent.com/45676906/97078753-35fa5300-1629-11eb-9e84-910885e02bba.png)

처음에는 master와 develop 브랜치가 존재한다. develop 브랜치도 master에서 시작된 브랜치이다. develop 브랜치에는 상시로 버그를 수정한 커밋들이 추가된다. 
그리고 `feature 브랜치는 무조건 develop 브랜치에서 시작하게 된다.` 기능 추가 작업이 완료되었다면 feature 브랜치는 develop 브랜치로 merge 된다. (그리고 바로 feature 브랜치는 삭제한다.)
이유는 `한번 머지한 기능 브랜치는 재사용하지 않기`위해서 이다.
<br>

develop에 이번 버전에 포함되는 모든 기능이 merge 되었다면 QA를 하기 위해 develop 브랜치에서부터 relase 브랜치를 생성한다. 
QA를 진행하면서 발생한 버그들은 release 브랜치에 수정된다. QA를 무사히 통과했다면 release 브랜치를 master와 develop 브랜치로 merge 한다.
마지막으로 출시된 master 브랜치에서 버전 태그를 추가한다.


<br>

## 커밋을 남기는 단위

- 커밋 하나는 하나의 작업만 하는 것이 좋다.
- 작업에 필요한 파일들 이외의 파일을 건드리는 것은 좋지 않다.
- 브랜치의 역할에 맡는 커밋을 해야한다. 