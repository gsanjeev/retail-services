package com.globomart.pricing.repository;
 
import com.globomart.pricing.model.ProductPrice;
import org.springframework.data.repository.CrudRepository;
 
public interface PricingRepository extends CrudRepository<ProductPrice, Integer> {
    ProductPrice findByCode(String code);
}