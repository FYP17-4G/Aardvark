public class TimeTest {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        properModulo(78723, 293892, 10000000);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;
        System.out.println("Time taken in ms = " + duration);
    }

    private static void properModulo (int a, int b, int iterations) {
        int out = 0;
        for (int i = 0; i < iterations; ++i) {
            out = (a % b + b) % b;
        }

        System.out.println("out = " + out);
    }


}
