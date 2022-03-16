package de.zwickau.vub.vending.service;

import de.zwickau.vub.vending.model.Product;

public interface ProductService {
    void createProduct(Product product) throws ProductException;
    void updateProduct(Product product) throws ProductException;
    Product findProductByName(String name);
    void deleteProductByName(String name) throws ProductException;

    Product buy(String name, Integer amount) throws ProductException;
}
