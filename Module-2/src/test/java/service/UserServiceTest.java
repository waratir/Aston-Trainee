package service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.UserDAO;
import entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private User createValidUser(Long id) {
        return User.builder()
                .id(id)
                .name("Default Name")
                .email("test@gmail.com")
                .age(20)
                .build();
    }

    @Test
    @DisplayName("Should successfully create the user")
    void createUser_Success() {
        User user = createValidUser(null);

        userService.createUser(user);

        verify(userDAO, times(1)).create(user);
    }

    @Test
    @DisplayName("Should return user by ID")
    void getUserById_Found() {
        Long userId = 1L;
        User expectedUser = createValidUser(userId);

        when(userDAO.read(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(userId);

        assertNotNull(actualUser);
        assertEquals(userId, actualUser.getId());
        assertEquals("test@gmail.com", actualUser.getEmail());
    }

    @Test
    @DisplayName("Should throw an exception if the ID does not exist")
    void getUserById_NotFound_ThrowsException() {
        when(userDAO.read(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    @DisplayName("Should successfully update user data")
    void updateUser_Success() {
        Long userId = 1L;
        User user = createValidUser(userId);

        userService.updateUser(user);

        verify(userDAO, times(1)).update(user);
    }

    @Test
    @DisplayName("Should throw an exception when attempting to update a user without an ID")
    void updateUser_NullId_ThrowsException() {
        User userWithoutId = createValidUser(null);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userWithoutId));

        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("Should delete the user if it exists")
    void deleteUser_Success() {
        Long id = 1L;
        User user = createValidUser(id);

        when(userDAO.read(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userDAO).delete(id);
    }
}