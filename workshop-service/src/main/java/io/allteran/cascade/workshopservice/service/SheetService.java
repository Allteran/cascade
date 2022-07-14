package io.allteran.cascade.workshopservice.service;

import io.allteran.cascade.workshopservice.domain.Order;
import io.allteran.cascade.workshopservice.dto.EmployeeDTO;
import io.allteran.cascade.workshopservice.dto.EmployeeResponse;
import io.allteran.cascade.workshopservice.dto.POSResponse;
import io.allteran.cascade.workshopservice.dto.PointOfSalesDTO;
import io.allteran.cascade.workshopservice.exception.NotFoundException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SheetService {
    private static final String ACCEPTANCE_FILENAME_PREFIX = "ACC_CERTIFICATE_";
    private static final String XLSX_TEMPLATE_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator +"static"+File.separator +"files" + File.separator;
    private static final String ACCEPTANCE_XLSX_TEMPLATE_NAME = "ACCEPTANCE_TEMPLATE.xlsx";
    private static final String REPAIR_XLSX_TEMPLATE_NAME = "REPAIR_TEMPLATE.xlsx";
    private static final String REPAIR_FILENAME_PREFIX = "REPAIR_CERTIFICATE_";

    @Value("${uri.manage-service.employee}")
    private String URI_EMPLOYEE;
    @Value("${uri.manage-service.pos}")
    private String URI_POS;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public SheetService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public ByteArrayInputStream generateAcceptanceCertificate(Order order) throws IOException {
        EmployeeResponse authorResponse = webClientBuilder.build().get()
                .uri(URI_EMPLOYEE + order.getAuthorId())
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();
        POSResponse posResponse = webClientBuilder.build().get()
                .uri(URI_POS + order.getPosId())
                .retrieve()
                .bodyToMono(POSResponse.class)
                .block();

        if(authorResponse == null || authorResponse.getEmployeeDTOList().isEmpty()) {
            throw new NotFoundException("User with id [" + order.getAuthorId() + "] not found in database");
        }
        if(posResponse == null || posResponse.getPosList().isEmpty()) {
            throw new NotFoundException("POS with id [" + order.getPosId() + "] not found in database");
        }
        EmployeeDTO author = authorResponse.getEmployeeDTOList().get(0);
        PointOfSalesDTO pos = posResponse.getPosList().get(0);

        File currentDir = new File("workshop-service");
        String path = currentDir.getAbsolutePath() + "/" + XLSX_TEMPLATE_DIR + ACCEPTANCE_XLSX_TEMPLATE_NAME;
        FileInputStream file = null;
        Workbook workbook = null;
        file = new FileInputStream(path);
        workbook = new XSSFWorkbook(file);

        Sheet mainSheet = workbook.getSheetAt(0);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if(order.getCreationDate() == null) {
            order.setCreationDate(LocalDate.now());
        }

        mainSheet.getRow(6).getCell(8).setCellValue(dateFormatter.format(order.getCreationDate()));
        mainSheet.getRow(36).getCell(8).setCellValue(dateFormatter.format(order.getCreationDate()));

        //set POS params to cells
        mainSheet.getRow(2).getCell(1).setCellValue(pos.getCity() + ", " + pos.getAddress());
        mainSheet.getRow(32).getCell(1).setCellValue(pos.getCity() + ", " + pos.getAddress());

        //set author to cell
        mainSheet.getRow(4).getCell(1).setCellValue(author.getFirstName() + " " + author.getLastName());
        mainSheet.getRow(34).getCell(1).setCellValue(author.getFirstName() + " " + author.getLastName());

        //set customer's name to cell
        mainSheet.getRow(7).getCell(2).setCellValue(order.getCustomerName());
        mainSheet.getRow(37).getCell(2).setCellValue(order.getCustomerName());

        //set customer's phone
        mainSheet.getRow(8).getCell(2).setCellValue(order.getCustomerPhone());
        mainSheet.getRow(38).getCell(2).setCellValue(order.getCustomerPhone());

        //set device name and type
        mainSheet.getRow(9).getCell(2).setCellValue(order.getDeviceType().getName() + " " + order.getDeviceName());
        mainSheet.getRow(39).getCell(2).setCellValue(order.getDeviceType().getName() + " " + order.getDeviceName());

        //set SN
        mainSheet.getRow(10).getCell(2).setCellValue(order.getSerialNumber());
        mainSheet.getRow(40).getCell(2).setCellValue(order.getSerialNumber());

        //set defect
        mainSheet.getRow(11).getCell(2).setCellValue(order.getDefect());
        mainSheet.getRow(41).getCell(2).setCellValue(order.getDefect());

        //set equip set
        mainSheet.getRow(12).getCell(2).setCellValue(order.getEquipSet());
        mainSheet.getRow(42).getCell(2).setCellValue(order.getEquipSet());

        //set appearance
        mainSheet.getRow(13).getCell(2).setCellValue(order.getAppearance());
        mainSheet.getRow(43).getCell(2).setCellValue(order.getAppearance());

        //set preliminary price
        mainSheet.getRow(14).getCell(2).setCellValue(order.getPreliminaryPrice());
        mainSheet.getRow(44).getCell(2).setCellValue(order.getPreliminaryPrice());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ByteArrayInputStream generateRepairCertificate(Order order) throws IOException {
        EmployeeResponse authorResponse = webClientBuilder.build().get()
                .uri(URI_EMPLOYEE + order.getAuthorId())
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();
        POSResponse posResponse = webClientBuilder.build().get()
                .uri(URI_POS + order.getPosId())
                .retrieve()
                .bodyToMono(POSResponse.class)
                .block();

        if(authorResponse == null || authorResponse.getEmployeeDTOList().isEmpty()) {
            throw new NotFoundException("User with id [" + order.getAuthorId() + "] not found in database");
        }
        if(posResponse == null || posResponse.getPosList().isEmpty()) {
            throw new NotFoundException("POS with id [" + order.getPosId() + "] not found in database");
        }
        EmployeeDTO author = authorResponse.getEmployeeDTOList().get(0);
        PointOfSalesDTO pos = posResponse.getPosList().get(0);

        File currentDir = new File("workshop-service");
        String path = currentDir.getAbsolutePath() + "/" + XLSX_TEMPLATE_DIR + REPAIR_XLSX_TEMPLATE_NAME;
        FileInputStream file = null;
        Workbook workbook = null;
        file = new FileInputStream(path);
        workbook = new XSSFWorkbook(file);

        Sheet mainSheet = workbook.getSheetAt(0);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if(order.getIssueDate() == null) {
            order.setIssueDate(LocalDate.now());
        }

        //POS data
        mainSheet.getRow(2).getCell(1).setCellValue("г. " + pos.getCity() + ", " + pos.getAddress());
        mainSheet.getRow(36).getCell(1).setCellValue("г. " + pos.getCity() + ", " + pos.getAddress());

        //header
        mainSheet.getRow(7).getCell(8).setCellValue(dateFormatter.format(order.getIssueDate()));
        mainSheet.getRow(41).getCell(8).setCellValue(dateFormatter.format(order.getIssueDate()));

        //customer name
        mainSheet.getRow(8).getCell(2).setCellValue(order.getCustomerName());
        mainSheet.getRow(42).getCell(2).setCellValue(order.getCustomerName());

        //device
        mainSheet.getRow(9).getCell(2).setCellValue(order.getDeviceName());
        mainSheet.getRow(43).getCell(2).setCellValue(order.getDeviceName());

        //SN
        mainSheet.getRow(10).getCell(2).setCellValue(order.getSerialNumber());
        mainSheet.getRow(44).getCell(2).setCellValue(order.getSerialNumber());

        //defect
        mainSheet.getRow(11).getCell(2).setCellValue(order.getDefect());
        mainSheet.getRow(45).getCell(2).setCellValue(order.getDefect());

        //warranty
        mainSheet.getRow(13).getCell(2).setCellValue(order.getWarranty());
        mainSheet.getRow(47).getCell(2).setCellValue(order.getWarranty());

        //performed actions
        mainSheet.getRow(16).getCell(1).setCellValue(order.getPerformedActions());
        mainSheet.getRow(50).getCell(1).setCellValue(order.getPerformedActions());

        //total price
        mainSheet.getRow(16).getCell(9).setCellValue(order.getTotalPrice());
        mainSheet.getRow(18).getCell(9).setCellValue(order.getTotalPrice());
        mainSheet.getRow(50).getCell(9).setCellValue(order.getTotalPrice());
        mainSheet.getRow(52).getCell(9).setCellValue(order.getTotalPrice());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
