package com.globomart.catalog.controller;

import com.globomart.catalog.model.Product;
import com.globomart.catalog.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService productService;

    private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        logger.debug("Product created with id = " + savedProduct.getId());
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@PathVariable int id) {

        Product product = productService.getProduct(id);
        logger.debug("Product retrieved with code = " + ((product == null) ? null : product.getCode()));
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Collection<Product>> retrieveAllProducts() {

        Collection<Product> products = productService.retrieveAllProducts();
        logger.debug("All Products retrieved.");
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Collection<Product>> searchProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) String type) {
        Collection<Product> products = productService.retrieveProducts(keyword, type);
        logger.debug("All Products retrieved for keyword = " + keyword + " type = " + type);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @RequestMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    ResponseEntity<Void> removeProduct(@PathVariable int id) {

        productService.removeProduct(id);
        logger.debug("Products removed for id = " + id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    void emptyResultDataAccess(HttpServletResponse response) throws IOException {
        logger.error(" EmptyResultDataAccessException Error." );
        response.sendError(HttpStatus.NOT_FOUND.value(), "No such Product");
    }

}
