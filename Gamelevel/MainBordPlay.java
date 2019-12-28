package Gamelevel;

public class MainBordPlay {
    public static void main(String []args){
        Player player = new Player();
        player.play(1);

        AdvancedLevel aLevel = new AdvancedLevel();
        player.upgradeLevel(aLevel);  // 레벨 업
        player.play(2);

        SuperLevel sLevel = new SuperLevel();
        player.upgradeLevel(sLevel);
        player.play(3);

    }
}
