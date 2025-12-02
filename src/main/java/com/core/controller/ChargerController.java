package com.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.dto.ChargerDto;
import com.core.service.EvChargerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chargers")
public class ChargerController {
	
	private final EvChargerService evChargerService;
	
	@GetMapping("/index")
	public String index() {
		return "chargers/index"; 
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		List<ChargerDto> chargers = evChargerService.fetchChargers();
		model.addAttribute("chargers", chargers);
		return "chargers/list";
	}
	
//	@RequestMapping("/")
////	public String home() {
////		
////	}
	
	
}
