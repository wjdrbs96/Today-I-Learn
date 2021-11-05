# Sequelize 에서 JOIN 한 후에 데이터 구조를 간단히 만들기

기본적으로 프로젝트 세팅과 시퀄라이즈 세팅은 다 했다고 가정하고 정리해보겠습니다.  


## `models/index.js`

![1](https://cdn.inflearn.com/public/files/posts/8be4db85-7622-4f41-ad43-32a57a588145/1.png)

현재 `keyword` 테이블과 `mindmap` 테이블을 정의하였고, 두 테이블의 관계를 `다대다(Many-To-Many)`로 설정하였습니다. 다대다 관계이기 때문에 중간에
매핑 테이블이 필요한데 이것은 `through`를 통해서 `mindmap_keyword` 라는 테이블로 정의하였습니다. 

그리고 실행해서 만들어진 `ERD(Entity Relation Diagram)`은 아래와 같습니다. 

<img width="580" alt="스크린샷 2020-12-05 오후 10 17 45" src="https://user-images.githubusercontent.com/45676906/101244087-b4c5ce00-3747-11eb-9dfc-1f1c0c5e942b.png">

그리고 아래의 뷰를 보면서 API를 하나 만들어보겠습니다. 

<img width="360" alt="2nd_preview_popup_android" src="https://user-images.githubusercontent.com/45676906/101244142-079f8580-3748-11eb-8f47-7d66fcd337d8.png">

위의 화면에 대한 API를 만들 때 빨간 네모 안에 있는 데이터를 클라이언트에게 Response Data로 주려고 합니다. 

제가 만든 `ERD` 구조에서는 테이블을 JOIN 해야 위의 데이터들을 보내줄 수 있습니다. (이유는 키워드 개수 때문입니다.)

<br>

## `Sequelize 다대다 관계에서 JOIN 사용하기`

```javascript
db.keyword.belongsToMany(db.mindmap, {through : "mindmap_keyword", as : "hasMindmap"});
db.mindmap.belongsToMany(db.keyword, {through : "mindmap_keyword", as : "hasKeyword"});
```

위에서 `models/index.js`에서 위와 같이 다대다 관계를 설정했습니다. 여기서 볼 것은 `as`를 통해서 별칭을 준 것입니다. 
`hasMindmap`의 별칭은 `mindmap` - `mindmap_keyword` 테이블의 관계에서 `mindmap` 테이블을 `hasMindmap`의 별칭의 이름으로 사용하겠다는 뜻입니다.

그리고 `keyword` - `mindmap_keyword` 테이블의 관계에서는 `keyword` 테이블의 이름을 `hasKeyword` 라는 별칭으로 사용하겠다는 뜻입니다. 

<br>

그리고 이제 코드 로직을 아래와 같이 작성했습니다. 

![code](https://cdn.inflearn.com/public/files/posts/abe017b4-8696-4fc7-a5ec-0758f81e4691/2.png)

그리고 나서 포스트맨으로 테스트 해보겠습니다. 

![postman1](https://cdn.inflearn.com/public/files/posts/668ffee5-ba6a-4784-ac8f-71c99fa0f8c9/3.png)

그러면 위와 같이 결과는 제대로 나왔습니다. `하지만 뭔가 데이터 구조가 맘에 들지 않습니다.` 좀 더 단순하게 응답을 주고 싶습니다.

<br>

![code2](https://cdn.inflearn.com/public/files/posts/3ec54781-4939-4e55-8d0f-b6a860902f71/4.png)

그래서 위와 같이 `raw: true`라는 옵션을 주었습니다. 결과는 어떻게 나올까요?

![postman2](https://cdn.inflearn.com/public/files/posts/2887b6f0-add0-4f2a-92d2-1eacc75e7d97/5.png)

위와 같이 아까보다는 깔끔해지기는 한 것 같습니다. 하지만 `keywordCount`만 나왔으면 좋겠는데 `hasKeyword.keywordCount`와 같이 앞에 별칭이 붙은 것이 맘에 들지 않습니다,,

`keywordCount`만 나오게 하고 싶다면 어떻게 해야할까요? 우선 먼저 위의 코드를 실행했을 때 시퀄라이즈가 만들어주는 쿼리를 분석해보겠습니다.

```sql
SELECT `mindmap`.`id`, `mindmap`.`title`, `mindmap`.`start_date`, `mindmap`.`end_date`, `mindmap`.`contents`, `hasKeyword`.`id` AS `hasKeyword.id`, COUNT('keywordId') AS `hasKeyword.keywordCount` 
FROM `mindmaps` AS `mindmap` LEFT OUTER JOIN 
( `mindmap_keywords` AS `hasKeyword->mindmap_keyword` INNER JOIN `keyword` AS `hasKeyword` ON `hasKeyword`.`id` = `hasKeyword->mindmap_keyword`.`keywordId`) 
ON `mindmap`.`id` = `hasKeyword->mindmap_keyword`.`mindmapId` WHERE `mindmap`.`id` = '3';
```

쿼리는 위와 같습니다. 보기에는 상당히 복잡해보입니다. 쿼리 구조를 하나씩 분해해서 하나씩 알아보겠습니다. 

```
( `mindmap_keywords` AS `hasKeyword->mindmap_keyword` INNER JOIN `keyword` AS `hasKeyword` ON `hasKeyword`.`id` = `hasKeyword->mindmap_keyword`.`keywordId`)
```

먼저 위의 쿼리를 보면 `mindmap_keywords` 테이블과 `keyword` 테이블을 `INNER JOIN` 하고 있습니다. 위의 쿼리의 결과를 result 라고 잠시 부르겠습니다.

그리고 이번에는 좀 더 큰 구조의 쿼리를 보겠습니다. 

```
FROM `mindmaps` AS `mindmap` LEFT OUTER JOIN 
( `mindmap_keywords` AS `hasKeyword->mindmap_keyword` INNER JOIN `keyword` AS `hasKeyword` ON `hasKeyword`.`id` = `hasKeyword->mindmap_keyword`.`keywordId`) 
ON `mindmap`.`id` = `hasKeyword->mindmap_keyword`.`mindmapId` WHERE `mindmap`.`id` = '3';
```

`mindmaps` 테이블과 result(위에서 말한 이름)이 `LEFT OUTER JOIN`을 하고 있는 것을 볼 수 있습니다. 뭔가 많이 복잡해 보입니다,, 

<br>

`정리하자면 A 테이블 - B 테이블이 다대다 관계이고, 중간에 매핑테이블 C가 존재할 때 (A LEFT OUTER JOIN (B INNER JOIN C))의 형태라고 할 수 있습니다.` 

<br>

데이터 구조를 간단히 만들어 보기 전에 먼저 `raw: true`, `through: {}`를 아래와 같이 제외 시켜보겠습니다. 

![스크린샷 2020-12-06 오전 12 15 03](https://user-images.githubusercontent.com/45676906/101247390-bfd72900-375c-11eb-817b-17db06227933.png)

그러면 위와 같은 코드일 것입니다.

<img width="762" alt="스크린샷 2020-12-06 오전 12 15 54" src="https://user-images.githubusercontent.com/45676906/101247290-2871d600-375c-11eb-9ae5-57adf1baaab3.png">

그리고 포스트맨으로 테스트를 해보겠습니다. 그러면 위와 같이 결과가 나오게 됩니다. 왜 그럴까요? 

위에서 시퀄라이즈에서 만들어주는 쿼리를 보면 알 수 있듯이 `hasKeyword` 별칭 안에는 여러 JOIN이 만들어지기 때문에 위와 같은 구조가 나오게 되는 것입니다. 

그리고 `through: { attribute: [] }`를 통해서 매핑테이블(mindmap_keyword)의 결과를 조정할 수 있습니다. 

<br>

### 따라서 이제 데이터 구조를 간단히 하는 최종적인 정리를 해보려 합니다. 

![스크린샷 2020-12-06 오전 12 59 24](https://user-images.githubusercontent.com/45676906/101247711-64a63600-375e-11eb-9c5c-c834fff618ad.png)

위와 같이 `include` 안에서 `attributes`는 전부 빈 배열로 놓고 `GROUP BY`로 그룹을 지정하고 `count`하는 것은 `include` 밖으로 빼면 됩니다. 

> const sequelize = require('sequelize') 을 이용해서 fn, col을 사용할 수 있습니다.

<img width="727" alt="스크린샷 2020-12-06 오전 1 03 34" src="https://user-images.githubusercontent.com/45676906/101247827-ded6ba80-375e-11eb-969b-f3cdac99cbbe.png">

그러면 위와 같이 데이터 구조가 깔끔해진 것을 볼 수 있습니다. 


