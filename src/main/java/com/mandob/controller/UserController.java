package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.domain.User;
import com.mandob.projection.User.UserListProjection;
import com.mandob.projection.User.UserProfileProjection;
import com.mandob.projection.User.UserProjection;
import com.mandob.request.UserReq;
import com.mandob.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    @PostMapping
    public ApiResponse<UserProjection> createUser(@Valid @RequestBody UserReq userReq){
        return ApiResponse.created(userService.create(userReq));
    }

    @GetMapping
    public ApiPageResponse<UserListProjection> findAllUsers(PageRequestVM pr) {
        return ApiPageResponse.of(userService.findAll(UserListProjection.class, pr));
    }

    @GetMapping("me")
    public ApiResponse<UserProjection> findCurrentUserDetails(String userId) {
        return ApiResponse.ok(userService.findById(userId, UserProjection.class));
    }

    @GetMapping("{userId}")
    public ApiResponse<UserProjection> findUserById(@PathVariable String userId) {
        return ApiResponse.ok(userService.findById(userId, UserProjection.class));
    }

    @GetMapping("profile")
    public ApiResponse<UserProfileProjection> findCurrentUserProfile(String userId) {
        return ApiResponse.ok(userService.findById(userId, UserProfileProjection.class));
    }

//    @PutMapping("profile")
//    public ApiResponse<UserProfileProjection> updateCurrentUserProfile(@Valid @RequestBody UpdateUserProfileReq req) {
//        return ApiResponse.updated(userService.updateUserProfile(CurrentUser.getId(), req));
//    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String userId) {
        return userService.lookup(userId);
    }
}
