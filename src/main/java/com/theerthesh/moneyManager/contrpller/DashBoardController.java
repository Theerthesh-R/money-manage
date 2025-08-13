package com.theerthesh.moneyManager.contrpller;


import com.theerthesh.moneyManager.service.DashboardService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {
    private final DashboardService dashboardService;


    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashboardData(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboardData());
    }
}
