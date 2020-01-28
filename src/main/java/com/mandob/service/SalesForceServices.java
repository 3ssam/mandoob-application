package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Role;
import com.mandob.domain.Salesforce;
import com.mandob.domain.SalesforceMovement;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.domain.lookup.City;
import com.mandob.projection.SalesForce.SalesforceListProjection;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.projection.SalesForce.SalesforceProjection;
import com.mandob.repository.SalesforceMovementRepository;
import com.mandob.repository.SalesforceRepository;
import com.mandob.request.MovementReq;
import com.mandob.request.SalesforceReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesForceServices extends MasterService<Salesforce> {
    private final UserService userService;
    private final RoleService roleService;
    private final CityService cityService;
    //private final CustomerService customerService;
    //private final ScheduleVisitService visitService;
    private final SalesforceRepository salesforceRepository;
    private final SalesforceMovementRepository movementRepository;

    @Transactional
    public SalesforceProjection create(SalesforceReq req) {
        validateAssignedTo("", req);
        validateNewEmployeeCode(req.getEmployeeCode(),userService.findById(req.getCurrentUser()).getCompany().getId());
        userService.validateNewUserEmail(req.getEmail());
        Salesforce salesforce = new Salesforce();
        salesforce = createNewSaleForce(req,salesforce);
        salesforce.setCreatedBy(userService.findById(req.getCurrentUser()));
        salesforce.setCreatedAt(Instant.now());
        salesforce.setRole(userService.findById(req.getCurrentUser()).getRole());
        salesforceRepository.save(salesforce);
        userService.createNewUserData(salesforce);
        return findById(salesforce.getId(), SalesforceProjection.class);
    }

    public Salesforce createNewSaleForce(SalesforceReq req,Salesforce salesforce){
        if (req.getAssignedTo() == null)
        salesforce.setAssignedTo(null);
        else
            salesforce.setAssignedTo(salesforceRepository.getOne(req.getAssignedTo()));
        salesforce.setCity(cityService.findById(req.getCity()));
        salesforce.setDetailedAddress(req.getDetailedAddress());
        salesforce.setEmployeeCode(req.getEmployeeCode());
        salesforce.setGovernment(salesforce.getCity().getGovernment());
        //salesforce.setMovements(null);
        salesforce.setActivated(true);
        salesforce.setSuspended(false);
        salesforce.setNationalId(req.getNationalId());
        salesforce.setPhoneNumber(req.getPhoneNumber());
        salesforce.setSalesforceRole(req.getSalesforceRole());
        salesforce.setEmail(req.getEmail());
        salesforce.setPassword(req.getPassword());
        salesforce.setCompany(userService.findById(req.getCurrentUser()).getCompany());
        salesforce.setUpdatedBy(userService.findById(req.getCurrentUser()));
        salesforce.setUpdatedAt(Instant.now());
        salesforce.setArName(req.getArName());
        salesforce.setEnName(req.getEnName());
        return salesforce;
    }

    @Transactional
    public SalesforceProjection update(String id, SalesforceReq req) {
        Salesforce salesforce = findById(id);
        validateAssignedTo(id, req);
        validateUpdatedEmployeeCode(id, req.getEmployeeCode(),userService.findById(req.getCurrentUser()).getCompany().getId());
        userService.validateUserNewEmail(id, req.getEmail());
        salesforce = createNewSaleForce(req,salesforce);
        //salesforceMapper.toEntity(req, salesforce);
        //updateCityAndGovernment(req, salesforce);
        salesforceRepository.save(salesforce);
        return findById(salesforce.getId(), SalesforceProjection.class);
    }

    private void updateCityAndGovernment(SalesforceReq req, Salesforce salesforce) {
        City city = cityService.findById(req.getCity());
        salesforce.setCity(city);
        salesforce.setGovernment(city.getGovernment());
    }

    private void validateNewEmployeeCode(String employeeCode,String companyId) {
        if (StringUtils.isBlank(employeeCode))
            return;
        if (salesforceRepository.existsByEmployeeCodeAndCompanyId(employeeCode, companyId))
            throw new ApiValidationException("employeeCode", "already-exist");
    }

    private void validateUpdatedEmployeeCode(String salesforceId, String employeeCode,String companyId) {
        if (StringUtils.isBlank(employeeCode))
            return;
        if (salesforceRepository.existsByIdNotAndEmployeeCodeAndCompanyId(salesforceId, employeeCode, companyId))
            throw new ApiValidationException("employeeCode", "already-exist");
    }

    private void validateAssignedTo(String currentSalesId, SalesforceReq req) {
        if (ObjectUtils.equals(currentSalesId, req.getAssignedTo())) {
            throw new ApiValidationException("assignedTo", "can-not-be-assigned-to-himself");
        }
        switch (req.getSalesforceRole()) {
            case SALES_MANAGER: {
                req.setAssignedTo(null);
                break;
            }
            case SALES_SUPERVISOR: {
                validateSalesforceToRole(req.getAssignedTo(), SalesforceRole.SALES_MANAGER, "assignedTo");
                break;
            }
            case SALES_AGENT: {
                validateSalesforceToRole(req.getAssignedTo(), SalesforceRole.SALES_SUPERVISOR, "assignedTo");
                break;
            }
            default:
                throw new ApiValidationException("salesforceRole", "invalid-value");
        }
    }


    @Transactional
    public void postMovement(MovementReq req) {
        Salesforce salesforce = findById(req.getCurrentUser());
        SalesforceMovement movement = new SalesforceMovement();
        movement.setSalesforce(salesforce);
        movement.setLatitude(req.getLatitude());
        movement.setLongitude(req.getLongitude());
        movement.setStatus(req.getStatus());
//        if (req.getCustomer() != null){
//            movement.setCustomer(customerService.findById(req.getCustomer()));
//        }

        movementRepository.save(movement);
    }

    @Transactional(readOnly = true)
    public List<SalesforceMovementListProjection> findMovementsBy(String salesforceId) {
        return movementRepository.findAllBySalesforceId(salesforceId);
    }

    void validateSalesforceToRole(String id, SalesforceRole salesforceRole, String fieldName) {
        if (StringUtils.isBlank(id))
            throw new ApiValidationException(fieldName, "must-not-be-blank");
        if (!salesforceRepository.existsByIdAndSalesforceRole(id, salesforceRole))
            throw new ApiValidationException(fieldName, "must-be-" + salesforceRole.toString().toLowerCase());
    }


    public List<SalesforceListProjection> getSalesforceByRole(String roleId){
        Role role = roleService.findById(roleId);
        if (role == null)
        throw new ApiValidationException("Role Id", "not-exist");
        SalesforceRole salesforceRole = SalesforceRole.valueOf(role.getEnName().toUpperCase());
        List<SalesforceListProjection> list = salesforceRepository.findAllBySalesforceRole(salesforceRole);
        return list;
    }

    @Override
    protected BaseRepository<Salesforce> getRepository() {
        return salesforceRepository;
    }

}
