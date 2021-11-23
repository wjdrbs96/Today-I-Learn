package ExampleCode;

/**
 * created by Gyunny 2021/11/23
 */
public class MusicPlayer extends Thread {
    int type;
    MusicBox musicBox;

    public MusicPlayer(int type, MusicBox musicBox) {
        this.type = type;
        this.musicBox = musicBox;
    }

    @Override
    public void run() {
        switch (type) {
            case 1:
                musicBox.playMusicA();
                break;
            case 2:
                musicBox.playMusicB();
                break;
        }
    }
}
