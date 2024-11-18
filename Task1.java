package pr_3_11;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.Scanner;

public class Task1 {

    // генерац випадк масив з задан розмір та діапа знач
    public static int[] generateRandomArray(int size, int start, int end) {
        int[] array = new int[size];
        Random random = new Random();
        // від start -> end
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(end - start + 1) + start;
        }
        return array;
    }

    // вивед елемент мас на екр
    public static void displayArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");  //счерез пробіл
        }
        System.out.println();
    }

    // обчисл сум парн елемент за допомог Fork/Join Framework (Work Stealing)
    public static class PairSumTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        public PairSumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            // якщо розм сегмент масив менш або = 10 обчисл сум парн елемент без поділ задач
            if (end - start <= 10) {
                int sum = 0;
                for (int i = start; i < end - 1; i++) {
                    sum += array[i] + array[i + 1];  // сума пар елемент
                }
                return sum;
            } else {
                // якщо сегмент великий ділим задач на 2 частин
                int mid = (start + end) / 2;
                PairSumTask leftTask = new PairSumTask(array, start, mid);
                PairSumTask rightTask = new PairSumTask(array, mid, end);
                leftTask.fork();  // асинхронно вик задач
                int rightResult = rightTask.compute();  // обчисл прав задач
                int leftResult = leftTask.join();  // чек на резул лів задач
                return leftResult + rightResult;  // поверт сум обох результ
            }
        }
    }

    // використ ThreadPool для паралел вик задач з обчисл сум
    public static int calculateSumWithThreadPool(int[] array) throws InterruptedException, ExecutionException {
        // створ пул поток
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int taskSize = array.length / 4;  // 4 частин для обробк
        Future<Integer>[] results = new Future[4];

        // розбиваєм мас на 4 частин і подаєм їх в обробк
        for (int i = 0; i < 4; i++) {
            final int start = i * taskSize;
            final int end = (i == 3) ? array.length : start + taskSize;
            results[i] = executor.submit(() -> {
                int sum = 0;
                for (int j = start; j < end - 1; j++) {
                    sum += array[j] + array[j + 1];  // обчисл сум пар
                }
                return sum;
            });
        }

        // збир результ з усіх задач
        int totalSum = 0;
        for (Future<Integer> result : results) {
            totalSum += result.get();
        }

        executor.shutdown();  // заверш робот пул поток
        return totalSum;  // поверт заг сум
    }

    // основн мет для запуск задач
    public static void runTask() throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        int size = 0, start = 0, end = 0;

        // введ кільк елем мас
        while (true) {
            try {
                System.out.println("\033[32mВведіть кількість елементів масиву:\033[0m");
                size = scanner.nextInt();
                if (size <= 0) throw new IllegalArgumentException("Розмір масиву повинен бути більше нуля.");
                break;
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.err.println("\033[32mПомилка: Введіть коректне ціле число більше нуля.\033[0m");
                scanner.nextLine();
            }
        }

        // початк знач діап
        while (true) {
            try {
                System.out.println("\033[32mВведіть початкове значення діапазону:\033[0m");
                start = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.err.println("\033[32mПомилка: Введіть коректне ціле число.\033[0m");
                scanner.nextLine();
            }
        }

        // кінцев знач діап
        while (true) {
            try {
                System.out.println("\033[32mВведіть кінцеве значення діапазону:\033[0m");
                end = scanner.nextInt();
                if (end < start) throw new IllegalArgumentException("\033[32mКінцеве значення не може бути меншим за початкове.\033[0m");
                break;
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.err.println("\033[32mПомилка: Введіть коректне ціле число, яке більше або дорівнює початковому значенню.\033[0m");
                scanner.nextLine();
            }
        }

        // генерац випадк мас
        int[] array = generateRandomArray(size, start, end);
        System.out.println("\033[32mЗгенерований масив:\033[0m");
        displayArray(array);

        // Work Stealing (Fork/Join Framework)
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.nanoTime();
        int workStealingResult = forkJoinPool.invoke(new PairSumTask(array, 0, array.length));
        long endTime = System.nanoTime();
        System.out.println("\033[32mWork Stealing результат: \033[0m" + workStealingResult);
        System.out.println("\033[32mЧас виконання Work Stealing: \033[0m" + (endTime - startTime) / 1e6 + "\033[32m мс\033[0m");

        // Work Dealing (ThreadPool)
        startTime = System.nanoTime();
        int workDealingResult = calculateSumWithThreadPool(array);
        endTime = System.nanoTime();
        System.out.println("\033[32mWork Dealing результат: \033[0m" + workDealingResult);
        System.out.println("\033[32mЧас виконання Work Dealing: \033[0m" + (endTime - startTime) / 1e6 + "\033[32m мс\033[0m");
    }
}
