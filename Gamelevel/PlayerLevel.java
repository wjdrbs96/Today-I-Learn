package Gamelevel;

public abstract class PlayerLevel {

    public abstract void run();
    public abstract void jump();
    public abstract void turn();
    public abstract void showLevelMessage();

    final public void go(int count){  //템플릿 메소드
        for(int i=0; i < count; i++){
            jump();
        }
        turn();
    }
}
