package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserAccount;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.account.dto.AccountUpdateTransferLimitRequest;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.mapper.AccountMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public void createNew(AccountCreateRequest request) {
        //check acc type
        AccountType accountType = accountTypeRepository.findByAlias(request.alias())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid account type"
                        )
                       );
        // check user uuid
        User user = userRepository.findByUuid(request.userUUid()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User has been found!"));
        // map acc dto to acc type
        Account account = accountMapper.fromAccountCreateRequest(request);
                account.setAccountType(accountType);
                account.setActName(user.getName());
                account.setActNo("123456789");
                account.setTransferLimit(BigDecimal.valueOf(5000));
                account.setIsHidden(false);


        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccount.setIsDeleted(false);
        userAccount.setIsBlocked(false);
        userAccount.setCreatedAt(LocalDateTime.now());


        // if you not use cachate you need to save user 1 and save userAccount 1
        userAccountRepository.save(userAccount);

    }

    @Override
    public AccountResponse findByAccountactNo(String actNo) {
        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(()->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Account hasn't been found!"
                        )
                        );

        UserAccount userAccount = userAccountRepository.findByAccount(account)
                .orElseThrow(()->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User account hasn't been found!"
                        )
                );
        User user = new User();
        UserResponse userResponse = new UserResponse(
                user.getUuid(),
                user.getName(),
                user.getProfileImage(),
                user.getGender(),
                user.getDob()

        );
        userAccount.setUser(user);

        return new AccountResponse(
                account.getAlias(),
                userResponse
        );
    }

    @Override
    public AccountResponse renameByActNo(String actNo, AccountRenameRequest renameRequest) {
        // check actN if exists
        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));
        // check old alias and new alias
        if (account.getAlias().equals(renameRequest.newName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "New name must not be the same as old name");
        }
        account.setAlias(renameRequest.newName());
        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }
    @Transactional

    @Override
    public void hideAccount(String actNo) {
        if (!accountRepository.existsByActNo(actNo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Account has not been found");
        }
        try { // handle error form database
            accountRepository.hideAccountByActNo(actNo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong!");
        }
    }

    @Override
    public Page<AccountResponse> findList(int page, int size) {
        // validate page and size
        if(page<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page number must be greater than or equal o");
        }
        if(size<1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page size must be greater than or equal 1");
        }
        Sort sortByActName = Sort.by(Sort.Direction.ASC,"actName");
        PageRequest pageRequest = PageRequest.of(page,size,sortByActName);

        Page<Account> accounts =accountRepository.findAll(pageRequest);

        return accounts.map(accountMapper::toAccountResponse);
    }
    @Override
    public AccountResponse updateTransferLimit(String actNo, AccountUpdateTransferLimitRequest accountUpdateTransferLimitRequest) {
        Account account = accountRepository.findByActNo(actNo).orElseThrow(
                ()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account no is valid"
                )
        );
        account.setTransferLimit(accountUpdateTransferLimitRequest.transferLimit());
        account=  accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

}
