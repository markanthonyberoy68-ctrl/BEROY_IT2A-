package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void showMenu(User adminUser) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ADMIN / BUSINESS OWNER DASHBOARD ===");
            System.out.println("Logged in as: " + adminUser.fullName());
            System.out.println("[1] View All System Users");
            System.out.println("[2] View Pending Registrations");
            System.out.println("[3] Approve User Account");
            System.out.println("[4] Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayAllUsers();
                    break;
                case "2":
                    displayPendingUsers();
                    break;
                case "3":
                    handleUserApproval();
                    break;
                case "4":
                    System.out.println("Logging out of Admin Dashboard...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayAllUsers() {
        List<User> users = userDao.getAllUsers();
        printUserTable(users, "ALL SYSTEM USERS");
    }

    private void displayPendingUsers() {
        List<User> users = userDao.getPendingUsers();
        printUserTable(users, "PENDING REGISTRATIONS");
    }

    private void handleUserApproval() {
        List<User> pendingUsers = userDao.getPendingUsers();
        if (pendingUsers.isEmpty()) {
            System.out.println("\nNo pending user accounts found.");
            return;
        }

        printUserTable(pendingUsers, "PENDING ACCOUNTS FOR APPROVAL");
        System.out.print("Enter User ID to approve (or enter 0 to cancel): ");

        try {
            int targetId = Integer.parseInt(scanner.nextLine());
            if (targetId == 0) {
                System.out.println("Approval action cancelled.");
                return;
            }

            // Verify if ID exists in the pending list
            boolean exists = pendingUsers.stream().anyMatch(u -> u.userId() == targetId);
            if (exists) {
                boolean success = userDao.approveUser(targetId);
                if (success) {
                    System.out.println("Success! User ID " + targetId + " has been APPROVED.");
                } else {
                    System.out.println("Failed to update user status in database.");
                }
            } else {
                System.out.println("Invalid User ID or user is not in the pending list.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric User ID.");
        }
    }

    // Helper method for formatted console output using exact formatting requirements
    private void printUserTable(List<User> users, String title) {
        System.out.println("\n--- " + title + " ---");
        if (users.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        // Formatted headers: ID (5), Username (15), Role (12), Status (10)
        String format = "%-6s %-16s %-20s %-12s %-10s%n";
        System.out.printf(format, "ID", "Username", "Full Name", "Role", "Status");
        System.out.println("------------------------------------------------------------------");

        for (User u : users) {
            System.out.printf(format, u.userId(), u.username(), u.fullName(), u.role(), u.status());
        }
        System.out.println("------------------------------------------------------------------");
    }
}