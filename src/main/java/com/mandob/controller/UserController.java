package com.mandob.controller;

import com.mandob.domain.User;
import com.mandob.projection.User.UserListProjection;
import com.mandob.request.UserReq;
import com.mandob.response.ApiPageResponse;
import com.mandob.response.ApiResponse;
import com.mandob.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody UserReq userReq){
        return ApiResponse.created(userService.create(userReq));
    }



    @GetMapping
    public ApiPageResponse<UserListProjection> findAllUsers(String Page) {
        //return ApiPageResponse.of(userService.findAll(UserListProjection.class, pr));
        return null;
    }

//    @GetMapping
//    public ApiResponse<UserProjection> findCurrentUserDetails() {
//        //return ApiResponse.ok(userService.findById(CurrentUser.getId(), UserProjection.class));
//        return null;
//    }

//    @GetMapping("{userId}")
//    @PreAuthorize("hasAuthority('USER_READ')")
//    public ApiResponse<UserProjection> findUserById(@PathVariable String userId) {
//        return ApiResponse.ok(userService.findById(userId, UserProjection.class));
//    }
//
//    @GetMapping("profile")
//    public ApiResponse<UserProfileProjection> findCurrentUserProfile() {
//        return ApiResponse.ok(userService.findById(CurrentUser.getId(), UserProfileProjection.class));
//    }
//
//    @PutMapping()
//    public ApiResponse<UserProfileProjection> updateCurrentUserProfile(@Valid @RequestBody UpdateUserProfileReq req) {
//        return ApiResponse.updated(userService.updateUserProfile(CurrentUser.getId(), req));
//    }
}
