package com.globomart.pricing.controller;

import com.globomart.pricing.controller.exception.PriceNotFoundException;
import com.globomart.pricing.model.Product;
import com.globomart.pricing.model.ProductPrice;
import com.globomart.pricing.service.PricingService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/pricing")
public class PricingController {

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@Autowired
	private PricingService pricingService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${message:Hello default}")
	private String message;

	private static final Logger logger = LoggerFactory.getLogger(PricingController.class);

	@RequestMapping(value = "/product/{code}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@HystrixCommand(ignoreExceptions = {PriceNotFoundException.class}, fallbackMethod = "getProductPriceFallback")
	public ResponseEntity<?> getProductPrice(@PathVariable String code) throws PriceNotFoundException {

		ProductPrice price = pricingService.getProductPrice(code);

		if (price == null) {
		    logger.error("Price not found.");
		    throw  new PriceNotFoundException("Price is not available for product");
        }
		logger.debug("ProductPrice found for code = " + price.getCode());
		logger.debug("Price of Product = " + price.getPrice());
		ServiceInstance catalogService=loadBalancerClient.choose("catalog");
		logger.info("Catalog Service Uri:" + catalogService.getUri());
		
		String baseUrl=catalogService.getUri().toString();
		
		String catalogUrl=baseUrl+"/catalog/search?keyword={code}";
		Map<String, String> params = new HashMap<String, String>();
    		params.put("code", code);
		
		ResponseEntity<Product[]> response = restTemplate.getForEntity(catalogUrl, Product[].class, params);
		Product product = null;
		if (response.getBody() != null && response.getBody().length > 0){
			product = response.getBody()[0];
			product.setPrice(price);
			logger.debug("Product found for code = " + code);
			return new ResponseEntity<>(product, HttpStatus.OK);
		}
		else {
			logger.debug("Product node found for code = " + code);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
    @RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<?> saveProductPrice(@RequestBody ProductPrice productPrice) {
		ProductPrice savedProductPrice = pricingService.saveProductPrice(productPrice);
		logger.debug("Saved ProductPrice for code = " + savedProductPrice.getCode());
		return new ResponseEntity<>(savedProductPrice, HttpStatus.CREATED);
    }

	ResponseEntity<?> getProductPriceFallback(String code) throws PriceNotFoundException {

		ProductPrice price = pricingService.getProductPrice(code);
		if (price == null) {
			logger.error("Price not found.");
			throw  new PriceNotFoundException("Price is not available for product");
		}
		Product product = new Product();
		product.setCode(code);
		product.setPrice(price);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	ResponseEntity<Void> removeProduct(@PathVariable int id) {

		pricingService.removeProductPrice(id);
		logger.debug("ProductPrice removed for id = " + id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	ResponseEntity<Collection<ProductPrice>> retrieveAllProducts() {

		Collection<ProductPrice> allProductsPrice = pricingService.retrieveAllProductsPrice();
		logger.info("allProductsPrice size:" + allProductsPrice.size());

		logger.debug("All Products retrieved.");
		return new ResponseEntity<>(allProductsPrice, HttpStatus.OK);
	}

}
