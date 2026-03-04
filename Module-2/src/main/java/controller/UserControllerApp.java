package controller;

import dto.UserDTO;
import exception.EntityNotFoundException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import service.UserService;

@Slf4j
public class UserControllerApp {
    private final UserService userService;
    private final InputHandler inputHandler;

    public UserControllerApp(UserService userService, InputHandler inputHandler) {
        this.userService = userService;
        this.inputHandler = inputHandler;
    }

    public void start() {
        while (true) {
            printMenu();
            String choice = inputHandler.readString("Select an option: ");

            try {
                switch (choice) {
                    case "1" -> createUser();
                    case "2" -> findUser();
                    case "3" -> updateUser();
                    case "4" -> deleteUser();
                    case "5" -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (ValidationException | EntityNotFoundException e) {
                System.err.println("Business Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("System Error: Something went wrong.");
                log.error("Unexpected error", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Create User | 2. Find User | 3. Update User | 4. Delete User | 5. Exit");
    }

    private void createUser() {
        UserDTO dto = UserDTO.builder()
                .name(inputHandler.readString("Enter name: "))
                .email(inputHandler.readString("Enter email: "))
                .age(inputHandler.readInt("Enter age: "))
                .build();

        userService.createUser(dto);
        System.out.println("User successfully created!");
    }

    private void findUser() {
        Long id = inputHandler.readLong("Enter User ID: ");
        UserDTO user = userService.getUserById(id);
        System.out.println("Found: " + user);
    }

    private void updateUser() {
        Long id = inputHandler.readLong("Enter ID of user to update: ");

        UserDTO current = userService.getUserById(id);

        UserDTO updatedDto = UserDTO.builder()
                .id(id)
                .name(inputHandler.readString("New name (current: " + current.getName() + "): "))
                .email(inputHandler.readString("New email (current: " + current.getEmail() + "): "))
                .age(current.getAge())
                .build();

        userService.updateUser(updatedDto);
        System.out.println("User updated!");
    }

    private void deleteUser() {
        Long id = inputHandler.readLong("Enter User ID to delete: ");

        userService.deleteUser(UserDTO.builder().id(id).build());
        System.out.println("User deleted!");
    }
}
