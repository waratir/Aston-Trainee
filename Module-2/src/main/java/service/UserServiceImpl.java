package service;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import mapper.UserMapper;

/**
 * Service layer for User entity.
 * Provides methods for user creates, updates, and management.
 */
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserServiceImpl(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user after basic validation.
     *
     * @param userDTO Entity to be created.
     */
    @Override
    public void createUser(UserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        User user = userMapper.toEntity(userDTO);

        try {
            userDAO.create(user);
            log.info("User successfully created with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to create user: {}", userDTO.getEmail());
            throw e;
        }
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id Primary key of the user.
     * @throws EntityNotFoundException if user not found.
     */
    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);

        return userDAO.read(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("User lookup failed: ID {} not found", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
    }

    /**
     * Updates existing user information.
     *
     * @param userDTO User entity with updated fields.
     */
    @Override
    public void updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new ValidationException("Update failed: ID is required");
        }

        log.info("Attempting to update user with ID: {}", userDTO.getId());

        User existingUser = userDAO.read(userDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Update failed: User with ID {} not found", userDTO.getId());
                    return new EntityNotFoundException("User not found with ID: " + userDTO.getId());
                });

        log.info("Updating user {} (new email: {})", existingUser.getEmail(), userDTO.getEmail());

        User updatedEntity = userMapper.toEntity(userDTO);
        userDAO.update(updatedEntity);
    }

    /**
     * Removes a user from the system by ID.
     *
     * @param userDTO Identifier of the user to delete.
     */
    @Override
    public void deleteUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new ValidationException("Cannot delete user: ID is missing");
        }
        User entity = userDAO.read(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userDTO.getId()));

        log.info("Deleting user: {} (ID: {})", entity.getEmail(), entity.getId());

        userDAO.delete(entity.getId());
    }
}
