package com.aicube.log_proj.log;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogAnalyzerController {
    
    private final LogAnalyzerService logAnalyzerService;

    public LogAnalyzerController(LogAnalyzerService logAnalyzerService) {
        this.logAnalyzerService = logAnalyzerService;
    }

    @GetMapping("/analyze/{date}")
    public Map<String, Object> analyzeLog(@PathVariable String date) {
        List<LogEntry> entries = logAnalyzerService.parseLogFile(date);
        
        Map<String, Object> analysis = new HashMap<>();
        if (entries.isEmpty()) {
            analysis.put("message", date + " 날짜의 로그 데이터가 없습니다.");
            analysis.put("totalRequests", 0);
        } else {
            analysis.put("totalRequests", entries.size());
            analysis.put("requestsByIp", logAnalyzerService.getRequestsByIp(entries));
            analysis.put("requestsByUri", logAnalyzerService.getRequestsByUri(entries));
            analysis.put("requestsByMethod", logAnalyzerService.getRequestsByMethod(entries));
        }
        
        return analysis;
    }

    @GetMapping("/download/{date}")
    public ResponseEntity<Resource> downloadExcel(@PathVariable String date) {
        try {
            List<LogEntry> entries = logAnalyzerService.parseLogFile(date);
            logAnalyzerService.exportToExcel(entries, date);

            Path file = Paths.get("logs/client-requests-" + date + ".xlsx");
            Resource resource = new UrlResource(file.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"client-requests-" + date + ".xlsx\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }
} 