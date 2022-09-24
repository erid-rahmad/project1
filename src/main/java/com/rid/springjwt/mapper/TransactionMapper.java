package com.rid.springjwt.mapper;

import com.rid.springjwt.models.Transaction;
import com.rid.springjwt.models.TransactionDTO;
import com.rid.springjwt.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { User.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "user.name", target = "username")
    TransactionDTO toDto(Transaction transaction);
}
