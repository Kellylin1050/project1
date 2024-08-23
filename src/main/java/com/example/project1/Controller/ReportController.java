package com.example.project1.Controller;

import com.example.project1.Service.impl.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Report輸出",description = "JasperReports報表輸出")
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/generateReport")
    public ResponseEntity<byte[]> generateReport(@RequestParam String customerName,
                                                 @RequestParam String supplierName,
                                                 @RequestParam String shipNum,
                                                 @RequestParam String orderId,
                                                 @RequestParam String logo,
                                                 @RequestParam String orderMemo,
                                                 @RequestParam String format) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("customerName", customerName);
            parameters.put("supplierName", supplierName);
            parameters.put("shipNum", shipNum);
            parameters.put("orderId", orderId);
            parameters.put("logo", logo);
            parameters.put("orderMemo", orderMemo);

            byte[] report = reportService.generateReport("shipOrderDetailReport", parameters, format);

            HttpHeaders headers = new HttpHeaders();
            if ("pdf".equalsIgnoreCase(format)) {
                headers.add("Content-Disposition", "inline; filename=shipOrderDetailReport.pdf");
                return ResponseEntity.ok().headers(headers).body(report);
            } else if ("xlsx".equalsIgnoreCase(format)) {
                headers.add("Content-Disposition", "inline; filename=shipOrderDetailReport.xlsx");
                return ResponseEntity.ok().headers(headers).body(report);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /*@Autowired
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
}
