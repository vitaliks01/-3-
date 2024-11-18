package pr_3_11;
import java.util.concurrent.*;

public class ParallelTaskRunner {

    public static void main(String[] args) {
        // cтв пул поток для паралел вик 2 задач
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // обчисл попарн сум елемент мас
        Runnable task1 = () -> {
            try {
                Task1.runTask();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Помилка під час виконання Task1: " + e.getMessage());
                e.printStackTrace();
            }
        };

        // файл із певн розшир
        Runnable task2 = () -> {
            try {
                Task2.runTask();
            } catch (Exception e) {
                System.err.println("Помилка під час виконання Task2: " + e.getMessage());
                e.printStackTrace();
            }
        };

        try {
            // парал вик 2 задач
            System.err.println("Запуск завдання 1...");
            executorService.submit(task1);

            System.err.println("Запуск завдання 2...");
            executorService.submit(task2);

        } catch (Exception e) {
            System.err.println("Помилка при виконанні задач: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // заверш роб пул поток
        }
    }
}
