package com.victor.picpay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.victor.picpay.dtos.TransactionDetailsDTO;
import com.victor.picpay.entities.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "payer.firstName", target = "payerName")
    @Mapping(source = "payee.firstName", target = "payeeName")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "time", target = "date")
    TransactionDetailsDTO toDetailsDTO(Transaction t);
    
}
