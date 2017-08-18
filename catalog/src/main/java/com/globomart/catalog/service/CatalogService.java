package com.globomart.catalog.service;

import com.globomart.catalog.model.Product;

import java.util.Collection;

public interface CatalogService {


    public Product createProduct(Product product);

    public Collection<Product> retrieveAllProducts();

    public Collection<Product> retrieveProducts(String keyword, String type);


    public Product getProduct(int id);

    public void removeProduct(int id);
}
