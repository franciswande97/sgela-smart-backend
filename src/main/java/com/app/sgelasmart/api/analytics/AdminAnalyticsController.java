package com.app.sgelasmart.api.analytics;

import com.app.sgelasmart.api.analytics.dto.AdminDashboardResponse;
import com.app.sgelasmart.common.TemplateType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminAnalyticsController {
    private final AdminAnalyticsService adminAnalyticsService;

    public AdminAnalyticsController(AdminAnalyticsService adminAnalyticsService) {
        this.adminAnalyticsService = adminAnalyticsService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard(
            @RequestParam(defaultValue = "LAST_30_DAYS") DatePreset preset,
            @RequestParam(required = false) TemplateType template,
            @RequestParam(required = false) String search) {
        return adminAnalyticsService.getDashboard(preset, template, search);
    }
}