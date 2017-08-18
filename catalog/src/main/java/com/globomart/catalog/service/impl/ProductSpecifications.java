package com.globomart.catalog.service.impl;

import com.globomart.catalog.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


final class ProductSpecifications {


   private ProductSpecifications() {}

   static Specification<Product> codeOrDescriptionContainsIgnoreCase(String keyword) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(keyword);
            return cb.or(
                    cb.like(cb.lower(root.<String>get("code")), containsLikePattern),
                    cb.like(cb.lower(root.<String>get("description")), containsLikePattern)
            );
        };
    }

  static Specification<Product> productPerType(String type) {
    return
            new Specification<Product>() {
          @Override
      public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("type"), type);
      }
    };
  }


    private static String getContainsLikePattern(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return "%";
        }
        else {
            return "%" + keyword.toLowerCase() + "%";
        }
    }
}