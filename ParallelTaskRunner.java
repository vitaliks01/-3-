package pr_3_11;
import java.util.concurrent.*;

public class ParallelTaskRunner {

    public static void main(String[] args) {
        // ств пул поток для парал вик двох завд
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // обчисл попарної сум елемент мас
        Runnable task1 = () -> {
            Task1.runTask();
        };

        // підрахунок файлів з певним розширенням
        Runnable task2 = () -> {
            Task2.runTask();
        };

        try {
            // паралел вик обох завд
            System.err.println("Запуск завдання 1...");
            executorService.submit(task1);

            System.err.println("Запуск завдання 2...");
            // чек на вик завд 1 перед поч завд 2
            executorService.submit(task2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // заверш роб пул поток
        }
    }
}
