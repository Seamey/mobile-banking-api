package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.user.dto.UserCreateRequest;
import co.istad.mbanking.features.user.dto.UserDetailsResponse;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // SourceType = UserCreateRequest (Parameter)
    // TargetType = User (ReturnType)
    // Name convension entity base
    //mean User to usercreateRequest
    User fromUserCreateRequest(UserCreateRequest userCreateRequest);
    // the other style
    // use for partially update
    void fromUserCreateRequest2(@MappingTarget User user, UserCreateRequest userCreateRequest);

    UserDetailsResponse toUserDetailsResponse(User user);

    //@Beanapping : use for ignore some ponit that null if null ignore them
    // map only have value(cuz we using patch so some part can be null it called partially update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUserUpdateRequest(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
}
