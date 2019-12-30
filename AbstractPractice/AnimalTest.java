package AbstractPractice;

public class AnimalTest {
    public static void main(String[] args){
        Animal ani = new dog();
        Animal ami = new Cat();

        ani.eat();
        ami.eat();

        AnimalTest test = new AnimalTest();
        test.AnimalEat(ani);
        test.AnimalEat(ami);
    }
    public void AnimalEat(Animal a){
        a.eat();
    }

}
