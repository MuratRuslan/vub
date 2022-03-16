package de.zwickau.vub.vending.repository;

import de.zwickau.vub.vending.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);

 
    Optional<Product> findByName(String name);

    void deleteByName(String name);
}
