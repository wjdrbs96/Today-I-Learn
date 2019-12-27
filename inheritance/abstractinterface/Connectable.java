package inheritance.abstractinterface;

public interface Connectable {
    public static final String name = "연결방법: USB";

    public abstract void connect(); //추상메소드
    //interface 는 public static, public abstract 를 생략해도 있다고 간주함
    //인터페이스 안에는 일반필드와 일반 메소드는 사용 불가능
}
