package mapper;

import dto.UserDTO;
import entity.User;

public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO dto);
}
