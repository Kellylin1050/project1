package com.example.project1.Controller;

import com.example.project1.Service.impl.ReportService;
import com.example.project1.config.CustomDataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Report輸出", description = "JasperReports報表輸出")
@RestController
@RequestMapping("/report")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "輸出報表",
            description = "根據資料庫提供的檔案輸出報表"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功輸出報表"),
            @ApiResponse(responseCode = "400",description = "不支持的報告格式"),
            @ApiResponse(responseCode = "500", description = "報告生成失敗or報告生成過程中出現錯誤")
    })
    @GetMapping("/generateReport")
    public ResponseEntity<?> generateReport(@RequestParam String customerName,
                                            @RequestParam String supplierName,
                                            @RequestParam String shipNum,
                                            @RequestParam String orderId,
                                            @RequestParam String orderMemo,
                                            @RequestParam String format) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("customerName", customerName);
            parameters.put("supplierName", supplierName);
            parameters.put("shipNum", shipNum);
            parameters.put("orderId", orderId);
            parameters.put("orderMemo", orderMemo);

            BufferedImage logoImage = getImage("/static/SmartSelect_20230201_184858_Dcard.jpg");
            if (logoImage != null) {
                parameters.put("logo", logoImage);
            } else {
                logger.warn("Logo image not found or couldn't be loaded.");
            }

            // 準備報告數據
            List<Map<String, Object>> data = prepareReportData();
            JRDataSource dataSource = new CustomDataSource(data);

            byte[] report = reportService.generateReport("shipOrderDetailReport", parameters, dataSource, format);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("報告生成失敗");
            }
            HttpHeaders headers = new HttpHeaders();
            if ("pdf".equalsIgnoreCase(format)) {
                headers.add("Content-Disposition", "inline; filename=shipOrderDetailReport.pdf");
                return ResponseEntity.ok().headers(headers).body(report);
            } else if ("xlsx".equalsIgnoreCase(format)) {
                headers.add("Content-Disposition", "inline; filename=shipOrderDetailReport.xlsx");
                return ResponseEntity.ok().headers(headers).body(report);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("不支持的報告格式: " + format);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("報告生成過程中出現錯誤");
        }
    }

    private List<Map<String, Object>> prepareReportData() {
        List<Map<String, Object>> data = new ArrayList<>();
        // 填充數據，這裡放入示例數據
        data.add(Map.of(
                "productId", 1L, // Use Long for productId
                "productName", "Product 1",
                "quantity", 10, // Use Integer for quantity
                "spec1", "Spec A",
                "spec2", "Spec B",
                "originalModel", "Model X"
        ));
        System.out.println("Report Data : " + data);
        return data;
    }


    /*private ResponseEntity<byte[]> createResponseEntity(String format, byte[] report) {
        HttpHeaders headers = new HttpHeaders();
        String fileName = "shipOrderDetailReport." + format.toLowerCase();
        headers.add("Content-Disposition", "inline; filename=" + fileName);

        if ("pdf".equalsIgnoreCase(format)) {
            headers.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(headers).body(report);
        } else if ("xlsx".equalsIgnoreCase(format)) {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().headers(headers).body(report);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }*/

    public BufferedImage getImage(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            return ImageIO.read(resource.getInputStream());
        } catch (IOException e) {
            logger.error("Error loading image: " + imagePath, e);
            return null; // 或者返回一個默認的圖片
        }
    }
}

 /*    @Autowired
    private ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @Operation(
            summary = "輸出報表",
            description = "根據資料庫提供的檔案輸出報表"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功輸出報表"),
            @ApiResponse(responseCode = "500", description = "輸出報表失敗")
    })
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam String format){
        try {
            byte[] reportBytes = reportService.exportReport(format);
            HttpHeaders headers =new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=user_role_book_report." + format);
            return new ResponseEntity<>(reportBytes,headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

