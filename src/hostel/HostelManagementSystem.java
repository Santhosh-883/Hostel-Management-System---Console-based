package hostel;

import hostel.data.DataManager;
import hostel.panels.StudentPanel;
import hostel.panels.WardenPanel;
import hostel.utils.ConsoleUtils;

import java.util.Scanner;

public class HostelManagementSystem {
    public static void main(String[] args) {
        System.out.println("=== Hostel Management System ===");
        System.out.println("Initializing database...");

        System.out.println("Database initialized successfully!");
        System.out.println();
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }

        DataManager dataManager = new DataManager();
        WardenPanel wardenPanel = new WardenPanel(dataManager);
        StudentPanel studentPanel = new StudentPanel(dataManager);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleUtils.clearConsole();
            System.out.println("\n--- Welcome to the Hostel Management System! ---");
            System.out.println("1. Warden Panel");
            System.out.println("2. Student Panel");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ConsoleUtils.clearConsole();
                    wardenPanel.showMenu();
                    break;
                case 2:
                    ConsoleUtils.clearConsole();
                    studentPanel.login();
                    break;
                case 3:
                    ConsoleUtils.clearConsole();
                    running = false;
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
