package com.example.Mobile.banking.feature.account;

import com.example.Mobile.banking.domain.Account;
import com.example.Mobile.banking.feature.account.dto.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {
    List<AccountTypeResponse> findList();

    AccountTypeResponse findByAlias(String alias);


}
