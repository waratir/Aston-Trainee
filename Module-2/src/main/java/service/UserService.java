package service;

import dao.UserDAO;
import entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for User entity.
 * Provides methods for user creates, updates, and management.
 */
@Slf4j
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user after basic validation.
     *
     * @param user Entity to be created.
     * @throws IllegalArgumentException if user data is invalid.
     */
    public void createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        userDAO.create(user);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id Primary key of the user.
     * @throws EntityNotFoundException if user not found.
     */
    public User getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);
        return userDAO.read(id)
                .orElseThrow(() -> {
                    log.warn("User lookup failed: ID {} not found", id);
                    return new EntityNotFoundException("User not found: " + id);
                });
    }

    /**
     * Updates existing user information.
     *
     * @param user User entity with updated fields.
     */
    public void updateUser(User user) {
        if (user.getId() == null) {
            log.error("Update failed: User ID is null");
            throw new IllegalArgumentException("ID is required for update");
        }
        log.info("Updating user data for ID: {}", user.getId());
        userDAO.update(user);
    }

    /**
     * Removes a user from the system by ID.
     *
     * @param id Identifier of the user to delete.
     */
    public void deleteUser(Long id) {
        getUserById(id);
        log.info("Deleting user with ID: {}", id);
        userDAO.delete(id);
    }

}
