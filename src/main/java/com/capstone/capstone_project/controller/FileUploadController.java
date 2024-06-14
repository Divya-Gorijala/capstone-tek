package com.capstone.capstone_project.controller;


import com.capstone.capstone_project.service.ExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@RestController
public class FileUploadController {
//    private final S3Service s3Service;
    private final ExcelService excelService;

    @GetMapping
    public String welcome() {
        return "Welcome to Spring boot World!";
    }


//    @Autowired
//    public FileUploadController(S3Service s3Service,ExcelService excelService) {
//        this.s3Service = s3Service;
//        this.excelService= excelService;
//    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            s3Service.uploadFile(file);
//            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        @PostMapping("/upload")
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload an Excel file!");
            }

            try (InputStream inputStream = file.getInputStream()) {
               // s3Service.uploadFile(file); // Assuming S3 upload method, customize as needed

                Workbook workbook = new XSSFWorkbook(inputStream); // XSSFWorkbook for .xlsx files

                // Process each sheet and row
                processExcelWorkbook(workbook);

                return ResponseEntity.ok("File uploaded successfully!");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload or process Excel file: " + e.getMessage());
            }
        }
    private void processExcelWorkbook(Workbook workbook) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Process each row
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    // Process each cell as needed
                    String cellValue = getCellValueAsString(cell);
                    System.out.print(cellValue + "\t");
                }
                System.out.println();
            }
        }

        // Example: Save parsed data into database using service
        excelService.saveDataFromExcel(workbook);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}

