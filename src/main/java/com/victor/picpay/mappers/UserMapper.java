package com.victor.picpay.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.victor.picpay.dtos.requests.UpdateUserDTO;
import com.victor.picpay.dtos.requests.UserDTO;
import com.victor.picpay.dtos.responses.UserInfoDTO;
import com.victor.picpay.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "cpfCnpj", target = "cpfCnpj")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userType", target = "userType")
    User dtoToUser(UserDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "userType", target = "userType")
    UserInfoDTO toInfoDto(User user);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDTO dto, @MappingTarget User user);
}
