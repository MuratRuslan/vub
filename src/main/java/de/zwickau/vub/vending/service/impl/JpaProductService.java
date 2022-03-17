package de.zwickau.vub.vending.service.impl;

import de.zwickau.vub.vending.model.Product;
import de.zwickau.vub.vending.model.Role;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.repository.ProductRepository;
import de.zwickau.vub.vending.repository.UserRepository;
import de.zwickau.vub.vending.service.ProductException;
import de.zwickau.vub.vending.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class JpaProductService implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createProduct(Product product) throws ProductException {
        if(productRepository.existsByName(product.getName())){
            throw new ProductException("Product with given name exists!");
        }
        if(product.getAmountAvailable() == null) {
            product.setAmountAvailable(0);
        }
        if(product.getCost() == null) {
            product.setCost(0);
        }
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) throws ProductException {
        productRepository.save(product);
    }

    @Override
    public Product findProductByName(String name) {
        return productRepository.findByName(name).get();
    }

    @Override
    public void deleteProductByName(String name) throws ProductException {
        productRepository.deleteByName(name);
    }

    @Transactional
    @Override
    public Product buy(String name, Integer amount) throws ProductException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getAuthorities().contains(Role.BUYER)) {
            throw new ProductException("Only buyers can buy products!");
        }
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        User foundUser = userRepository.findByUsername(username).get();

        if(!productRepository.existsByName(name)) {
            throw new ProductException("Product with given name does not exists!");
        }
        var product = productRepository.findByName(name).get();
        int cost = product.getCost() * amount;
        if(foundUser.getDeposit() < cost) {
            throw new ProductException("Not enough deposit!");
        }
        if(amount > product.getAmountAvailable()) {
            throw new ProductException("Not enough of available product!");
        }
        product.setAmountAvailable(product.getAmountAvailable() - amount);
        productRepository.save(product);
        foundUser.setDeposit(foundUser.getDeposit() - cost);
        userRepository.save(foundUser);
        return product;
    }
}
