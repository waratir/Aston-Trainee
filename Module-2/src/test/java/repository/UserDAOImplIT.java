package repository;

import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOImplIT extends BaseIT {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl(sessionFactory);
    }

    @Test
    @DisplayName("Create & Read: should save user and find it by ID")
    void createAndReadUser() {
        User user = User.builder()
                .name("Alex")
                .email("alex@gmail.com")
                .age(33)
                .build();

        userDAO.create(user);

        Optional<User> found = userDAO.read(user.getId());

        assertTrue(found.isPresent());
        assertEquals("alex@gmail.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Update: should change user data in the database")
    void updateUser() {
        User user = User.builder()
                .name("Zhan")
                .email("zhan@gmail.com")
                .age(30)
                .build();
        userDAO.create(user);
        Long id = user.getId();

        user.setName("TestName");
        user.setAge(31);

        userDAO.update(user);

        User updated = userDAO.read(id).orElseThrow();
        assertEquals("TestName", updated.getName());
        assertEquals(31, updated.getAge());
        assertEquals("zhan@gmail.com", updated.getEmail());
    }

    @Test
    @DisplayName("Delete: should remove user from the database")
    void deleteUser() {
        User user = User.builder()
                .name("Test2")
                .email("test2@gmail.com")
                .age(20)
                .build();
        userDAO.create(user);
        Long id = user.getId();

        userDAO.delete(id);

        Optional<User> deleted = userDAO.read(id);
        assertTrue(deleted.isEmpty(), "User should be deleted from DB");
    }

    @Test
    @DisplayName("Read Non-Existent: should return empty optional")
    void readNonExistentUser() {
        Optional<User> found = userDAO.read(999L);
        assertTrue(found.isEmpty());
    }
}
