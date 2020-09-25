package yte.intern.project.manageActivities.mapper;

import org.mapstruct.Mapper;
import yte.intern.project.manageActivities.dto.UserDTO;
import yte.intern.project.manageActivities.entity.Users;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDTO mapToDto(Users users);

    Users mapToEntity(UserDTO userDTO);

    List<UserDTO> mapToDto(List<Users> usersList);

    List<Users> mapToEntity(List<UserDTO> userDTOList);


}

