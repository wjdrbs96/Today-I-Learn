package Gamelevel;

public class SuperLevel extends PlayerLevel {

    public void run(){
        System.out.println("많이 빨리 달립니다");
    }

    public void jump(){
        System.out.println("점프 높이 합니다");
    }

    public void turn(){
        System.out.println("turn 잘합니다");
    }

    public void showLevelMessage(){
        System.out.println("========고급자 입니다========");
    }
}
