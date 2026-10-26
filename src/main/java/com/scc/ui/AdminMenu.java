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
            System.out.println("[4] Update User Role & Status");
            System.out.println("[5] Logout");
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
                    handleUserUpdate(adminUser);
                    break;
                case "5":
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
        int targetId = readIntInput("Enter User ID to approve (or enter 0 to cancel): ");

        if (targetId == 0) {
            System.out.println("Approval action cancelled.");
            return;
        }

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
    }

    private void handleUserUpdate(User currentAdmin) {
        List<User> users = userDao.getAllUsers();
        printUserTable(users, "ALL SYSTEM USERS - UPDATE MANAGEMENT");

        int targetId = readIntInput("Enter User ID to update (or enter 0 to cancel): ");
        if (targetId == 0) {
            System.out.println("Update action cancelled.");
            return;
        }

        // Self-Edit Restriction Security Rule[cite: 8]
        if (targetId == currentAdmin.userId()) {
            System.out.println("\n[!] WARNING: Modifying the currently active admin account is restricted.");
            return;
        }

        boolean exists = users.stream().anyMatch(u -> u.userId() == targetId);
        if (!exists) {
            System.out.println("User ID not found.");
            return;
        }

        String newRole = readValidRole();
        String newStatus = readValidStatus();

        boolean success = userDao.updateUserRoleAndStatus(targetId, newRole, newStatus);
        if (success) {
            System.out.println("\n[+] Success! User ID " + targetId + " updated to Role: " + newRole + ", Status: " + newStatus);
        } else {
            System.out.println("\n[-] Failed to update user in the database.");
        }
    }

    // --- Input Validation Wrappers (Deliverable 7 Requirement)[cite: 8] ---

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a valid number.");
            }
        }
    }

    private String readValidRole() {
        while (true) {
            System.out.print("Enter new role (ADMIN, DISPATCHER, RIDER): ");
            String role = scanner.nextLine().trim().toUpperCase();
            if (role.equals("ADMIN") || role.equals("DISPATCHER") || role.equals("RIDER") || role.equals("BUSINESS OWNER") || role.equals("OFFICE STAFF") || role.equals("DELIVERY RIDER")) {
                return role;
            }
            System.out.println("[!] Invalid role. Please choose a valid system role.");
        }
    }

    private String readValidStatus() {
        while (true) {
            System.out.print("Enter new status (APPROVED, PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("[!] Invalid status. Must be APPROVED or PENDING.");
        }
    }

    private void printUserTable(List<User> users, String title) {
        System.out.println("\n--- " + title + " ---");
        if (users.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        String format = "%-6s %-16s %-20s %-12s %-10s%n";
        System.out.printf(format, "ID", "Username", "Full Name", "Role", "Status");
        System.out.println("------------------------------------------------------------------");

        for (User u : users) {
            System.out.printf(format, u.userId(), u.username(), u.fullName(), u.role(), u.status());
        }
        System.out.println("------------------------------------------------------------------");
    }
}