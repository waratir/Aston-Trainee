package dao;

import entity.User;

import java.util.Optional;

public interface UserDAO {
    void create(User user);
    Optional<User> read(Long id);
    void update(User user);
    void delete(Long id);
}
