# `2장: JPA 시작`

프로젝트 세팅과 H2 Database를 설치를 했다고 가정하고 글을 시작해보겠습니다. 

<br>

## `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>jpa-basic</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>
        <!-- H2 데이터베이스 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.199</version>
        </dependency>

        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
    </dependencies>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>
</project>
```

![스크린샷 2021-08-24 오후 2 25 19](https://user-images.githubusercontent.com/45676906/130560597-e05841b2-3f2e-481d-a89d-b6c1e8c8fe38.png)

위와 같이 `h2 database`, `hibernate`, `javax.xml.bind` 의존성 3개를 추가해주겠습니다. 

<br> <br>

## `JPA 설정하기 - persistence.xml`

![스크린샷 2021-08-24 오후 2 27 08](https://user-images.githubusercontent.com/45676906/130560755-24b8d2a2-e6fb-4a4f-a5d7-1b7794dde7c8.png)

표준 위치가 정해져 있어서 위와 같이 위치를 잘 맞춰서 파일을 만들어야 합니다.

<br>

```xml
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>  
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>    <!-- 데이터베이스 방언 -->
            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <property name="hibernate.jdbc.batch_size" value="10"/>
            <property name="hibernate.hbm2ddl.auto" value="create" />
        </properties>
    </persistence-unit>
</persistence>
```

<br> <br>

## `데이터베이스 방언`

<img width="1093" alt="스크린샷 2021-08-24 오후 2 31 30" src="https://user-images.githubusercontent.com/45676906/130561157-921cfbc4-a751-401e-9195-7c1acc4561ae.png">

데이터베이스가 변경되어도 애플리케이션 코드를 변경할 필요 없이 데이터베이스 방언만 교체하면 됩니다. 각 DB 마다 약간씩 SQL 문법과 함수가 차이가 있는데 이처럼 SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능을 JPA에서는 방언이라고 합니다. 

<br> <br>

## `JPA 구동 방식`

<img width="553" alt="스크린샷 2021-08-24 오후 2 33 46" src="https://user-images.githubusercontent.com/45676906/130561404-a884dac8-c921-4b6d-959e-7d4fb1813982.png">

위에서 설정한 `persistence.xml`을 읽어서 `EntityManageFactory`를 생성합니다. 바로 실습을 해보겠습니다. 

<br>

```java
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {
    
    @Id
    private Long id;
    private String name;
    
    // Getter, Setter 존재한다고 가정
}
```

```java
public class JpaMain {

    public static void main(String[]  args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        
        tx.begin();

        Member member = new Member();
        member.setId(1L);
        member.setName("Gyunny");
        em.persist(member);
        em.close();

        emf.close();

    }
}
```

위와 같이 코드를 작성하고 실행하면 제대로 작동하지 않습니다. 왜냐하면 `트랜잭션 처리를 하지 않았기 때문!!`

<br>

```java
public class JpaMain {

    public static void main(String[]  args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml 에서 작성한 이름
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setId(1L);
            member.setName("Gyunny");
            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
```

그래서 이번에는 트랜잭션 처리를 해준 후에 데이터를 저장을 했는데 아래와 같이 잘 저장이 되는 것을 볼 수 있습니다.

<br>

<img width="199" alt="스크린샷 2021-08-24 오후 2 44 08" src="https://user-images.githubusercontent.com/45676906/130562534-dd7a4d09-70d8-4858-90fa-6172fe5ebffd.png">

<br> <br>

## `주의사항`

- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유해서 사용하기
- 엔티티 매니저는 쓰레드간에 공유 X
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 함!

