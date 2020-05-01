package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.Company;
import com.mandob.domain.Customer;
import com.mandob.domain.Order;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.projection.Customer.CustomerListProjection;
import com.mandob.projection.SalesForce.SalesforceListProjection;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.repository.*;
import com.mandob.response.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportsServices {
    private SalesforceRepository salesforceRepository;
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private ScheduleVisitRepository visitRepository;
    private SalesforceMovementRepository movementRepository;
    private OrderRepository orderRepository;

    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    public List<SalesforceListProjection> getSalesAgentByCompanyId(String companyId) {
        Company company = companyRepository.getOne(companyId);
        if (company == null)
            throw new ApiValidationException("Company Id", "company-id-is0not-vaild");
        List<SalesforceListProjection> salesforces = salesforceRepository.findAllByCompanyAndSalesforceRole(company, SalesforceRole.SALES_AGENT);
        return salesforces;
    }

    public List<ScheduleVisitReport> getVisitReport(String companyId, String salesagentId,
                                                    String customerId,
                                                    String salesagentCode,
                                                    String start,
                                                    String end,
                                                    String reportType) {
        LocalDate from = null;
        LocalDate to = null;
        Customer customer = null;
        Salesforce salesforce = null;
        List<Salesforce> salesforces = new ArrayList<>();
        if (start != null)
            from = LocalDate.parse(start);
        if (end != null)
            to = LocalDate.parse(end).plusDays(1);
        if (companyId != null) {
            Company company = companyRepository.getOne(companyId);
            if (company == null)
                throw new ApiValidationException("Company Id", "company-id-is0not-vaild");
            salesforces = salesforceRepository.findByCompanyAndSalesforceRole(company, SalesforceRole.SALES_AGENT);
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
        return getVisits(salesforces, customer, from, to, reportType);
        //return service.getSalesAgentByCompanyId(companyId);
    }

    private List<ScheduleVisitReport> getVisits(List<Salesforce> salesforces, Customer customer, LocalDate from,
                                                LocalDate to,
                                                String reportType) {
        List<ScheduleVisitReport> reports = new ArrayList<>();
        if (from == null)
            from = LocalDate.of(2018, 1, 1);
        if (to == null)
            //to = LocalDate.now();
            to = LocalDate.of(2030, 1, 1);
        for (int i = 0; i < salesforces.size(); i++) {
            ScheduleVisitReport salesforceReport = new ScheduleVisitReport();
            salesforceReport.setSalesforceName(salesforces.get(i).getArName());
            salesforceReport.setSalesforceCode(salesforces.get(i).getEmployeeCode());
            salesforceReport.setSalesforceId(salesforces.get(i).getId());
            List<VisitReport> visitsReport = new ArrayList<>();
            if (customer != null) {
                List<ScheduleVisitListProjection> visitList = visitRepository.findAllBySalesforceAndCustomerAndScheduleDateBetween(salesforces.get(i), customer, from.toString(), to.toString());
                for (int j = 0; j < visitList.size(); j++)
                    visitsReport.add(getReport(visitList.get(j), salesforces.get(i), customer, reportType));

            } else {
                List<Customer> customers = salesforces.get(i).getCustomers();
                visitsReport = getVisitsCustomerList(salesforces.get(i), customers, from, to, visitsReport, reportType);
            }
            salesforceReport.setReportRows(visitsReport);
            reports.add(salesforceReport);
        }
        return reports;
    }

    private List<VisitReport> getVisitsCustomerList(Salesforce salesforce, List<Customer> customers, LocalDate from,
                                                    LocalDate to, List<VisitReport> visitsReport,
                                                    String reportType) {
        for (int i = 0; i < customers.size(); i++) {
            List<ScheduleVisitListProjection> visitList = visitRepository.findAllBySalesforceAndCustomerAndScheduleDateBetween(salesforce, customers.get(i), from.toString(), to.toString());
            for (int j = 0; j < visitList.size(); j++) {
                visitsReport.add(getReport(visitList.get(j), salesforce, customers.get(i), reportType));
            }
        }
        return visitsReport;
    }

    private VisitReport getReport(ScheduleVisitListProjection visit, Salesforce salesforce, Customer customer, String reportType) {
        if ("TIME".equalsIgnoreCase(reportType))
            return getVisitTime(visit, salesforce, customer);
        else if ("DISTANCE".equalsIgnoreCase(reportType))
            return getVisitDistance(visit, salesforce, customer);
        return null;
    }

    private TimeVisitReport getVisitTime(ScheduleVisitListProjection visit, Salesforce salesforce, Customer customer) {
        TimeVisitReport visitReport = new TimeVisitReport();
        visitReport.setCustomerArabicName(customer.getArName());
        visitReport.setCustomerEnglishName(customer.getEnName());
        visitReport.setEmployeeCode(salesforce.getEmployeeCode());
        visitReport.setSalesAgentArabicName(salesforce.getArName());
        visitReport.setSalesAgentEnglishName(salesforce.getEnName());
        visitReport.setScheduleDate(visit.getScheduleDate());
        LocalDateTime start = LocalDateTime.parse(visit.getScheduleDate()).withHour(0).withMinute(0);
        LocalDateTime end = start.withHour(23).withMinute(59);

        visitReport.setVisitState(visit.getVisitStatus().toString());
        LocalDateTime visitDate = LocalDateTime.parse(visit.getScheduleDate());
        LocalDateTime checkIn = null;
        LocalDateTime checkOut = null;
        int duration = -1;
        int delay = -1;
        List<SalesforceMovementListProjection> movements = movementRepository.findBySalesforceAndCustomerAndAndDateTimeBetween(salesforce, customer, start, end);
        for (int i = 0; i < movements.size(); i++) {
            if ("CHECKIN".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkIn = movements.get(i).getDateTime();
                visitReport.setCheckInDateTime(checkIn.toString());
            } else if ("CHECKOUT".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkOut = movements.get(i).getDateTime();
                visitReport.setCheckOutDateTime(checkOut.toString());
            }
        }
        if (checkIn != null && checkOut != null) {
            duration = ScheduleVisitService.getDurationBetweenDate(checkIn, checkOut);
            delay = ScheduleVisitService.getDurationBetweenDate(checkIn, visitDate);
            visitReport.setDelay(String.valueOf(delay));
            visitReport.setDuration(String.valueOf(duration));
        }

        return visitReport;
    }

    private DistanceVisitReport getVisitDistance(ScheduleVisitListProjection visit, Salesforce salesforce, Customer customer) {
        DistanceVisitReport visitReport = new DistanceVisitReport();
        visitReport.setCustomerArabicName(customer.getArName());
        visitReport.setCustomerEnglishName(customer.getEnName());
        visitReport.setEmployeeCode(salesforce.getEmployeeCode());
        visitReport.setSalesAgentArabicName(salesforce.getArName());
        visitReport.setSalesAgentEnglishName(salesforce.getEnName());
        visitReport.setVisitLatitude(visit.getLatitude());
        visitReport.setVisitLongitude(visit.getLongitude());
        LocalDateTime start = LocalDateTime.parse(visit.getScheduleDate()).withHour(0).withMinute(0);
        LocalDateTime end = start.withHour(23).withMinute(59);

        visitReport.setVisitState(visit.getVisitStatus().toString());
        LocalDateTime visitDate = LocalDateTime.parse(visit.getScheduleDate());
        String checkInLong = null;
        String checkInLat = null;
        String checkOutLong = null;
        String checkOutLat = null;

        double distance = -1;
        List<SalesforceMovementListProjection> movements = movementRepository.findBySalesforceAndCustomerAndAndDateTimeBetween(salesforce, customer, start, end);
        for (int i = 0; i < movements.size(); i++) {
            if ("CHECKIN".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkInLong = movements.get(i).getLongitude();
                checkInLat = movements.get(i).getLatitude();
                visitReport.setCheckInLongitude(checkInLong);
                visitReport.setCheckInLatitude(checkInLat);
            } else if ("CHECKOUT".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkOutLong = movements.get(i).getLongitude();
                checkOutLat = movements.get(i).getLatitude();
                visitReport.setCheckOutLongitude(checkOutLong);
                visitReport.setCheckOutLongitude(checkOutLat);
            }
        }
        if (checkInLong != null && checkInLat != null) {
            distance = distance(Double.parseDouble(checkInLat), Double.parseDouble(visit.getLatitude()), Double.parseDouble(checkInLong), Double.parseDouble(visit.getLongitude()));
            visitReport.setDistance(String.valueOf(distance));
        }

        return visitReport;
    }

    public List<CustomerListProjection> getCustomersOfSalesforce(String salesforceId) {
        Salesforce salesforce = salesforceRepository.findById(salesforceId).get();
        List<Customer> salesForceCustomers = salesforce.getCustomers();
        List<String> customer_ids = new ArrayList<>();
        List<CustomerListProjection> customers = new ArrayList<>();
        for (Customer customer : salesForceCustomers) {
//            customer_ids.add(customer.getId());
            customers.add(customerRepository.findAllById(customer.getId()));
        }
//        List<CustomerListProjection> list = customerRepository.findAllById(customer_ids);
        return customers;
    }

    public OrderReport getOrdersReport(String orderId, String salesagentId,
                                       String customerId,
                                       String start,
                                       String end) {

        if (orderId != null)
            return getOneReport(orderId);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //LocalDateTime time = LocalDateTime.parse(start,format);
        Instant from = null;
        Instant to = null;
        Salesforce salesforce = null;
        List<Customer> customers = new ArrayList<>();
        if (start != null)
            from = LocalDateTime.parse(start, format).minusHours(2).toInstant(ZoneOffset.UTC);
        else
            from = LocalDateTime.now().minusYears(1L).toInstant(ZoneOffset.UTC);
        if (end != null)
            to = LocalDateTime.parse(end, format).minusHours(2).toInstant(ZoneOffset.UTC);
        else
            to = LocalDateTime.now().plusYears(20L).toInstant(ZoneOffset.UTC);
        if (salesagentId != null) {
            salesforce = salesforceRepository.getOne(salesagentId);
            if (salesforce == null)
                throw new ApiValidationException("salesforce Id", "salesforce-id-is-not-vaild");
            customers = salesforce.getCustomers();
        } else if (customerId != null) {
            Customer customer = customerRepository.getOne(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
            customers.add(customer);
        }
        return createOrderReport(customers, from, to);
    }

    private OrderReport createOrderReport(List<Customer> customers, Instant from, Instant to) {
        OrderReport report = new OrderReport();
        List<OrderItem> orderItems = new ArrayList<>();

        double sumOrders = 0.0;
        int countOrders = 0;
        for (int i = 0; i < customers.size(); i++) {
            OrderReport tempReport = getOrderDetails(customers.get(i), from, to);
            orderItems.addAll(tempReport.getOrders());
            sumOrders += tempReport.getTotalOfOrders();
            countOrders += tempReport.getNumberOfOrders();
        }
        report.setNumberOfOrders(countOrders);
        report.setTotalOfOrders(sumOrders);
        report.setOrders(orderItems);
        return report;
    }

    private OrderReport getOrderDetails(Customer customer, Instant from, Instant to) {
        List<OrderItem> orderItems = new ArrayList<>();
        double sumOrders = 0.0;
        int countOrders = 0;
        List<Order> orders = orderRepository.findByCustomerAndCreatedAtBetween(customer, from, to);
        for (int i = 0; i < orders.size(); i++) {
            OrderItem orderItem = new OrderItem();
            sumOrders += orders.get(i).getTotalOrder();
            countOrders++;
            orderItem.setTotalOfOrder(String.valueOf(orders.get(i).getTotalOrder()));
            orderItem.setOrderDate(orders.get(i).getCreatedAt().plus(Duration.ofHours(2)).toString());
            orderItem.setOrderId(orders.get(i).getId());
            orderItem.setCustomerArabicName(customer.getArName());
            orderItem.setCustomerEnglishName(customer.getEnName());
            orderItem.setStatus(orders.get(i).getStatus().toString());
            orderItem.setSalesAgentArabicName(customer.getSalesforce().getArName());
            orderItem.setSalesAgentEnglishName(customer.getSalesforce().getEnName());
            orderItems.add(orderItem);
        }
        OrderReport report = new OrderReport();
        report.setNumberOfOrders(countOrders);
        report.setTotalOfOrders(sumOrders);
        report.setOrders(orderItems);
        return report;
    }

    private OrderReport getOneReport(String orderId) {
        Order order = orderRepository.findById(orderId).get();
        OrderReport report = new OrderReport();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setTotalOfOrder(String.valueOf(order.getTotalOrder()));
        orderItem.setOrderDate(order.getCreatedAt().plus(Duration.ofHours(2)).toString());
        orderItem.setOrderId(order.getId());
        orderItem.setCustomerArabicName(order.getCustomer().getArName());
        orderItem.setCustomerEnglishName(order.getCustomer().getEnName());
        orderItem.setStatus(order.getStatus().toString());
        orderItem.setSalesAgentArabicName(order.getCustomer().getSalesforce().getArName());
        orderItem.setSalesAgentEnglishName(order.getCustomer().getSalesforce().getEnName());
        orderItems.add(orderItem);
        report.setNumberOfOrders(1);
        report.setTotalOfOrders(order.getTotalOrder());
        report.setOrders(orderItems);
        return report;
    }

}
