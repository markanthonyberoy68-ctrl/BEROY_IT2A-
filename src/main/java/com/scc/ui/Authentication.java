package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== WATER REFILL SYSTEM - AUTHENTICATION ===");
            System.out.println("[1] Login");
            System.out.println("[2] Register (New Account)");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "3":
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.loginUser(username, password);
        if (user != null) {
            if ("PENDING".equalsIgnoreCase(user.status())) {
                System.out.println("\n[!] Your account is currently PENDING approval by an Administrator.");
                return;
            }
            System.out.println("\n[+] Login successful! Welcome, " + user.fullName() + " (" + user.role() + ")");
            routeUserRole(user);
        } else {
            System.out.println("\n[-] Invalid username or password.");
        }
    }

    private void handleRegistration() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("\nSelect Role:");
        System.out.println("[1] Admin / Business Owner");
        System.out.println("[2] Dispatcher / Office Staff");
        System.out.println("[3] Delivery Rider");
        System.out.print("Choose role option: ");

        String roleChoice = scanner.nextLine();
        String role;

        switch (roleChoice) {
            case "1":
                role = "ADMIN";
                break;
            case "2":
                role = "DISPATCHER";
                break;
            case "3":
                role = "RIDER";
                break;
            default:
                System.out.println("Invalid role selected. Defaulting to RIDER.");
                role = "RIDER";
        }

        boolean success = userDao.registerUser(username, password, fullName, role);
        if (success) {
            System.out.println("\n[+] Registration successful! Your account status is PENDING approval.");
        } else {
            System.out.println("\n[-] Registration failed. Username may already exist.");
        }
    }

    private void routeUserRole(User user) {
        String role = user.role().toUpperCase();

        if ("ADMIN".equals(role) || "BUSINESS OWNER".equals(role)) {
            AdminMenu adminMenu = new AdminMenu(userDao, scanner);
            adminMenu.showMenu(user);
        } else if ("DISPATCHER".equals(role) || "OFFICE STAFF".equals(role)) {
            System.out.println("\nRouting to Dispatcher Dashboard... (Menu coming in next deliverable)");
        } else if ("RIDER".equals(role) || "DELIVERY RIDER".equals(role)) {
            System.out.println("\nRouting to Delivery Rider Dashboard... (Menu coming in next deliverable)");
        } else {
            System.out.println("\nUnknown user role: " + role);
        }
    }
}