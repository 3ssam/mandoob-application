package com.mandob.controller;

import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Role.RoleListProjection;
import com.mandob.projection.Role.RoleProjection;
import com.mandob.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ApiPageResponse<RoleListProjection> findall(PageRequestVM pr) {
        return ApiPageResponse.of(roleService.findAll(RoleListProjection.class, pr));
    }

    @GetMapping("{roleId}")
    public ApiResponse<RoleProjection> findRoleById(@PathVariable String roleId) {
        return ApiResponse.ok(roleService.getRoleById(roleId));
    }

}
