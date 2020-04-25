package com.example.persistence.repository;

import java.util.List;

import com.example.persistence.entity.ProductsMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductsMasterRepositoryImpl implements ProductsMasterRepository {
	@Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<ProductsMaster> productsMasterList() {
		String strSQL = "SELECT * FROM ProductsMaster ORDER BY ProductsCode";

        List<ProductsMaster> pMList = jdbcTemplate.query(strSQL,
                (rs, rowNum) -> new ProductsMaster(
                        rs.getString("ProductsCode"),
                        rs.getString("ProductsName"),
                        rs.getInt("UnitPrice")                 
                        )
                );

        return pMList;
	}
}