package com.victor.picpay.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.victor.picpay.dtos.requests.WalletDTO;
import com.victor.picpay.dtos.responses.WalletInfoDTO;
import com.victor.picpay.entities.Wallet;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "userId", target = "user.id")
    @BeanMapping(ignoreByDefault = true)
    Wallet dtoToWallet(WalletDTO walletDTO);

    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.cpfCnpj", target = "cpfCnpj")
    @Mapping(source = "user.userType", target = "userType")
    WalletInfoDTO fromWalletToDto(Wallet wallet);
}
