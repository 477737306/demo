package com.cmit.testing.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportReportUtil {

    public static void exportExelMerge(Map<String,Object> map , HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream out = response.getOutputStream();
        Map<String,Object> map1= (Map<String,Object>)map.get("reportHeader");
        String reportName = (String) map1.get("reportName");
        reportName = reportName+"报告.xls";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(reportName, "utf-8"));
        createExcelMerge(map,out);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        //TODO

    }

    public static void createExcelMerge(Map<String,Object> map,OutputStream out){
        Map<String,Integer> caseId_column = new HashMap<>();
        Map<String,Object> reportHeaderMap = (Map<String,Object>)map.get("reportHeader");
        String reportName = (String)reportHeaderMap.get("reportName");
        Workbook wk = new HSSFWorkbook();
        Sheet sheet = wk.createSheet(reportName);
        List<Map<String,Object>> busList = (List<Map<String,Object>>)reportHeaderMap.get("businessList");
        String[] arr = {"序号","总体通过率","","不通过"};
        int [] ColumnWidth = {1500,5000,2000,2000};
        HSSFCellStyle headerStyle = (HSSFCellStyle) wk.createCellStyle();           // 样式对象
        HSSFCellStyle style = (HSSFCellStyle) wk.createCellStyle();           // 样式对象

        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  // 垂直居中 3.16版本VerticalAlignment.CENTER)
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //水平居中 HorizontalAlignment.CENTER

        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  // 垂直居中 3.16版本VerticalAlignment.CENTER)
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //水平居中 HorizontalAlignment.CENTER

        CellRangeAddress region = new CellRangeAddress(0,1,0,0);
        CellRangeAddress region1 = new CellRangeAddress(0,1,3,3);
//        setRegionStyle(sheet,region,headerStyle);  设置合并单元格的边框
//        setRegionStyle(sheet,region1,headerStyle); 设置合并单元格的边框
        sheet.addMergedRegion(region);
        sheet.addMergedRegion(region1);
        //设置单元格样式边框
        /*headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);*/
        Row row = sheet.createRow(0);
        Cell cell =null;
        int i = 0;
        for( ; i <arr.length  ; i++){
            cell = row.getCell(i);
            if(cell == null){
                cell = row.createCell(i);
            }
            cell.setCellValue(arr[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, ColumnWidth[i]);
        }
        Row row2 = sheet.createRow(1);
        Cell cell2 = row2.createCell(1);
        cell2.setCellValue("省份");
        cell2.setCellStyle(headerStyle);
        cell2 = row2.createCell(2);
        cell2.setCellValue("通过率");
        cell2.setCellStyle(headerStyle);
        /*int start = arr.length;*/
        int end = 0;
        for(int t = 0; t < busList.size(); t++){
            Map<String,Object> busMap = busList.get(t);
            List<Map<String,String>> caseList = (List<Map<String,String>>)busMap.get("testCases");
            end = i + caseList.size();
            sheet.addMergedRegion(new CellRangeAddress(0,0,i,end-1));
            cell = row.createCell(i);
            cell.setCellValue((String)busMap.get("businessName"));
            cell.setCellStyle(headerStyle);
            for(int y = 0; y < caseList.size(); y++){
                int column = y+i;
                cell2 = row2.createCell(column);
                cell2.setCellValue(caseList.get(y).get("testCaseName"));
                caseId_column.put(caseList.get(y).get("testCaseId") + "_" + caseList.get(y).get("type"),column);
                cell2.setCellStyle(headerStyle);
            }
            i += caseList.size();
            cell.setCellStyle(headerStyle);
        }
        List<Map<String,Object>> dataList = (List<Map<String,Object>>)map.get("reportData");
        int dataRowNum = dataList.size();
        double successRateAll = 0;
        for (int k = 0 ; k < dataRowNum; k++) {
            row = sheet.createRow(k+2);
            Map<String,Object> dataMap = dataList.get(k);
            String phone = (String) dataMap.get("phone");
            String province = (String) dataMap.get("province");
           /* int columnTotal = sheet.getRow(1).getPhysicalNumberOfCells(); //获取总列数*/
            for(int x = 0 ; x < i; x++){
                cell = row.createCell(x);
                if(x>3){
                    cell.setCellValue("未执行");
                }
                cell.setCellStyle(style);
            }
            row.getCell(0).setCellValue(k+1);
//            row.getCell(1).setCellValue(province+"("+phone+")");
            row.getCell(1).setCellValue(province);
            List<Map<String,String>> caseList = (List<Map<String,String>>)dataMap.get("caseAll");
            int fail = 0;
            double success = 0.0;
            for(Map<String,String> caseMap : caseList){
                String testCaseId = caseMap.get("testCaseId");
                String type = caseMap.get("type");
                int byColumn = caseId_column.get(testCaseId + "_" + type);
                String state = caseMap.get("passStatus");
                if("0".equals(state)){
                    row.getCell(byColumn).setCellValue("成功");
//                    Font font=wk.createFont();
//                    font.setColor(Font.COLOR_RED);//HSSFColor.VIOLET.index //字体颜色
//                    font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
                    success++;
                }else if("1".equals(state)){
                    row.getCell(byColumn).setCellValue("失败");
                    fail++;
                }else{
                    row.getCell(byColumn).setCellValue("未执行");
                }
            }
            cell = row.getCell(2);
            cell.setCellStyle(style);
            double a = (success+fail);
            double successRate = success/a;
            successRateAll += successRate;
            cell.setCellValue(ToolUtil.numericalPrecision(successRate*100,2)+"%");
            cell = row.getCell(3);
            cell.setCellStyle(style);
            cell.setCellValue(fail);
        }
        double v = 0.3334 * 100;
        double count = (ToolUtil.numericalPrecision((successRateAll / dataRowNum*100),2) );
        sheet.getRow(0).getCell(2).setCellValue(  count + "%");

//        sheet.getRow(0).getCell(2).setCellValue("=SUM(C3:C"+(2+dataRowNum)+")");
        try {
            wk.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                wk.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, HSSFCellStyle cs) {

        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
                for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                        cell.setCellValue("");
                    }
                    if(i == region.getFirstRow()) {
                        cs.setBorderTop(CellStyle.BORDER_THIN);
                    }
                    if(i == region.getLastRow()){
                        cs.setBorderBottom(CellStyle.BORDER_THIN);
                    }
                    if (j == region.getFirstColumn()) {
                        cs.setBorderLeft(CellStyle.BORDER_THIN);
                    }
                    if (j == region.getLastColumn()) {
                        cs.setBorderRight(CellStyle.BORDER_THIN);
                    }
                    cell.setCellStyle(cs);
                }
            }
    }
}
