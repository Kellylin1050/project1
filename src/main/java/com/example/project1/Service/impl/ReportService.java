package com.example.project1.Service.impl;

import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.User;
import com.example.project1.Service.NewBookService;
import com.example.project1.Service.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private UserService userService;
    @Autowired
    private NewBookService newbookService;

    public byte[] exportReport(String reportFormat) throws JRException {
        List<User> users = userService.findAllUser();
        List<NewBook> books = newbookService.findAllBook();

        // 填充報表資料源
        JRDataSource usersDataSource = new JRBeanCollectionDataSource(users);
        JRDataSource booksDataSource = new JRBeanCollectionDataSource(books);

        // 加載報表模板
        InputStream reportStream = getClass().getResourceAsStream("JasperReport/user_role_book_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

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
}
