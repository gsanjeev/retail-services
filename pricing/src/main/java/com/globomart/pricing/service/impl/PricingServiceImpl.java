package com.globomart.pricing.service.impl;

import com.globomart.pricing.model.Product;
import com.globomart.pricing.model.ProductPrice;
import com.globomart.pricing.repository.PricingRepository;
import com.globomart.pricing.service.PricingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class PricingServiceImpl implements PricingService {

    @Autowired
    private PricingRepository pricingRepository;
 
    private Logger logger = Logger.getLogger(PricingServiceImpl.class);

 
    @Transactional(readOnly=true)
    public ProductPrice getProductPrice(String code) {
        logger.debug("Retrieve ProductPrice for code = " + code);
        return pricingRepository.findByCode(code);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=false)
    public ProductPrice saveProductPrice(ProductPrice productPrice) {
        logger.debug("Save ProductPrice for code = " + productPrice.getCode());
        return pricingRepository.save(productPrice);
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<ProductPrice> retrieveAllProductsPrice() {
        logger.debug("Retrieve all ProductsPrice");
        Collection<ProductPrice> allProductsPrice = new ArrayList<>();
        pricingRepository.findAll().forEach(allProductsPrice::add);
        logger.info("allProductsPrice size:" + allProductsPrice.size());
        return allProductsPrice;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=false)
    public void removeProductPrice(int id) {
        logger.debug("Remove Product for id = " + id);
        try {
            pricingRepository.delete(id);
        } catch (EmptyResultDataAccessException exp) {
            return;
        }
    }

}
