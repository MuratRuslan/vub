package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.jwt.JwtRequestFilter;
import de.zwickau.vub.vending.model.Role;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserControllerTest {

    MockMvc mockMvc;

    @MockBean
    UserService userService;


    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }


    @Test
    public void depositOk() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/deposit/50")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void depositWrongAmount() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/deposit/33")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void resetDepositOk() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/reset")).andDo(print()).andExpect(status().isOk());
    }

    private User newBuyer() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setDeposit(0);
        mockUser.setUsername("murat");
        mockUser.setRole(Role.BUYER);
        return mockUser;
    }
}