package org.latheild.dt.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private volatile static ExcelUtil instance;

    public static ExcelUtil getInstance() {
        if (instance == null) {
            synchronized (ExcelUtil.class) {
                if (instance == null) {
                    instance = new ExcelUtil();
                }
            }
        }
        return instance;
    }

    private ExcelUtil() {
    }

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public List<List<List>> readExcel(String filePath) {
        try {
            File xlsFile = new File(filePath);
            if (xlsFile.isFile() && xlsFile.exists()) {
                String[] typeCheck = xlsFile.getName().split("\\.");
                Workbook workbook;
                if ("xls".equals(typeCheck[1])) {
                    FileInputStream fis = new FileInputStream(xlsFile);
                    workbook = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(typeCheck[1])) {
                    workbook = new XSSFWorkbook(xlsFile);
                } else {
                    logger.error("Wrong file type.");
                    return null;
                }

                List<List<List>> sheets = new ArrayList<>();
                int sheetSize = workbook.getNumberOfSheets();
                for (int k = 0; k < sheetSize; ++k) {
                    List<List> rows = new ArrayList<>();
                    Sheet sheet = workbook.getSheetAt(k);
                    int firstRowNumow = sheet.getFirstRowNum();
                    int lastRowNumow = sheet.getLastRowNum();
                    for (int i = firstRowNumow; i <= lastRowNumow; ++i) {
                        List<Object> columns = new ArrayList<>();
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            int firstCellIndex = row.getFirstCellNum();
                            int lastCellIndex = row.getLastCellNum();
                            for (int j = firstCellIndex; j <= lastCellIndex; ++j) {
                                Cell cell = row.getCell(j);
                                if (cell != null) {
                                    switch (cell.getCellTypeEnum()) {
                                        case STRING:
                                            columns.add(cell.getRichStringCellValue().toString());
                                            break;
                                        case BOOLEAN:
                                            columns.add(cell.getBooleanCellValue());
                                            break;
                                        case NUMERIC:
                                            if (DateUtil.isCellDateFormatted(cell)) {
                                                columns.add(cell.getDateCellValue());
                                            } else {
                                                //columns.add(cell.getNumericCellValue());
                                                if (cell.getNumericCellValue() == (int)cell.getNumericCellValue()) {
                                                    columns.add((int)cell.getNumericCellValue());
                                                } else {
                                                    columns.add(cell.getNumericCellValue());
                                                }
                                            }
                                            break;
                                        case FORMULA:
                                            columns.add(cell.getCellFormula());
                                            break;
                                        default:

                                    }
                                }
                            }
                        }
                        rows.add(columns);
                    }
                    sheets.add(rows);
                }

                return sheets;
            } else {
                logger.error("File not exist.");
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
