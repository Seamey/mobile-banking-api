package com.example.Mobile.banking.mapper;

import com.example.Mobile.banking.domain.User;
import com.example.Mobile.banking.feature.user.dto.UserCreateRequest;
import com.example.Mobile.banking.feature.user.dto.UserDetailsResponse;
import com.example.Mobile.banking.feature.user.dto.UserResponse;
import com.example.Mobile.banking.feature.user.dto.UserUpdateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel ="spring")

public interface UserMapper {

    // SourType = UserCreateRequest (Parameter)
    // targetType  = User (return type)

    // Name convension entity base
    //mean User to usercreateRequest
    User fromUserCreateRequest(UserCreateRequest userCreateRequest);
    // mean UserDatilResponse to User
    UserDetailsResponse toUserDetailsResponse(User user);
    // the other style
    // use for partilly update
    //void fromUserCreateRequest2(@MappingTarget User user, UserCreateRequest userCreateRequest)


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUserUpdateRequest(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

}
