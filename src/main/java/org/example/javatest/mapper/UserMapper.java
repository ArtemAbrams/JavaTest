package org.example.javatest.mapper;

import org.example.javatest.dto.UserDto;
import org.example.javatest.dto.request.CreateUserRequest;
import org.example.javatest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(CreateUserRequest createUserRequest);
    UserDto toUserDto(User user);
}
