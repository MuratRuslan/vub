package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.model.Product;
import de.zwickau.vub.vending.model.Role;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.ProductService;
import de.zwickau.vub.vending.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    UserService userService;

    @Test
    void buyProductOk() throws Exception {
        var product = newProduct();
        when(productService.buy("book1", 3)).thenReturn(product);
        this.mockMvc.perform(post("/product/book1/buy/2")).andDo(print()).andExpect(status().isOk());
    }


    Product newProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setAmountAvailable(5);
        product.setName("book1");
        product.setCost(10);
        return product;
    }
}