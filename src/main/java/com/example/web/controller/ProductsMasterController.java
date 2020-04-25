package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.ProductsMasterService;
import com.example.web.form.ProductsMasterForm;

@Controller
@RequestMapping("/productsMaster")
public class ProductsMasterController {
	@Autowired
	ProductsMasterService productsMasterService;

	@GetMapping("/index")
	public String indexGet(Model model) {
		ProductsMasterForm productsMasterForm = new ProductsMasterForm();
		productsMasterForm.setPmList(productsMasterService.productsMasterList());

		model.addAttribute("productsMasterForm", productsMasterForm);
		return "productsMaster/index";
	}

}

