package com.mandob.controller;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.User;
import com.mandob.request.AuthReq;
import com.mandob.response.AuthRes;
import com.mandob.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("authenticate")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping
    public AuthRes authenticate(@Valid @RequestBody AuthReq req) {
        User user = userService.validateEmail(req.getUsername());
        if (!user.getPassword().equals(req.getPassword()))
            throw new ApiValidationException("password", "is-not-correct");
        return new AuthRes(user.getId());
    }
}
