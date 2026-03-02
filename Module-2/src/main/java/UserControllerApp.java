import dao.UserDAO;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import service.UserService;
import util.HibernateUtil;

import java.util.Scanner;

@Slf4j
public class UserControllerApp {
    private final UserService userService;
    private final Scanner scanner;

    public UserControllerApp(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> createUser();
                    case "2" -> findUser();
                    case "3" -> updateUser();
                    case "4" -> deleteUser();
                    case "5" -> {
                        running = false;
                        System.out.println("Exiting application...");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                log.error("Console application error: ", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Create User");
        System.out.println("2. Find User by ID");
        System.out.println("3. Update User");
        System.out.println("4. Delete User");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private void createUser() {
        User user = new User();
        System.out.print("Enter name: ");
        user.setName(scanner.nextLine());
        System.out.print("Enter email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Enter age: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        userService.createUser(user);
        System.out.println("User successfully created!");
    }

    private void findUser() {
        System.out.print("Enter User ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userService.getUserById(id);
        System.out.println("Found: " + user);
    }

    private void updateUser() {
        System.out.print("Enter ID of user to update: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);

        System.out.print("Enter new name (current: " + user.getName() + "): ");
        user.setName(scanner.nextLine());
        System.out.print("Enter new email (current: " + user.getEmail() + "): ");
        user.setEmail(scanner.nextLine());

        userService.updateUser(user);
        System.out.println("User updated!");
    }

    private void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        userService.deleteUser(id);
        System.out.println("User deleted!");
    }
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserDAO userDAO = new UserDAO(sessionFactory);
        UserService userService = new UserService(userDAO);
        UserControllerApp app = new UserControllerApp(userService);

        app.start();
    }
}
