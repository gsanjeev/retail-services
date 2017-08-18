package com.globomart.catalog.service.impl;

import com.globomart.catalog.service.CatalogService;
import com.globomart.catalog.repository.ProductRepository;
import org.apache.log4j.Logger;
import com.globomart.catalog.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.*;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private ProductRepository productRepository;
 
    private Logger logger = Logger.getLogger(CatalogServiceImpl.class);

    @Transactional(propagation= Propagation.SUPPORTS, readOnly=false)
    public Product createProduct(Product product) {
        logger.debug("Save Product with code =" + product.getCode());
        return productRepository.save(product);
    }

    @Transactional(readOnly=true)
    public Collection<Product> retrieveAllProducts() {
        logger.debug("Retrieve all Products");
        Collection<Product> allProducts = new ArrayList<>();
        productRepository.findAll().forEach(allProducts::add);

        return allProducts;
    }

    @Transactional(readOnly=true)
    public Collection<Product> retrieveProducts(String keyword, String type) {
	Specifications<Product> spec = null;

	    logger.debug("Retrieve Products for keyword = " + keyword + " and type = " + type);
        Collection<Product> result = null;
        if (keyword != null && !keyword.isEmpty()) {
            spec = where(ProductSpecifications.codeOrDescriptionContainsIgnoreCase(keyword));
        }

        if (type!=null && !type.isEmpty()) {
            spec = spec == null ? where(ProductSpecifications.productPerType(type))
                    :spec.and(ProductSpecifications.productPerType(type));
            result = productRepository.findAll(spec);
        }
        return productRepository.findAll(spec);
    }

 
    @Transactional(readOnly=true)
    public Product getProduct(int id) {
        logger.debug("Retrieve Products for id = " + id);
        return productRepository.findOne(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS, readOnly=false)
    public void removeProduct(int id) {
        logger.debug("Remove Product for id = " + id);
        try {
            productRepository.delete(id);
        } catch (EmptyResultDataAccessException exp) {
            return;
        }
    }

}
