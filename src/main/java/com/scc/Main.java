import dao.DatabaseConnection;
import dao.UserDao;
import ui.Menu;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseConnection.initializeDatabase();

            System.out.println(
                    "SQLite database connected successfully!");

            try (Scanner scanner = new Scanner(System.in)) {

                UserDao userDao = new UserDao();

                Menu menu = new Menu(scanner, userDao);

                menu.start();
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to start application: "
                            + e.getMessage());
        }
    }
}