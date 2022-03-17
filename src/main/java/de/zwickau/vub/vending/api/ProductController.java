package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.model.Product;
import de.zwickau.vub.vending.service.ProductException;
import de.zwickau.vub.vending.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasRole('SELLER')")
    public ResponseEntity<String> postProduct(@RequestBody Product product) {
        try {
            productService.createProduct(product);
            return ResponseEntity.ok("Product registered!");
        } catch (ProductException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @PutMapping
    @PreAuthorize("isAuthenticated() and hasRole('SELLER')")
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        try {
            productService.updateProduct(product);
            return ResponseEntity.ok("Product updated!");
        } catch (ProductException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("isAuthenticated() and hasRole('SELLER')")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {
        try {
            productService.deleteProductByName(name);
            return ResponseEntity.ok("Product deleted!");
        } catch (ProductException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{name}")
    @PreAuthorize("isAuthenticated()")
    public Product getProduct(@PathVariable String name) {
        return productService.findProductByName(name);
    }

    @PostMapping("/{name}/buy/{amount}")
    public ResponseEntity<Product> buyProduct(@PathVariable String name, @PathVariable Integer amount) {
        try {
            Product product = productService.buy(name, amount);
            return ResponseEntity.ok(product);
        } catch (ProductException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
