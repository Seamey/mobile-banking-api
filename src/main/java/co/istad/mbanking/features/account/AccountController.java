package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.account.dto.AccountUpdateTransferLimitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@RequestBody AccountCreateRequest request){
        accountService.createNew(request);
    }
    @GetMapping("/{actNo}")
    AccountResponse findByActNo(@PathVariable String actNo){
        return accountService.findByAccountactNo(actNo);
    }

    @PutMapping("/{actNo}/rename")
    AccountResponse renameBYActNo(@PathVariable String actNo,
                                  @Valid @RequestBody AccountRenameRequest request) {
        return accountService.renameByActNo(actNo,request);
    }
    @PutMapping("/{actNo}/hide")
    void hideAccount(@PathVariable String actNo) {
        accountService.hideAccount(actNo);
    }

    @GetMapping
    Page<AccountResponse> findList( @RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "25") int size
    ) {
        return accountService.findList(page, size);
    }

    @PutMapping("/{actNo}/updateLimit")
    AccountResponse updateTransferLimit(@PathVariable String actNo, @RequestBody AccountUpdateTransferLimitRequest request){
        return accountService.updateTransferLimit(actNo,request);
    }

}
