package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.persistence.entity.ProductsMaster;
import com.example.persistence.repository.ProductsMasterRepository;

@Service
public class ProductsMasterServiceImpl implements ProductsMasterService {
	@Autowired
	ProductsMasterRepository productsMasterRepository;

	@Override
	public List<ProductsMaster> productsMasterList() {
		List<ProductsMaster> pMList = productsMasterRepository.productsMasterList();

        return pMList;
	}
}
