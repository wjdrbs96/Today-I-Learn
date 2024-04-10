package ExampleCode;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Test
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 03. 26.
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("start");
        for (int i = 0; i < 50; ++i) {
            CompletableFuture.supplyAsync(
                    () -> {
                        System.out.println("Thread: " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                        }
                        return null;
                    },
                    SELECT_THREAD_POOL
            );
        }

        System.out.println("end");
    }

    public static final ThreadPoolExecutor SELECT_THREAD_POOL = new ThreadPoolExecutor(
            20, 20,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
}
