package ExampleCode;

/**
 * created by Gyunny 2021/11/23
 */
public class MusicBox {
    public synchronized void playMusicA( ) {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicA !!");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playMusicB() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicB !!");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
