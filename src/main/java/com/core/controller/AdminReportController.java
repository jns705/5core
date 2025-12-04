package com.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.dto.CalendarEvent;
import com.core.dto.MemoRequest;

@Controller
@RequestMapping("/admin")
public class AdminReportController {
	    
	    // 👈 필드 선언 필수!
	    private List<CalendarEvent> calendarEvents = new ArrayList<>();
	    
	    @GetMapping("/report")
	    public String requestAdminReport(Model model) {  // 👈 Model 추가!
	        model.addAttribute("calendarEvents", calendarEvents);
	        return "admin/report";
	    }
	    
	    @PostMapping("/report/memo/save")  // 👈 /admin 경로 자동 추가됨
	    @ResponseBody
	    public Map<String, Object> saveMemo(@RequestBody MemoRequest request) {
	        CalendarEvent event = new CalendarEvent();
	        event.setId(UUID.randomUUID().toString());
	        event.setTitle(request.getTitle());
	        event.setDate(request.getDate());
	        event.setColor("#10b981");
	        
	        calendarEvents.add(event);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("id", event.getId());
	        response.put("success", true);
	        return response;
	    }
	    
	    @DeleteMapping("/report/memo/delete/{id}")
	    @ResponseBody
	    public Map<String, Object> deleteMemo(@PathVariable String id) {
	        boolean removed = calendarEvents.removeIf(e -> e.getId().equals(id));
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", removed);
	        return response;
	    }
	}

