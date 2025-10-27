package hostel.utils;

/**
 * Utility class for console operations
 */
public class ConsoleUtils {

    /**
     * Clears the console screen
     * Works on Windows, macOS, and Linux
     */
    public static void clearConsole() {
        try {
            // For Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            // For macOS and Linux
            else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Fallback: print multiple newlines if clearing fails
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Prints a separator line for better visual organization
     */
    public static void printSeparator() {
        System.out.println("=".repeat(60));
    }

    /**
     * Prints a header with the given title
     */
    public static void printHeader(String title) {
        clearConsole();
        printSeparator();
        System.out.println("  " + title.toUpperCase());
        printSeparator();
    }
}
