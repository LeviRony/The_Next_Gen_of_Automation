package utilities;


import java.io.BufferedReader;
import java.util.concurrent.*;

public class UserInput {

    public static String getInputWithTimeout(BufferedReader reader, String prompt, int timeoutSeconds, String defaultValue) {
        System.out.print(prompt);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> reader.readLine());
        try {
            // Waiting for user input with a timeout
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timeout! Using default: " + defaultValue);
            future.cancel(true); // cancel the task
            return defaultValue;
        } catch (Exception e) {
            System.out.println("Error reading input, using default: " + defaultValue);
            return defaultValue;
        } finally {
            executor.shutdownNow();
        }
    }
}
