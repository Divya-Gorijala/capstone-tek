package com.capstone.capstone_project.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveDataFromExcel(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String id = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String description = getCellValueAsString(row.getCell(2));

                // Insert into MySQL
                String sql = "INSERT INTO order_details (id, name, description) VALUES (?, ?, ?)";
                jdbcTemplate.update(sql, id, name, description);
            }
        }
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

