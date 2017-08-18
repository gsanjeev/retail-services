package com.globomart.pricing.service;

import com.globomart.pricing.model.Product;
import com.globomart.pricing.model.ProductPrice;

public interface PricingService {
 
    public ProductPrice getProductPrice(String productCode);

    ProductPrice saveProductPrice(ProductPrice productPrice);
}
