package com.example.project1.Service.impl;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class ReportService {

    private final DataSource dataSource;

    public ReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generateReport(String jasperFileName, Map<String, Object> parameters, String format) throws Exception {
        // 載入 .jasper 檔案
        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/" + jasperFileName + ".jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // 填充報表
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

        // 根據格式生成報表
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
            throw new IllegalArgumentException("Unknown report format: " + format);
        }
    }
   /* @Autowired
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
}
