package com.example.service.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.example.service")
@EnableTransactionManagement
public class ServiceConfig {
	 @Bean
	    public PlatformTransactionManager transactionManager(DataSource dataSource) {
	        DataSourceTransactionManager transactionManager =
	                new DataSourceTransactionManager(dataSource);
	        return transactionManager;
	    }
}
