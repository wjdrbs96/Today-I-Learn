package ExampleCode;

/**
 * created by Gyunny 2021/11/23
 */
public class MusicExam {
    public static void main(String[] args) {
        MusicBox box = new MusicBox();

        MusicPlayer musicPlayer1 = new MusicPlayer(1, box);
        MusicPlayer musicPlayer2 = new MusicPlayer(2, box);

        musicPlayer1.start();
        musicPlayer2.start();
    }
}
