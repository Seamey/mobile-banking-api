package com.example.Mobile.banking.mapper;

import com.example.Mobile.banking.domain.AccountType;
import com.example.Mobile.banking.feature.account.dto.AccountTypeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel ="spring")
public interface AccountTypeMapper {
    AccountTypeResponse toAccountTypeResponse(AccountType accountType);

    List<AccountTypeResponse> toAccountTypeResponseList(List<AccountType> accountTypes);

}
