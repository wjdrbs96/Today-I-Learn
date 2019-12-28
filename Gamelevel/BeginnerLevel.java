package Gamelevel;

public class BeginnerLevel extends PlayerLevel {

    public void run(){
        System.out.println("천천히 달립니다");
    }

    public void jump(){
        System.out.println("점프 못합니다");
    }

    public void turn(){
        System.out.println("turn 못합니다");
    }

    public void showLevelMessage(){
        System.out.println("========초급자 입니다========");
    }
}
