package com.aicube.log_proj.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class LogEntry {
    private LocalDateTime timestamp;
    private String clientIp;
    private String method;
    private String uri;
    private String userAgent;
} 