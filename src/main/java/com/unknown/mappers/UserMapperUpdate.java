package com.unknown.mappers;

import com.unknown.dto.UpdateUserRequest;
import com.unknown.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapperUpdate {

    @Mapping(target = "id", ignore = true)
    User toUpdate(UpdateUserRequest request,@MappingTarget User user);
}
