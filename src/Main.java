import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> aA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> bB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> cC = new ArrayBlockingQueue<>(100);
    public static void main(String[] args) throws InterruptedException {


textGenerator();

        Thread a = new Thread(() -> {
            char letter = 'a';
            int maxA = maxChar(aA,letter );
            System.out.println("Максимальное кол " + letter + "->" + maxA);
        });
        a.start();
        Thread b = new Thread(() -> {
            char letter = 'b';
            int maxB = maxChar(bB, letter);
            System.out.println("Максимальное кол " + letter + "->" + maxB);
        });
        b.start();
        Thread c = new Thread(() -> {
            char letter = 'c';
            int maxC = maxChar(cC, letter);
            System.out.println("Максимальное кол " + letter + "->" + maxC);
        });
        c.start();
    }
    public static Thread textGenerator(){
        Thread textGenerator = new Thread(() ->
        {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    aA.put(text);
                    bB.put(text);
                    cC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        textGenerator.start();
        return textGenerator;
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static int maxChar(BlockingQueue<String> queue, char letter ) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10_000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "STOP");
            return -1;
        }
        return max;
    }
}