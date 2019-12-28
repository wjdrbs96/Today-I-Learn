package Gamelevel;

public class Player {
    private PlayerLevel level;

    public Player(){
        level = new BeginnerLevel();  // 이 구절에 의해서 MainbordPlay 에 있는 player 참조 변수랑 level 이랑
        // 어떻게 연결이 되길래 BeginnerLevel() 로 연결이 되는거지?
        level.showLevelMessage();
    }

    public PlayerLevel getLevel(){
        return level;
    }

    public void upgradeLevel(PlayerLevel level){
        this.level = level;
        level.showLevelMessage();
    }

    public void play(int count){
        level.go(count);
    }


}
