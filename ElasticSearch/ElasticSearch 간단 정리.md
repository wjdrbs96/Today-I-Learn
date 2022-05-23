## `ElasticSearch 간단하게 정리하기`

이번 글은 아래 내용은 [여기](https://esbook.kimjmin.net/)에 대해서 정리한 글입니다.

<br>

## `ElasticSearch를 사용한 이유는 무엇일까?`

보통 저는 사이드 프로젝트에서 검색을 구현해야 할 때는 `MySQL Like`를 사용해서 구현했습니다. 하지만 `MySQL Like %title%` 검색은 인덱스를 이용하지 못하기 때문에 데이터의 양이 많다면 DB 서버에 부하를 주는 작업일 수 있습니다.

그렇기에 `MySQL Like`로 검색을 구현하기에는 한계가 있다고 생각합니다. 그런데 만약에 검색해야 하는 데이터 종류가 한개가 아니라 여러 개라면 어떻게 할 수 있을까요?

```sql
SELECT *
FROM velad v
WHERE v.serviceType Like '%test%' AND v.serviceLocale Like '%test%' ...
```

위와 같이 검색의 컬럼 마다 `Like` 검색을 사용하게 될 것입니다. 하나만 Like를 사용해도 DB 서버에 부담이 될 수 있는데, 검색해야 할 종류가 여러가지라면 더욱 더 사용하기 힘들 것입니다.

<br>

```sql
SELECT *
FROM velad v
WHERE v.serviceType Like 'test%' AND v.serviceLocale Like 'test%' ...
```

그러면 MySQL 인덱스를 이용할 수 있게 %를 뒤에만 넣으면 어떠냐 할 수 있는데요. 이렇게 했을 때는 검색어에 맞는 검색 데이터가 잘 나오지 않을 수 있고 위에서 말한 것과 마찬가지로 검색해야 하는 조건이 여러 개라면 사용하기 쉽지 않습니다.

즉, MySQL Like를 사용해서 간단하게 검색을 구현하는 것은 불가능할 것 같고,  검색 개발에 많이 사용되는 ElasticSearch를 사용해야겠다는 생각을 했습니다.

<br>

## `ElasticSearch 기본 개념과 용어 정리`

- `색인(indexing)`: 데이터가 검색될 수 있는 구조로 변경하기 위해 원본 문서를 검색어 토큰들로 변환하여 저장하는 일련의 과정
- `인덱스 (index, indices)`: 색인 과정을 거친 결과물, 또는 색인 데이터가 저장되는 저장소
- `검색(search)`: 인덱스에 들어있는 검색어 토큰들을 포함하고 있는 문서를 찾아가는 과정
- `질의(query)`: 사용자가 원하는 문서를 찾거나 집계 결과를 출력하기 위해 검색 시 입력하는 검색어 또는 검색 조건

<img width="781" alt="스크린샷 2022-05-23 오후 10 57 11" src="https://user-images.githubusercontent.com/45676906/169835873-16040e37-e454-4f3d-be8f-d4456d2941e4.png">

- [https://esbook.kimjmin.net/02-install/2.1](https://esbook.kimjmin.net/02-install/2.1)

<br>

### `인덱스와 샤드 - Index & Shards`

`ElasticSearch`에서 단일 데이터 단위를 `도큐먼트(Document)` 라고 하며 이 도큐먼트를 모아놓은 집합을 `인덱스(Index)` 라고 합니다.

데이터를 Elasticsearch에 저장하는 행위는 `색인`, 그리고 도큐먼트의 집합 단위는 `인덱스` 라고 할 수 있습니다.

> 원본 Document를 색인의 과정을 거친 후의 Document를 Elastic Search Index 테이블에 저장한다.

여기서 색인할 때 `두 개의 정보를 가지고 있는데 그것이 바로 setting, mappings` 입니다. 각각 어떤 특징이 있는지 알아보겠습니다.

- [https://esbook.kimjmin.net/03-cluster/3.2-index-and-shards](https://esbook.kimjmin.net/03-cluster/3.2-index-and-shards)

<br>

### `settings란?`

`settings`에는 여러가지 설정을 할 수 있지만 대표적으로 `analyzer`, `tokenizer`, `filter`로 구성할 수 있습니다.

<img width="810" alt="123123" src="https://user-images.githubusercontent.com/45676906/169836098-4bca1741-59a2-47be-8b42-2ef3c5ede37a.png">

즉, `settings`에 넣을 수 있는 값은 3가지가 존재하는데 각각 어떤 것들이 있는지 좀 더 자세히 알아보겠습니다.

- [https://esbook.kimjmin.net/07-settings-and-mappings/7.1-settings](https://esbook.kimjmin.net/07-settings-and-mappings/7.1-settings)

<br> 

### `토크나이저 - Tokenizer`

`Tokenizer`에는 크게 `Standard`, `Letter`, `Whitespace` 3가지 토크나이저가 존재합니다.

- `Standard`: 공백으로 텀을 구분하면서 "@"과 같은 일부 특수문자를 제거합니다. "jumped!"의 느낌표, "meters."의 마침표 처럼 단어 끝에 있는 특수문자는 제거되지만 "quick.brown_FOx" 또는 "3.5" 처럼 중간에 있는 마침표나 밑줄 등은 제거되거나 분리되지 않습니다.
- `Letter`: 알파벳을 제외한 모든 공백, 숫자, 기호들을 기준으로 `Term`을 분리합니다. "quick.brown_FOx" 같은 단어도 "quick", "brown", "FOx" 처럼 모두 분리됩니다.
- `Whitespace`: 스페이스, 탭, 그리고 줄바꿈 같은 공백만을 기준으로 `Term`을 분리합니다. 특수문자 "@" 그리고 "meters." 의 마지막에 있는 마침표도 사라지지 않고 그대로 남아있습니다.

> 3개의 토크나이저 중에 Letter 토크나이저의 경우 검색 범위가 넓어져서 원하지 않는 결과가 많이 나올 수 있고, 반대로 Whitespace의 경우 특수문자를 거르지 않기 때문에 정확하게 검색을 하지 않으면 검색 결과가 나오지 않을 수 있습니다. 따라서 `보통은 Standard 토크나이저를 많이 사용합니다.`

- [https://esbook.kimjmin.net/06-text-analysis/6.5-tokenizer/6.5.1-standard-letter-whitespace](https://esbook.kimjmin.net/06-text-analysis/6.5-tokenizer/6.5.1-standard-letter-whitespace)

<br>

## `토큰 필터 - Token Filter`

### `Lowercase Filter`

영어나 유럽어 기반의 텍스트는 대소문자가 있어 검색할 때는 대소문자에 상관 없이검색이 가능하도록 처리 해 주어야 합니다. `보통은 텀 들을 모두 소문자로 변경하여 저장하는데 이 역할을 하는 것이 Lowercase 토큰 필터`입니다. Lowercase 토큰 필터는 거의 모든 텍스트 검색 사례에서 사용되는 토큰 필터입니다.

- [https://esbook.kimjmin.net/06-text-analysis/6.6-token-filter/6.6.1-lowercase-uppercase](https://esbook.kimjmin.net/06-text-analysis/6.6-token-filter/6.6.1-lowercase-uppercase)

<br>

### `mappings란?`

<img width="795" alt="123123123" src="https://user-images.githubusercontent.com/45676906/169837462-b1eedd81-7a22-4087-b821-a7634c897f73.png">

ElasticSearch에 `mappings`을 정의한대로 `인덱스` 필드들이 생긴다고 이해를 하면 됩니다. 즉, 제가 `title`, `content`, `createdAt`으로 검색한다면 `Property`에는 3개가 들어갈 것입니다.

- [https://esbook.kimjmin.net/07-settings-and-mappings/7.2-mappings](https://esbook.kimjmin.net/07-settings-and-mappings/7.2-mappings)

<br>

## `Bool 쿼리란?`

> 본문 검색에서 여러 쿼리를 조합하기 위해서는 상위에 bool 쿼리를 사용하고 그 안에 다른 쿼리들을 넣는 식으로 사용이 가능합니다. bool 쿼리는 다음의 4개의 인자를 가지고 있으며 그 인자 안에 다른 쿼리들을 배열로 넣는 방식으로 동작합니다.

- `must` : 쿼리가 참인 도큐먼트들을 검색합니다.
- `must_not` : 쿼리가 거짓인 도큐먼트들을 검색합니다.
- `should` : 검색 결과 중 이 쿼리에 해당하는 도큐먼트의 점수를 높입니다.
- `filter` : 쿼리가 참인 도큐먼트를 검색하지만 스코어를 계산하지 않습니다. must 보다 검색 속도가 빠르고 캐싱이 가능합니다.

- [https://esbook.kimjmin.net/05-search/5.2-bool](https://esbook.kimjmin.net/05-search/5.2-bool)

즉, `Bool 쿼리`는 색인을 통해서 ElasticSearch에 저장되어 있는 문서들을 검색하기 위한 문법입니다.

<br>

### `Bool 쿼리로 검색`

위의 보면 검색 조건으로 들어온 값을 Bool 쿼리를 통해서 ElasticSearch의 문서들을 조회하고 있는 것을 볼 수 있습니다.

보면 `Term`, `Filter`, `Must`, `Term` 등등 보이고 있는데요. 위에서 정리하지 않았던 것은 [Term 쿼리](https://esbook.kimjmin.net/06-text-analysis/6.3-analyzer-1/6.3.1-term)인데 `Term 쿼리는 일치하는 것만 결과로 반환해주는 것`입니다.

- [https://esbook.kimjmin.net/06-text-analysis/6.3-analyzer-1/6.3.1-term](https://esbook.kimjmin.net/06-text-analysis/6.3-analyzer-1/6.3.1-term)

<br>

### `풀 색인(Full indexing), 증분 색인(Incremental indexing)은 무엇일까?`

처음에는 `풀 색인`, `증분 색인` 용어가 어떤 것을 말하는지 잘 몰랐는데요.

- `풀 색인` : 모든 데이터를 다 색인하여 인덱스 테이블에 저장하는 것
- `증분 색인` : 전체 색인 완료 이후부터 추가된 데이터를 색인하여 인덱스 테이블에 저장하는 것

간략하게 저만의 용어로 요약하면 위와 같이 표현할 수 있습니다.

<br>

<img width="615" alt="스크린샷 2022-05-23 오후 10 59 58" src="https://user-images.githubusercontent.com/45676906/169836481-50ea80ee-7202-41a2-b776-e136576e3778.png">

MySQL 이라는 RDBMS 저장소가 존재하고 ElasticSearch에도 데이터를 저장할 수 있는 `인덱스`가 존재합니다.

즉, 클라이언트가 어떤 문서를 생성하여 MySQL에 저장했다면 MySQL에 저장된 데이터를 ElasticSearch에 싱크를 맞추는 작업이 필요합니다. 그 싱크를 맞추는 작업을 `풀 색인`, `증분 색인` 이라고 합니다.

다시 위의 정의에 맞춰서 확인해보면 ElasticSearch에 데이터가 하나도 존재하지 않아서 MySQL에 존재하는 모든 데이터를 ElasticSearch에 싱크 맞추는 작업을 한다면 `풀 색인`이 될 것이고, 풀 색인 이후에 MySQL에 추가되는 데이터를 ElasticSearch에 싱크를 맞추고 싶다면 `증분 색인`이 될 것입니다.

위에서 계속 보았듯이 `색인` 이라는 과정을 거쳐서 ElasticSearch 인덱스에 데이터를 저장하여 검색이 용이한 구조로 만드는 것입니다.

<br>

## `Reference`

- [https://esbook.kimjmin.net/](https://esbook.kimjmin.net/)