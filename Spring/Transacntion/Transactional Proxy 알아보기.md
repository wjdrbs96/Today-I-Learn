## `@Transactional Proxy 알아보기`

```java
public void register(Member member) {
    try {
      tx.begin();
      memberRepository.save(member);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    }
}
```

```java
@Transactional
public void register(Member member) {
    memberRepository.save(member);
}
```

```java
public class MemberServiceProxy {
  private final MemberService memberService;
  private final TransactonManager manager = TransactionManager.getInstance();

  public MemberServiceProxy(MemberService memberService) {
    this.memberService = memberService;
  }

  public void register(Member member) {
    try {
      manager.begin();
      memberService.register(member);
      manager.commit();
    } catch (Exception e) {
      manager.rollback();
    }
  }
}
```