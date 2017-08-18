package com.globomart.catalog.repository;
 
import com.globomart.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
 
public interface ProductRepository extends CrudRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Product findByCode(String code);
}