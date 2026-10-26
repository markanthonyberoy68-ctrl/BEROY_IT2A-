package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final UserDao userDao;

    public Menu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void displayRoleMenu(User user) {
        String role = user.role().toUpperCase();

        switch (role) {
            case "ADMIN":
            case "BUSINESS OWNER":
                displayAdminMenu(user);
                break;
            case "DISPATCHER":
            case "OFFICE STAFF":
                displayDispatcherMenu(user);
                break;
            case "RIDER":
            case "DELIVERY RIDER":
                displayRiderMenu(user);
                break;
            default:
                System.out.println("Unknown role: " + role);
        }
    }

    private void displayAdminMenu(User user) {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("\n=== ADMIN / BUSINESS OWNER DASHBOARD ===");
            System.out.println("Logged in as: " + user.fullName());
            System.out.println("[1] Manage Users / Approve Accounts");
            System.out.println("[2] Products & Subscription Plans");
            System.out.println("[3] View All Reports & Daily Summary");
            System.out.println("[4] Audit Logs (Container History)");
            System.out.println("[5] Delete / Deactivate Records");
            System.out.println("[6] Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("-> [Feature] User & Account Management (Deliverable 5)");
                    break;
                case "2":
                    System.out.println("-> [Feature] Subscription Plans Management");
                    break;
                case "3":
                    System.out.println("-> [Feature] Daily Delivery Report");
                    break;
                case "4":
                    System.out.println("-> [Feature] Container Movement Audit Logs");
                    break;
                case "5":
                    System.out.println("-> [Feature] Deactivate / Delete Records");
                    break;
                case "6":
                    System.out.println("Logging out of Admin Dashboard...");
                    inAdminMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayDispatcherMenu(User user) {
        boolean inDispatcherMenu = true;
        while (inDispatcherMenu) {
            System.out.println("\n=== DISPATCHER / OFFICE STAFF DASHBOARD ===");
            System.out.println("Logged in as: " + user.fullName());
            System.out.println("[1] Register New Subscriber");
            System.out.println("[2] Search Subscribers");
            System.out.println("[3] Create Delivery Schedule / Order");
            System.out.println("[4] Assign Riders & Track Schedules");
            System.out.println("[5] Record Payments");
            System.out.println("[6] Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("-> [Feature] Register New Subscriber");
                    break;
                case "2":
                    System.out.println("-> [Feature] Search Subscribers");
                    break;
                case "3":
                    System.out.println("-> [Feature] Create Delivery Schedule");
                    break;
                case "4":
                    System.out.println("-> [Feature] Assign Delivery Riders");
                    break;
                case "5":
                    System.out.println("-> [Feature] Record Customer Payments");
                    break;
                case "6":
                    System.out.println("Logging out of Dispatcher Dashboard...");
                    inDispatcherMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayRiderMenu(User user) {
        boolean inRiderMenu = true;
        while (inRiderMenu) {
            System.out.println("\n=== DELIVERY RIDER DASHBOARD ===");
            System.out.println("Logged in as: " + user.fullName());
            System.out.println("[1] View My Assigned Deliveries");
            System.out.println("[2] Mark Delivery Status (Delivered / Missed)");
            System.out.println("[3] Record Containers Dropped Off & Picked Up");
            System.out.println("[4] Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("-> [Feature] View Daily Delivery List");
                    break;
                case "2":
                    System.out.println("-> [Feature] Update Delivery Status");
                    break;
                case "3":
                    System.out.println("-> [Feature] Update Container Status (In Stock/With Customer/Damaged/Lost)");
                    break;
                case "4":
                    System.out.println("Logging out of Rider Dashboard...");
                    inRiderMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}