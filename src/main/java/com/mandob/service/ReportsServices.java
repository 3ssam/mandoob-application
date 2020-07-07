package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.*;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.projection.Customer.CustomerListProjection;
import com.mandob.projection.SalesForce.SalesforceListProjection;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.repository.*;
import com.mandob.response.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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


    private static double distance(double lat1,
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
                throw new ApiValidationException("Company Id", "company-id-is-not-vaild");
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
        if (salesforces.isEmpty())
            salesforces = salesforceRepository.findAll();
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
            List<VisitReport> visitsReport = new ArrayList<>();
            if (customer != null) {
                List<ScheduleVisitListProjection> visitList = visitRepository.findAllBySalesforceAndCustomerAndScheduleDateBetween(salesforces.get(i), customer, from.toString(), to.toString());
                for (int j = 0; j < visitList.size(); j++)
                    visitsReport.add(getReport(visitList.get(j), salesforces.get(i), customer, reportType));

            } else {
                List<Customer> customers = salesforces.get(i).getCustomers();
                visitsReport = getVisitsCustomerList(salesforces.get(i), customers, from, to, visitsReport, reportType);
            }
            if (!visitsReport.isEmpty()) {
                ScheduleVisitReport salesforceReport = new ScheduleVisitReport();
                salesforceReport.setSalesforceName(salesforces.get(i).getArName());
                salesforceReport.setSalesforceCode(salesforces.get(i).getEmployeeCode());
                salesforceReport.setSalesforceId(salesforces.get(i).getId());
                salesforceReport.setReportRows(visitsReport);
                salesforceReport.setVisitCount(salesforceReport.getReportRows().size());
                reports.add(salesforceReport);
            }
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
        List<SalesforceMovementListProjection> movements = movementRepository.findBySalesforceAndCustomerAndAndDateTimeBetween(salesforce, customer, start.toString(), end.toString());
        //2020-09-03 16:59:11
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < movements.size(); i++) {
            if ("CHECKIN".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkIn = LocalDateTime.parse(movements.get(i).getDateTime(), formatter);
                visitReport.setCheckInDateTime(checkIn.toString());
            } else if ("CHECKOUT".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkOut = LocalDateTime.parse(movements.get(i).getDateTime(), formatter);
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
        visitReport.setVisitAddress(visit.getAddress());
        LocalDateTime start = LocalDateTime.parse(visit.getScheduleDate()).withHour(0).withMinute(0);
        LocalDateTime end = start.withHour(23).withMinute(59);

        visitReport.setVisitState(visit.getVisitStatus().toString());
        LocalDateTime visitDate = LocalDateTime.parse(visit.getScheduleDate());
        String checkInLong = null;
        String checkInLat = null;
        String checkOutLong = null;
        String checkOutLat = null;

        double distance = -1;
        List<SalesforceMovementListProjection> movements = movementRepository.findBySalesforceAndCustomerAndAndDateTimeBetween(salesforce, customer, start.toString(), end.toString());
        for (int i = 0; i < movements.size(); i++) {
            if ("CHECKIN".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkInLong = movements.get(i).getLongitude();
                checkInLat = movements.get(i).getLatitude();
                visitReport.setCheckInLongitude(checkInLong);
                visitReport.setCheckInLatitude(checkInLat);
                visitReport.setCheckInAddress(movements.get(i).getAddress());
            } else if ("CHECKOUT".equalsIgnoreCase(movements.get(i).getStatus())) {
                checkOutLong = movements.get(i).getLongitude();
                checkOutLat = movements.get(i).getLatitude();
                visitReport.setCheckOutLongitude(checkOutLong);
                visitReport.setCheckOutLongitude(checkOutLat);
                visitReport.setCheckOutAddress(movements.get(i).getAddress());
            }
        }
        if (checkInLong != null && checkInLat != null) {
            distance = distance(Double.parseDouble(checkInLat), Double.parseDouble(visit.getLatitude()), Double.parseDouble(checkInLong), Double.parseDouble(visit.getLongitude()));
            DecimalFormat df = new DecimalFormat("0.00");
            visitReport.setDistance(String.valueOf(df.format(distance)));
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
            from = LocalDateTime.now().minusYears(10L).toInstant(ZoneOffset.UTC);
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
        } else
            customers = customerRepository.findAll();
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

    public MovementReport getMovementsReport(String salesagentId,
                                             String start,
                                             String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (start == null)
            start = LocalDate.now().minusYears(20).format(formatter).toString();
        if (end == null)
            end = LocalDate.now().plusYears(1).format(formatter).toString();
        LocalDate localDate = LocalDate.parse(start, formatter);
        LocalDateTime from = localDate.atTime(0, 0, 0);
        localDate = LocalDate.parse(end, formatter);
        LocalDateTime to = localDate.atTime(23, 59, 59);
        List<SalesforceMovement> movements = new ArrayList<>();
        if (salesagentId != null) {
            Salesforce salesforce = salesforceRepository.getOne(salesagentId);
            if (salesforce == null)
                throw new ApiValidationException("salesforce Id", "salesforce-id-is-not-vaild");
            movements = movementRepository.findBySalesforceIdAndDateTimeBetween(salesagentId, from.toString(), to.toString());
        } else
            movements = movementRepository.findAllByDateTimeBetween(from.toString(), to.toString());
        MovementReport report = new MovementReport();
        report.setTotal(movements.size());
        List<Movement> movementList = new ArrayList<>();
        for (int i = 0; i < movements.size(); i++) {
            Movement movement = new Movement();
            movement.setAddress(movements.get(i).getAddress());
            movement.setLatitude(movements.get(i).getLatitude());
            movement.setLongitude(movements.get(i).getLongitude());
            movement.setMovementTime(movements.get(i).getDateTime());
            movement.setSalesAgentArabicName(movements.get(i).getSalesforce().getArName());
            movement.setSalesAgentEnglishName(movements.get(i).getSalesforce().getEnName());
            movement.setStatus(movements.get(i).getStatus().toString());
            movementList.add(movement);
        }
        report.setMovements(movementList);
        return report;
    }

    public ResponseEntity<Resource> DownloadReport(String companyId,
                                                   String orderId,
                                                   String salesagentId,
                                                   String salesagentCode,
                                                   String customerId,
                                                   String start,
                                                   String end,
                                                   String reportType) {
        String filename = null;
        InputStreamResource file = null;
        if (reportType.equalsIgnoreCase("DISTANCE")) {
            filename = "VisitsDistanceReport.xlsx";
            file = new InputStreamResource(loadTimeOrDistanceSheet(getVisitReport(companyId, salesagentId, customerId, salesagentCode, start, end, reportType), reportType));
        } else if (reportType.equalsIgnoreCase("TIME")) {
            filename = "VisitsTimeReport.xlsx";
            file = new InputStreamResource(loadTimeOrDistanceSheet(getVisitReport(companyId, salesagentId, customerId, salesagentCode, start, end, reportType), reportType));
        } else if (reportType.equalsIgnoreCase("ORDER")) {
            filename = "OrdersReport.xlsx";
            file = new InputStreamResource(loadOrdersSheet(getOrdersReport(orderId, salesagentId, customerId, start, end)));
        } else if (reportType.equalsIgnoreCase("MOVEMENT")) {
            filename = "TrackingReport.xlsx";
            file = new InputStreamResource(loadTrackingSheet(getMovementsReport(salesagentId, start, end)));
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    private ByteArrayInputStream loadTimeOrDistanceSheet(List<ScheduleVisitReport> reports, String type) {
        String[] HEADERs;
        String SHEET;
        boolean distanceCheck = true;
        if (type.equalsIgnoreCase("Distance")) {
            HEADERs = new String[]{"Agent Arabic Name", "Agent English Name",
                    "Customer Arabic Name", "Customer English Name",
                    "Check In Area", "Check In Longitude", "Check In Latitude",
                    "Check Out Area", "Check Out Longitude", "Check Out Latitude",
                    "Visit Area", "Visit Longitude", "Visit Latitude", "Visit Status",
                    "Distance"};
            SHEET = "Distance Report";
        } else {
            HEADERs = new String[]{"Agent Arabic Name", "Agent English Name",
                    "Customer Arabic Name", "Customer English Name",
                    "Check In Date", "Check Out Date",
                    "Visit Status", "Visit Date", "Visit Duration", "Delay"};
            SHEET = "Time Report";
            distanceCheck = false;
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            int rowIndex = 0;
            Row row;
            for (int i = 0; i < reports.size(); i++) {
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue("Agent ID is : " + reports.get(i).getSalesforceId());
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue("Agent Code is " + reports.get(i).getSalesforceCode());
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue("Agent Name is " + reports.get(i).getSalesforceName());
                Row headerRow = sheet.createRow(rowIndex++);

                for (int col = 0; col < HEADERs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(HEADERs[col]);
                }

                for (VisitReport visit : reports.get(i).getReportRows()) {
                    row = sheet.createRow(rowIndex++);
                    if (distanceCheck) {
                        DistanceVisitReport distance = (DistanceVisitReport) visit;
                        row.createCell(0).setCellValue(distance.getSalesAgentArabicName());
                        row.createCell(1).setCellValue(distance.getSalesAgentEnglishName());
                        row.createCell(2).setCellValue(distance.getCustomerArabicName());
                        row.createCell(3).setCellValue(distance.getCustomerEnglishName());
                        row.createCell(4).setCellValue(distance.getCheckInAddress());
                        row.createCell(5).setCellValue(distance.getCheckInLongitude());
                        row.createCell(6).setCellValue(distance.getCheckInLatitude());
                        row.createCell(7).setCellValue(distance.getCheckOutAddress());
                        row.createCell(8).setCellValue(distance.getCheckOutLongitude());
                        row.createCell(9).setCellValue(distance.getCheckOutLatitude());
                        row.createCell(10).setCellValue(distance.getVisitAddress());
                        row.createCell(11).setCellValue(distance.getVisitLongitude());
                        row.createCell(12).setCellValue(distance.getVisitLatitude());
                        row.createCell(13).setCellValue(distance.getVisitState());
                        row.createCell(14).setCellValue(distance.getDistance());
                    } else {
                        TimeVisitReport time = (TimeVisitReport) visit;
                        row.createCell(0).setCellValue(time.getSalesAgentArabicName());
                        row.createCell(1).setCellValue(time.getSalesAgentEnglishName());
                        row.createCell(2).setCellValue(time.getCustomerArabicName());
                        row.createCell(3).setCellValue(time.getCustomerEnglishName());
                        row.createCell(4).setCellValue(time.getCheckInDateTime());
                        row.createCell(5).setCellValue(time.getCheckOutDateTime());
                        row.createCell(6).setCellValue(time.getVisitState());
                        row.createCell(7).setCellValue(time.getScheduleDate());
                        row.createCell(8).setCellValue(time.getDuration());
                        row.createCell(9).setCellValue(time.getDelay());
                    }
                }

                row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue("Number of Visits is " + reports.get(i).getVisitCount());
                rowIndex += 4;
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    private ByteArrayInputStream loadTrackingSheet(MovementReport report) {
        String[] HEADERs = {"Arabic Name", "English Name", "Movement Status", "Movement Time", "Area", "Longitude", "Latitude"};
        String SHEET = "Tracking Report";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int roeIndex = 1;
            for (Movement movement : report.getMovements()) {
                Row row = sheet.createRow(roeIndex++);
                row.createCell(0).setCellValue(movement.getSalesAgentArabicName());
                row.createCell(1).setCellValue(movement.getSalesAgentEnglishName());
                row.createCell(2).setCellValue(movement.getStatus());
                row.createCell(3).setCellValue(movement.getMovementTime());
                row.createCell(4).setCellValue(movement.getAddress());
                row.createCell(5).setCellValue(movement.getLongitude());
                row.createCell(6).setCellValue(movement.getLatitude());
            }
            Row row = sheet.createRow(roeIndex);
            row.createCell(0).setCellValue("Number of Movements is : " + report.getMovements().size());
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    private ByteArrayInputStream loadOrdersSheet(OrderReport report) {
        String[] HEADERs = {"Customer Arabic Name", "Customer English Name",
                "Agent Arabic Name", "Agent English Name",
                "Order Time", "Order Status", "Price "};
        String SHEET = "Orders Report";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int roeIndex = 1;
            for (OrderItem order : report.getOrders()) {
                Row row = sheet.createRow(roeIndex++);
                row.createCell(0).setCellValue(order.getCustomerArabicName());
                row.createCell(1).setCellValue(order.getCustomerEnglishName());
                row.createCell(2).setCellValue(order.getSalesAgentArabicName());
                row.createCell(3).setCellValue(order.getSalesAgentEnglishName());
                row.createCell(4).setCellValue(order.getOrderDate());
                row.createCell(5).setCellValue(order.getStatus());
                row.createCell(6).setCellValue(order.getTotalOfOrder());
            }
            Row row = sheet.createRow(roeIndex++);
            row.createCell(0).setCellValue("Number of Orders is : " + report.getNumberOfOrders());
            row = sheet.createRow(roeIndex);
            row.createCell(0).setCellValue("Total of Orders is : " + report.getTotalOfOrders());
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }


}
