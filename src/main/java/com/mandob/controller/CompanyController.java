package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.domain.Company;
import com.mandob.projection.Company.CompanyListProjection;
import com.mandob.projection.Company.CompanyProjection;
import com.mandob.request.CompanyReq;
import com.mandob.response.ApiPageResponse;
import com.mandob.response.ApiResponse;
import com.mandob.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public List<Company> findAllCompanies(PageRequestVM pr) {
        List<Company> all1 = companyService.findAll();
        Page<CompanyListProjection> all = companyService.findAll(CompanyListProjection.class, pr);
        List<CompanyListProjection> all12 = all.getContent();
        return all1;
    }

    @GetMapping("{companyId}")
    public ApiResponse<CompanyProjection> findCompanyById(@PathVariable String companyId) {
        return ApiResponse.ok(companyService.findById(companyId, CompanyProjection.class));
    }

    @PostMapping
    public ApiResponse<CompanyProjection> createCompany(@Valid @RequestBody CompanyReq req) {
        return ApiResponse.created(companyService.create(req));
    }

    @PutMapping("{companyId}")
    public ApiResponse<CompanyProjection> updateCompany(@PathVariable String companyId, @Valid @RequestBody CompanyReq req) {
        return ApiResponse.updated(companyService.update(req,companyId));
    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String companyId) {
        return companyService.lookup(companyId);
    }
}
