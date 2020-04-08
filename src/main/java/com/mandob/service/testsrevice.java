package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.Company;
import com.mandob.domain.Customer;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.repository.CompanyRepository;
import com.mandob.repository.CustomerRepository;
import com.mandob.repository.SalesforceRepository;
import com.mandob.repository.ScheduleVisitRepository;
import com.mandob.response.ScheduleVisitReportRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class testsrevice {
    private SalesforceRepository salesforceRepository;
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private ScheduleVisitRepository visitRepository;
//    public void testcode(testReq testReq) {
//        byte[] imageByteArray = Base64.getDecoder().decode(testReq.getImage());
//        FileOutputStream fos = null;
//        try {
//            String fileName = "image" + Product.count + ".jpg";
//            Product.Increase();
//            String fileLocation = new File("src\\main\\resources\\static\\images").getAbsolutePath() + "\\"+fileName;
//            File file = new File(fileLocation);
//            if (file.createNewFile())
//            {
//                System.out.println("File is created!");
//            } else {
//                System.out.println("File already exists.");
//            }
//            fos = new FileOutputStream(file);
//            fos.write(imageByteArray);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                fos.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//
//    }

    public List<Salesforce> getSalesAgentByCompanyId(String companyId) {
        Company company = companyRepository.getOne(companyId);
        if (company == null)
            throw new ApiValidationException("Company Id", "company-id-is0not-vaild");
        List<Salesforce> salesforces = salesforceRepository.findByCompanyAndSalesforceRole(company, SalesforceRole.SALES_AGENT);
        return salesforces;
    }

    public void getVisitReport(String companyId, String salesagentId,
                               String customerId,
                               String salesagentCode,
                               LocalDate from,
                               LocalDate to) {
        Customer customer = null;
        Salesforce salesforce = null;
        List<Salesforce> salesforces = new ArrayList<>();

        if (companyId != null) {
            salesforces = getSalesAgentByCompanyId(companyId);
        } else if (salesagentCode != null) {
            salesforce = salesforceRepository.findByEmployeeCode(salesagentCode);
            if (salesforce == null)
                throw new ApiValidationException("salesforce code", "salesforce-code-is-not-vaild");
            salesforces.add(salesforce);
        } else if (salesagentCode != salesagentId) {
            salesforce = salesforceRepository.getOne(salesagentId);
            if (salesforce == null)
                throw new ApiValidationException("salesforce Id", "salesforce-id-is-not-vaild");
            salesforces.add(salesforce);
        }
        if (customerId != null) {
            customer = customerRepository.getOne(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        }

        //return service.getSalesAgentByCompanyId(companyId);
    }

    private void getVisits(List<Salesforce> salesforces, Customer customer, LocalDate from,
                           LocalDate to) {
        List<ScheduleVisitReportRes> reportRes = new ArrayList<>();
        if (from == null)
            from = LocalDate.of(2018, 1, 1);
        if (to == null)
            to = LocalDate.now();
        for (int i = 0; i < salesforces.size(); i++) {
            if (customer != null) {
                List<ScheduleVisitListProjection> visitListProjections = visitRepository.findAllBySalesforceAndCustomerAndScheduleDateBetween(salesforces.get(i), customer, from.toString(), to.toString());
            }
        }
    }

}
