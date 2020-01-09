package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Company;
import com.mandob.projection.Company.CompanyProjection;
import com.mandob.repository.CompanyRepository;
import com.mandob.repository.UserRepository;
import com.mandob.request.CompanyReq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService extends MasterService<Company> {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    @Transactional
    public CompanyProjection create(CompanyReq req){
        if (companyRepository.existsByArName(req.getArName()))
            throw new ApiValidationException("arName", "already-exist");
        if (companyRepository.existsByEnName(req.getEnName()))
            throw new ApiValidationException("enName", "already-exist");
        Company company = new Company();
        company.setCreatedAt(Instant.now());
        company.setCreatedBy(userRepository.getOne(req.getCurrentUser()));
        company = createObject(req,company);
        companyRepository.save(company);
        return findById(company.getId(), CompanyProjection.class);
    }

    @Transactional
    public CompanyProjection update(CompanyReq req, String id){
        Company company = findById(id);
        if (companyRepository.existsByArNameAndIdNot(req.getArName(), id))
            throw new ApiValidationException("arName", "already-exist");
        if (companyRepository.existsByEnNameAndIdNot(req.getEnName(), id))
            throw new ApiValidationException("enName", "already-exist");
        company = createObject(req,company);
        companyRepository.save(company);
        return findById(company.getId(), CompanyProjection.class);
    }

    private Company createObject(CompanyReq req,Company company){
        company.setSuspended(req.getSuspended());
        company.setArName(req.getArName());
        company.setEnName(req.getEnName());
        company.setUpdatedBy(userRepository.getOne(req.getCurrentUser()));
        company.setUpdatedAt(Instant.now());
        return company;
    }

    @Override
    protected BaseRepository<Company> getRepository() {
        return companyRepository;
    }


}
