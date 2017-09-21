package com.globomart.pricing.service;

import com.globomart.pricing.model.Product;
import com.globomart.pricing.model.ProductPrice;

import java.util.Collection;

public interface PricingService {
 
    public ProductPrice getProductPrice(String productCode);

    ProductPrice saveProductPrice(ProductPrice productPrice);

    public Collection<ProductPrice> retrieveAllProductsPrice();

    public void removeProductPrice(int id);
}
