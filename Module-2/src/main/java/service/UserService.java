package service;

import dto.UserDTO;

public interface UserService {
    void createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    void updateUser(UserDTO userDTO);
    void deleteUser(UserDTO userDTO);
}