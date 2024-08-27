package com.example.project1.Service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);


    public byte[] generateReport(String jasperFileName, Map<String, Object> parameters, JRDataSource dataSource, String format) throws Exception {
        InputStream jasperStream = null;
        try {
            // 獲取Jasper報告文件的輸入流
            ClassPathResource resource = new ClassPathResource("/templates/" + jasperFileName + ".jasper");
            jasperStream = resource.getInputStream();
            if (jasperStream == null) {
                logger.error("找不到報告文件: /templates/" + jasperFileName + ".jasper");
                throw new IllegalArgumentException("找不到報告文件: /templates/" + jasperFileName + ".jasper");
            }

            // 加載Jasper報告
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            // 填充報告數據
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 調試信息：檢查是否生成了報告頁面
            if (jasperPrint.getPages().isEmpty()) {
                logger.error("生成的報告頁面是空的，檢查模板和數據源。");
                throw new RuntimeException("生成的報告頁面是空的，檢查模板和數據源。");
            } else {
                logger.info("成功生成報告，頁數: " + jasperPrint.getPages().size());
            }

            // 根據指定的格式導出報告
            if ("pdf".equalsIgnoreCase(format)) {
                return JasperExportManager.exportReportToPdf(jasperPrint);
            } else if ("xlsx".equalsIgnoreCase(format)) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setOnePagePerSheet(true);
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setDetectCellType(true);
                configuration.setWhitePageBackground(false);
                exporter.setConfiguration(configuration);

                exporter.exportReport();
                return byteArrayOutputStream.toByteArray();
            } else {
                throw new IllegalArgumentException("未知的報告格式: " + format);
            }
        } catch (Exception e) {
            logger.error("生成報告時發生錯誤", e);
            throw e;
        } finally {
            if (jasperStream != null) {
                try {
                    jasperStream.close();
                } catch (IOException ex) {
                    logger.error("關閉報告模板輸入流時發生錯誤", ex);
                }
            }
        }
    }
}

 /*   @Autowired
    private UserService userService;
    @Autowired
    private NewBookService newbookService;

    public byte[] exportReport(String reportFormat) throws JRException, IOException {
        List<User> users = userService.findAllUser();
        List<NewBook> books = newbookService.findAllBook();

        // 填充報表資料源
        JRDataSource usersDataSource = new JRBeanCollectionDataSource(users);
        JRDataSource booksDataSource = new JRBeanCollectionDataSource(books);

        // 加載報表模板
        InputStream is = getClass().getClassLoader().getResourceAsStream("JasperReport/user_role_book_report.jrxml");
        //InputStream is = new ClassPathResource("JasperReport/user_role_book_report.jrxml").getInputStream();

        if (is == null) {
            throw new FileNotFoundException("File not found:JasperReport/user_role_book_report.jrxml");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(is);


        // 設定報表參數
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("UsersDataSource", usersDataSource);
        parameters.put("BooksDataSource", booksDataSource);

        // 填充報表
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // 匯出報表
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if ("pdf".equalsIgnoreCase(reportFormat)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } else if ("excel".equalsIgnoreCase(reportFormat)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
        }

        return outputStream.toByteArray();
    }
*/


    /*public byte[] exportReport(String reportFormat, Map<String, Object> parameters, List<?> datasource, String templateName) throws Exception{

        InputStream reportStream  = new ClassPathResource(templateName).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(datasource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

        if (reportFormat.equalsIgnoreCase("pdf")) {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } else if (reportFormat.equalsIgnoreCase("xlsx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
            reportConfig.setSheetNames(new String[]{"Report"});
            exporter.setConfiguration(reportConfig);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();
            return out.toByteArray();
        } else {
            throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
        }
    }*/

