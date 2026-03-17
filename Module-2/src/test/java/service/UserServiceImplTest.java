package service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO testDto;
    private User testEntity;

    @BeforeEach
    void setUp() {
        testDto = new UserDTO(1L, "Alex", "test@gmail.com", 25);
        testEntity = new User(1L, "Alex", "test@gmail.com", 25, null);
    }

    @Test
    @DisplayName("createUser: should successfully create the user")
    void createUser_Success() {
        when(userMapper.toEntity(testDto)).thenReturn(testEntity);

        userService.createUser(testDto);

        verify(userMapper).toEntity(testDto);
        verify(userDAO).create(testEntity);
    }

    @Test
    @DisplayName("createUser: should propagate exception when DAO fails")
    void createUser_ThrowsException_WhenDaoFails() {
        when(userMapper.toEntity(testDto)).thenReturn(testEntity);
        doThrow(new RuntimeException("DB Error")).when(userDAO).create(testEntity);

        assertThrows(RuntimeException.class, () -> userService.createUser(testDto));
    }

    @Test
    @DisplayName("getUserById: should return UserDTO when user exists")
    void getUserById_Success() {
        when(userDAO.read(1L)).thenReturn(Optional.of(testEntity));
        when(userMapper.toDto(testEntity)).thenReturn(testDto);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(testDto.getEmail(), result.getEmail());
        verify(userDAO).read(1L);
    }

    @Test
    @DisplayName("getUserById: should throw EntityNotFoundException when user does not exist")
    void getUserById_NotFound_ThrowsException() {
        when(userDAO.read(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(1L));

        assertTrue(ex.getMessage().contains("User not found with ID: 1"));
    }

    @Test
    @DisplayName("updateUser: should successfully update user when record exists")
    void updateUser_Success() {
        when(userDAO.read(1L)).thenReturn(Optional.of(testEntity));
        when(userMapper.toEntity(testDto)).thenReturn(testEntity);

        userService.updateUser(testDto);

        verify(userDAO).read(1L);
        verify(userDAO).update(testEntity);
    }

    @Test
    @DisplayName("updateUser: should throw ValidationException when ID is null")
    void updateUser_NullId_ThrowsValidationException() {
        testDto.setId(null);

        assertThrows(ValidationException.class, () -> userService.updateUser(testDto));
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("updateUser: should throw EntityNotFoundException when user to update does not exist")
    void updateUser_UserNotFound_ThrowsException() {
        Long userId = testDto.getId();
        when(userDAO.read(userId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(testDto));

        assertTrue(ex.getMessage().contains("User not found with ID: " + userId));

        verify(userDAO, never()).update(any());
        verify(userDAO).read(userId);
    }

    @Test
    @DisplayName("deleteUser: should successfully remove user when record exists")
    void deleteUser_Success() {
        when(userDAO.read(1L)).thenReturn(Optional.of(testEntity));

        userService.deleteUser(testDto);

        verify(userDAO).read(1L);
        verify(userDAO).delete(1L);
    }

    @Test
    @DisplayName("deleteUser: should throw ValidationException when ID is null")
    void deleteUser_NullId_ThrowsValidationException() {
        testDto.setId(null);

        assertThrows(ValidationException.class, () -> userService.deleteUser(testDto));
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("deleteUser: should throw EntityNotFoundException when user to delete is not found")
    void deleteUser_NotFound_ThrowsException() {
        when(userDAO.read(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(testDto));
        verify(userDAO, never()).delete(anyLong());
    }
}