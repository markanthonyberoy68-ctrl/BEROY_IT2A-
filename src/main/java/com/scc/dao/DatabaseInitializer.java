package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "full_name TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'PENDING'" +
                ");";

        String createSubscribersTable = "CREATE TABLE IF NOT EXISTS subscribers (" +
                "subscriber_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "contact_number TEXT NOT NULL, " +
                "plan_type TEXT NOT NULL, " +
                "start_date TEXT NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'ACTIVE'" +
                ");";

        String createContainersTable = "CREATE TABLE IF NOT EXISTS containers (" +
                "container_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "container_code TEXT UNIQUE NOT NULL, " +
                "container_type TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "current_subscriber_id INTEGER, " +
                "deposit_amount REAL DEFAULT 0.0, " +
                "FOREIGN KEY (current_subscriber_id) REFERENCES subscribers(subscriber_id) ON DELETE SET NULL" +
                ");";

        String createDeliveriesTable = "CREATE TABLE IF NOT EXISTS deliveries (" +
                "delivery_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subscriber_id INTEGER NOT NULL, " +
                "rider_id INTEGER NOT NULL, " +
                "delivery_date TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "total_amount REAL NOT NULL, " +
                "payment_status TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "FOREIGN KEY (subscriber_id) REFERENCES subscribers(subscriber_id), " +
                "FOREIGN KEY (rider_id) REFERENCES users(user_id)" +
                ");";

        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS container_transactions (" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "container_id INTEGER NOT NULL, " +
                "subscriber_id INTEGER NOT NULL, " +
                "delivery_id INTEGER, " +
                "action TEXT NOT NULL, " +
                "transaction_date TEXT NOT NULL, " +
                "FOREIGN KEY (container_id) REFERENCES containers(container_id), " +
                "FOREIGN KEY (subscriber_id) REFERENCES subscribers(subscriber_id), " +
                "FOREIGN KEY (delivery_id) REFERENCES deliveries(delivery_id)" +
                ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createSubscribersTable);
            stmt.execute(createContainersTable);
            stmt.execute(createDeliveriesTable);
            stmt.execute(createTransactionsTable);

            System.out.println("Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}