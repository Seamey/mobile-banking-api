package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserAccount;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

// map stract use to like

@Mapper(componentModel = "spring", uses = {
        UserMapper.class,
        AccountTypeMapper.class
})
public interface AccountMapper {
    Account fromAccountCreateRequest(AccountCreateRequest accountCreateRequest);

    // use for make sure for Mapping know userAccountList(cuz change name) target
    // Noted make sure the name is the SAME the properties
//   @Mapping(source = "userAccountList", target ="user",qualifiedByName = "mapUserResponse")
   AccountResponse toAccountResponse(Account account);


//
//    // use create abstract  method  do by map abstract
//    @Named("mapUserResponse")
////  custom for get data in user from UserAccount ( cuz user and acc has relationship)
//    default UserResponse mapUserResponse(List<UserAccount> userAccountList){ // parameter is the source get form accountModel so need to has feild (datatype and name is the same)the same model
//        return toUserResponse(userAccountList.get(0).getUser());
//            // get(0) cuz get single row's user cuz all object of user is the same
//    }
    // write the abstarct again
//    UserResponse toUserResponse(User user);



    /// if you want to use mapper del mean hz you need to move source to source target to target
    // like move User to UserMapper , Accout to AccoutMapper
}
