## `@Builder, @AllArgsConstructor 알아보기`

이번 글에서는 `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor` 애노테이션을 사용할 때 알아야 할 점에 대해서 정리해보겠습니다. 

![스크린샷 2021-11-21 오전 12 12 33](https://user-images.githubusercontent.com/45676906/142731561-6338a678-7d5c-444c-90de-c044d864dc61.png)

위와 같이 `@Builder`, `@NoArgsConstructor`를 사용했을 때 `Lombok`에서 만들어주는 메소드를 알아보겠습니다. 

<br>

![스크린샷 2021-11-21 오전 12 23 19](https://user-images.githubusercontent.com/45676906/142731867-9a6be2d1-7084-44e9-aeb7-228b49f3c4cb.png)

만들어주는 메소드를 보면 `기본 생성자`, `빌더 관련 메소드들`을 만들어주는 것을 볼 수 있습니다. `Builder`가 어떤 것인지 안다면 `전체 생성자`가 필요하다는 것을 알 수 있습니다. (Builder가 궁금하다면 [여기](https://devlog-wjdrbs96.tistory.com/258?category=925183) 에서 확인하실 수 있습니다.)

`Builder`란 생성자의 매개변수가 많을 때 좀 더 편리하게 객체를 만들 수 있게 도와주는 것이고, `Builder` 패턴이 어떻게 구현되어 있는지 보면 `전체 생성자`가 필요하다는 것을 알 수 있습니다. 

<br>

![스크린샷 2021-11-21 오전 12 14 20](https://user-images.githubusercontent.com/45676906/142731599-a1221e3b-4ad6-434a-8b42-9916f5577399.png)

그래서 위의 `Class`처럼 `@Builder`, `@NoArgsConstructor` 어노테이션만 사용한 채로 실행하면 위와 같은 에러가 발생합니다. 왜냐하면 `전체 생성자`가 없기 때문입니다. 잘 생각해보면 당연한 것입니다. `Builder`를 통해서 필요한 매개변수만 조립해서 객체를 만들 수도 있고 다 조립해서 만들 수도 있는데 전체 생성자가 없다는 것은 말이 안되기 때문입니다.  

<br>

![스크린샷 2021-11-21 오전 12 30 41](https://user-images.githubusercontent.com/45676906/142732056-fc291127-77a6-4963-b132-2a48d76a62b6.png)

그런데 위처럼 `@NoArgsConstructor`를 없애고 `Builder` 어노테이션만 존재하면 에러가 발생하지 않습니다. 그래서 이번에 `@NoArgsConstructor` 없이 `Builder` 어노테이션만 있을 때 어떤 메소드를 생성해주는지 알아보겠습니다.    

<br>

![스크린샷 2021-11-21 오전 12 32 27](https://user-images.githubusercontent.com/45676906/142732099-60b077bd-047f-4ed1-916d-3fd16525f5dc.png)

이번에는 아까와는 다르게 `전체 생성자`가 생성되어 있는 것을 볼 수 있는데요. `@NoArgsConstructor`인 기본 생성자가 없다고 전체 생성자가 생기는 것이 궁금해서 `@Builder` 어노테이션 내부 설명을 읽어보았습니다. 

<br>

![스크린샷 2021-11-21 오전 12 20 35](https://user-images.githubusercontent.com/45676906/142732145-98d6c95e-ebf0-43ae-8acc-afdbab4df816.png)

> @AllArgsConstructor(access = AccessLevel.PACKAGE)처럼 모든 필드를 인수로 사용하여 Package-Private 생성자가 생성됩니다. 명시적 @XArgsConstrator 주석을 추가하지 않은 경우에만 생성됩니다.

<br>

즉, `@NoArgsConstrator` 어노테이션이나 다른 생성자가 존재하지 않을 때 `전체 생성자`를 자동으로 만들어주는 것입니다. 기본 생성자 또는 다른 생성자가 존재한다면 직접 `전체 생성자`를 만들어주어야 합니다.

<br>

![스크린샷 2021-11-21 오전 12 39 51](https://user-images.githubusercontent.com/45676906/142732329-55bb1fd4-bc82-4d7d-9c55-bdc2d1db407f.png)

기본 생성자가 필요한 상태에서 `@Builder`를 사용하고 싶다면 위와 같이 `기본 생성자`, `전체 생성자`, `빌더` 어노테이션을 사용하면 에러 없이 사용할 수 있습니다. 