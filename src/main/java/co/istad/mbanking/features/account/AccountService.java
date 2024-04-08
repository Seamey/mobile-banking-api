package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.account.dto.AccountUpdateTransferLimitRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    void createNew(AccountCreateRequest request);
    AccountResponse findByAccountactNo(String actNo);

    AccountResponse renameByActNo(String actNo, AccountRenameRequest renameRequest);
    void hideAccount(String actNo);
    Page<AccountResponse> findList(int page, int size);
    AccountResponse updateTransferLimit(String actNo, AccountUpdateTransferLimitRequest accountUpdateTransferLimitRequest);

}
