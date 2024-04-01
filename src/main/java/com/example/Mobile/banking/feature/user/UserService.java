package com.example.Mobile.banking.feature.user;

import com.example.Mobile.banking.feature.user.dto.UserCreateRequest;
import com.example.Mobile.banking.feature.user.dto.UserDetailResponse;
import com.example.Mobile.banking.feature.user.dto.UserDetailsResponse;
import com.example.Mobile.banking.feature.user.dto.UserResponse;
import com.example.Mobile.banking.feature.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void createNew(UserCreateRequest userCreateRequest);

    UserResponse updateByUuid(String uuid, UserUpdateRequest userUpdateRequest);

    Page<UserResponse> findList(int page, int limit);

    UserDetailsResponse findByUuid(String uuid);

    BasedMessage blockByUuid(String uuid);

    void deleteByUuid(String uuid);

}
