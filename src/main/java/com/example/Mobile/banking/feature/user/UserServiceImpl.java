package com.example.Mobile.banking.feature.user;

import com.example.Mobile.banking.domain.User;
import com.example.Mobile.banking.feature.user.dto.UserCreateRequest;
import com.example.Mobile.banking.feature.user.dto.UserDetailsResponse;
import com.example.Mobile.banking.feature.user.dto.UserResponse;
import com.example.Mobile.banking.feature.user.dto.UserUpdateRequest;
import com.example.Mobile.banking.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserMapper userMapper;
//    @Override
//    public void createNew(UserCreateRequest userCreateRequest) {
//
//        if(userRepository.existsByPhoneNumber(userCreateRequest.phoneNumber()))
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "PhoneNumber doesn't exits"
//            );
//        if (userRepository.existsByStudentIdCard(userCreateRequest.studentIdCard()))
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Student card Id doesn't exists"
//            );
//        if (userRepository.existsByNationalCardId(userCreateRequest.nationalCardId()))
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Student card Id doesn't exists"
//            );
//        if(!userCreateRequest.password().equals(userCreateRequest.confirmedPassword())){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Password doesn't match");
//        }
//        User user = userMapper.fromUserCreateRequest(userCreateRequest);
//        user.setProfileImage("avatar.png");
//        user.setUuid(UUID.randomUUID().toString());
//        user.setCreateAt(LocalDate.now());
//        user.setIsDeleted(false);
//        user.setIsBlocked(false);
//
//        List<Roles> rolesList = new ArrayList<>();
//        Roles userRoles = rolesRepository.findByName("USER")
//                        .orElseThrow(()-> new ResponseStatusException(
//                                HttpStatus.NOT_FOUND,
//                                        "Roles USERS has not been found")
//                                );
//        rolesList.add(userRoles);
//        user.setRoles(rolesList);
//
//
//        // use for dynamic roles,
////        Roles newUserRoles = new Roles();
////        newUserRoles.setName("USERS");
////        rolesList.add(newUserRoles);
//     //   user.setRoles(rolesList);
//
//        userRepository.save(user);
//        // save is auto management transaction :  if success commit , if fail is rollback
//
//    }

    @Override
    public void createNew(UserCreateRequest userCreateRequest) {

        if (userRepository.existsByPhoneNumber(userCreateRequest.phoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number has already been existed!"
            );
        }

        if (userRepository.existsByNationalCardId(userCreateRequest.nationalCardId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "National card ID has already been existed!"
            );
        }

        if (userRepository.existsByStudentIdCard(userCreateRequest.studentIdCard())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Student card ID has already been existed!"
            );
        }

        if (!userCreateRequest.password()
                .equals(userCreateRequest.confirmedPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password doesn't match!"
            );
        }

        // DTO pattern (mapstruct ft. lombok)
        User user = userMapper.fromUserCreateRequest(userCreateRequest);
        user.setUuid(UUID.randomUUID().toString());
        user.setProfileImage("avatar.png");
        user.setCreateAt(LocalDate.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);

        // Assign default user role
        List<Role> roles = new ArrayList<>();
        Role userRole = rolesRepository.findByName("USER")
                .orElseThrow(()->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,"Role USER has not found!"
                        ));

        // Create dynamic role from client  // do it by manualy
        userCreateRequest.roles().forEach(r -> {
            Role newRole = rolesRepository.findByName(r.name())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Role USER has not been found!"));
            roles.add(newRole);
        });

        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public UserResponse updateByUuid(String uuid, UserUpdateRequest userUpdateRequest) {

        // check uuid if exists
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        log.info("before user: {}", user);

        userMapper.fromUserUpdateRequest(userUpdateRequest, user);


        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> findList(int page, int limit) {
        // Create pageRequest object
        PageRequest pageRequest = PageRequest.of(page, limit);
        // Invoke findAll(pageRequest)
        Page<User> users = userRepository.findAll(pageRequest);
        // Map result of pagination to response
        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserDetailsResponse findByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        return userMapper.toUserDetailsResponse(user);
    }

    @Transactional
    @Override
    public BasedMessage blockByUuid(String uuid) {

        if (!userRepository.existsByUuid(uuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User has not been found!");
        }

        userRepository.blockByUuid(uuid);

        return new BasedMessage("User has been blocked");
    }

    @Override
    public void deleteByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));
        userRepository.delete(user);
    }


}
