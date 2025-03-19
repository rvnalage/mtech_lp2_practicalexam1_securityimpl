package com.example.ecommerce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ecommerce.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product findByName(String name);

}
