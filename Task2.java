package pr_3_11;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Task2 {

    // рекурсивн завдан для підрахун файл з конкретн розшир
    static class FileCounterTask extends RecursiveTask<Long> {
        private final File directory;  // директорія
        private final String extension;  // розширення для пош

        // конструктор для ініціалізації директорії та розшир
        public FileCounterTask(File directory, String extension) {
            this.directory = directory;
            this.extension = extension;
        }

        // мет обчисл кільк файл
        @Override
        protected Long compute() {
            long count = 0;  // лічил знайд файл
            File[] files = directory.listFiles();  // отрим спис файл і папок у директорії

            // якщо директорія пуста або неможливо отримати список файлів, повертаємо 0
            if (files == null) return count;

            // ств список підзадач для обробк підкаталогів
            var subTasks = new java.util.ArrayList<FileCounterTask>();

            // перебир всі файл та директорії
            for (File file : files) {
                if (file.isDirectory()) {  // якщо це директорія
                    FileCounterTask subTask = new FileCounterTask(file, extension);  // ств підзадач для цієї директорії
                    subTasks.add(subTask);  // дод підзадач в список
                    subTask.fork();  // асинхрон запуск підзадачу
                } else if (file.getName().endsWith(extension)) {  // якщо це файл з потрібним розшир
                    count++;  // збільш лічил
                }
            }

            // чек на заверш всіх підзадач та збир їх рез
            for (FileCounterTask subTask : subTasks) {
                count += subTask.join();  // дод рез підзадач до заг кількості
            }

            return count;  // поверт заг кільк знайден файл
        }
    }

    // основн мет для запуск завд
    public static void runTask() {
        Scanner scanner = new Scanner(System.in);

        // 1 запит шляху до директорії для пош файл
        File directory = null;
        while (directory == null || !directory.isDirectory()) {
            System.out.println("\033[34mВведіть коректний шлях до директорії для пошуку файлів:\033[0m");
            String directoryPath = scanner.nextLine();  // чит шлях до директор
            directory = new File(directoryPath);  // ств об директор

            // перевірка
            if (!directory.exists() || !directory.isDirectory()) {
                System.err.println("\033[34mПомилка: Вказаний шлях не є існуючою директорією. Спробуйте ще раз.\033[0m");
            }
        }

        // 2 запит формат файл для пошук
        System.out.println("\033[34mВведіть розширення файлів для пошуку (за замовчуванням: .pdf):\033[0m");
        String extension = scanner.nextLine();  // читаєм розшир файл
        if (extension.isEmpty()) {  // якщо користувач не ввів розшир за замовчуванням ставимо .pdf
            extension = ".pdf";
        } else if (!extension.startsWith(".")) {  // ящо розшир не поч з крапки, дод її
            extension = "." + extension;
        }

        // 3 викон завдан підрахунк файл з заданим розшир
        System.out.println("\033[34mПочинається підрахунок файлів з розширенням " + extension + " у директорії " + directory.getAbsolutePath() + "...\033[0m");

        long startTime = System.nanoTime();  // час початку викон
        ForkJoinPool pool = new ForkJoinPool();  // ств пул потоків для виконання завдань
        FileCounterTask task = new FileCounterTask(directory, extension);  // ств основну задачу для підрахунку
        long count = pool.invoke(task);  // запуск задач в пулі поток
        long endTime = System.nanoTime();  // фікс час заверш вик

        // 4 вивед результату
        System.out.println("\033[34mКількість файлів з розширенням " + extension + ": " + count + "\033[0m");
        System.out.println("\033[34mЧас виконання: " + (endTime - startTime) / 1e6 + " мс\033[0m");
        System.out.println("\033[34mЗавдання завершено успішно.\033[0m");
    }
}
