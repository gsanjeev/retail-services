package com.globomart.pricing.model;

public class Product {

    private Integer id;
 
    private Integer version;
 
    private String code;
    private String description;
    private String imageUrl;
    private ProductPrice price;
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public Integer getVersion() {
        return version;
    }
 
    public void setVersion(Integer version) {
        this.version = version;
    }
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public String getImageUrl() {
        return imageUrl;
    }
 
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
 
    public ProductPrice getPrice() {
        return price;
    }
 
    public void setPrice(ProductPrice price) {
        this.price = price;
    }
}