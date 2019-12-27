package inheritance.overriding;

import com.sun.media.jfxmediaimpl.HostUtils;

public class Faculty extends Person {
    public String univ;
    public long number;

    public Faculty(String name, long number, String univ, long idNumber){
        super(name,number);
        this.univ = univ;
        this.number = idNumber;
    }

    public long getSNumber(){
        return super.number;
    }

    public void printInfo(){  //오버라이딩 메소드
        System.out.print("이름: " + super.name + " 주민번호: " +super.number);
        System.out.println(" 대학: " +univ + " 직원번호: " + number);
    }
}
