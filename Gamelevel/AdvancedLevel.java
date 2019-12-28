package Gamelevel;

public class AdvancedLevel extends PlayerLevel{

    public void run(){
        System.out.println("조금 빨리 달립니다");
    }

    public void jump(){
        System.out.println("점프 조금 가능합니다");
    }

    public void turn(){
        System.out.println("턴 조금 됩니다");
    }

    public void showLevelMessage(){
        System.out.println("========중급자 입니다========");
    }
}
