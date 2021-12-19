## `Spring Security로 인증, 인가 개발하는 법`

- cors().and() : 이 구문은 안에 들어가보면 주석으로 내용이 나와있지만 CorsFilter라는 필터가 존재하는데 이를 활성화 시키는 작업입니다.

- csrf().disable() : 세션을 사용하지 않고 JWT 토큰을 활용하여 진행하고 REST API를 만드는 작업이기때문에 csrf 사용은 disable 처리합니다.

- sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) : 현재 스프링 시큐리티에서 세션을 관리하지 않겠다는 뜻입니다. 서버에서 관리되는 세션 없이 클라이언트에서 요청하는 헤더에 token을 담아보낸다면 서버에서 토큰을 확인하여 인증하는 방식을 사용할 것이므로 서버에서 관리되어야할 세션이 필요없어지게 됩니다.

- authorizeRequests() : 이제부터 인증절차에 대한 설정을 진행하겠다는 것입니다.

- antMatchers() : 특정 URL 에 대해서 어떻게 인증처리를 할지 결정합니다.

- permitAll() : 스프링 시큐리티에서 인증이 되지 않더라도 통과시켜 누구에게나 사용을 열어줍니다.

- authenticated() : 요청내에 스프링 시큐리티 컨텍스트 내에서 인증이 완료되어야 api를 사용할 수 있습니다. 인증이 되지 않은 요청은 403(Forbidden)이 내려집니다.