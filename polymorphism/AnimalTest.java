package polymorphism;

import java.util.ArrayList;

class Animal{
    public void move(){
        System.out.println("동물이 움직입니다");
    }
}

class human extends Animal{
    public void move(){
        System.out.println("사람은 두발로 걷습니다");
    }
}

class Tiger extends Animal{
    public void move(){
        System.out.println("호랑이가 네발로 뜁니다");
    }
}

class Eagle extends Animal{
    public void move(){
        System.out.println("독수리가 하늘을 날아갑니다");
    }
}
public class AnimalTest {
    public static void main(String []args){
        Animal hu = new human(); // 업 캐스팅
        Animal ti = new Tiger(); // 업 캐스팅
        Animal Ea = new Eagle(); // 업 캐스팅

        AnimalTest test = new AnimalTest();
        test.moveAnimal(hu);
        test.moveAnimal(ti);
        test.moveAnimal(Ea);
        //이렇게 호출을 하면 각각에 맞게 메소드가 호출이 되는데 이것을 다형성 이라고 함
        System.out.println("=====================================");
        //ArrayList를 이용해보자

        ArrayList<Animal> animalList = new ArrayList<Animal>();  // <> 안에 Animal 도 가능함
        animalList.add(hu); animalList.add(ti); animalList.add(Ea);
        for(Animal animal : animalList){
            animal.move();
        }
    }

    public void moveAnimal(Animal animal){  //매게변수의 자료형이 Animal 인 것을 확인하자
        animal.move(); //어떤 Animal 이 올지 모르기 때문에 자료형을 Animal로 해줘야 함
    }

}
