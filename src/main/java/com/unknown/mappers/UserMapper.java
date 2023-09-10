package com.unknown.mappers;

import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.UpdateUserRequest;
import com.unknown.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
User toUser(CreateUserRequest request);
}

