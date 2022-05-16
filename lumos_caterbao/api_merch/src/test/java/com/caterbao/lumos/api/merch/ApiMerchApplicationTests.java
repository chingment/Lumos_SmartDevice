package com.caterbao.lumos.api.merch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
class ApiMerchApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Test
    public void text_export() throws Exception {
        File file = new File("/Users/chingment/Documents/books.xlsx");
        InputStream is = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0); // 获取表格页码
        if (sheet != null) {
            int rowNum = sheet.getLastRowNum(); // 获取该页表共有多少行
            for (int j = 1; j <= rowNum; j++) {  // 一般来说第一行是标题,所以第二行开始读取
                Row row = sheet.getRow(j); // 获取表格行

                String rfId = row.getCell(0).getStringCellValue();
                String skuName = row.getCell(1).getStringCellValue();
                String skuId = row.getCell(2).getStringCellValue();
            }
        }


    }
}
