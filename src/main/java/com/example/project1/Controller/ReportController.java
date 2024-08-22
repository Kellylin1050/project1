package com.example.project1.Controller;

import com.example.project1.Entity.NewBook;
import com.example.project1.Service.NewBookService;
import com.example.project1.Service.impl.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.checkerframework.checker.units.qual.N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Report輸出",description = "JasperReports報表輸出")
@RestController
@RequestMapping("/report")
public class ReportController {

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
    }

}
