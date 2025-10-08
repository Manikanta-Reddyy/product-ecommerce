package com.example.demoe.mapper;

import com.example.demoe.dto.RoleDto;
import com.example.demoe.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role role);
    Role toEntity(Role role);
}
