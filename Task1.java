package pr_3_11;
import java.util.Random;

public class Task1 {

    // генерац випадк мас задан розмір та діап знач
    public static int[] generateRandomArray(int size, int start, int end) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(end - start + 1) + start; // генер випадк чис в меж від start до end
        }
        return array;
    }

    // вивед мас на екран
    public static void displayArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    // обчисл попарн сум елемен мас
    public static int calculateSum(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length - 1; i++) {
            sum += array[i] + array[i + 1]; // (a1 + a2) + (a2 + a3) + ... (an-1 + an)
        }
        return sum;
    }

    // мет вик завд
    public static void runTask() {
        System.out.println("\033[32mЗавдання 1: Початок обчислення попарної суми елементів масиву...\033[0m");

        // запит у кор розм мас, почат та кінц знач для генер мас
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("\033[32mВведіть кількість елементів масиву:\033[0m");
        int size = scanner.nextInt();
        System.out.println("\033[32mВведіть початкове значення діапазону:\033[0m");
        int start = scanner.nextInt();
        System.out.println("\033[32mВведіть кінцеве значення діапазону:\033[0m");
        int end = scanner.nextInt();

        // генерац випадк мас
        int[] array = generateRandomArray(size, start, end);
        System.out.println("\033[32mЗгенерований масив:\033[0m");
        displayArray(array);

        // почат обчис час
        long startTime = System.nanoTime();
        int result = calculateSum(array);
        long endTime = System.nanoTime();

        // вивед рез
        System.out.println("\033[32mРезультат обчислення попарної суми: \033[0m" + result);
        System.out.println("\033[32mЧас виконання завдання 1: \033[0m" + (endTime - startTime) / 1e6 + " мс");

        System.out.println("\033[32mЗавдання 1: Завершено.\033[0m");
    }
}
