package com.knits.enterprise.utils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ExcelHelper {

    public static ByteArrayInputStream toExcel(Map<String, List<Object>> data) {
        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Selected Employee Data");

            List<String> headers = new ArrayList<>(data.keySet());
            Row headerRow = sheet.createRow(0);
            IntStream.range(0, headers.size()).forEach(column -> {
                Cell cell = headerRow.createCell(column);
                cell.setCellValue(headers.get(column));
            });

            headers.stream().forEach(header->{
                List<Object> valuesForKey = data.get(header);

                if (valuesForKey != null && !valuesForKey.isEmpty()) {
                    int columnIndex = headers.indexOf(header);

                    valuesForKey.stream().forEach(value ->{
                        int rowIndex = sheet.getLastRowNum() + 1;
                        Row row = sheet.getRow(rowIndex);
                        if (row == null) {
                            row = sheet.createRow(rowIndex);
                        }
                        setCellValue(row.createCell(columnIndex), value);
                    });
                }
            });

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage());
        }
    }

    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("dd/MM/yyyy"));
            cell.setCellStyle(cellStyle);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
