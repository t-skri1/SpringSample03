package com.example.web.form;

import java.util.List;

import com.example.persistence.entity.ProductsMaster;

import lombok.Data;

@Data
public class ProductsMasterForm {
	private List<ProductsMaster> pmList;
}