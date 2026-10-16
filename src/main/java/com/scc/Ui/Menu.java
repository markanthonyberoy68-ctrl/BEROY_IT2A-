package ui;

import dao.UserDao;
import model.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final UserDao userDao;

    public Menu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void start() {

        boolean running = true;

        while (running) {
            System.out.println("\n===== ACCOUNT SYSTEM =====");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> registerAccount();
                    case "2" -> login();
                    case "3" -> {
                        running = false;
                        System.out.println("Application closed.");
                    }
                    default ->
                            System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println(
                        "Database error: " + e.getMessage());
            }
        }
    }

    private void registerAccount() throws SQLException {

        System.out.println("\n--- REGISTER ACCOUNT ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.isBlank() || password.isBlank()) {
            System.out.println(
                    "Username and password are required.");
            return;
        }

        System.out.println("\nSelect Role:");
        System.out.println("1. TEACHER");
        System.out.println("2. STUDENT");
        System.out.print("Enter choice: ");

        String roleChoice = scanner.nextLine().trim();

        String role;

        switch (roleChoice) {
            case "1" -> role = "TEACHER";
            case "2" -> role = "STUDENT";
            default -> {
                System.out.println("Invalid role.");
                return;
            }
        }

        boolean registered = userDao.register(
                username, password, role);

        if (registered) {
            System.out.println(
                    "\nRegistration successful!");
            System.out.println("Username: " + username);
            System.out.println("Role: " + role);
            System.out.println("Status: PENDING");
            System.out.println(
                    "Your account is awaiting approval.");
        } else {
            System.out.println(
                    "Registration failed. Username may already exist.");
        }
    }

    private void login() throws SQLException {

        System.out.println("\n--- LOGIN ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> result =
                userDao.login(username, password);

        if (result.isEmpty()) {
            System.out.println(
                    "Invalid username or password.");
            return;
        }

        User user = result.get();

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + user.username());
        System.out.println("Role: " + user.roles());
        System.out.println("Status: " + user.status());

        if ("PENDING".equals(user.status())) {
            System.out.println(
                    "Your account is pending approval.");
        }

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println("\n===== USER MENU =====");
            System.out.println("1. View Account Details");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n--- ACCOUNT DETAILS ---");
                    System.out.println("ID: " + user.id());
                    System.out.println(
                            "Username: " + user.username());
                    System.out.println(
                            "Role: " + user.roles());
                    System.out.println(
                            "Status: " + user.status());
                }

                case "2" -> {
                    loggedIn = false;
                    System.out.println(
                            "Logout successful!");
                    System.out.println(
                            "Returning to main menu...");
                }

                default ->
                        System.out.println("Invalid choice.");
            }
        }
    }
}