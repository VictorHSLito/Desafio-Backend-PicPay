package com.victor.picpay.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "cpfCnpj", target = "cpfCnpj")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userType", target = "userType")
    @BeanMapping(ignoreByDefault = true)
    User dtoToUser(UserDTO dto);
}
