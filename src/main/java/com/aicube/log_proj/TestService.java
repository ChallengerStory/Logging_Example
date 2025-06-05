package com.aicube.log_proj;

import org.springframework.stereotype.Service;

@Service
class TestService {
    private Integer cnt = 0;

    public String findInfo() {
      cnt++;
      return "This is a test" + cnt;
    }
}
