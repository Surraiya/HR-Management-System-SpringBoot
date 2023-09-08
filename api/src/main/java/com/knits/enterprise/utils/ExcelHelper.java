package com.knits.enterprise.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class ExcelHelper {

    public static ByteArrayInputStream toExcel(Map<String, List<Object>> data) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()
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

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date dateValue = cell.getDateCellValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    return dateFormat.format(dateValue);
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<List<Object>> getExcelFileValues(InputStream inputStream) {
        List<List<Object>> cellValues = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            boolean skipFirstRow = true;

            for (Row row : sheet) {

                if (skipFirstRow){
                    skipFirstRow = false;
                    continue;
                }
                if(row == null){
                    break;
                }

                List<Object> rowValues = new ArrayList<>();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);
                    rowValues.add(cellValue);
                }

                cellValues.add(rowValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(cellValues);
        return cellValues;
    }
}
