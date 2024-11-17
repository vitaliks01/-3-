package pr_3_11;
import java.io.File;

public class Task2 {

    // мет для підрах файл з певн розшир
    public static long countFilesWithExtension(File directory, String extension) {
        long count = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        count += countFilesWithExtension(file, extension); // рекурсив перевір підкаталоги
                    } else if (file.getName().endsWith(extension)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // мет для вик завдан
    public static void runTask() {
        System.out.println("\033[34mЗавдання 2: Початок підрахунку файлів з певним розширенням...\033[0m");

        // запит у користувач шлях до директорії
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("\033[34mВведіть шлях до директорії:\033[0m");
        String directoryPath = scanner.nextLine();

        // встановл розшир за замовч (.pdf)
        String extension = ".pdf";
        System.out.println("\033[34mЗа замовчуванням використовується розширення \033[0m" + extension + "\n\033[34mДля іншого розширення введіть його вручну, або натисніть Enter для використання за замовчуванням.\033[0m");

        // запит у користув, якщо хоч змін розшир
        String inputExtension = scanner.nextLine();
        if (!inputExtension.isEmpty()) {
            extension = inputExtension.startsWith(".") ? inputExtension : "." + inputExtension;  // дод крап, якщо її нема
        }

        File directory = new File(directoryPath);
        long count = countFilesWithExtension(directory, extension);

        System.out.println("\033[34mКількість файлів з розширенням \033[0m" + extension + ": " + count);

        System.out.println("\033[34mЗавдання 2: Завершено.\033[0m");
    }
}
