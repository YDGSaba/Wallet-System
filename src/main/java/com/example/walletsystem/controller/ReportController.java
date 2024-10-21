package com.example.walletsystem.controller;

import com.example.walletsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/monthly/{accountId}")
    public ResponseEntity<String> getMonthlyReport(
            @PathVariable Long accountId,
            @RequestParam int month,
            @RequestParam int year) {
        String report = reportService.generateMonthlyReport(accountId, month, year);
        return ResponseEntity.ok(report);
    }
}
